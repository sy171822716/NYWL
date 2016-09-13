/**   
 * @Title: AddSourceInfoActivity.java 
 * @Package com.resmanager.client.user.order.delivery 
 * @Description: 添加货物明细
 * @author ShenYang   
 * @date 2015-12-22 下午3:52:38 
 * @version V1.0   
 */
package com.resmanager.client.user.order.delivery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.main.photoalbum.Bimp;
import com.resmanager.client.model.GoodsListModel;
import com.resmanager.client.model.GoodsModel;
import com.resmanager.client.model.LocationModel;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.model.ScanBimpModel;
import com.resmanager.client.scan.CatpureActivity;
import com.resmanager.client.user.order.UploadCache;
import com.resmanager.client.user.order.delivery.UploadImageAsyncTask.UploadResourceListener;
import com.resmanager.client.user.order.goods.GetGoodsByOrderIDAsyncTask;
import com.resmanager.client.user.order.goods.GoodsListActivity;
import com.resmanager.client.user.order.goods.GetGoodsByOrderIDAsyncTask.GetGoodsByOrderIdListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.DESUtils;
import com.resmanager.client.utils.FileUtils;
import com.resmanager.client.utils.LocationUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.utils.LocationUtils.PoiSearchListener;
import com.resmanager.client.view.CustomDialog;
import com.resmanager.client.view.CustomDialog.ToDoListener;

/**
 * @ClassName: AddSourceInfoActivity
 * @Description: 添加货物明细
 * @author ShenYang
 * @date 2015-12-22 下午3:52:38
 * 
 */
@SuppressLint({ "InflateParams", "HandlerLeak" })
public class AddSourceInfoActivity extends TopContainActivity implements OnClickListener {
	private ImageView resource_img;// 货物图片
	private RadioGroup recyle_isneed_rb;// 是否需要回收
	private RadioButton need_rb,noneed_rb;
	private TextView scan_flag_txt, choose_resource_type_txt;
	private Button uploading_btn;
	private ScanBimpModel scanBimpModel = new ScanBimpModel();// 扫描类
	private CustomDialog noticeDialog;
	private int isneedrecyle = 1;// 是否需要回收0否，1是，默认需要回收
	private String workId;
	private String orderIds;
	private LocationModel locationModel = null;
	private ArrayList<GoodsModel> goodsModels;
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
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_img:
			this.finish();
			break;
		case R.id.resource_img:
			Tools.takePhoto(this);
			break;
		case R.id.uploading_btn:
			checkUploading();// 检查上传
			break;
		case R.id.choose_resource_type_txt:
			if (goodsModels != null && goodsModels.size() > 0) {
				Intent chooseGoodsIntent = new Intent(AddSourceInfoActivity.this, GoodsListActivity.class);
				chooseGoodsIntent.putExtra("goodsModels", goodsModels);
				startActivityForResult(chooseGoodsIntent, ContactsUtils.CHOOSE_GOODS_RESULT);
			} else {
				Tools.showToast(this, "暂无货物规格信息");
			}
			break;
		case R.id.scan_flag_txt:
			Intent scanIntent = new Intent(this, CatpureActivity.class);
			scanIntent.putExtra("flagType", -1);// 没有用到
			startActivityForResult(scanIntent, ContactsUtils.SCAN_RESULT);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: checkUploading
	 * @Description: 检查桶信息
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void checkUploading() {
		String noticeStr = "";
		if (scanBimpModel.getBmp() == null || scanBimpModel.getBmpPath().equals("")) {
			noticeStr = "请添加货物图片";
		} else if (scanBimpModel.getLabelCode().equals("")) {
			noticeStr = "请添加货物二维码";
		} else if (scanBimpModel.getResourceTypeId().equals("")) {
			noticeStr = "请选择货物规格";
		} else {
			scanBimpModel.setRecyle(isneedrecyle);// 设置是否需要回收
			if (noticeDialog == null) {
				noticeDialog = new CustomDialog(this, R.style.myDialogTheme);
				noticeDialog.setContentText("请确认货物信息是否填写完毕");
				noticeDialog.setToDoListener(new ToDoListener() {

					@Override
					public void doSomething() {
						noticeDialog.dismiss();
						// 上传
						uploadImg(scanBimpModel.getBmp(), DESUtils.decrypt(scanBimpModel.getLabelCode()), scanBimpModel.getResourceTypeId(),
								scanBimpModel.isRecyle());
					}
				});
			}
			noticeDialog.show();
		}
		if (!noticeStr.equals("")) {
			Tools.showToast(this, noticeStr);
		}
	}

	/*
	 * (非 Javadoc) <p>Title: getTopView</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getTopView()
	 */
	@SuppressLint("InflateParams")
	@Override
	protected View getTopView() {
		return inflater.inflate(R.layout.custom_title_bar, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		locationModel = new LocationModel();
		orderIds = getIntent().getExtras().getString("orderIds");
		workId = getIntent().getExtras().getString("workId");// 批次号
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("添加货物");
		uploading_btn = (Button) centerView.findViewById(R.id.uploading_btn);
		resource_img = (ImageView) centerView.findViewById(R.id.resource_img);
		recyle_isneed_rb = (RadioGroup) centerView.findViewById(R.id.recyle_isneed_rb);
		scan_flag_txt = (TextView) centerView.findViewById(R.id.scan_flag_txt);
		need_rb = (RadioButton) centerView.findViewById(R.id.need_rb);
		noneed_rb = (RadioButton) centerView.findViewById(R.id.noneed_rb);
		scan_flag_txt.setOnClickListener(this);
		choose_resource_type_txt = (TextView) centerView.findViewById(R.id.choose_resource_type_txt);
		choose_resource_type_txt.setOnClickListener(this);
		uploading_btn = (Button) centerView.findViewById(R.id.uploading_btn);
		uploading_btn.setOnClickListener(this);
		resource_img.setOnClickListener(this);
		recyle_isneed_rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int vid) {
				switch (vid) {
				case R.id.need_rb:
					isneedrecyle = 1;
					break;
				case R.id.noneed_rb:
					isneedrecyle = 0;
				default:
					break;
				}
			}
		});
		getGoodsByOrderIds();
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
	}

	@Override
	protected View getCenterView() {
		return inflater.inflate(R.layout.scan_info_upload, null);
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

		case ContactsUtils.CHOOSE_GOODS_RESULT:
			if (data != null) {
				GoodsModel goodsModel = (GoodsModel) data.getExtras().getSerializable("goodsModel");
				scanBimpModel.setResourceTypeId(goodsModel.getGoodsnameID());
				scanBimpModel.setResourceTypeName(goodsModel.getGoodsName());
				scanBimpModel.setPackageType(goodsModel.getPackagetype());
				choose_resource_type_txt.setText(goodsModel.getGoodsName());
				choose_resource_type_txt.setTextColor(getResources().getColor(R.color.red));
			}
			break;
		case ContactsUtils.SCAN_RESULT:
			if (data != null) {
				String flagContent = data.getStringExtra("result");// 加密后的二维码内容，解密后传递给接口进行校验
				scan_flag_txt.setTextColor(getResources().getColor(R.color.middle_gray));
				try {
					scan_flag_txt.setText(Tools.getShowLabelCode(flagContent));
					scan_flag_txt.setTextColor(getResources().getColor(R.color.red));
					scanBimpModel.setLabelCode(flagContent);
				} catch (Exception e) {
					Tools.showToast(this, "非法的二维码标签");
					e.printStackTrace();
				}
			}
			break;

		}
	}

	/**
	 * HANDLER
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Bitmap bm = (Bitmap) msg.obj;
				if (bm != null) {

					resource_img.setImageBitmap(bm);
				}
				break;
			}
			super.handleMessage(msg);
		}
	};

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
					scanBimpModel.setBmp(bm);
					scanBimpModel.setBmpPath(path);
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

	/**
	 * 
	 * @Title: uploadImg
	 * @Description: 上传图片
	 * @param @param path 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void uploadImg(Bitmap bmp, String flagContent, final String goodsId, int isRecyle) {
		UploadImageAsyncTask uploadImageAsyncTask = new UploadImageAsyncTask(this, bmp, flagContent, workId, goodsId, isRecyle, this.orderIds,
				this.locationModel.getName(), this.locationModel.getAddress(), String.valueOf(this.locationModel.getLng()), String.valueOf(this.locationModel
						.getLat()));
		uploadImageAsyncTask.setUploadResourceListener(new UploadResourceListener() {

			@Override
			public void uploadResult(ResultModel resultModel, Bitmap bmp, String flagContent) {
				if (resultModel != null) {
					if (resultModel.getResult().equals("true")) {
						if (DeliveryActivity.selectSkuMap.containsKey(goodsId)) {
							int num = DeliveryActivity.selectSkuMap.get(goodsId);
							num += 1;
							DeliveryActivity.selectSkuMap.put(goodsId, num);
						}
						UploadCache.scanBimpModels.add(scanBimpModel);// 上传成功后添加至缓存
						Tools.showToast(AddSourceInfoActivity.this, "货物添加成功");
						Intent intent = new Intent(AddSourceInfoActivity.this, DeliverySourceGrid.class);
						intent.putExtra("workId", workId);
						intent.putExtra("orderIds", orderIds);
						startActivity(intent);
						finish();
					} else {
						Tools.showToast(AddSourceInfoActivity.this, resultModel.getDescription());
					}
				} else {
					Tools.showToast(AddSourceInfoActivity.this, "货物添加失败，请检查网络");
				}
			}
		});
		uploadImageAsyncTask.execute();
	}

	private void getGoodsByOrderIds() {
		GetGoodsByOrderIDAsyncTask getGoodsByOrderIDAsyncTask = new GetGoodsByOrderIDAsyncTask(this, orderIds.toString(), ContactsUtils.ORP_NONE);
		getGoodsByOrderIDAsyncTask.setGetGoodsByOrderIdListener(new GetGoodsByOrderIdListener() {

			@Override
			public void getGoodsResult(GoodsListModel goodsListModel, int orpType) {
				if (goodsListModel != null) {
					if (goodsListModel.getResult().equals("true")) {
						goodsModels = goodsListModel.getData();
						if (goodsModels != null) {
							if (goodsModels.size() == 1) {
								// 默认显示
								GoodsModel goodsModel = goodsModels.get(0);
								scanBimpModel.setResourceTypeId(goodsModel.getGoodsnameID());
								scanBimpModel.setResourceTypeName(goodsModel.getGoodsName());
								scanBimpModel.setPackageType(goodsModel.getPackagetype());
								choose_resource_type_txt.setText(goodsModel.getGoodsName());
								choose_resource_type_txt.setTextColor(getResources().getColor(R.color.red));
								if(goodsModel.getPackagetype().equals("油罐")){
									noneed_rb.setChecked(true);
									need_rb.setChecked(false);
									need_rb.setEnabled(false);
								}
							}
						}
					} else {
						Tools.showToast(AddSourceInfoActivity.this, goodsListModel.getDescription());
					}
				} else {
					Tools.showToast(AddSourceInfoActivity.this, "订单产品获取失败，请检查网络");
				}
			}
		});
		getGoodsByOrderIDAsyncTask.execute();
	}

}
