/**   
 * @Title: UploadingImageAsyncTask.java 
 * @Package com.resmanager.client.user.order.unloading 
 * @Description: 卸货图片上传
 * @author ShenYang  
 * @date 2015-12-11 下午12:29:56 
 * @version V1.0   
 */
package com.resmanager.client.user.order.unloading;

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
 * @Description: 卸货图片上传
 * @author ShenYang
 * @date 2015-12-11 下午12:29:56
 * 
 */
public class UploadingImageAsyncTask extends AsyncTask<Void, Void, String> {

	private Context context;
	private Dialog loadingDialog;
	private String orderId, sourceoid, workID, labelCode;
	private Bitmap image;
	private UploadUploadingResourceListener uploadUploadingResourceListener;
	private String locationName, locationAddr, lon, lat;

	public UploadingImageAsyncTask(Context context, String orderId, String sourcoid, String workId, String labelCode, Bitmap image, String locationName,
			String locationAddr, String lat, String lon) {
		this.context = context;
		this.orderId = orderId;
		this.sourceoid = sourcoid;
		this.workID = workId;
		this.labelCode = labelCode;
		this.locationName = locationName;
		this.locationAddr = locationAddr;
		this.lon = lon;
		this.lat = lat;
		this.image = image;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.DischargeCargo_ScanUpload);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("OrderID", this.orderId);
		ws.addProperty("Sourceoid", this.sourceoid);
		ws.addProperty("WorkID", this.workID);
		ws.addProperty("LabelCode", this.labelCode);
		ws.addProperty("UserID", ContactsUtils.userDetailModel.getUserId());
		ws.addProperty("NickName", ContactsUtils.userDetailModel.getNickName());
		ws.addProperty("image", Tools.getImageByte(image));
		ws.addProperty("fileName", this.sourceoid + "-" + labelCode + "-2-" + Tools.getNowTime() + ".jpg");
		ws.addProperty("NetworkType", Tools.GetNetworkType(context));
		ws.addProperty("NetworkStrength", "");
		ws.addProperty("MapLocationName", locationName);
		ws.addProperty("MapSpecificLocation", locationAddr);
		ws.addProperty("Longitude", lon);
		ws.addProperty("Latitude", lat);
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
				getUploadUploadingResourceListener().uploadUploadingResult(resultModel, image, labelCode);
			} catch (Exception e) {
				e.printStackTrace();
				getUploadUploadingResourceListener().uploadUploadingResult(null, image, labelCode);
			}
		} else {
			getUploadUploadingResourceListener().uploadUploadingResult(null, image, labelCode);
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

	public UploadUploadingResourceListener getUploadUploadingResourceListener() {
		return uploadUploadingResourceListener;
	}

	public void setUploadUploadingResourceListener(UploadUploadingResourceListener uploadUploadingResourceListener) {
		this.uploadUploadingResourceListener = uploadUploadingResourceListener;
	}

	public interface UploadUploadingResourceListener {
		public void uploadUploadingResult(ResultModel resultModel, Bitmap bmp, String flagContent);
	}
}
