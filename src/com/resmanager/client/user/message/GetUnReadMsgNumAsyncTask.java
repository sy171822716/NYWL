/**   
 * @Title: GetUnReadMsgNumAsyncTask.java 
 * @Package com.resmanager.client.user.message 
 * @Description: 获取未读消息数量
 * @author ShenYang   
 * @date 2016-1-15 下午10:17:29 
 * @version V1.0   
 */
package com.resmanager.client.user.message;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.os.AsyncTask;

/**
 * @ClassName: GetUnReadMsgNumAsyncTask
 * @Description:获取未读消息数量
 * @author ShenYang
 * @date 2016-1-15 下午10:17:29
 * 
 */
public class GetUnReadMsgNumAsyncTask extends AsyncTask<Void, Void, String> {
	private GetUnReadListenr getUnReadListenr;

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetUnreadMailCount);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("UserID", ContactsUtils.userDetailModel.getUserId());
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
				ResultModel resultModel = JSON.parseObject(rv, ResultModel.class);
				getUnReadListenr.getUnReadResult(resultModel);
			} catch (Exception e) {
				getUnReadListenr.getUnReadResult(null);
				e.printStackTrace();
			}
		} else {
			getUnReadListenr.getUnReadResult(null);
		}

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	public GetUnReadListenr getGetUnReadListenr() {
		return getUnReadListenr;
	}

	public void setGetUnReadListenr(GetUnReadListenr getUnReadListenr) {
		this.getUnReadListenr = getUnReadListenr;
	}

	public interface GetUnReadListenr {
		public void getUnReadResult(ResultModel resultModel);
	}
}
