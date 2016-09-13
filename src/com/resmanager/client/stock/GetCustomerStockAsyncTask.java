/**   
 * @Title: GetCustomerStockAsyncTask.java 
 * @Package com.resmanager.client.stock 
 * @Description:  获取客户库存列表
 * @author ShenYang  
 * @date 2016-1-19 上午8:55:20 
 * @version V1.0   
 */
package com.resmanager.client.stock;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.StockListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

/**
 * @ClassName: GetCustomerStockAsyncTask
 * @Description: 获取客户库存列表
 * @author ShenYang
 * @date 2016-1-19 上午8:55:20
 * 
 */
public class GetCustomerStockAsyncTask extends AsyncTask<Void, Void, String> {
	private int currentPage;
	private String CustomerName;
	private Dialog loadingDialog;
	private int orpType;
	private Context context;
	private boolean flag;
	private GetCustomerStocListListener getCustomerStocListListener;

	public GetCustomerStockAsyncTask(Context context, int currentPage, int orpType, String CustomerName, boolean flag) {
		this.context = context;
		this.currentPage = currentPage;
		this.orpType = orpType;
		this.CustomerName = CustomerName;
		this.flag = flag;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Stock_CustomerList);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("CustomerName", CustomerName);
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
				StockListModel stockListModel = JSON.parseObject(rv, StockListModel.class);
				getCustomerStocListListener.getCustomerStockResult(orpType, stockListModel);
			} catch (Exception e) {
				getCustomerStocListListener.getCustomerStockResult(orpType, null);
			}
		} else {
			getCustomerStocListListener.getCustomerStockResult(orpType, null);
		}
		if (orpType == ContactsUtils.ORP_NONE) {
			if (flag == true) {
				if (this.loadingDialog != null && this.loadingDialog.isShowing()) {
					this.loadingDialog.cancel();
					this.loadingDialog = null;
				}
			}
		}

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (orpType == ContactsUtils.ORP_NONE) {
			if (flag == true) {
				if (this.loadingDialog == null) {
					this.loadingDialog = CommonView.LoadingDialog(context);
				}
				if (this.loadingDialog.isShowing() == false) {
					this.loadingDialog.show();
				}
			}

		}

	}

	public GetCustomerStocListListener getGetCustomerStocListListener() {
		return getCustomerStocListListener;
	}

	public void setGetCustomerStocListListener(GetCustomerStocListListener getCustomerStocListListener) {
		this.getCustomerStocListListener = getCustomerStocListListener;
	}

	public interface GetCustomerStocListListener {
		public void getCustomerStockResult(int orpType, StockListModel stockListModel);
	}

}
