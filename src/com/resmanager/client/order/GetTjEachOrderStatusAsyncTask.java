/**   
 * @Title: GetTjEachOrderStatusAsyncTask.java 
 * @Package com.resmanager.client.order 
 * @Description: 获取各个状态订单数
 * @author ShenYang   
 * @date 2015-12-23 下午3:33:40 
 * @version V1.0   
 */
package com.resmanager.client.order;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.OrderStatusNumberModel2;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @ClassName: GetTjEachOrderStatusAsyncTask
 * @Description:获取各个状态订单数
 * @author ShenYang
 * @date 2015-12-23 下午3:33:40
 * 
 */
public class GetTjEachOrderStatusAsyncTask extends AsyncTask<Void, Void, String> {

	private Dialog loadingDialog;
	private Context context;
	private GetOrderStatusNumberListener getOrderStatusNumberListener;
//	private String OrderStateCode;
//	private String IsUsed;
//	private String UserID;
//	private String ordercustomer;
//	private String Saleoid;
//	private String Town;
//	private String Days;
//	private String DayType;

	public GetTjEachOrderStatusAsyncTask(Context context) {
		this.context = context;
//		this.ordercustomer = ordercustomer;
//		this.OrderStateCode = OrderStateCode;
//		this.IsUsed = IsUsed;
//		this.UserID = UserID;
//		this.Saleoid = Saleoid;
//		this.Town = Town;
//		this.Days = Days;
//		this.DayType = DayType;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetTjEachOrderStatus);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
//		ws.addProperty("IsUsed", IsUsed);
//		ws.addProperty("UserID", UserID);
//		ws.addProperty("ordercustomer", ordercustomer);
//		ws.addProperty("Saleoid", Saleoid);
//		ws.addProperty("Town", Town);
//		ws.addProperty("Days", Days);
//		ws.addProperty("DayType", DayType);
//		ws.addProperty("OrderStateCode", OrderStateCode);
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
		if (loadingDialog.isShowing()) {
			loadingDialog.cancel();
			loadingDialog = null;
		}
		if (rv != null) {
			try {
				OrderStatusNumberModel2 orderStatusNumberModel = JSON.parseObject(rv, OrderStatusNumberModel2.class);
				getOrderStatusNumberListener.getOrderStatusNumberResult(orderStatusNumberModel);
			} catch (Exception e) {
				getOrderStatusNumberListener.getOrderStatusNumberResult(null);
				e.printStackTrace();
			}

		} else {
			getOrderStatusNumberListener.getOrderStatusNumberResult(null);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (loadingDialog == null) {
			this.loadingDialog = CommonView.LoadingDialog(context);
		}
		if (loadingDialog.isShowing() == false) {
			loadingDialog.show();
		}
	}

	public GetOrderStatusNumberListener getGetOrderStatusNumberListener() {
		return getOrderStatusNumberListener;
	}

	public void setGetOrderStatusNumberListener(GetOrderStatusNumberListener getOrderStatusNumberListener) {
		this.getOrderStatusNumberListener = getOrderStatusNumberListener;
	}

	public interface GetOrderStatusNumberListener {
		public void getOrderStatusNumberResult(OrderStatusNumberModel2 orderStatusNumberModel);
	}
}
