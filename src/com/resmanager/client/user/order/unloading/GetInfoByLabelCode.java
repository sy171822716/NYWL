package com.resmanager.client.user.order.unloading;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.GoodsResultmodel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

public class GetInfoByLabelCode extends AsyncTask<Void, Void, String> {
	private String labelCode;
	private String workId;
	private Dialog loadingDailog;
	private Context context;
	private GetInfoByLabelListener getInfoByLabelListener;

	public GetInfoByLabelCode(Context context, String labelCode, String workId) {
		this.context = context;
		this.labelCode = labelCode;
		this.workId = workId;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetDeliveryLabelsInfo);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("WorkID", workId);
		ws.addProperty("LabelCode", labelCode);
		try {
			String jsonstr = ws.start();
			return jsonstr;
		} catch (IOException | XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (非 Javadoc) <p>Title: onPostExecute</p> <p>Description: </p>
	 * 
	 * @param result
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String rv) {
		super.onPostExecute(rv);
		if (loadingDailog.isShowing()) {
			this.loadingDailog.cancel();
			this.loadingDailog = null;
		}

		if (rv != null) {
			try {
				GoodsResultmodel goodsResultmodel = JSON.parseObject(rv, GoodsResultmodel.class);
				getInfoByLabelListener.getInfoByLabelResult(goodsResultmodel);
			} catch (Exception e) {
				getInfoByLabelListener.getInfoByLabelResult(null);
			}
		} else {
			getInfoByLabelListener.getInfoByLabelResult(null);
		}
	}

	/*
	 * (非 Javadoc) <p>Title: onPreExecute</p> <p>Description: </p>
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (loadingDailog == null) {
			this.loadingDailog = CommonView.LoadingDialog(context);
		}
		if (loadingDailog.isShowing() == false) {
			this.loadingDailog.show();
		}
	}

	public GetInfoByLabelListener getGetInfoByLabelListener() {
		return getInfoByLabelListener;
	}

	public void setGetInfoByLabelListener(GetInfoByLabelListener getInfoByLabelListener) {
		this.getInfoByLabelListener = getInfoByLabelListener;
	}

	public interface GetInfoByLabelListener {
		public void getInfoByLabelResult(GoodsResultmodel goodsResultmodel);
	}

}
