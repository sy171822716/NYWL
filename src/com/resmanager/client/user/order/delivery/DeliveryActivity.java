/**   
 * @Title: DeliveryActivity.java 
 * @Package com.resmanager.client.user.order 
 * @Description:发货
 * @author ShenYang  
 * @date 2015-12-6 下午4:45:53 
 * @version V1.0   
 */
package com.resmanager.client.user.order.delivery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.GoodsPackageQtyListModel;
import com.resmanager.client.model.GoodsPackageQtyModel;
import com.resmanager.client.model.LocationModel;
import com.resmanager.client.model.Order;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.model.ScanBimpModel;
import com.resmanager.client.model.TempScanBimpModel;
import com.resmanager.client.model.TempScanBimpModels;
import com.resmanager.client.order.OrderDetailActivity;
import com.resmanager.client.order.OrderListAdapter;
import com.resmanager.client.user.order.ChooseLocationActivity;
import com.resmanager.client.user.order.UploadCache;
import com.resmanager.client.user.order.delivery.ConfirnDeliveryAsyncTask.DeliveryListener;
import com.resmanager.client.user.order.delivery.DeleteAllDeliveryPhotoAsyncTask.DelAllDeliveryListener;
import com.resmanager.client.user.order.delivery.DeliveryContinueAsyncTask.DeliveryContinueListener;
import com.resmanager.client.user.order.delivery.GetGoodsCountByOrderIDSAsyncTask.GetGoodsCountListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.DESUtils;
import com.resmanager.client.utils.LocationUtils;
import com.resmanager.client.utils.LocationUtils.PoiSearchListener;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.CustomDialog;
import com.resmanager.client.view.CustomDialog.CancelBtnListener;
import com.resmanager.client.view.CustomDialog.ToDoListener;
import com.resmanager.client.view.DefineListView;

/**
 * @ClassName: DeliveryActivity
 * @Description: 发货
 * @author ShenYang
 * @date 2015-12-6 下午4:45:53
 * 
 */
@SuppressLint({ "InflateParams", "HandlerLeak" })
public class DeliveryActivity extends TopContainActivity implements OnClickListener {
	private Button add_source_btn, delivery_btn;
	private ArrayList<Order> orders;
	private DefineListView order_list, goods_package_count_list;
	private TextView location_str_txt;
	private LocationModel locationModel;
	private String workID = "";// 批次号，由客户端生成
	private StringBuffer orderBuffer;
	private CustomDialog confirnDeliveryDialog, exitDialog, continueDialog;
	private EditText remark_txt;
	public static int NUM = 0;
	public static Map<String, Integer> skuMap = new HashMap<String, Integer>();
	public static Map<String, Integer> selectSkuMap = new HashMap<String, Integer>();
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				List<PoiItem> pois = (List<PoiItem>) msg.obj;
				if (pois.size() > 0) {
					PoiItem poiItem = pois.get(0);
					locationModel.setAddress(poiItem.getSnippet());
					locationModel.setName(poiItem.getTitle());
					locationModel.setLat(poiItem.getLatLonPoint().getLatitude());
					locationModel.setLng(poiItem.getLatLonPoint().getLongitude());
					location_str_txt.setTextColor(getResources().getColor(R.color.orange_color));
					location_str_txt.setText(poiItem.getTitle());
				}
				break;

			default:
				break;
			}
		};
	};

	/*
	 * (非 Javadoc) <p>Title: onClick</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_img:
			showExitDialog();
			break;
		case R.id.add_source_btn:
			// 跳转至查看当前上传图片的界面
			if (NUM > 0) {
				if (UploadCache.scanBimpModels.size() > 0) {
					Intent deliveryIntent = new Intent(DeliveryActivity.this, DeliverySourceGrid.class);
					deliveryIntent.putExtra("workId", this.workID);
					deliveryIntent.putExtra("orderIds", this.orderBuffer.toString());
					startActivity(deliveryIntent);
				} else {
					Intent addIntent = new Intent(DeliveryActivity.this, AddSourceInfoActivity.class);
					addIntent.putExtra("workId", this.workID);
					addIntent.putExtra("orderIds", this.orderBuffer.toString());
					startActivity(addIntent);
				}
			} else {
				Tools.showToast(this, "货物数量为0，不能发货");
			}
			break;
		case R.id.location_str_txt:
			Intent chooseLocationIntent = new Intent(DeliveryActivity.this, ChooseLocationActivity.class);
			chooseLocationIntent.putExtra("current_location", location_str_txt.getText().toString());
			startActivityForResult(chooseLocationIntent, ContactsUtils.CHOOSE_LOCATION_RESULT);
			break;
		case R.id.delivery_btn:
			if (this.locationModel == null || this.locationModel == null || this.locationModel.getAddress().equals("")) {
				Tools.showToast(this, "位置获取失败，请稍后再试");
			} else if (NUM != 0 && UploadCache.scanBimpModels.size() != NUM) {
				Tools.showToast(this, "实际发货数与订单货物数量不符");
			} else {
				showConfirmDialog();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 弹出对话框让用户最后一次选择
	 */
	private void showContinueDialog(final ArrayList<TempScanBimpModel> data, String orderids) {
		if (continueDialog == null) {
			continueDialog = new CustomDialog(this, R.style.myDialogTheme);
			String[] ids = orderids.split(",");
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < ids.length; i++) {
				sb.append(ids[i] + "<br>");
			}
			String noticestr = "该订单您之前上传过发货图片数据，<br>相关订单号:<br>" + sb.toString() + "是否继续上传？";
			continueDialog.setContentText(noticestr, false);
			continueDialog.setOkCancelBtnText("继续", "清空");
			continueDialog.setToDoListener(new ToDoListener() {

				@Override
				public void doSomething() {
					for (TempScanBimpModel tempScanBimpModel : data) {
						ScanBimpModel scanBimpModel = new ScanBimpModel();
						scanBimpModel.setLabelCode(DESUtils.encrypt(tempScanBimpModel.getLabelCode()));
						scanBimpModel.setBmpPath(tempScanBimpModel.getOriginal_Path());
						scanBimpModel.setThumbPath(tempScanBimpModel.getThumb_Path());
						scanBimpModel.setPackageType(tempScanBimpModel.getPackagetype());
						scanBimpModel.setResourceTypeName(tempScanBimpModel.getGoodsName());
						UploadCache.scanBimpModels.add(scanBimpModel);
						add_source_btn.setTextColor(getResources().getColor(R.color.orange_color));
						add_source_btn.setText("点击查看，已添加" + UploadCache.scanBimpModels.size() + "/" + NUM);
					}
				}
			});
			continueDialog.setCancelBtnListener(new CancelBtnListener() {

				@Override
				public void cancel() {
					deleteAllPic();
				}
			});
		}
		continueDialog.show();
	}

	/**
	 * 弹出对话框让用户最后一次选择
	 */
	private void showConfirmDialog() {
		if (confirnDeliveryDialog == null) {
			confirnDeliveryDialog = new CustomDialog(this, R.style.myDialogTheme);
			confirnDeliveryDialog.setContentText("确认开始送货？");
			confirnDeliveryDialog.setToDoListener(new ToDoListener() {

				@Override
				public void doSomething() {
					confirnDelivery();
				}
			});
		}
		confirnDeliveryDialog.show();
	}

	/**
	 * 弹出确认退出对话框让用户选择
	 */
	private void showExitDialog() {
		if (exitDialog == null) {
			exitDialog = new CustomDialog(this, R.style.myDialogTheme);
			exitDialog.setContentText("是否确认退出发货界面？");
			exitDialog.setToDoListener(new ToDoListener() {

				@Override
				public void doSomething() {
					// deleteAllPic();
					finish();
				}
			});
		}
		exitDialog.show();
	}

	/**
	 * 
	 * @Title: deleteAllPic
	 * @Description: 如果未发货退出的话，则删除所有的图片
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void deleteAllPic() {
		DeleteAllDeliveryPhotoAsyncTask deleteAllDeliveryPhotoAsyncTask = new DeleteAllDeliveryPhotoAsyncTask(this, workID);
		deleteAllDeliveryPhotoAsyncTask.setDelAllDeliveryListener(new DelAllDeliveryListener() {

			@Override
			public void delAllDeliveryResult(ResultModel resultModel) {
				Tools.showToast(DeliveryActivity.this, "已清空");
			}
		});
		deleteAllDeliveryPhotoAsyncTask.execute();
	}

	/**
	 * 
	 * @Title: deliveryContinue
	 * @Description: 发货续传
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void deliveryContinue() {
		DeliveryContinueAsyncTask deliveryContinueAsyncTask = new DeliveryContinueAsyncTask(this.orderBuffer.toString());
		deliveryContinueAsyncTask.setDeliveryContinueListener(new DeliveryContinueListener() {

			@Override
			public void deliveryContinueResult(TempScanBimpModels deliveryScanListModel) {
				if (deliveryScanListModel != null) {
					if (deliveryScanListModel.getResult().equals("true")) {
						if (deliveryScanListModel.getData() != null && deliveryScanListModel.getData().size() > 0) {
							workID = deliveryScanListModel.getData().get(0).getWorkID();
							showContinueDialog(deliveryScanListModel.getData(), deliveryScanListModel.getDescription());
						}
					}
				}
			}
		});
		deliveryContinueAsyncTask.execute();
	}

	/**
	 * 
	 * @Title: confirnDelivery
	 * @Description: 确认发货
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void confirnDelivery() {
		ConfirnDeliveryAsyncTask confirnDeliveryAsyncTask = new ConfirnDeliveryAsyncTask(this, workID, orderBuffer.toString(), this.locationModel.getName(),
				this.locationModel.getAddress(), String.valueOf(this.locationModel.getLng()), String.valueOf(this.locationModel.getLat()), remark_txt.getText()
						.toString().trim());
		confirnDeliveryAsyncTask.setDeliveryListener(new DeliveryListener() {

			@Override
			public void deliveryResult(ResultModel rm) {
				if (rm != null) {
					if (rm.getResult().equals("true")) {
						Tools.showToast(DeliveryActivity.this, "发货成功");
						ContactsUtils.ISUPLOAD_LOC = true;// 开始上传位置
						finish();
					} else {
						Tools.showToast(DeliveryActivity.this, rm.getDescription());
					}
				} else {
					Tools.showToast(DeliveryActivity.this, "发货失败，请检查网络");
				}
			}
		});
		confirnDeliveryAsyncTask.execute();

	}

	/*
	 * (非 Javadoc) <p>Title: getTopView</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getTopView()
	 */
	@Override
	protected View getTopView() {
		View topView = inflater.inflate(R.layout.custom_title_bar, null);

		return topView;
	}

	/*
	 * (非 Javadoc) <p>Title: getCenterView</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getCenterView()
	 */

	@Override
	protected View getCenterView() {
		View contentView = inflater.inflate(R.layout.delivery_new, null);
		return contentView;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		topView.findViewById(R.id.title_left_img).setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("发货");
		workID = Tools.getGUID();
		locationModel = new LocationModel();
		orderBuffer = new StringBuffer();
		orders = (ArrayList<Order>) getIntent().getExtras().getSerializable("orders");
		for (int i = 0; i < orders.size(); i++) {
			NUM += orders.get(i).getQuantity();
			if (i + 1 == orders.size()) {
				orderBuffer.append(orders.get(i).getOrderID());
			} else {
				orderBuffer.append(orders.get(i).getOrderID() + ",");
			}

		}

		delivery_btn = (Button) centerView.findViewById(R.id.delivery_btn);
		delivery_btn.setOnClickListener(this);
		add_source_btn = (Button) centerView.findViewById(R.id.add_source_btn);
		add_source_btn.setOnClickListener(this);
		order_list = (DefineListView) centerView.findViewById(R.id.order_list);
		goods_package_count_list = (DefineListView) centerView.findViewById(R.id.goods_package_count_list);
		OrderListAdapter orderListAdapter = new OrderListAdapter(this, orders, false);
		order_list.setAdapter(orderListAdapter);
		order_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> v, View arg1, int position, long arg3) {
				Order order = (Order) v.getAdapter().getItem(position);
				Intent intent = new Intent(DeliveryActivity.this, OrderDetailActivity.class);
				intent.putExtra("orderId", order.getOrderID());
				startActivity(intent);
			}
		});
		location_str_txt = (TextView) centerView.findViewById(R.id.location_str_txt);
		if (ContactsUtils.baseAMapLocation != null) {
			LocationUtils locationUtils = new LocationUtils(this);
			locationUtils.searchRound(this, ContactsUtils.baseAMapLocation.getLatitude(), ContactsUtils.baseAMapLocation.getLongitude(), 0,
					ContactsUtils.ORP_NONE, new PoiSearchListener() {

						@Override
						public void onPoiSearched(PoiResult poiResult, int resultCode, int orpType) {
							if (resultCode == 0) {
								List<PoiItem> pois = poiResult.getPois();
								Message msg = new Message();
								msg.what = 0;
								msg.obj = pois;
								mHandler.sendMessage(msg);
							}
						}
					});
		}
		location_str_txt.setOnClickListener(this);
		remark_txt = (EditText) centerView.findViewById(R.id.remark_txt);
		deliveryContinue();
		getGoodsCount();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case ContactsUtils.CHOOSE_LOCATION_RESULT:
			if (data != null) {
				PoiItem poiInfo = (PoiItem) data.getParcelableExtra("poiInfo");
				locationModel.setAddress(poiInfo.getSnippet());
				locationModel.setName(poiInfo.getTitle());
				locationModel.setLat(poiInfo.getLatLonPoint().getLatitude());
				locationModel.setLng(poiInfo.getLatLonPoint().getLongitude());
				location_str_txt.setText(poiInfo.getTitle());
				location_str_txt.setTextColor(getResources().getColor(R.color.orange_color));
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 105&116&114&103&72&75&75&75&78&76&76&72&75&75&
	 * 
	 * @Title: getGoodsCount
	 * @Description:获取货物个数
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getGoodsCount() {
		GetGoodsCountByOrderIDSAsyncTask getGoodsCountByOrderIDSAsyncTask = new GetGoodsCountByOrderIDSAsyncTask(this, orderBuffer.toString());
		getGoodsCountByOrderIDSAsyncTask.setGetGoodsCountListener(new GetGoodsCountListener() {

			@Override
			public void getGoodsCountResult(GoodsPackageQtyListModel goodsPackageQtyListModel) {
				if (goodsPackageQtyListModel != null) {
					if (goodsPackageQtyListModel.getResult().equals("true")) {
						ArrayList<GoodsPackageQtyModel> data = goodsPackageQtyListModel.getData();
						for (int i = 0; i < data.size(); i++) {
							GoodsPackageQtyModel goodsPackageQtyModel = data.get(i);
							skuMap.put(goodsPackageQtyModel.getGoodsnameID(), goodsPackageQtyModel.getQuantity());
							selectSkuMap.put(goodsPackageQtyModel.getGoodsnameID(), 0);
						}
						GoodsPkgCountListAdapter goodsPkgCountListAdapter = new GoodsPkgCountListAdapter(DeliveryActivity.this, data);
						goods_package_count_list.setAdapter(goodsPkgCountListAdapter);
					} else {
						Tools.showToast(DeliveryActivity.this, goodsPackageQtyListModel.getDescription());
					}
				} else {
					Tools.showToast(DeliveryActivity.this, "发货明细获取失败，请检查网络");
				}
			}
		});
		getGoodsCountByOrderIDSAsyncTask.execute();
	}

	@Override
	public void onBackPressed() {
		showExitDialog();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (NUM != 0) {
			if (UploadCache.scanBimpModels.size() > 0) {
				add_source_btn.setTextColor(getResources().getColor(R.color.orange_color));
				add_source_btn.setText("点击查看，已添加" + UploadCache.scanBimpModels.size() + "/" + NUM);
			} else {
				add_source_btn.setTextColor(getResources().getColor(R.color.middle_gray));
				add_source_btn.setText("添加货物信息");
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		UploadCache.resetBimp();// 重置缓存
		DeliveryActivity.NUM = 0; // 数量清空
	}

}
