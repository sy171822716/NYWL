/**   
 * @Title: GetCustomerListAsyncTask.java 
 * @Package com.resmanager.client.user.recyle 
 * @Description: 获取客户列表
 * @author ShenYang  
 * @date 2016-1-5 上午9:36:52 
 * @version V1.0   
 */
package com.resmanager.client.user.recyle;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.CustomerListModel;
import com.resmanager.client.model.CustomerModel;
import com.resmanager.client.system.DBHelper;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @ClassName: GetCustomerListAsyncTask
 * @Description: 获取客户列表
 * @author ShenYang
 * @date 2016-1-5 上午9:36:52
 * 
 */
@SuppressLint("DefaultLocale")
public class GetCustomerListAsyncTask extends AsyncTask<Void, Void, CustomerListModel> {
	private String customerName;
	private int currentPage;
	private Dialog loadingDialog;
	private Context context;
	private GetCustomerListListener getCustomerListListener;
	private DBHelper dbHelper;
	private boolean isGetFromNet;// 是否从网络获取
	private CharacterParser characterParser;

	public GetCustomerListAsyncTask(Context context, String customerName, int currentPage, boolean isGetFromNet) {
		this.context = context;
		this.isGetFromNet = isGetFromNet;
		this.currentPage = currentPage;
		this.customerName = customerName;
		this.dbHelper = new DBHelper(context);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
	}

	@Override
	protected CustomerListModel doInBackground(Void... arg0) {
		ArrayList<CustomerModel> customerModels = dbHelper.getCustomerModels();
		if (customerModels.size() > 0 && isGetFromNet == false) {
			// 数据库获取
			CustomerListModel customerListModel = new CustomerListModel();
			customerListModel.setResult("true");
			customerListModel.setData(customerModels);
			return customerListModel;
		} else {
			WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetCustomerList);
			ws.addProperty("UserKey", ContactsUtils.USER_KEY);
			ws.addProperty("CustomerName", customerName);
			ws.addProperty("pageSize", 10000000);
			ws.addProperty("pageIndex", currentPage);
			try {
				String jsonStr = ws.start();
				CustomerListModel customerListModel = JSON.parseObject(jsonStr, CustomerListModel.class);
				if (customerListModel.getResult().equals("true")) {
					customerListModel.setData(filledData(customerListModel.getData()));
				}
				return customerListModel;
			} catch (IOException | XmlPullParserException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(CustomerListModel customerListModel) {
		super.onPostExecute(customerListModel);
		if (customerListModel != null) {
			try {
				getCustomerListListener.getCustomerListResult(customerListModel);
			} catch (Exception e) {
				getCustomerListListener.getCustomerListResult(null);
				e.printStackTrace();
			}
		} else {
			getCustomerListListener.getCustomerListResult(null);
		}
		if (loadingDialog.isShowing()) {
			this.loadingDialog.cancel();
			this.loadingDialog = null;
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (loadingDialog == null) {
			loadingDialog = CommonView.LoadingDialog(context);
		}
		if (loadingDialog.isShowing() == false) {
			this.loadingDialog.show();
		}
	}

	public GetCustomerListListener getGetCustomerListListener() {
		return getCustomerListListener;
	}

	public void setGetCustomerListListener(GetCustomerListListener getCustomerListListener) {
		this.getCustomerListListener = getCustomerListListener;
	}

	public interface GetCustomerListListener {
		public void getCustomerListResult(CustomerListModel customerListModel);
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private ArrayList<CustomerModel> filledData(ArrayList<CustomerModel> data) {
		ArrayList<CustomerModel> mSortList = new ArrayList<CustomerModel>();
		dbHelper.deleteTable();// 清空表
		for (int i = 0; i < data.size(); i++) {
			CustomerModel customerModel = new CustomerModel();
			customerModel.setCustomerID(data.get(i).getCustomerID());
			customerModel.setCustomerName(data.get(i).getCustomerName());
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(data.get(i).getCustomerName());
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				customerModel.setSortLetters(sortString.toUpperCase());
			} else {
				customerModel.setSortLetters("#");
			}
			mSortList.add(customerModel);
			dbHelper.insertToCustomer(customerModel.getCustomerID(), customerModel.getCustomerName(), customerModel.getSortLetters());
		}
		return mSortList;

	}
}
