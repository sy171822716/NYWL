/**   
 * @Title: GetStockLabelsByCustomerId.java 
 * @Package com.resmanager.client.stock 
 * @Description: 根据客户获取对应的标签
 * @author ShenYang  
 * @date 2016-1-19 下午12:28:33 
 * @version V1.0   
 */
package com.resmanager.client.stock;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.LabelPackageListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @ClassName: GetStockLabelsByCustomerId
 * @Description:根据客户获取对应的标签
 * @author ShenYang
 * @date 2016-1-19 下午12:28:33
 * 
 */
public class GetStockLabelsByCustomerId extends AsyncTask<Void, Void, String> {

	private String customerId;
	private Context context;
	private Dialog loadingDialog;
	private GetLabelPackageListener getLabelPackageListener;

	public GetStockLabelsByCustomerId(Context context, String customerId) {
		this.context = context;
		this.customerId = customerId;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Stock_CompanyLabelList);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("CustomerID", customerId);
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
		if (rv != null) {
			try {
				LabelPackageListModel labelPackageListModel = JSON.parseObject(rv, LabelPackageListModel.class);
				getLabelPackageListener.getLabelPackageResult(labelPackageListModel);
			} catch (Exception e) {
				getLabelPackageListener.getLabelPackageResult(null);
				e.printStackTrace();
			}
		} else {
			getLabelPackageListener.getLabelPackageResult(null);
		}
		if (this.loadingDialog != null && this.loadingDialog.isShowing()) {
			this.loadingDialog.cancel();
			this.loadingDialog = null;
		}

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (this.loadingDialog == null) {
			this.loadingDialog = CommonView.LoadingDialog(context);
		}
		if (this.loadingDialog.isShowing() == false) {
			this.loadingDialog.show();
		}
	}

	public GetLabelPackageListener getGetLabelPackageListener() {
		return getLabelPackageListener;
	}

	public void setGetLabelPackageListener(GetLabelPackageListener getLabelPackageListener) {
		this.getLabelPackageListener = getLabelPackageListener;
	}

	public interface GetLabelPackageListener {
		public void getLabelPackageResult(LabelPackageListModel labelPackageListModel);
	}
}
