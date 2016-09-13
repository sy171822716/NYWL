package com.resmanager.client.user.recyle;

import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.main.photoalbum.Bimp;
import com.resmanager.client.model.RecyleLabelListModel;
import com.resmanager.client.model.RecyleLabelModel;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.model.ScanBimpModel;
import com.resmanager.client.user.order.UploadCache;
import com.resmanager.client.user.recyle.GetLabelByCustomerListAsyncTask.GetLabelByCustomerListener;
import com.resmanager.client.user.recyle.RecoveryImageAsyncTask.UploadRecyleResourceListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.CustomDialog;
import com.resmanager.client.view.DefineListView;
import com.resmanager.client.view.CustomDialog.ToDoListener;

@SuppressLint({ "InflateParams", "HandlerLeak" })
public class AddRecyleSource extends TopContainActivity implements OnClickListener {
	private String customerId;
	private ArrayList<RecyleLabelModel> recyleLabelModels;
	private TextView choose_flag_txt;
	private DefineListView source_lisview;
	private ImageView resource_img;
	private ArrayList<RecyleLabelModel> selectedLabelModels = new ArrayList<RecyleLabelModel>();
	private Bitmap recyleImg;
	private CustomDialog recyleDialog;
	private String workId;

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
			this.finish();
			break;
		case R.id.choose_flag_txt:
			// if (recyleLabelModels == null) {
			getLabelByCustomId();
			// } else {
			// Intent intent = new Intent(this, ChooseResourceActivity.class);
			// intent.putExtra("recyleLabelModels", recyleLabelModels);
			// intent.putExtra("selectedLabelModels", selectedLabelModels);
			// startActivityForResult(intent,
			// ContactsUtils.CHOOSE_LABEL_RESULT);
			// }
			break;
		case R.id.resource_img:
			Tools.takePhoto(this);
			break;
		case R.id.ok_btn:
			if (selectedLabelModels.size() == 0) {
				Tools.showToast(this, "请选择需要回收的标签");
			} else if (recyleImg == null) {
				Tools.showToast(this, "请对需要回收的货物进行拍照");
			} else {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < selectedLabelModels.size(); i++) {
					if (i == (selectedLabelModels.size() - 1)) {
						sb.append(selectedLabelModels.get(i).getLabelCode());
					} else {
						sb.append(selectedLabelModels.get(i).getLabelCode() + ",");
					}
				}
				showConfirmDialog(recyleImg, sb.toString());
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 弹出对话框让用户最后一次选择
	 */
	private void showConfirmDialog(final Bitmap bmp, final String labelCodes) {
		if (recyleDialog == null) {
			recyleDialog = new CustomDialog(this, R.style.myDialogTheme);
			recyleDialog.setContentText("请确认回收信息是否填写完整?");
			recyleDialog.setToDoListener(new ToDoListener() {

				@Override
				public void doSomething() {
					uploadRecyleImg(bmp, labelCodes);
					recyleDialog.dismiss();
				}
			});
		}
		recyleDialog.show();
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
					recyleImg = bm;
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
					// scanBimpModel.setBmp(bm);
					// scanBimpModel.setBmpPath(path);
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

	@Override
	protected View getCenterView() {
		return inflater.inflate(R.layout.add_recyle_resource, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		workId = getIntent().getExtras().getString("workId");
		customerId = getIntent().getExtras().getString("customerId");
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("添加货物");
		choose_flag_txt = (TextView) centerView.findViewById(R.id.choose_flag_txt);
		choose_flag_txt.setOnClickListener(this);
		source_lisview = (DefineListView) centerView.findViewById(R.id.source_lisview);
		resource_img = (ImageView) centerView.findViewById(R.id.resource_img);
		resource_img.setOnClickListener(this);
		centerView.findViewById(R.id.ok_btn).setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: getLabelByCustomId
	 * @Description:获取用户那可回收的桶
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getLabelByCustomId() {
		GetLabelByCustomerListAsyncTask getLabelByCustomerListAsyncTask = new GetLabelByCustomerListAsyncTask(this, customerId, workId, "", true);
		getLabelByCustomerListAsyncTask.setGetLabelByCustomerListener(new GetLabelByCustomerListener() {

			@Override
			public void getLabelByCustomerResult(RecyleLabelListModel recyleLabelListModel) {
				if (recyleLabelListModel != null) {
					if (recyleLabelListModel.getResult().equals("true")) {
						if (recyleLabelListModel.getData() != null) {
							recyleLabelModels = recyleLabelListModel.getData();
						}
						Intent intent = new Intent(AddRecyleSource.this, ChooseResourceActivity.class);
						intent.putExtra("recyleLabelModels", recyleLabelModels);
						intent.putExtra("selectedLabelModels", selectedLabelModels);
						intent.putExtra("customerId", customerId);
						intent.putExtra("workId", workId);
						startActivityForResult(intent, ContactsUtils.CHOOSE_LABEL_RESULT);
					} else {
						Tools.showToast(AddRecyleSource.this, recyleLabelListModel.getDescription());
					}
				} else {
					Tools.showToast(AddRecyleSource.this, "可回收货物获取失败，请检查网络");
				}
			}
		});
		getLabelByCustomerListAsyncTask.execute();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case ContactsUtils.CHOOSE_LABEL_RESULT:
			if (data != null) {
				selectedLabelModels = (ArrayList<RecyleLabelModel>) data.getExtras().getSerializable("recyleLabelModels");
				if (selectedLabelModels.size() > 0) {
					ChooseResourceListAdapter chooseResourceListAdapter = new ChooseResourceListAdapter(AddRecyleSource.this, selectedLabelModels, false);
					source_lisview.setAdapter(chooseResourceListAdapter);
					source_lisview.setVisibility(View.VISIBLE);
				} else {
					source_lisview.setVisibility(View.GONE);
				}
			}
			break;
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
	}

	/**
	 * // * // * @Title: uploadImg // * @Description: 上传图片 // * @param @param
	 * path 设定文件 // * @param type // * 1:二维码上传，2：遗失二维码 // * @return void 返回类型 //
	 * * @throws //
	 */
	public void uploadRecyleImg(Bitmap tempbmp, String flagContent) {

		String recPId = Tools.getGUID();// 图片ID。用户删除时服务器对应
		RecoveryImageAsyncTask recoveryImageAsyncTask = new RecoveryImageAsyncTask(this, workId, flagContent, tempbmp, recPId);
		recoveryImageAsyncTask.setUploadRecyleResourceListener(new UploadRecyleResourceListener() {

			@Override
			public void uploadRecyleResult(ResultModel resultModel, Bitmap bmp, String flagContent, String RecPID) {
				if (resultModel != null) {
					if (resultModel.getResult().equals("true")) {
						Tools.showToast(AddRecyleSource.this, "旧桶添加成功");
						ScanBimpModel scanBimpModel = new ScanBimpModel();
						scanBimpModel.setBmp(bmp);
						scanBimpModel.setLabelCode(flagContent);
						scanBimpModel.setRecPID(RecPID);
						UploadCache.scanBimpModels.add(scanBimpModel);
						Intent recyleIntent = new Intent(AddRecyleSource.this, RecyleSourceGrid.class);
						recyleIntent.putExtra("customerId", customerId);
						recyleIntent.putExtra("workId", workId);
						startActivity(recyleIntent);
						finish();
					} else {
						Tools.showToast(AddRecyleSource.this, resultModel.getDescription());
					}
				} else {
					Tools.showToast(AddRecyleSource.this, "旧桶添加失败，请检查网络");
				}
			}
		});
		recoveryImageAsyncTask.execute();
	}

}
