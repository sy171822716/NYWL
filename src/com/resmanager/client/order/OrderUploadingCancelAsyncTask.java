package com.resmanager.client.order;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

public class OrderUploadingCancelAsyncTask extends AsyncTask<Void, Void, String> {
	private Context context;
	private Dialog loadingDialog;
	private OrdeDischargeCancelListener orderDischargeCancelListener;
	private String WorkID;
	private String OrderID;

	public OrderUploadingCancelAsyncTask(Context context, String WorkID, String OrderID) {
		this.context = context;
		this.WorkID = WorkID;
		this.OrderID = OrderID;
	}

	@Override
	protected String doInBackground(Void... arg0) {

		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Discharge_Cancel);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("WorkID", WorkID);
		ws.addProperty("OrderID", OrderID);
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
				ResultModel resultModel = JSON.parseObject(rv, ResultModel.class);
				orderDischargeCancelListener.orderDischargeCancelResult(resultModel);
			} catch (Exception e) {
				e.printStackTrace();
				orderDischargeCancelListener.orderDischargeCancelResult(null);
			}
		} else {
			orderDischargeCancelListener.orderDischargeCancelResult(null);
		}
	}

	

	public OrdeDischargeCancelListener getOrderDischargeCancelListener() {
		return orderDischargeCancelListener;
	}

	public void setOrderDischargeCancelListener(OrdeDischargeCancelListener orderDischargeCancelListener) {
		this.orderDischargeCancelListener = orderDischargeCancelListener;
	}



	public interface OrdeDischargeCancelListener {
		public void orderDischargeCancelResult(ResultModel resultModel);
	}

}
