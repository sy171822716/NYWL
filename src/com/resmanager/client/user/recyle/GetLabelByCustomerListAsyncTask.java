package com.resmanager.client.user.recyle;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.RecyleLabelListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

public class GetLabelByCustomerListAsyncTask extends AsyncTask<Void, Void, String> {
	private Context context;
	private String customerID;
	private String LabelCode;
	private Dialog loadingDialog;
	private GetLabelByCustomerListener getLabelByCustomerListener;
	private String workId;
	private boolean isShowing;

	public GetLabelByCustomerListAsyncTask(Context context, String customerID, String workId, String LabelCode, boolean isShowing) {
		this.context = context;
		this.workId = workId;
		this.customerID = customerID;
		this.LabelCode = LabelCode;
		this.isShowing = isShowing;
	}

	@Override
	protected String doInBackground(Void... arg0) {

		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetLabelByCustomerList);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("CustomerID", customerID);
		ws.addProperty("WorkID", workId);
		ws.addProperty("LabelCode", LabelCode);
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
		if (isShowing) {
			if (loadingDialog.isShowing()) {
				this.loadingDialog.cancel();
				this.loadingDialog = null;
			}
		}

		if (rv != null) {
			try {
				RecyleLabelListModel recyleLabelListModel = JSON.parseObject(rv, RecyleLabelListModel.class);
				getLabelByCustomerListener.getLabelByCustomerResult(recyleLabelListModel);
			} catch (Exception e) {
				getLabelByCustomerListener.getLabelByCustomerResult(null);
				e.printStackTrace();
			}
		} else {
			getLabelByCustomerListener.getLabelByCustomerResult(null);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (isShowing) {
			if (loadingDialog == null) {
				loadingDialog = CommonView.LoadingDialog(context);
			}
			if (loadingDialog.isShowing() == false) {
				this.loadingDialog.show();
			}
		}
	}

	public GetLabelByCustomerListener getGetLabelByCustomerListener() {
		return getLabelByCustomerListener;
	}

	public void setGetLabelByCustomerListener(GetLabelByCustomerListener getLabelByCustomerListener) {
		this.getLabelByCustomerListener = getLabelByCustomerListener;
	}

	/**
	 * 
	 * @ClassName: GetLabelByCustomerListener
	 * @Description: 获取返回
	 * @author ShenYang
	 * @date 2016-1-7 下午1:25:44
	 * 
	 */
	public interface GetLabelByCustomerListener {
		public void getLabelByCustomerResult(RecyleLabelListModel recyleLabelListModel);
	}
}
