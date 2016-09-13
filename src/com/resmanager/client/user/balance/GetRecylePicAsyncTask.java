/**   
 * @Title: GetUserRecyleList.java 
 * @Package com.resmanager.client.user.balance 
 * @Description: 获取用户回收图片列表
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
import com.resmanager.client.model.RecylePicListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

/**
 * @ClassName: GetUserRecyleList
 * @Description: 获取用户回收图片列表
 * @author ShenYang
 * @date 2016-1-27 下午1:15:48
 * 
 */
public class GetRecylePicAsyncTask extends AsyncTask<Void, Void, String> {
	private Context context;
	private String WorkID;
	private Dialog loadingDialog;
	private GetRecylePicListener getRecylePicListener;

	public GetRecylePicAsyncTask(Context context, String WorkID) {
		this.context = context;
		this.WorkID = WorkID;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Recovery_PicList);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("WorkID", this.WorkID);
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
				RecylePicListModel recylePicListModel = JSON.parseObject(rv, RecylePicListModel.class);
				getRecylePicListener.getRecylePicResult(recylePicListModel);
			} catch (Exception e) {
				getRecylePicListener.getRecylePicResult(null);
				e.printStackTrace();
			}
		} else {
			getRecylePicListener.getRecylePicResult(null);
		}
		if (this.loadingDialog != null) {
			if (this.loadingDialog.isShowing()) {
				this.loadingDialog.cancel();
				this.loadingDialog = null;
			}
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

	public GetRecylePicListener getGetRecylePicListener() {
		return getRecylePicListener;
	}

	public void setGetRecylePicListener(GetRecylePicListener getRecylePicListener) {
		this.getRecylePicListener = getRecylePicListener;
	}

	public interface GetRecylePicListener {
		public void getRecylePicResult(RecylePicListModel recylePicListModel);
	}
}
