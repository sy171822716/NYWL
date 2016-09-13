/**   
 * @Title: GetDriverListAsyncTask.java 
 * @Package com.resmanager.client.order 
 * @Description: 订单分配
 * @author ShenYang  
 * @date 2016-2-25 下午3:52:38 
 * @version V1.0   
 */
package com.resmanager.client.order;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.DriverModel;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

/**
 * @ClassName: GetDriverListAsyncTask
 * @Description: 订单分配
 * @author ShenYang
 * @date 2016-2-25 下午3:52:38
 * 
 */
public class OrderFPAsyncTask extends AsyncTask<Void, Void, String> {

	private Context context;
	private Dialog loadingDialog;
	private DriverModel driverModel;
	private String orderIds;
	private OrderSendListener orderSendListener;

	public OrderFPAsyncTask(Context context, String orderIds, DriverModel driverModel) {
		this.context = context;
		this.driverModel = driverModel;
		this.orderIds = orderIds;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Order_fp);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("OrderIDS", orderIds);
		ws.addProperty("DriverID", driverModel.getUserId());
		ws.addProperty("DriverName", driverModel.getNickName());
		ws.addProperty("CarNumber", driverModel.getCID());
		ws.addProperty("DriverTel", driverModel.getPhone());
		ws.addProperty("shippington", driverModel.getShippington());
		try {
			String rv = ws.start();
			return rv;
		} catch (IOException | XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String rv) {
		super.onPostExecute(rv);
		try {
			if (rv != null) {
				ResultModel resultModel = JSON.parseObject(rv, ResultModel.class);
				orderSendListener.orderSendResult(resultModel);
			} else {
				orderSendListener.orderSendResult(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			orderSendListener.orderSendResult(null);
		}

		if (this.loadingDialog != null && this.loadingDialog.isShowing()) {
			this.loadingDialog.dismiss();
			this.loadingDialog = null;
		}

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (this.loadingDialog == null) {
			this.loadingDialog = CommonView.LoadingDialog(context);
		}
		this.loadingDialog.show();
	}

	public OrderSendListener getOrderSendListener() {
		return orderSendListener;
	}

	public void setOrderSendListener(OrderSendListener orderSendListener) {
		this.orderSendListener = orderSendListener;
	}

	public interface OrderSendListener {
		public void orderSendResult(ResultModel resultModel);
	}
}
