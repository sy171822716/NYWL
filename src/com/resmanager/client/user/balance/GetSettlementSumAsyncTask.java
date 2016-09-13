/**
 * 
 */
package com.resmanager.client.user.balance;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.SettledDataModel;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

/**
 * @author ShenYang 获取驾驶员结算明细
 * 
 */
public class GetSettlementSumAsyncTask extends AsyncTask<Void, Void, String> {
	private GetSettledNumListener getSettledNumListener;

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Driver_Settlement_Sum);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("UserID", ContactsUtils.userDetailModel.getUserId());
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
		if (rv != null) {
			try {
				SettledDataModel settledDataModel = JSON.parseObject(rv, SettledDataModel.class);
				getSettledNumListener.getSettledNumResult(settledDataModel);
			} catch (Exception e) {
				getSettledNumListener.getSettledNumResult(null);
				e.printStackTrace();
			}
		} else {
			getSettledNumListener.getSettledNumResult(null);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// if (loadingDialog == null) {
		// this.loadingDialog = CommonView.LoadingDialog(context);
		// }
		// if (loadingDialog.isShowing() == false)
		// this.loadingDialog.show();
	}

	public GetSettledNumListener getGetSettledNumListener() {
		return getSettledNumListener;
	}

	public void setGetSettledNumListener(GetSettledNumListener getSettledNumListener) {
		this.getSettledNumListener = getSettledNumListener;
	}

	public interface GetSettledNumListener {
		public void getSettledNumResult(SettledDataModel settledDataModel);
	}
}
