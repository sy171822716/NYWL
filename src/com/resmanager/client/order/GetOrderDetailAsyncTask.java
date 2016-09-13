/**   
 * @Title: GetOrderDetailAsyncTask.java 
 * @Package com.resmanager.client.order 
 * @Description:获取订单明细
 * @author ShenYang  
 * @date 2015-12-3 下午1:16:08 
 * @version V1.0   
 */
package com.resmanager.client.order;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.OrderDetailInfo;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @ClassName: GetOrderDetailAsyncTask
 * @Description: 获取订单明细
 * @author ShenYang
 * @date 2015-12-3 下午1:16:08
 * 
 */
public class GetOrderDetailAsyncTask extends AsyncTask<Void, Void, String> {

	private String orderId;
	private Dialog loadingDialog;
	private String userKey;
	private GetOrderDetailListener getOrderDetailListener;

	public GetOrderDetailAsyncTask(Context context, String orderId, String userKey) {
		this.loadingDialog = CommonView.LoadingDialog(context);
		this.userKey = userKey;
		this.orderId = orderId;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GET_ORDER_MODEL);
		ws.addProperty("UserKey", this.userKey);
		ws.addProperty("OrderID", this.orderId);
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

		if (this.loadingDialog != null && this.loadingDialog.isShowing()) {
			this.loadingDialog.cancel();
		}
		if (rv != null) {
			try {
				OrderDetailInfo orderDetailInfo = JSON.parseObject(rv, OrderDetailInfo.class);
				getGetOrderDetailListener().getOrderDetailResult(orderDetailInfo);
			} catch (Exception e) {
				e.printStackTrace();
				getGetOrderDetailListener().getOrderDetailResult(null);
			}
		} else {
			getGetOrderDetailListener().getOrderDetailResult(null);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (this.loadingDialog != null && this.loadingDialog.isShowing() == false) {
			this.loadingDialog.show();
		}
	}

	public GetOrderDetailListener getGetOrderDetailListener() {
		return getOrderDetailListener;
	}

	public void setGetOrderDetailListener(GetOrderDetailListener getOrderDetailListener) {
		this.getOrderDetailListener = getOrderDetailListener;
	}

	public interface GetOrderDetailListener {
		public void getOrderDetailResult(OrderDetailInfo orderDetailInfo);
	}
}
