/**   
 * @Title: GetUserListAsyncTask.java 
 * @Package com.resmanager.client.map 
 * @Description: 获取用户列表
 * @author ShenYang  
 * @date 2016-1-19 下午12:28:33 
 * @version V1.0   
 */
package com.resmanager.client.map;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.UserListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @ClassName: GetStockLabelsByCustomerId
 * @Description:根据客户获取对应的标签
 * @author ShenYang
 * @date 2016-1-19 下午12:28:33
 * 
 */
public class GetUserListAsyncTask extends AsyncTask<Void, Void, String> {

	private Context context;
	private Dialog loadingDialog;
	private GetUserListListener getUserListListener;

	public GetUserListAsyncTask(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetUserList);
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
		if (rv != null) {
			try {
				UserListModel userListModel = JSON.parseObject(rv, UserListModel.class);
				getUserListListener.getUserListResult(userListModel);
			} catch (Exception e) {
				getUserListListener.getUserListResult(null);
				e.printStackTrace();
			}
		} else {
			getUserListListener.getUserListResult(null);
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

	public GetUserListListener getGetUserListListener() {
		return getUserListListener;
	}

	public void setGetUserListListener(GetUserListListener getUserListListener) {
		this.getUserListListener = getUserListListener;
	}

	public interface GetUserListListener {
		public void getUserListResult(UserListModel userListModel);
	}
}
