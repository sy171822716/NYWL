/**   
 * @Title: GetDriverListAsyncTask.java 
 * @Package com.resmanager.client.order 
 * @Description: 获取驾驶员列表异步线程 
 * @author ShenYang  
 * @date 2016-2-25 下午3:52:38 
 * @version V1.0   
 */
package com.resmanager.client.order;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.DriverListResultModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

/**
 * @ClassName: GetDriverListAsyncTask
 * @Description: 获取驾驶员列表异步线程
 * @author ShenYang
 * @date 2016-2-25 下午3:52:38
 * 
 */
public class GetDriverListAsyncTask extends AsyncTask<Void, Void, String> {

	private Context context;
	private Dialog loadingDialog;
	private GetDriverListListener getDriverListListener;

	public GetDriverListAsyncTask(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetUserList_fp);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		try {
			String rv = ws.start();
			return rv;
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
				DriverListResultModel driverListResultModel = JSON.parseObject(rv, DriverListResultModel.class);
				getDriverListListener.getDriverResult(driverListResultModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
			getDriverListListener.getDriverResult(null);
		}

		if (this.loadingDialog != null && this.loadingDialog.isShowing()) {
			this.loadingDialog.dismiss();
			this.loadingDialog = null;
		}

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (this.loadingDialog == null) {
			this.loadingDialog = CommonView.LoadingDialog(context);
		}
		this.loadingDialog.show();
	}

	public GetDriverListListener getGetDriverListListener() {
		return getDriverListListener;
	}

	public void setGetDriverListListener(GetDriverListListener getDriverListListener) {
		this.getDriverListListener = getDriverListListener;
	}

	public interface GetDriverListListener {
		public void getDriverResult(DriverListResultModel driverListResultModel);
	}
}
