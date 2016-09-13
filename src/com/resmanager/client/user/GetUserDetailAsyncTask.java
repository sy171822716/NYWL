/**   
 * @Title: GetUserDetailAsyncTask.java 
 * @Package com.resmanager.client.user 
 * @Description: 获取用户详细信息
 * @author ShenYang  
 * @date 2015-12-1 下午2:38:06 
 * @version V1.0   
 */
package com.resmanager.client.user;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.UserDetailResult;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @ClassName: GetUserDetailAsyncTask
 * @Description:获取用户详细信息
 * @author ShenYang
 * @date 2015-12-1 下午2:38:06
 * 
 */
public class GetUserDetailAsyncTask extends AsyncTask<Void, Void, String> {

	private Dialog loadingDialog;
	private String userKey;
	private Context context;
	private GetUserDetailListener getUserDetailListener;

	public GetUserDetailAsyncTask(Context context, String userKey) {
		this.context = context;
		this.loadingDialog = CommonView.LoadingDialog(context);
		this.userKey = userKey;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GET_USERINFO);
		ws.addProperty("UserKey", userKey);
		try {
			String json = ws.start();
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (this.loadingDialog == null) {
			this.loadingDialog = CommonView.LoadingDialog(context);
		}
		this.loadingDialog.show();
	}

	@Override
	protected void onPostExecute(String rv) {
		super.onPostExecute(rv);
		this.loadingDialog.cancel();
		this.loadingDialog = null;

		if (rv != null) {
			try {
				UserDetailResult userDetailModel = JSON.parseObject(rv, UserDetailResult.class);
				getGetUserDetailListener().getUserDetailResult(userDetailModel);
			} catch (Exception e) {
				e.printStackTrace();
				getGetUserDetailListener().getUserDetailResult(null);
			}
		} else {
			getGetUserDetailListener().getUserDetailResult(null);
		}
	}

	public GetUserDetailListener getGetUserDetailListener() {
		return getUserDetailListener;
	}

	public void setGetUserDetailListener(GetUserDetailListener getUserDetailListener) {
		this.getUserDetailListener = getUserDetailListener;
	}

	public interface GetUserDetailListener {
		public void getUserDetailResult(UserDetailResult userDetailModel);
	}

}
