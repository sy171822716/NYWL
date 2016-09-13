/**   
 * @Title: GetOrderListAsyncTask.java 
 * @Package com.resmanager.client.order 
 * @Description:获取订单列表
 * @author ShenYang  
 * @date 2015-12-1 上午9:35:05 
 * @version V1.0   
 */
package com.resmanager.client.flag;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.FlagModelResult;
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
public class GetOrderListByLabelAsyncTask extends AsyncTask<Void, Void, String> {
	private int currentPage;
	private boolean isShowDialog;
	private Dialog loadingDialog;
	private int orpType;
	private GetOrderListByLabelListener getOrderListByLabelListener;
	private String searchStr;
	private String startDate = "";
	private String endDate = "";
	private String DayType = "";
	private String ordercustomer = "";
	private String saler = "";
	private String DriverID = "";
	private String Warehouse = "";

	public GetOrderListByLabelAsyncTask(Context context, int currentPage, String searchStr, String startDate, String endDate, String DayType,
			String ordercustomer, String saler, String DriverID, String Warehouse, boolean isShowDialog, int orpType) {
		this.currentPage = currentPage;
		this.orpType = orpType;
		this.searchStr = searchStr;
		this.startDate = startDate;
		this.endDate = endDate;
		this.ordercustomer = ordercustomer;
		this.saler = saler;
		this.DriverID = DriverID;
		this.Warehouse = Warehouse;
		this.DayType = DayType;
		this.isShowDialog = isShowDialog;
		if (isShowDialog) {
			this.loadingDialog = CommonView.LoadingDialog(context);
		}
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetOrderInfo_ByLabel);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("LabelCode", this.searchStr);
		ws.addProperty("StartDate", this.startDate);
		ws.addProperty("EndDate", this.endDate);
		ws.addProperty("DayType", this.DayType);
		ws.addProperty("ordercustomer", this.ordercustomer);
		ws.addProperty("saler", this.saler);
		ws.addProperty("DriverID", this.DriverID);
		ws.addProperty("Warehouse", this.Warehouse);
		ws.addProperty("pageSize", ContactsUtils.PAGE_SIZE);
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
				FlagModelResult orderListModel = JSON.parseObject(rv, FlagModelResult.class);
				getOrderListByLabelListener.getOrderListByLabelResult(orpType, orderListModel);
			} catch (Exception e) {
				getOrderListByLabelListener.getOrderListByLabelResult(orpType, null);
			}
		} else {
			getOrderListByLabelListener.getOrderListByLabelResult(orpType, null);
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

	public GetOrderListByLabelListener getGetOrderListByLabelListener() {
		return getOrderListByLabelListener;
	}

	public void setGetOrderListByLabelListener(GetOrderListByLabelListener getOrderListByLabelListener) {
		this.getOrderListByLabelListener = getOrderListByLabelListener;
	}

	public interface GetOrderListByLabelListener {
		public void getOrderListByLabelResult(int orpType, FlagModelResult orderListModel);
	}

}
