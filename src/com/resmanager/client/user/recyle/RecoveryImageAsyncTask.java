/**   
 * @Title: UploadingImageAsyncTask.java 
 * @Package com.resmanager.client.user.order.unloading 
 * @Description: 卸货图片上传
 * @author ShenYang  
 * @date 2015-12-11 下午12:29:56 
 * @version V1.0   
 */
package com.resmanager.client.user.recyle;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * @ClassName: UploadingImageAsyncTask
 * @Description: 桶回收图片上传
 * @author ShenYang
 * @date 2015-12-11 下午12:29:56
 * 
 */
public class RecoveryImageAsyncTask extends AsyncTask<Void, Void, String> {

	private Context context;
	private Dialog loadingDialog;
	private String workID, labelCode, RecPID;
	private Bitmap image;
	private UploadRecyleResourceListener uploadRecyleResourceListener;

	public RecoveryImageAsyncTask(Context context, String workId, String labelCode, Bitmap image, String RecPID) {
		this.context = context;
		this.workID = workId;
		this.RecPID = RecPID;
		this.labelCode = labelCode;
		this.image = image;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Recovery_ScanUpload);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("WorkID", this.workID);
		ws.addProperty("LabelCodes", this.labelCode);
		ws.addProperty("UserID", ContactsUtils.userDetailModel.getUserId());
		ws.addProperty("NickName", ContactsUtils.userDetailModel.getNickName());
		ws.addProperty("image", Tools.getImageByte(image));
		ws.addProperty("fileName", this.workID + "-" + labelCode + "-3-" + Tools.getNowTime() + ".jpg");
		ws.addProperty("NetworkType", Tools.GetNetworkType(context));
		ws.addProperty("NetworkStrength", "");
		ws.addProperty("RecPID", RecPID);
		try {
			return ws.start();
		} catch (IOException | XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String rv) {
		super.onPostExecute(rv);
		if (rv != null) {
			try {
				ResultModel resultModel = JSON.parseObject(rv, ResultModel.class);
				getUploadRecyleResourceListener().uploadRecyleResult(resultModel, image, labelCode, RecPID);
			} catch (Exception e) {
				e.printStackTrace();
				getUploadRecyleResourceListener().uploadRecyleResult(null, image, labelCode, RecPID);
			}
		} else {
			getUploadRecyleResourceListener().uploadRecyleResult(null, image, labelCode, RecPID);
		}
		if (loadingDialog.isShowing()) {
			this.loadingDialog.cancel();
			this.loadingDialog = null;
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (this.loadingDialog == null) {
			this.loadingDialog = CommonView.LoadingDialog(context);
		}
		if (loadingDialog.isShowing() == false) {
			this.loadingDialog.show();
		}
	}

	public UploadRecyleResourceListener getUploadRecyleResourceListener() {
		return uploadRecyleResourceListener;
	}

	public void setUploadRecyleResourceListener(UploadRecyleResourceListener uploadRecyleResourceListener) {
		this.uploadRecyleResourceListener = uploadRecyleResourceListener;
	}

	public interface UploadRecyleResourceListener {
		public void uploadRecyleResult(ResultModel resultModel, Bitmap bmp, String flagContent, String recPid);
	}
}
