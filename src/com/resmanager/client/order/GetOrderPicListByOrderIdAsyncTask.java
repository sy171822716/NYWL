/**   
 * @Title: GetGoodsInfoByOrderId.java 
 * @Package com.resmanager.client.order 
 * @Description: TODO 
 * @author ShenYang  
 * @date 2015-12-30 上午9:22:13 
 * @version V1.0   
 */
package com.resmanager.client.order;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.OrderPicListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @ClassName: GetGoodsInfoByOrderId
 * @Description: TODO
 * @author ShenYang
 * @date 2015-12-30 上午9:22:13
 * 
 */
public class GetOrderPicListByOrderIdAsyncTask extends AsyncTask<Void, Void, String> {
	private Context context;
	private String orderId;
	private Dialog loadingDialog;
	private GetOrderPicListListener getOrderPicListListener;

	public GetOrderPicListByOrderIdAsyncTask(Context context, String orderId) {
		this.context = context;
		this.orderId = orderId;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetOrderPicList);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("OrderID", orderId);
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
		if (this.loadingDialog.isShowing()) {
			this.loadingDialog.cancel();
			this.loadingDialog = null;
		}
		if (rv != null) {
			try {
				OrderPicListModel orderPicListModel = JSON.parseObject(rv, OrderPicListModel.class);
				getOrderPicListListener.getOrderPicListResult(orderPicListModel);
			} catch (Exception e) {
				getOrderPicListListener.getOrderPicListResult(null);
				e.printStackTrace();
			}
		} else {
			getOrderPicListListener.getOrderPicListResult(null);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (loadingDialog == null) {
			this.loadingDialog = CommonView.LoadingDialog(context);
		}
		if (this.loadingDialog.isShowing() == false) {
			this.loadingDialog.show();
		}
	}

	public GetOrderPicListListener getGetOrderPicListListener() {
		return getOrderPicListListener;
	}

	public void setGetOrderPicListListener(GetOrderPicListListener getOrderPicListListener) {
		this.getOrderPicListListener = getOrderPicListListener;
	}

	public interface GetOrderPicListListener {
		public void getOrderPicListResult(OrderPicListModel orderPicListModel);
	}
}
