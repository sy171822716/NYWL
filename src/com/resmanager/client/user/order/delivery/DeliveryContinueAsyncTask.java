package com.resmanager.client.user.order.delivery;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.TempScanBimpModels;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

public class DeliveryContinueAsyncTask extends AsyncTask<Void, Void, String> {
	private String OrderIDS;
	private DeliveryContinueListener deliveryContinueListener;

	public DeliveryContinueAsyncTask(String OrderIDS) {
		this.OrderIDS = OrderIDS;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		if (ContactsUtils.userDetailModel != null) {
			WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Delivery_Continue);
			ws.addProperty("UserKey", ContactsUtils.USER_KEY);
			ws.addProperty("OrderIDS", this.OrderIDS);
			ws.addProperty("UserID", ContactsUtils.userDetailModel.getUserId());
			try {
				String jsonStr = ws.start();
				return jsonStr;
			} catch (IOException | XmlPullParserException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(String rv) {
		super.onPostExecute(rv);
		if (rv != null) {
			try {
				TempScanBimpModels tempScanBimpModels = JSON.parseObject(rv, TempScanBimpModels.class);
				deliveryContinueListener.deliveryContinueResult(tempScanBimpModels);
			} catch (Exception e) {
				e.printStackTrace();
				deliveryContinueListener.deliveryContinueResult(null);
			}
		} else {
			deliveryContinueListener.deliveryContinueResult(null);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	public DeliveryContinueListener getDeliveryContinueListener() {
		return deliveryContinueListener;
	}

	public void setDeliveryContinueListener(DeliveryContinueListener deliveryContinueListener) {
		this.deliveryContinueListener = deliveryContinueListener;
	}

	public interface DeliveryContinueListener {
		public void deliveryContinueResult(TempScanBimpModels deliveryScanListModel);
	}
}
