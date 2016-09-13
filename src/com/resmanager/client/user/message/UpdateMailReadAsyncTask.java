package com.resmanager.client.user.message;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.os.AsyncTask;

public class UpdateMailReadAsyncTask extends AsyncTask<Void, Void, String> {
	private String msgId;

	public UpdateMailReadAsyncTask(String msgId) {
		this.msgId = msgId;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.UpdateMailRead);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("ReceiverId", msgId);
		try {
			String jsonStr = ws.start();
			return jsonStr;
		} catch (IOException | XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

}
