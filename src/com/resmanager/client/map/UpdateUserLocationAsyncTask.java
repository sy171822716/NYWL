package com.resmanager.client.map;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.os.AsyncTask;

public class UpdateUserLocationAsyncTask extends AsyncTask<Void, Void, String> {
	private String Longitude;
	private String Latitude;

	public UpdateUserLocationAsyncTask(String Longitude, String Latitude) {
		this.Latitude = Latitude;
		this.Longitude = Longitude;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		//当登录用户为驾驶员时，才上传位置
		if (ContactsUtils.userDetailModel != null && ContactsUtils.userDetailModel.getUserType().equals("1")) {
			WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.UpdateDriver_Track);
			ws.addProperty("UserKey", ContactsUtils.USER_KEY);
			ws.addProperty("UserID", ContactsUtils.userDetailModel.getUserId());
			ws.addProperty("Longitude", Longitude);
			ws.addProperty("Latitude", Latitude);
			try {
				String json = ws.start();
				System.out.println(json);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
