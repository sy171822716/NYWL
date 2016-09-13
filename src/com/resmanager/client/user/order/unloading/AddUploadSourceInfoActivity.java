/**   
 * @Title: AddSourceInfoActivity.java 
 * @Package com.resmanager.client.user.order.delivery 
 * @Description: 添加货物明细
 * @author ShenYang   
 * @date 2015-12-22 下午3:52:38 
 * @version V1.0   
 */
package com.resmanager.client.user.order.unloading;

import java.io.IOException;
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
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.main.photoalbum.Bimp;
import com.resmanager.client.model.GoodsModel;
import com.resmanager.client.model.GoodsResultmodel;
import com.resmanager.client.model.LocationModel;
import com.resmanager.client.model.Order;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.model.ScanBimpModel;
import com.resmanager.client.scan.CatpureActivity;
import com.resmanager.client.user.order.UploadCache;
import com.resmanager.client.user.order.unloading.GetInfoByLabelCode.GetInfoByLabelListener;
import com.resmanager.client.user.order.unloading.UploadingImageAsyncTask.UploadUploadingResourceListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.DESUtils;
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
public class AddUploadSourceInfoActivity extends TopContainActivity implements OnClickListener {
	private ImageView resource_img;// 货物图片
	private TextView scan_flag_txt, resource_typename_txt, resource_package_type_txt;
	private Button uploading_btn;
	private ScanBimpModel scanBimpModel = new ScanBimpModel();// 扫描类
	private CustomDialog noticeDialog;
	private Order order;
	private LocationModel locationModel = null;
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
	 * @Description: 确认卸货
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
			noticeStr = "请重新扫描二维码";
		} else {
			if (noticeDialog == null) {
				noticeDialog = new CustomDialog(this, R.style.myDialogTheme);
				noticeDialog.setContentText("是否确认添加至卸货列表?");
				noticeDialog.setToDoListener(new ToDoListener() {

					@Override
					public void doSomething() {
						noticeDialog.dismiss();
						// 上传
						uploadImg(scanBimpModel.getBmp(), DESUtils.decrypt(scanBimpModel.getLabelCode()));
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
		order = (Order) getIntent().getSerializableExtra("order");
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("添加货物");
		uploading_btn = (Button) centerView.findViewById(R.id.uploading_btn);
		resource_img = (ImageView) centerView.findViewById(R.id.resource_img);
		resource_typename_txt = (TextView) centerView.findViewById(R.id.resource_typename_txt);
		resource_package_type_txt = (TextView) centerView.findViewById(R.id.resource_package_type_txt);
		scan_flag_txt = (TextView) centerView.findViewById(R.id.scan_flag_txt);
		scan_flag_txt.setOnClickListener(this);
		uploading_btn = (Button) centerView.findViewById(R.id.uploading_btn);
		uploading_btn.setOnClickListener(this);
		resource_img.setOnClickListener(this);
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
		return inflater.inflate(R.layout.uploading_scan_info, null);
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
					getLabelDetail(DESUtils.decrypt(flagContent));
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
	public void uploadImg(Bitmap bmp, String labelCode) {
		UploadingImageAsyncTask uploadingImageAsyncTask = new UploadingImageAsyncTask(this, order.getOrderID(), order.getSaleoid(), order.getWorkID(),
				labelCode, bmp, this.locationModel.getName(), this.locationModel.getAddress(), String.valueOf(this.locationModel.getLat()),
				String.valueOf(this.locationModel.getLng()));
		uploadingImageAsyncTask.setUploadUploadingResourceListener(new UploadUploadingResourceListener() {

			@Override
			public void uploadUploadingResult(ResultModel resultModel, Bitmap bmp, String flagContent) {
				if (resultModel != null) {
					if (resultModel.getResult().equals("true")) {
						UploadCache.scanBimpModels.add(scanBimpModel);// 上传成功后添加至缓存
						String goodsId = scanBimpModel.getResourceTypeId();
						if (UploadingActivity.selectSkuMap.containsKey(goodsId)) {
							int num = UploadingActivity.selectSkuMap.get(goodsId);
							num += 1;
							UploadingActivity.selectSkuMap.put(goodsId, num);
						}
						Tools.showToast(AddUploadSourceInfoActivity.this, "货物添加成功");
						Intent uploadIntent = new Intent(AddUploadSourceInfoActivity.this, UploadSourceGrid.class);
						uploadIntent.putExtra("order", order);
						startActivity(uploadIntent);
						finish();
					} else {
						Tools.showToast(AddUploadSourceInfoActivity.this, resultModel.getDescription());
					}
				} else {
					Tools.showToast(AddUploadSourceInfoActivity.this, "货物添加失败，请检查网络");
				}
			}
		});
		uploadingImageAsyncTask.execute();
	}

	/**
	 * 
	 * @Title: getLabelDetail
	 * @Description: 获取标签详细信息
	 * @param @param labelCode 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getLabelDetail(String labelCode) {
		GetInfoByLabelCode getInfoByLabelCode = new GetInfoByLabelCode(this, labelCode, order.getWorkID());
		getInfoByLabelCode.setGetInfoByLabelListener(new GetInfoByLabelListener() {

			@Override
			public void getInfoByLabelResult(GoodsResultmodel goodsResultmodel) {
				if (goodsResultmodel != null) {

					if (goodsResultmodel.getResult().equals("true")) {
						if (goodsResultmodel.getData() != null) {
							String goodsNameId = goodsResultmodel.getData().getGoodsnameID();
							if (UploadingActivity.skuMap.containsKey(goodsNameId)) {
								int num = UploadingActivity.skuMap.get(goodsNameId);
								if (UploadingActivity.selectSkuMap != null) {
									if (UploadingActivity.selectSkuMap.containsKey(goodsNameId)) {
										int selectNum = UploadingActivity.selectSkuMap.get(goodsNameId);
										if (selectNum == num) {
											Tools.showToast(AddUploadSourceInfoActivity.this, "该类型货物已全部添加");
											initGoodsInfo(null);
											return;
										}
									}
								}
								initGoodsInfo(goodsResultmodel.getData());
							} else {
								initGoodsInfo(null);
								Tools.showToast(AddUploadSourceInfoActivity.this, "发货订单中不包含此货物型号");
							}

						} else {
							Tools.showToast(AddUploadSourceInfoActivity.this, "标签信息获取失败，请检查网络");
						}
					} else {
						initGoodsInfo(null);
						Tools.showToast(AddUploadSourceInfoActivity.this, goodsResultmodel.getDescription());
					}
				} else {
					initGoodsInfo(null);
					Tools.showToast(AddUploadSourceInfoActivity.this, "标签信息获取失败，请检查网络");
				}
			}
		});
		getInfoByLabelCode.execute();
	}

	/**
	 * 
	 * @Title: initGoodsInfo
	 * @Description: 显示标签信息
	 * @param @param goodsModel 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void initGoodsInfo(GoodsModel goodsModel) {
		if (goodsModel != null) {
			resource_typename_txt.setVisibility(View.VISIBLE);
			resource_package_type_txt.setVisibility(View.VISIBLE);
			resource_package_type_txt.setText("包装：" + goodsModel.getPackagetype());
			resource_typename_txt.setText("产品：" + goodsModel.getGoodsName());
			scanBimpModel.setResourceTypeId(goodsModel.getGoodsnameID());
			scanBimpModel.setPackageType(goodsModel.getPackagetype());
			scanBimpModel.setResourceTypeName(goodsModel.getGoodsName());
		} else {
			scanBimpModel.setResourceTypeId("");
			scanBimpModel.setPackageType("");
			scanBimpModel.setResourceTypeName("");
			resource_typename_txt.setVisibility(View.GONE);
			resource_package_type_txt.setVisibility(View.GONE);
		}
	}

}
