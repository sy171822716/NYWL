/**   
 * @Title: GetUserRecyleList.java 
 * @Package com.resmanager.client.user.balance 
 * @Description: 获取用户回收列表
 * @author ShenYang  
 * @date 2016-1-27 下午1:15:48 
 * @version V1.0   
 */
package com.resmanager.client.user.balance;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.RecyleListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

/**
 * @ClassName: GetUserRecyleList
 * @Description: 获取用户回收列表
 * @author ShenYang
 * @date 2016-1-27 下午1:15:48
 * 
 */
public class GetUserRecyleList extends AsyncTask<Void, Void, String> {
	private Context context;
	private String stateCode;
	private int pageIndex;
	private Dialog loadingDialog;
	private int orpType;
	private GetRecyleListListener getRecyleListListener;

	public GetUserRecyleList(Context context, String stateCode, int pageIndex, int orpType) {
		this.context = context;
		this.stateCode = stateCode;
		this.pageIndex = pageIndex;
		this.orpType = orpType;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.RecoveryList);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("StateCode", this.stateCode);
		ws.addProperty("UserID", ContactsUtils.userDetailModel.getUserId());
		ws.addProperty("pageSize", ContactsUtils.PAGE_SIZE);
		ws.addProperty("pageIndex", this.pageIndex);
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
				RecyleListModel recyleListModel = JSON.parseObject(rv, RecyleListModel.class);
				getRecyleListListener.getRecyleListResult(recyleListModel, orpType);
			} catch (Exception e) {
				getRecyleListListener.getRecyleListResult(null, orpType);
				e.printStackTrace();
			}
		} else {
			getRecyleListListener.getRecyleListResult(null, orpType);
		}

		if (orpType == ContactsUtils.ORP_NONE) {
			if (this.loadingDialog != null && this.loadingDialog.isShowing()) {
				this.loadingDialog.cancel();
				this.loadingDialog = null;
			}
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (orpType == ContactsUtils.ORP_NONE) {
			if (this.loadingDialog == null) {
				this.loadingDialog = CommonView.LoadingDialog(context);
			}
			if (this.loadingDialog.isShowing() == false) {
				this.loadingDialog.show();
			}
		}
	}

	public GetRecyleListListener getGetRecyleListListener() {
		return getRecyleListListener;
	}

	public void setGetRecyleListListener(GetRecyleListListener getRecyleListListener) {
		this.getRecyleListListener = getRecyleListListener;
	}

	public interface GetRecyleListListener {
		void getRecyleListResult(RecyleListModel recyleListModel, int orpType);
	}
}
