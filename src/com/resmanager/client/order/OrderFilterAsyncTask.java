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

public class OrderFilterAsyncTask extends AsyncTask<Void, Void, String> {
	private Context context;
	private Dialog loadingDialog;
	private OrderFilterListener orderFilterListener;
	private int IsUsed;// 0:无效 1:有效
	private Map<Integer, Order> checkedMap;

	public OrderFilterAsyncTask(Context context, Map<Integer, Order> checkedMap, int IsUsed) {
		this.context = context;
		this.checkedMap = checkedMap;
		this.IsUsed = IsUsed;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<Integer, Order> entry : checkedMap.entrySet()) {
			Order order = entry.getValue();
			sb.append(order.getOrderID()+",");
		}
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.OrderFilter);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("OrderIDS", sb.toString());
		ws.addProperty("IsUsed", IsUsed);
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
				orderFilterListener.orderFilterResult(resultModel);
			} catch (Exception e) {
				e.printStackTrace();
				orderFilterListener.orderFilterResult(null);
			}
		} else {
			orderFilterListener.orderFilterResult(null);
		}
	}

	public OrderFilterListener getOrderFilterListener() {
		return orderFilterListener;
	}

	public void setOrderFilterListener(OrderFilterListener orderFilterListener) {
		this.orderFilterListener = orderFilterListener;
	}

	public interface OrderFilterListener {
		public void orderFilterResult(ResultModel resultModel);
	}

}
