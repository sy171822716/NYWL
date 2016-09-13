/**   
 * @Title: RecyleActivity.java 
 * @Package com.resmanager.client.user.recyle 
 * @Description: 回收界面 
 * @author ShenYang  
 * @date 2016-1-5 上午10:43:34 
 * @version V1.0   
 */
package com.resmanager.client.user.recyle;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.main.photoalbum.Bimp;
import com.resmanager.client.model.CustomerModel;
import com.resmanager.client.model.LocationModel;
import com.resmanager.client.model.RecyleTempListModel;
import com.resmanager.client.model.RecyleTempModel;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.model.ScanBimpModel;
import com.resmanager.client.user.order.UploadCache;
import com.resmanager.client.user.recyle.RecoveryConfirmAsyncTask.RecoveryListener;
import com.resmanager.client.user.recyle.RecoveryContinueAsyncTask.RecyleContinueListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.LocationUtils;
import com.resmanager.client.utils.LocationUtils.PoiSearchListener;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.CustomDialog;
import com.resmanager.client.view.CustomDialog.CancelBtnListener;
import com.resmanager.client.view.CustomDialog.ToDoListener;

/**
 * @ClassName: RecyleActivity
 * @Description: 回收界面
 * @author ShenYang
 * @date 2016-1-5 上午10:43:34
 * 
 */
@SuppressLint({ "InflateParams", "HandlerLeak" })
public class RecyleActivity extends TopContainActivity implements OnClickListener {
	private TextView location_str_txt, customer_name;
	private TextView add_source_btn;
	private LocationModel locationModel;
	private CustomerModel customerModel;
	private String workId;
	private ImageView recyle_img;
	private EditText recyle_remark_txt;
	private Bitmap uploading_id = null;
	private CustomDialog exitDialog, confirmUploadingDialog, continueRecyleDialog;
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
						if (tempItem.getTitle().equals(customerModel.getCustomerName())) {
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
			confirmUploadingDialog.setContentText("确认提交回收？");
			confirmUploadingDialog.setToDoListener(new ToDoListener() {

				@Override
				public void doSomething() {
					confirmRecyle();
					confirmUploadingDialog.dismiss();
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
			exitDialog.setContentText("是否确认退出回收界面？");
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

	/**
	 * 弹出是否继续回收对话框
	 */
	private void showContinueRecyleDialog(final ArrayList<RecyleTempModel> recyleTempModels) {
		if (continueRecyleDialog == null) {
			continueRecyleDialog = new CustomDialog(this, R.style.myDialogTheme);
			continueRecyleDialog.setContentText("有上次未完成的回收，是否继续");
			continueRecyleDialog.setToDoListener(new ToDoListener() {

				@Override
				public void doSomething() {
					workId = recyleTempModels.get(0).getWorkID();
					for (int i = 0; i < recyleTempModels.size(); i++) {
						RecyleTempModel recyleTempModel = recyleTempModels.get(i);
						if (recyleTempModel.getWorkID().equals(workId)) {
							ScanBimpModel scanBimpModel = new ScanBimpModel();
							scanBimpModel.setBmpPath(recyleTempModel.getOriginal_Path() + "");
							scanBimpModel.setThumbPath(recyleTempModel.getThumb_Path() + "");
							scanBimpModel.setLabelCode(recyleTempModel.getLabels() + "");
							UploadCache.scanBimpModels.add(scanBimpModel);
						}
					}
					updateNum();
					continueRecyleDialog.dismiss();

				}
			});
			continueRecyleDialog.setCancelBtnListener(new CancelBtnListener() {

				@Override
				public void cancel() {
					// 清空
					recyleCancel(recyleTempModels.get(0).getWorkID());
					continueRecyleDialog.dismiss();
				}
			});
		}
		continueRecyleDialog.show();
	}

	/**
	 * 
	 * @Title: confirmRecyle
	 * @Description:确认回收
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void confirmRecyle() {
		if (this.locationModel != null && this.locationModel.getAddress() != null && !this.locationModel.getAddress().equals("")) {
			RecoveryConfirmAsyncTask recoveryConfirmAsyncTask = new RecoveryConfirmAsyncTask(RecyleActivity.this, workId, locationModel.getName(),
					locationModel.getAddress(), String.valueOf(locationModel.getLng()), String.valueOf(locationModel.getLat()), recyle_remark_txt.getText()
							.toString(), customerModel.getCustomerID(), customerModel.getCustomerName(), uploading_id);
			recoveryConfirmAsyncTask.setRecoveryListener(new RecoveryListener() {

				@Override
				public void recoveryResult(ResultModel rm) {
					if (rm != null) {
						if (rm.getResult().equals("true")) {
							Tools.showToast(RecyleActivity.this, "回收成功");
							finish();
						} else {
							Tools.showToast(RecyleActivity.this, rm.getDescription());
						}
					} else {
						Tools.showToast(RecyleActivity.this, "回收失败，请检查网络");
					}
				}
			});
			recoveryConfirmAsyncTask.execute();
		} else {
			Tools.showToast(this, "位置获取失败，请稍后再试");
		}
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
			if (UploadCache.scanBimpModels.size() == 0) {
				Intent addIntent = new Intent(this, AddRecyleSource.class);
				addIntent.putExtra("customerId", customerModel.getCustomerID());
				addIntent.putExtra("workId", workId);
				startActivity(addIntent);
			} else {
				Intent addIntent = new Intent(this, RecyleSourceGrid.class);
				addIntent.putExtra("customerId", customerModel.getCustomerID());
				addIntent.putExtra("workId", workId);
				startActivity(addIntent);
			}
			break;
		case R.id.recyle_btn:
			if (uploading_id == null) {
				Tools.showToast(this, "请上传回收单照片");
			} else if (UploadCache.scanBimpModels.size() == 0) {
				Tools.showToast(this, "请上传桶的照片以及标签");
			} else {
				showConfirmDialog();
			}
			break;
		case R.id.recyle_img:
			Tools.takePhoto(this);
			break;
		default:
			break;
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
		return inflater.inflate(R.layout.recyle_layout, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		workId = Tools.getGUID();
		customerModel = (CustomerModel) getIntent().getSerializableExtra("customerModel");
		locationModel = new LocationModel();
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("回收");
		location_str_txt = (TextView) centerView.findViewById(R.id.location_str_txt);
		location_str_txt.setOnClickListener(this);
		customer_name = (TextView) centerView.findViewById(R.id.customer_name);
		customer_name.setText(customerModel.getCustomerName());
		add_source_btn = (TextView) centerView.findViewById(R.id.add_source_btn);
		add_source_btn.setOnClickListener(this);
		centerView.findViewById(R.id.recyle_btn).setOnClickListener(this);
		recyle_img = (ImageView) centerView.findViewById(R.id.recyle_img);
		recyle_img.setOnClickListener(this);
		recyle_remark_txt = (EditText) centerView.findViewById(R.id.recyle_remark_txt);
		// if (ContactsUtils.baseAMapLocation != null && ContactsUtils.poiItem
		// != null) {
		// locationModel.setAddress(ContactsUtils.poiItem.getSnippet());
		// locationModel.setName(ContactsUtils.poiItem.getTitle());
		// locationModel.setLat(ContactsUtils.poiItem.getLatLonPoint().getLatitude());
		// locationModel.setLng(ContactsUtils.poiItem.getLatLonPoint().getLongitude());
		// location_str_txt.setTextColor(getResources().getColor(R.color.orange_color));
		// location_str_txt.setText(ContactsUtils.poiItem.getTitle());
		// }
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
		recyleContinue();
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
					recyle_img.setImageBitmap(bm);
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
	protected void onDestroy() {
		super.onDestroy();
		UploadCache.resetBimp();
	}

	/**
	 * 
	 * @Title: recyleContinue
	 * @Description: 回收续传
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void recyleContinue() {
		RecoveryContinueAsyncTask recoveryContinueAsyncTask = new RecoveryContinueAsyncTask(this, customerModel.getCustomerID());
		recoveryContinueAsyncTask.setRecyleContinueListener(new RecyleContinueListener() {

			@Override
			public void recyleContinueResult(RecyleTempListModel recyleTempListModel) {
				if (recyleTempListModel != null) {
					if (recyleTempListModel.getResult().equals("true")) {
						showContinueRecyleDialog(recyleTempListModel.getData());
					}
				}
			}
		});
		recoveryContinueAsyncTask.execute();
	}

	/**
	 * 
	 * @Title: recyleContinue
	 * @Description: 取消续传
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void recyleCancel(String workid) {
		RecoveryCancelAsyncTask recoveryCancelAsyncTask = new RecoveryCancelAsyncTask(this, workid);
		recoveryCancelAsyncTask.execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateNum();
	}

	private void updateNum() {
		if (UploadCache.scanBimpModels.size() > 0) {
			add_source_btn.setTextColor(getResources().getColor(R.color.orange_color));
			add_source_btn.setText("已上传 " + UploadCache.scanBimpModels.size() + " 个油桶");
		} else {
			add_source_btn.setTextColor(getResources().getColor(R.color.light_gray));
			add_source_btn.setText("点击添加货物信息");
		}
	}
}
