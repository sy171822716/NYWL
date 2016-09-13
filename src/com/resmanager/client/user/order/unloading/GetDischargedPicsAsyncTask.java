package com.resmanager.client.user.order.unloading;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.ScanBimpModel;
import com.resmanager.client.model.TempScanBimpModel;
import com.resmanager.client.model.TempScanBimpModels;
import com.resmanager.client.user.order.UploadCache;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.DESUtils;
import com.resmanager.client.utils.WebServiceUtil;

public class GetDischargedPicsAsyncTask extends AsyncTask<Void, Void, String> {
	private Context context;
	private String orderId;
	private Dialog loadingDialog;
	private GetDischaragePicListener getDischaragePicListener;
	public GetDischargedPicsAsyncTask(Context context, String orderId) {
		this.context = context;
		this.orderId = orderId;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Discharge_Continue);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("OrderID", orderId);
		try {
			String jsonStr = ws.start();
			TempScanBimpModels tempScanBimpModels = JSON.parseObject(jsonStr, TempScanBimpModels.class);
			if (tempScanBimpModels.getResult().equals("true")) {
				ArrayList<TempScanBimpModel> data = tempScanBimpModels.getData();
				for (TempScanBimpModel tempScanBimpModel : data) {
					ScanBimpModel scanBimpModel = new ScanBimpModel();
					scanBimpModel.setLabelCode(DESUtils.encrypt(tempScanBimpModel.getLabelCode()));
					scanBimpModel.setBmpPath(tempScanBimpModel.getOriginal_Path());
					scanBimpModel.setThumbPath(tempScanBimpModel.getThumb_Path());
					scanBimpModel.setPackageType(tempScanBimpModel.getPackagetype());
					scanBimpModel.setResourceTypeName(tempScanBimpModel.getGoodsName());
					UploadCache.scanBimpModels.add(scanBimpModel);
				}
			}
			return null;
		} catch (IOException | XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String rv) {
		super.onPostExecute(rv);
		if (loadingDialog.isShowing()) {
			this.loadingDialog.dismiss();
		}
		getDischaragePicListener.getDischaragePicReuslt();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (loadingDialog == null) {
			this.loadingDialog = CommonView.LoadingDialog(context);
		}
		if (loadingDialog.isShowing() == false) {
			this.loadingDialog.show();
		}
	}
	
	
	public GetDischaragePicListener getGetDischaragePicListener() {
		return getDischaragePicListener;
	}

	public void setGetDischaragePicListener(GetDischaragePicListener getDischaragePicListener) {
		this.getDischaragePicListener = getDischaragePicListener;
	}


	public interface GetDischaragePicListener{
		public void getDischaragePicReuslt();
	}

}
