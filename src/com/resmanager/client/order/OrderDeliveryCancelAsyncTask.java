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

public class OrderDeliveryCancelAsyncTask extends AsyncTask<Void, Void, String> {
	private Context context;
	private Dialog loadingDialog;
	private OrderDeliveryCancelListener orderDeliveryCancelListener;
	private String WorkID;

	public OrderDeliveryCancelAsyncTask(Context context, String WorkID) {
		this.context = context;
		this.WorkID = WorkID;
	}

	@Override
	protected String doInBackground(Void... arg0) {

		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Delivery_Cancel);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("WorkID", WorkID);
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
				orderDeliveryCancelListener.orderDeliveryCancelResult(resultModel);
			} catch (Exception e) {
				e.printStackTrace();
				orderDeliveryCancelListener.orderDeliveryCancelResult(null);
			}
		} else {
			orderDeliveryCancelListener.orderDeliveryCancelResult(null);
		}
	}

	public OrderDeliveryCancelListener getOrderDeliveryCancelListener() {
		return orderDeliveryCancelListener;
	}

	public void setOrderDeliveryCancelListener(OrderDeliveryCancelListener orderDeliveryCancelListener) {
		this.orderDeliveryCancelListener = orderDeliveryCancelListener;
	}

	public interface OrderDeliveryCancelListener {
		public void orderDeliveryCancelResult(ResultModel resultModel);
	}

}
