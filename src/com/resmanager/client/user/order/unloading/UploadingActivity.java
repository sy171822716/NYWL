/**   
 * @Title: DeliveryActivity.java 
 * @Package com.resmanager.client.user.order 
 * @Description:卸货
 * @author ShenYang  
 * @date 2015-12-6 下午4:45:53 
 * @version V1.0   
 */
package com.resmanager.client.user.order.unloading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.main.photoalbum.Bimp;
import com.resmanager.client.model.GoodsPackageQtyListModel;
import com.resmanager.client.model.GoodsPackageQtyModel;
import com.resmanager.client.model.LocationModel;
import com.resmanager.client.model.Order;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.order.OrderDetailActivity;
import com.resmanager.client.order.OrderListAdapter;
import com.resmanager.client.user.order.UploadCache;
import com.resmanager.client.user.order.delivery.GetGoodsCountByOrderIDSAsyncTask;
import com.resmanager.client.user.order.delivery.GoodsPkgCountListAdapter;
import com.resmanager.client.user.order.delivery.GetGoodsCountByOrderIDSAsyncTask.GetGoodsCountListener;
import com.resmanager.client.user.order.unloading.ConfirnUploadingAsyncTask.UploadingListener;
import com.resmanager.client.user.order.unloading.GetDischargedPicsAsyncTask.GetDischaragePicListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.LocationUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.utils.LocationUtils.PoiSearchListener;
import com.resmanager.client.view.CustomDialog;
import com.resmanager.client.view.DefineListView;
import com.resmanager.client.view.CustomDialog.ToDoListener;

/**
 * @ClassName: DeliveryActivity
 * @Description: 卸货
 * @author ShenYang
 * @date 2015-12-6 下午4:45:53
 * 
 */
@SuppressLint({ "InflateParams", "UseSparseArrays", "HandlerLeak" })
public class UploadingActivity extends TopContainActivity implements OnClickListener {
	private Button add_source_btn, uploading_btn;
	private ArrayList<Order> orders;
	private DefineListView order_list;
	private TextView location_str_txt, resource_recyle_txt, source_num_txt;
	private LocationModel locationModel;
	// 用于存放临时卸载图片以及二维码
	private EditText uploading_remark_txt, qianshou_man_edit, qianshou_man_phone_edit;
	private CustomDialog confirmUploadingDialog, exitDialog;
	private ImageView add_upload_id_img;
	private Bitmap uploading_id = null;
	public static int NUM = 0;
	private DefineListView goods_package_count_list;
	public static Map<String, Integer> skuMap = new HashMap<String, Integer>();
	public static Map<String, Integer> selectSkuMap = new HashMap<String, Integer>();
	private int IsInsteadXH = 0;// 是否代卸貨

	/**
	 * 
	 * @Title: getDischargePics
	 * @Description: 获取之前上传的图片标签信息
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getDischargePics() {
		GetDischargedPicsAsyncTask getDischargedPicsAsyncTask = new GetDischargedPicsAsyncTask(this, orders.get(0).getOrderID());
		getDischargedPicsAsyncTask.setGetDischaragePicListener(new GetDischaragePicListener() {

			@Override
			public void getDischaragePicReuslt() {
				tongji();
			}
		});
		getDischargedPicsAsyncTask.execute();
	}

	/**
	 * HANDLER
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Bitmap bm = (Bitmap) msg.obj;
				if (bm != null) {
					uploading_id = bm;
					add_upload_id_img.setImageBitmap(bm);
				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				List<PoiItem> pois = (List<PoiItem>) msg.obj;

				if (pois.size() > 0) {
					PoiItem poiItem = pois.get(0);
					for (int i = 0; i < pois.size(); i++) {
						PoiItem tempItem = pois.get(i);
						if (tempItem.getTitle().equals(orders.get(0).getOrdercustomer())) {
							poiItem = tempItem;
						}
					}
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

	/**
	 * 弹出对话框让用户最后一次选择
	 */
	private void showConfirmDialog() {
		if (confirmUploadingDialog == null) {
			confirmUploadingDialog = new CustomDialog(this, R.style.myDialogTheme);
			confirmUploadingDialog.setContentText("提交审核前，请确认卸货信息是否填写完毕<br>确认提交？");
			confirmUploadingDialog.setToDoListener(new ToDoListener() {

				@Override
				public void doSomething() {
					confirnUploading();
				}
			});
		}
		confirmUploadingDialog.show();
	}

	/**
	 * 弹出确认退出对话框让用户选择
	 */
	private void showExitDialog() {
		if (exitDialog == null) {
			exitDialog = new CustomDialog(this, R.style.myDialogTheme);
			exitDialog.setContentText("是否确认退出卸货界面？");
			exitDialog.setToDoListener(new ToDoListener() {

				@Override
				public void doSomething() {
					exitDialog.dismiss();
					finish();
				}
			});
		}
		exitDialog.show();
	}

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
			if (UploadCache.scanBimpModels.size() > 0 && UploadCache.scanBimpModels.size() <= NUM) {
				Intent uploadIntent = new Intent(UploadingActivity.this, UploadSourceGrid.class);
				uploadIntent.putExtra("order", orders.get(0));
				startActivityForResult(uploadIntent, ContactsUtils.ADD_RESOURCE_RESULT);
			} else {
				Intent addIntent = new Intent(UploadingActivity.this, AddUploadSourceInfoActivity.class);
				addIntent.putExtra("order", orders.get(0));
				startActivity(addIntent);
			}
			break;
		case R.id.location_str_txt:
			// Intent chooseLocationIntent = new Intent(UploadingActivity.this,
			// ChooseLocationActivity.class);
			// chooseLocationIntent.putExtra("current_location",
			// location_str_txt.getText().toString());
			// startActivityForResult(chooseLocationIntent,
			// ContactsUtils.CHOOSE_LOCATION_RESULT);
			break;

		case R.id.uploading_btn:
			if (UploadingActivity.NUM != UploadCache.scanBimpModels.size()) {
				Tools.showToast(this, "卸货数量与订单货物数量不符");
			} else if (uploading_id == null) {
				Tools.showToast(this, "请上传卸货单照片");
			} else if (qianshou_man_edit.getText().toString().trim().equals("")) {
				Tools.showToast(this, "请输入签收人");
			} else if (qianshou_man_phone_edit.getText().toString().trim().equals("")) {
				Tools.showToast(this, "请输入签收人联系方式");
			} else {
				showConfirmDialog();
			}
			break;
		case R.id.add_upload_id_img:
			Tools.takePhoto(this);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: getGoodsCount
	 * @Description:获取货物个数
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getGoodsCount(String orderId) {
		GetGoodsCountByOrderIDSAsyncTask getGoodsCountByOrderIDSAsyncTask = new GetGoodsCountByOrderIDSAsyncTask(this, orderId);
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
						GoodsPkgCountListAdapter goodsPkgCountListAdapter = new GoodsPkgCountListAdapter(UploadingActivity.this, data);
						goods_package_count_list.setAdapter(goodsPkgCountListAdapter);
					} else {
						Tools.showToast(UploadingActivity.this, goodsPackageQtyListModel.getDescription());
					}
				} else {
					Tools.showToast(UploadingActivity.this, "发货明细获取失败，请检查网络");
				}
			}
		});
		getGoodsCountByOrderIDSAsyncTask.execute();
	}

	/**
	 * 
	 * @Title: confirnDelivery
	 * @Description: 确认卸货
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void confirnUploading() {
		if (this.locationModel != null && this.locationModel.getAddress() != null && !this.locationModel.getAddress().equals("")) {
			ConfirnUploadingAsyncTask confirnDeliveryAsyncTask = new ConfirnUploadingAsyncTask(this, orders.get(0).getWorkID(), orders.get(0).getOrderID(),
					orders.get(0).getSaleoid(), this.locationModel.getName(), this.locationModel.getAddress(), String.valueOf(this.locationModel.getLng()),
					String.valueOf(this.locationModel.getLat()), uploading_remark_txt.getText().toString(), qianshou_man_edit.getText().toString().trim(),
					qianshou_man_phone_edit.getText().toString().trim(), uploading_id, IsInsteadXH);
			confirnDeliveryAsyncTask.setUploadingListener(new UploadingListener() {

				@Override
				public void uploadingResult(ResultModel rm) {
					if (rm != null) {
						if (rm.getResult().equals("true")) {
							ContactsUtils.ISUPLOAD_LOC = false;// 禁止上传位置
							Tools.showToast(UploadingActivity.this, "卸货成功，订单已提交");
							finish();
						} else {
							Tools.showToast(UploadingActivity.this, rm.getDescription());
						}
					} else {
						Tools.showToast(UploadingActivity.this, "卸货失败，请检查网络");
					}
				}
			});
			confirnDeliveryAsyncTask.execute();
		} else {
			Tools.showToast(this, "位置获取失败，请稍后再试");
		}
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
		topView.findViewById(R.id.title_left_img).setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("卸货");
		return topView;
	}

	// /**
	// *
	// * @Title: deleteAllUploadingPhoto
	// * @Description: 删除之前上传的无用图片
	// * @param 设定文件
	// * @return void 返回类型
	// * @throws
	// */
	// private void deleteAllUploadingPhoto() {
	// DeleteAllUploadingPhotoAsyncTask deleteAllUploadingPhotoAsyncTask = new
	// DeleteAllUploadingPhotoAsyncTask(this, orders.get(0).getWorkID());
	// deleteAllUploadingPhotoAsyncTask.setDelAllUploadingListener(new
	// DelAllUploadingListener() {
	//
	// @Override
	// public void delAllUploadingResult(ResultModel resultModel) {
	// }
	// });
	// deleteAllUploadingPhotoAsyncTask.execute();
	// }

	/*
	 * (非 Javadoc) <p>Title: getCenterView</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getCenterView()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected View getCenterView() {
		locationModel = new LocationModel();
		orders = (ArrayList<Order>) getIntent().getExtras().getSerializable("orders");
		IsInsteadXH = getIntent().getIntExtra("IsInsteadXH", 0);
		getDischargePics();
		UploadingActivity.NUM = orders.get(0).getQuantity();// 数量
		// deleteAllUploadingPhoto();// 防止之前上传过的冗余数据，先删除
		View contentView = inflater.inflate(R.layout.uploading_layout, null);
		goods_package_count_list = (DefineListView) contentView.findViewById(R.id.goods_package_count_list);
		uploading_btn = (Button) contentView.findViewById(R.id.uploading_btn);
		uploading_btn.setOnClickListener(this);
		add_source_btn = (Button) contentView.findViewById(R.id.add_source_btn);
		add_source_btn.setOnClickListener(this);
		resource_recyle_txt = (TextView) contentView.findViewById(R.id.resource_recyle_txt);
		resource_recyle_txt.setOnClickListener(this);
		order_list = (DefineListView) contentView.findViewById(R.id.order_list);
		uploading_remark_txt = (EditText) contentView.findViewById(R.id.uploading_remark_txt);
		qianshou_man_edit = (EditText) contentView.findViewById(R.id.qianshou_man_edit);
		qianshou_man_phone_edit = (EditText) contentView.findViewById(R.id.qianshou_man_phone_edit);
		source_num_txt = (TextView) contentView.findViewById(R.id.source_num_txt);
		OrderListAdapter orderListAdapter = new OrderListAdapter(this, orders, false);
		order_list.setAdapter(orderListAdapter);
		order_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> v, View arg1, int position, long arg3) {
				Order order = (Order) v.getAdapter().getItem(position);
				Intent intent = new Intent(UploadingActivity.this, OrderDetailActivity.class);
				intent.putExtra("orderId", order.getOrderID());
				startActivity(intent);
			}
		});
		location_str_txt = (TextView) contentView.findViewById(R.id.location_str_txt);
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
		source_num_txt.setText("(数量:" + orders.get(0).getQuantity() + ")");
		add_upload_id_img = (ImageView) contentView.findViewById(R.id.add_upload_id_img);
		add_upload_id_img.setOnClickListener(this);
		getGoodsCount(orders.get(0).getOrderID());
		return contentView;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ContactsUtils.TAKE_PHOTO_RESULT:
			if (data != null) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					String path = extras.getString("image_path");
					loading(path);
				}
			}
			break;
		}
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

	@Override
	protected void onResume() {
		super.onResume();
		tongji();

	}

	/**
	 * 
	 * @Title: tongji
	 * @Description: 统计上传了多少桶
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void tongji() {
		if (UploadingActivity.NUM != 0) {
			if (UploadCache.scanBimpModels.size() > 0) {
				add_source_btn.setTextColor(getResources().getColor(R.color.orange_color));
				add_source_btn.setText("点击查看，已添加" + UploadCache.scanBimpModels.size() + "/" + UploadingActivity.NUM);
			} else {
				add_source_btn.setTextColor(getResources().getColor(R.color.middle_gray));
				add_source_btn.setText("添加货物信息");
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		UploadCache.resetBimp();
		UploadingActivity.NUM = 0; // 数量清空
	}

	/**
	 * 
	 * @Title: loading
	 * @Description: 异步加载图片
	 * @param @param path 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void loading(final String path) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Bitmap bm = Bimp.revitionImageSize(path);
					Message message = new Message();
					message.what = 1;
					message.obj = bm;
					handler.sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
