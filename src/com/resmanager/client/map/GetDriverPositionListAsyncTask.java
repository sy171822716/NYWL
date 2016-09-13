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

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.UserLocationListModel;
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
public class GetDriverPositionListAsyncTask extends AsyncTask<Void, Void, String> {

	private Context context;
	private Dialog loadingDialog;
	private GetUserLocationListListener getUserLocationListListener;
	private boolean isShowDialog;

	public GetDriverPositionListAsyncTask(Context context, boolean isShowDialog) {
		this.context = context;
		this.isShowDialog = isShowDialog;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetDriver_PositionList);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
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
		try {
			if (rv != null) {
				UserLocationListModel userLocationListModel = JSON.parseObject(rv, UserLocationListModel.class);
				if (userLocationListModel != null) {
					getUserLocationListListener.onSuccess(userLocationListModel);
				} else {
					getUserLocationListListener.onFaile();
				}
			} else {
				getUserLocationListListener.onFaile();
			}
		} catch (Exception e) {
			e.printStackTrace();
			getUserLocationListListener.onFaile();
		}
		if (isShowDialog) {
			if (this.loadingDialog != null && this.loadingDialog.isShowing()) {
				this.loadingDialog.cancel();
				this.loadingDialog = null;
			}
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (isShowDialog) {
			if (this.loadingDialog == null) {
				this.loadingDialog = CommonView.LoadingDialog(context);
			}
			if (this.loadingDialog.isShowing() == false) {
				this.loadingDialog.show();
			}
		}
	}

	public GetUserLocationListListener getGetUserLocationListListener() {
		return getUserLocationListListener;
	}

	public void setGetUserLocationListListener(GetUserLocationListListener getUserLocationListListener) {
		this.getUserLocationListListener = getUserLocationListListener;
	}

	public interface GetUserLocationListListener {
		void onSuccess(UserLocationListModel userLocationListModel);

		void onFaile();
	}

}
