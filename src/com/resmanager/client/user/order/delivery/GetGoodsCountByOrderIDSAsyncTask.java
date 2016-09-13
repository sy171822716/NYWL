/**   
 * @Title: GetGoodsCountByOrderIDSAsyncTask.java 
 * @Package com.resmanager.client.user.order.delivery 
 * @Description: 根据订单号查询产品以及包装物规格 多个订单号使用逗号分隔
 * @author ShenYang   
 * @date 2016-1-9 下午10:07:50 
 * @version V1.0   
 */
package com.resmanager.client.user.order.delivery;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.GoodsPackageQtyListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

/**
 * @ClassName: GetGoodsCountByOrderIDSAsyncTask
 * @Description: 根据订单号查询产品以及包装物规格
 * @author ShenYang
 * @date 2016-1-9 下午10:07:50
 * 
 */
public class GetGoodsCountByOrderIDSAsyncTask extends AsyncTask<Void, Void, String> {

	private String orderIds;
	private Context context;
	private Dialog loadingDialog;
	private GetGoodsCountListener getGoodsCountListener;

	public GetGoodsCountByOrderIDSAsyncTask(Context context, String orderIds) {
		this.context = context;
		this.orderIds = orderIds;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetGoodsCountByOrderID);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("OrderIDS", this.orderIds);
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
		if (loadingDialog.isShowing()) {
			this.loadingDialog.cancel();
			this.loadingDialog = null;
		}
		try {
			GoodsPackageQtyListModel goodsPackageQtyListModel = JSON.parseObject(rv, GoodsPackageQtyListModel.class);
			getGetGoodsCountListener().getGoodsCountResult(goodsPackageQtyListModel);
		} catch (Exception e) {
			getGetGoodsCountListener().getGoodsCountResult(null);
			e.printStackTrace();
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (loadingDialog == null) {
			this.loadingDialog = CommonView.LoadingDialog(context);
		}

		if (loadingDialog.isShowing() == false) {
			this.loadingDialog.show();
		}
	}

	public GetGoodsCountListener getGetGoodsCountListener() {
		return getGoodsCountListener;
	}

	public void setGetGoodsCountListener(GetGoodsCountListener getGoodsCountListener) {
		this.getGoodsCountListener = getGoodsCountListener;
	}

	public interface GetGoodsCountListener {
		public void getGoodsCountResult(GoodsPackageQtyListModel goodsPackageQtyListModel);
	}
}
