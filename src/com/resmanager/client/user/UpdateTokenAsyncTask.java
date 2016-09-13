package com.resmanager.client.user;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.os.AsyncTask;

public class UpdateTokenAsyncTask extends AsyncTask<Void, Void, String> {
	private Object token;

	public UpdateTokenAsyncTask(Object token) {
		this.token = token;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.UpdateMsgtoken);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("UserID", ContactsUtils.userDetailModel.getUserId());
		ws.addProperty("msgtoken", token);
		try {
			String jsonStr = ws.start();
			System.out.println(jsonStr);
		} catch (IOException | XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

}
