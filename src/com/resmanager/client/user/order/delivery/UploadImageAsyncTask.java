package com.resmanager.client.user.order.delivery;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.utils.WebServiceUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

@SuppressLint("SimpleDateFormat")
public class UploadImageAsyncTask extends AsyncTask<Void, Void, String> {

	private Bitmap bitmap;
	private UploadResourceListener uploadResourceListener;
	private String flagContent;
	private Dialog loadingDialog;
	private Context context;
	private String workId;
	private String goodsId;
	private int isRecyle;
	private Activity activity;
	private String orderIds;
	private String locationName, locationAddr, lon, lat;

	public UploadImageAsyncTask(Context context, Bitmap bitmap, String flagContent, String workId, String goodsId, int isRecyle, String orderIds,
			String locationName, String locationAddr, String lon, String lat) {
		this.activity = (Activity) context;
		this.bitmap = bitmap;
		this.flagContent = flagContent;
		this.context = context;
		this.workId = workId;
		this.goodsId = goodsId;
		this.orderIds = orderIds;
		this.isRecyle = isRecyle;
		this.locationName = locationName;
		this.locationAddr = locationAddr;
		this.lon = lon;
		this.lat = lat;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateNowStr = sdf.format(new Date());
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.IMAGE_UPLOAD);
		String imageByte = Tools.getImageByte(this.bitmap);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("WorkID", workId);
		ws.addProperty("LabelCode", flagContent);
		ws.addProperty("UserID", ContactsUtils.userDetailModel.getUserId());
		ws.addProperty("image", imageByte);
		ws.addProperty("fileName", flagContent + "-1-" + dateNowStr + ".jpg");
		ws.addProperty("GoodsID", goodsId);
		ws.addProperty("IsRecovery", isRecyle);
		ws.addProperty("NetworkType", Tools.GetNetworkType(context));
		ws.addProperty("NetworkStrength", "");
		ws.addProperty("OrderIDS", this.orderIds);
		ws.addProperty("MapLocationName", locationName);
		ws.addProperty("MapSpecificLocation", locationAddr);
		ws.addProperty("Longitude", lon);
		ws.addProperty("Latitude", lat);
		try {
			String json = ws.start();
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
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
				getUploadResourceListener().uploadResult(resultModel, bitmap, flagContent);
			} catch (Exception e) {
				e.printStackTrace();
				getUploadResourceListener().uploadResult(null, bitmap, flagContent);
			}
		} else {
			getUploadResourceListener().uploadResult(null, bitmap, flagContent);
		}
		if (!activity.isFinishing() && this.loadingDialog.isShowing()) {
			this.loadingDialog.cancel();
			this.loadingDialog = null;
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (!activity.isFinishing()) {
			if (this.loadingDialog == null) {
				this.loadingDialog = CommonView.LoadingDialog(context);
			}
			if (this.loadingDialog.isShowing() == false) {
				this.loadingDialog.show();
			}
		}
	}

	public UploadResourceListener getUploadResourceListener() {
		return uploadResourceListener;
	}

	public void setUploadResourceListener(UploadResourceListener uploadResourceListener) {
		this.uploadResourceListener = uploadResourceListener;
	}

	public interface UploadResourceListener {
		public void uploadResult(ResultModel resultModel, Bitmap bmp, String flagContent);
	}
}
