/**   
 * @Title: RecoveryConfirmAsyncTask.java 
 * @Package com.resmanager.client.user.recyle 
 * @Description: 回收续传
 * @author ShenYang  
 * @date 2016-1-8 下午4:57:51 
 * @version V1.0   
 */
package com.resmanager.client.user.recyle;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.RecyleTempListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @ClassName: RecoveryConfirmAsyncTask
 * @Description:回收续传
 * @author ShenYang
 * @date 2016-1-8 下午4:57:51
 * 
 */
public class RecoveryContinueAsyncTask extends AsyncTask<Void, Void, String> {
	private Context context;
	private String FromCustomerID;
	private Dialog loadingDialog;
	private RecyleContinueListener recyleContinueListener;

	public RecoveryContinueAsyncTask(Context context, String FromCustomerID) {
		this.context = context;
		this.FromCustomerID = FromCustomerID;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Recovery_Continue);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("UserID", ContactsUtils.userDetailModel.getUserId());
		ws.addProperty("FromCustomerID", FromCustomerID);
		try {
			String jsonStr = ws.start();
			return jsonStr;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String rv) {
		super.onPostExecute(rv);
		if (rv != null) {
			try {
				RecyleTempListModel recyleTempListModel = JSON.parseObject(rv, RecyleTempListModel.class);
				recyleContinueListener.recyleContinueResult(recyleTempListModel);
			} catch (Exception e) {
				e.printStackTrace();
				recyleContinueListener.recyleContinueResult(null);
			}
		} else {
			recyleContinueListener.recyleContinueResult(null);
		}

		if (this.loadingDialog.isShowing()) {
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

	public RecyleContinueListener getRecyleContinueListener() {
		return recyleContinueListener;
	}

	public void setRecyleContinueListener(RecyleContinueListener recyleContinueListener) {
		this.recyleContinueListener = recyleContinueListener;
	}

	interface RecyleContinueListener {
		void recyleContinueResult(RecyleTempListModel recyleTempListModel);
	}
}
