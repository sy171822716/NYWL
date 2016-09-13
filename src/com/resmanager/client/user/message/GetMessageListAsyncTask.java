/**   
 * @Title: GetMessageListAsyncTask.java 
 * @Package com.resmanager.client.user.message 
 * @Description: 获取消息列表
 * @author ShenYang  
 * @date 2016-1-18 上午10:06:16 
 * @version V1.0   
 */
package com.resmanager.client.user.message;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.MessageListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

/**
 * @ClassName: GetMessageListAsyncTask
 * @Description: 获取消息列表
 * @author ShenYang
 * @date 2016-1-18 上午10:06:16
 * 
 */
public class GetMessageListAsyncTask extends AsyncTask<Void, Void, String> {
	private int pageIndex;
	private Context context;
	private int orpType;
	private GetMessageListListener getMessageListListener;
	private Dialog loadingDialog;

	public GetMessageListAsyncTask(Context context, int pageIndex, int orpType) {
		this.context = context;
		this.orpType = orpType;
		this.pageIndex = pageIndex;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetMailList);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("UserID", ContactsUtils.userDetailModel.getUserId());
		ws.addProperty("pageSize", ContactsUtils.PAGE_SIZE);
		ws.addProperty("pageIndex", pageIndex);
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
		if (orpType == ContactsUtils.ORP_NONE) {
			if (this.loadingDialog != null) {
				if (this.loadingDialog.isShowing()) {
					this.loadingDialog.cancel();
					this.loadingDialog = null;
				}
			}
		}
		if (rv != null) {
			try {
				MessageListModel messageListModel = JSON.parseObject(rv, MessageListModel.class);
				getMessageListListener.getMessageResult(messageListModel, orpType);
			} catch (Exception e) {
				getMessageListListener.getMessageResult(null, orpType);
				e.printStackTrace();
			}
		} else {
			getMessageListListener.getMessageResult(null, orpType);
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

	public GetMessageListListener getGetMessageListListener() {
		return getMessageListListener;
	}

	public void setGetMessageListListener(GetMessageListListener getMessageListListener) {
		this.getMessageListListener = getMessageListListener;
	}

	public interface GetMessageListListener {
		public void getMessageResult(MessageListModel messageListModel, int orpType);
	}

}
