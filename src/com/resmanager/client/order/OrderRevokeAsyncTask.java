package com.resmanager.client.order;

import java.io.IOException;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.Order;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

public class OrderRevokeAsyncTask extends AsyncTask<Void, Void, String> {
	private Context context;
	private Dialog loadingDialog;
	private OrderRevokeListener orderRevokeListener;
	private Map<Integer, Order> checkedMap;

	public OrderRevokeAsyncTask(Context context, Map<Integer, Order> checkedMap) {
		this.context = context;
		this.checkedMap = checkedMap;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<Integer, Order> entry : checkedMap.entrySet()) {
			Order order = entry.getValue();
			sb.append(order.getOrderID()+",");
		}
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Order_cxfp);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("OrderIDS", sb.toString());
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
				orderRevokeListener.orderRevokeResult(resultModel);
			} catch (Exception e) {
				e.printStackTrace();
				orderRevokeListener.orderRevokeResult(null);
			}
		} else {
			orderRevokeListener.orderRevokeResult(null);
		}
	}



	public OrderRevokeListener getOrderRevokeListener() {
		return orderRevokeListener;
	}

	public void setOrderRevokeListener(OrderRevokeListener orderRevokeListener) {
		this.orderRevokeListener = orderRevokeListener;
	}



	public interface OrderRevokeListener {
		public void orderRevokeResult(ResultModel resultModel);
	}

}
