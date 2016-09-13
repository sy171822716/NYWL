/**   
 * @Title: UserLoginAsyncTask.java 
 * @Package com.resmanager.client.user 
 * @Description: 校验用户登录异步现成
 * @author ShenYang  
 * @date 2015-11-30 上午10:06:16 
 * @version V1.0   
 */
package com.resmanager.client.user;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.UserModel;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.utils.WebServiceUtil;

import android.content.Context;
import android.os.AsyncTask;

/**
 * @ClassName: UserLoginAsyncTask
 * @Description:校验用户登录异步现成
 * @author ShenYang
 * @date 2015-11-30 上午10:06:16
 * 
 */
public class UserLoginAsyncTask extends AsyncTask<Void, Void, String> {
	private String userName;
	private String userPass;
	private String Imei;
	private LoginListener loginListener;

	public UserLoginAsyncTask(Context context, String userName, String userPass, String Imei) {
		this.userName = userName;
		this.userPass = userPass;
		this.Imei = Imei;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.USER_LOGIN_METHOD);
		ws.addProperty("UserName", userName);
		ws.addProperty("Password", Tools.md5(userPass));
		ws.addProperty("IMEI", Imei);
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
				UserModel userModel = JSON.parseObject(rv, UserModel.class);
				loginListener.loginResult(userModel);
			} catch (Exception e) {
				e.printStackTrace();
				loginListener.loginResult(null);
			}

		} else {
			loginListener.loginResult(null);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	public LoginListener getLoginListener() {
		return loginListener;
	}

	public void setLoginListener(LoginListener loginListener) {
		this.loginListener = loginListener;
	}

	public interface LoginListener {
		public void loginResult(UserModel userModel);
	}
}
