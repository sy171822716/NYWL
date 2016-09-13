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

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.RecyleListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

/**
 * @ClassName: GetOrderListAsyncTask
 * @Description: 获取订单列表
 * @author ShenYang
 * @date 2015-12-1 上午9:35:05
 * 
 */
public class GetRecyleByLabelAsyncTask extends AsyncTask<Void, Void, String> {
	private int currentPage;
	private boolean isShowDialog;
	private Dialog loadingDialog;
	private int orpType;
	private GetRecyleListByLabelListener getRecyleListByLabelListener;
	private String searchStr;
	private String startDate = "";
	private String endDate = "";
	private String DayType = "";
	private String ordercustomer = "";
	private String DriverID = "";
	private String salername = "";

	public GetRecyleByLabelAsyncTask(Context context, int currentPage, String searchStr, String startDate, String endDate, String DayType,
			String ordercustomer, String DriverID, String salername, boolean isShowDialog, int orpType) {
		this.currentPage = currentPage;
		this.orpType = orpType;
		this.DayType = DayType;
		this.ordercustomer = ordercustomer;
		this.DriverID = DriverID;
		this.searchStr = searchStr;
		this.startDate = startDate;
		this.endDate = endDate;
		this.isShowDialog = isShowDialog;
		this.salername = salername;
		if (isShowDialog) {
			this.loadingDialog = CommonView.LoadingDialog(context);
		}
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Recovery_ByLabel);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("LabelCode", this.searchStr);
		ws.addProperty("StartDate", this.startDate);
		ws.addProperty("EndDate", this.endDate);
		ws.addProperty("DayType", this.DayType);
		ws.addProperty("ordercustomer", this.ordercustomer);
		ws.addProperty("DriverID", this.DriverID);
		ws.addProperty("saler_name", this.salername);
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
				RecyleListModel recyleListModel = JSON.parseObject(rv, RecyleListModel.class);
				getRecyleListByLabelListener.getRecyleListByLabelResult(orpType, recyleListModel);
			} catch (Exception e) {
				getRecyleListByLabelListener.getRecyleListByLabelResult(orpType, null);
				e.printStackTrace();
			}
		} else {
			getRecyleListByLabelListener.getRecyleListByLabelResult(orpType, null);
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

	public GetRecyleListByLabelListener getGetRecyleListByLabelListener() {
		return getRecyleListByLabelListener;
	}

	public void setGetRecyleListByLabelListener(GetRecyleListByLabelListener getRecyleListByLabelListener) {
		this.getRecyleListByLabelListener = getRecyleListByLabelListener;
	}

	public interface GetRecyleListByLabelListener {
		public void getRecyleListByLabelResult(int orpType, RecyleListModel recyleListModel);
	}

}
