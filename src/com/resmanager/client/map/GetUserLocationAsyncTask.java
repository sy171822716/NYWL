/**   
 * @Title: GetUserLocationAsyncTask.java 
 * @Package com.resmanager.client.map 
 * @Description: 根据用户位置
 * @author ShenYang  
 * @date 2016-1-19 下午12:28:33 
 * @version V1.0   
 */
package com.resmanager.client.map;

import java.io.IOException;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @ClassName: GetUserLocationAsyncTask
 * @Description:根据用户位置
 * @author ShenYang
 * @date 2016-1-19 下午12:28:33
 * 
 */
public class GetUserLocationAsyncTask extends AsyncTask<Void, Void, String> {

	private Context context;
	private Dialog loadingDialog;
	private GetUserLocationListener getUserLocationListener;
	private int userId;
	public GetUserLocationAsyncTask(Context context,int userId) {
		this.context = context;
		this.userId = userId;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetDriver_Track);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("UserID", userId);
		try {
			String jsonStr = ws.start();
			return jsonStr;
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
				@SuppressWarnings("unchecked")
				Map<String, String> locationMap = (Map<String, String>) JSON.parse(rv);
				getUserLocationListener.getUserLocationResult(locationMap);
			} catch (Exception e) {
				getUserLocationListener.getUserLocationResult(null);
				e.printStackTrace();
			}
		} else {
			getUserLocationListener.getUserLocationResult(null);
		}
		if (this.loadingDialog != null && this.loadingDialog.isShowing()) {
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
		if (this.loadingDialog.isShowing() == false) {
			this.loadingDialog.show();
		}
	}

	public GetUserLocationListener getGetUserLocationListener() {
		return getUserLocationListener;
	}

	public void setGetUserLocationListener(GetUserLocationListener getUserLocationListener) {
		this.getUserLocationListener = getUserLocationListener;
	}

	public interface GetUserLocationListener {
		public void getUserLocationResult(Map<String, String> locationMap);
	}

}
