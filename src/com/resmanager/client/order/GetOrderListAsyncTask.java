/**   
 * @Title: GetOrderListAsyncTask.java 
 * @Package com.resmanager.client.order 
 * @Description:获取订单列表
 * @author ShenYang  
 * @date 2015-12-1 上午9:35:05 
 * @version V1.0   
 */
package com.resmanager.client.order;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.OrderListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @ClassName: GetOrderListAsyncTask
 * @Description: 获取订单列表
 * @author ShenYang
 * @date 2015-12-1 上午9:35:05
 * 
 */
public class GetOrderListAsyncTask extends AsyncTask<Void, Void, String> {
	private int currentPage;
	private String orderState;
	private boolean isShowDialog;
	private String userKey;
	private String ordercustomer;// 客户名称
	private String Saleoid;// 订单号
	private String Town;// 所选区域
	private String Days;// 天数
	private String DayType;// 日期查询
	private Dialog loadingDialog;
	private int orpType;
	private GetOrderListListener getOrderListListener;
	private String userId	;
	private int isUsed;// 是否有效 0：待处理，1：有效，2：修改，3：新增，4：无效
	private String StartDate;
	private String EndDate;
	private String Packtype;
	private String saler;
	public GetOrderListAsyncTask(Context context, int currentPage, String orderState, String userKey, boolean isShowDialog, int orpType, String userId,
			int isUsed, String ordercustomer, String Saleoid, String Town, String Days, String DayType, String StartDate, String EndDate, String Packtype,String saler) {
		this.currentPage = currentPage;
		this.orderState = orderState;
		this.isShowDialog = isShowDialog;
		this.userId = userId;
		this.userKey = userKey;
		this.isUsed = isUsed;
		this.orpType = orpType;
		this.ordercustomer = ordercustomer;
		this.Saleoid = Saleoid;
		this.Town = Town;
		this.Days = Days;
		this.DayType = DayType;
		this.StartDate = StartDate;
		this.EndDate = EndDate;
		this.Packtype = Packtype;
		this.saler = saler;
		if (isShowDialog) {
			this.loadingDialog = CommonView.LoadingDialog(context);
		}
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GET_ORDER_LIST);
		ws.addProperty("UserKey", this.userKey);
		ws.addProperty("OrderStateCode", this.orderState);
		ws.addProperty("IsUsed", isUsed);
		ws.addProperty("UserID", userId);
		ws.addProperty("ordercustomer", ordercustomer);
		ws.addProperty("Saleoid", Saleoid);
		ws.addProperty("Town", Town);
		ws.addProperty("Packtype", Packtype);
		ws.addProperty("StartDate", StartDate);
		ws.addProperty("EndDate", EndDate);
		ws.addProperty("Days", Days);
		ws.addProperty("DayType", DayType);
		ws.addProperty("saler", saler);
		ws.addProperty("pageSize",ContactsUtils.PAGE_SIZE);// ContactsUtils.PAGE_SIZE
		ws.addProperty("pageIndex", this.currentPage);

		try {
			String json = ws.start();
			return json;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String rv) {
		super.onPostExecute(rv);
		if (rv != null) {
			try {
				OrderListModel orderListModel = JSON.parseObject(rv, OrderListModel.class);
				getOrderListListener.getOrderListResult(orpType, orderListModel);
			} catch (Exception e) {
				getOrderListListener.getOrderListResult(orpType, null);
			}
		} else {
			getOrderListListener.getOrderListResult(orpType, null);
		}
		if (isShowDialog) {
			this.loadingDialog.dismiss();
		}

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (isShowDialog) {
			this.loadingDialog.show();
		}
	}

	public GetOrderListListener getGetOrderListListener() {
		return getOrderListListener;
	}

	public void setGetOrderListListener(GetOrderListListener getOrderListListener) {
		this.getOrderListListener = getOrderListListener;
	}

	public interface GetOrderListListener {
		public void getOrderListResult(int orpType, OrderListModel orderListModel);
	}

}
