/**   
 * @Title: GetOrderTradeBillAsyncTask.java 
 * @Package com.resmanager.client.user.balance 
 * @Description: 获取订单结算明细异步线程
 * @author ShenYang  
 * @date 2016-1-15 上午9:05:15 
 * @version V1.0   
 */
package com.resmanager.client.user.balance;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.content.Context;
import android.os.AsyncTask;

/**
 * @ClassName: GetOrderTradeBillAsyncTask
 * @Description: 获取订单结算明细异步线程
 * @author ShenYang
 * @date 2016-1-15 上午9:05:15
 * 
 */
public class GetOrderTradeBillAsyncTask extends AsyncTask<Void, Void, String> {

	private String OrderStateCode;
	private int pageIndex = 1;

	public GetOrderTradeBillAsyncTask(Context context, String orderStateCode, int pageIndex) {
		this.OrderStateCode = orderStateCode;
		this.pageIndex = pageIndex;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetOrderTradeBill);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("UserID", ContactsUtils.userDetailModel.getUserId());
		ws.addProperty("OrderStateCode", OrderStateCode);
		ws.addProperty("pageSize", pageIndex);
		ws.addProperty("pageIndex", ContactsUtils.PAGE_SIZE);
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
			System.out.println(rv);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
}
