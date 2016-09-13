package com.resmanager.client.warehouse;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.WarseHouseListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

public class GetWarseHouseListAsyncTask extends AsyncTask<Void, Void, String> {
	private Context context;
	private Dialog loadingDialog;
	private GetWarseHouseListListener getWarseHouseListListener;

	public GetWarseHouseListAsyncTask(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(Void... arg0) {

		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Warehouse_List);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		try {
			String json = ws.start();
			return json;
		} catch (IOException | XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (loadingDialog == null) {
			this.loadingDialog = CommonView.LoadingDialog(context);
		}
		if (loadingDialog.isShowing() == false)
			this.loadingDialog.show();
	}

	@Override
	protected void onPostExecute(String rv) {
		super.onPostExecute(rv);
		if (loadingDialog.isShowing()) {
			this.loadingDialog.cancel();
			this.loadingDialog = null;
		}
		try {
			if (rv != null) {
				WarseHouseListModel warseHouseListModel = JSON.parseObject(rv, WarseHouseListModel.class);
				getWarseHouseListListener.getWarseHouseListResult(warseHouseListModel);
			} else {
				getWarseHouseListListener.getWarseHouseListResult(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			getWarseHouseListListener.getWarseHouseListResult(null);
		}

	}

	public GetWarseHouseListListener getGetWarseHouseListListener() {
		return getWarseHouseListListener;
	}

	public void setGetWarseHouseListListener(GetWarseHouseListListener getWarseHouseListListener) {
		this.getWarseHouseListListener = getWarseHouseListListener;
	}

	public interface GetWarseHouseListListener {
		public void getWarseHouseListResult(WarseHouseListModel warseHouseListModel);
	}

}
