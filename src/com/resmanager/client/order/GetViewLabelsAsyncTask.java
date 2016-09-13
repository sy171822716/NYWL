/**
 * 查看标签图片信息
 */
package com.resmanager.client.order;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.ViewLabelListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

public class GetViewLabelsAsyncTask extends AsyncTask<Void, Void, String> {
	private String orderID;
	private Context context;
	private Dialog loadingDialog;
	private GetViewLabelListener getLabelListener;
	private int picState;
	public GetViewLabelsAsyncTask(Context context, String orderId,int picState) {
		this.context = context;
		this.orderID = orderId;
		this.picState = picState;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.ViewLabelsRecord);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("OrderID", this.orderID);
		ws.addProperty("PicState", picState);
		try {
			String json = ws.start();
			return json;
		} catch (IOException | XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
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

	@Override
	protected void onPostExecute(String rv) {
		super.onPostExecute(rv);
		if (loadingDialog.isShowing()) {
			this.loadingDialog.cancel();
			this.loadingDialog = null;
		}

		if (rv != null) {
			try {
				ViewLabelListModel viewLabelListModel = JSON.parseObject(rv, ViewLabelListModel.class);
				getGetLabelListener().getViewWLabelResult(viewLabelListModel);
			} catch (Exception e) {
				e.printStackTrace();
				getGetLabelListener().getViewWLabelResult(null);
			}
		} else {
			getGetLabelListener().getViewWLabelResult(null);
		}
	}

	public GetViewLabelListener getGetLabelListener() {
		return getLabelListener;
	}

	public void setGetLabelListener(GetViewLabelListener getLabelListener) {
		this.getLabelListener = getLabelListener;
	}

	public interface GetViewLabelListener {
		public void getViewWLabelResult(ViewLabelListModel viewLabelListModel);
	}

}
