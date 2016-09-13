/**
 * 
 */
package com.resmanager.client.user.balance;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.TradeBillListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @author ShenYang 获取驾驶员结算明细
 * 
 */
public class GetDriverTradeBillAsyncTask extends AsyncTask<Void, Void, String> {

	private Context context;
	private Dialog loadingDialog;
	private String orderId;
	private GetTradeBillListener getTradeBillListener;

	public GetDriverTradeBillAsyncTask(Context context, String orderID) {
		this.context = context;
		this.orderId = orderID;

	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Driver_TradeBill);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("OrderID", orderId);
		try {
			String json = ws.start();
			return json;
		} catch (IOException | XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String rv) {
		super.onPostExecute(rv);
		if (loadingDialog.isShowing()) {
			this.loadingDialog.cancel();
			this.loadingDialog = null;
		}
		if (rv != null) {
			try {
				TradeBillListModel tradeBillListModel = JSON.parseObject(rv, TradeBillListModel.class);
				getTradeBillListener.getTraderBillResult(tradeBillListModel);
			} catch (Exception e) {
				e.printStackTrace();
				getTradeBillListener.getTraderBillResult(null);
			}
		} else {
			getTradeBillListener.getTraderBillResult(null);
		}
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

	public GetTradeBillListener getGetTradeBillListener() {
		return getTradeBillListener;
	}

	public void setGetTradeBillListener(GetTradeBillListener getTradeBillListener) {
		this.getTradeBillListener = getTradeBillListener;
	}

	public interface GetTradeBillListener {
		public void getTraderBillResult(TradeBillListModel tradeBillListModel);
	}
}
