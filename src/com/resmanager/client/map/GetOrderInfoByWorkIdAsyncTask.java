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

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.OrderListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

/**
 * @ClassName: GetUserLocationAsyncTask
 * @Description:获取正在运送中的订单异步线程
 * @author ShenYang
 * @date 2016-1-19 下午12:28:33
 * 
 */
public class GetOrderInfoByWorkIdAsyncTask extends AsyncTask<Void, Void, String> {

	private Context context;
	private Dialog loadingDialog;
	private String WorkID;
	private int orpType;
	private GetOnDispatchOrderListener getOnDispatchOrderListener;

	public GetOrderInfoByWorkIdAsyncTask(Context context, String WorkID, int orpType) {
		this.context = context;
		this.WorkID = WorkID;
		this.orpType = orpType;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		try {
			WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetOrderInfoByWorkID);
			ws.addProperty("UserKey", ContactsUtils.USER_KEY);
			ws.addProperty("WorkID", WorkID);
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
				OrderListModel orderListModel = JSON.parseObject(rv, OrderListModel.class);
				getOnDispatchOrderListener.getDispatchListResult(orpType, orderListModel);
			} catch (Exception e) {
				getOnDispatchOrderListener.getDispatchListResult(orpType, null);
			}
		} else {
			getOnDispatchOrderListener.getDispatchListResult(orpType, null);
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

	public GetOnDispatchOrderListener getGetOnDispatchOrderListener() {
		return getOnDispatchOrderListener;
	}

	public void setGetOnDispatchOrderListener(GetOnDispatchOrderListener getOnDispatchOrderListener) {
		this.getOnDispatchOrderListener = getOnDispatchOrderListener;
	}

	public interface GetOnDispatchOrderListener {
		public void getDispatchListResult(int orpType, OrderListModel orderListModel);
	}

}
