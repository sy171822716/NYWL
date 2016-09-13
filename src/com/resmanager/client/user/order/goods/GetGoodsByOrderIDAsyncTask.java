/**   
 * @Title: GetGoodsByOrderID.java 
 * @Package com.resmanager.client.user.order.delivery 
 * @Description: 根据订单编号找包含的产品
 * @author ShenYang   
 * @date 2015-12-25 下午3:55:18 
 * @version V1.0   
 */
package com.resmanager.client.user.order.goods;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.GoodsListModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @ClassName: GetGoodsByOrderID
 * @Description: 根据订单编号找包含的产品
 * @author ShenYang
 * @date 2015-12-25 下午3:55:18
 * 
 */
public class GetGoodsByOrderIDAsyncTask extends AsyncTask<Void, Void, String> {

	public String orderIds;// 若有多个订单，则用逗号分隔
	private Context context;
	private Dialog loadingDialog;
	private int orpType;
	private GetGoodsByOrderIdListener getGoodsByOrderIdListener;

	public GetGoodsByOrderIDAsyncTask(Context context, String orderIds, int orpType) {
		this.context = context;
		this.orderIds = orderIds;
		this.orpType = orpType;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GetGoodsByOrderID);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("OrderIDS", orderIds);
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
		if (rv != null) {
			try {
				GoodsListModel goodsListModel = JSON.parseObject(rv, GoodsListModel.class);
				getGoodsByOrderIdListener.getGoodsResult(goodsListModel, orpType);
			} catch (Exception e) {
				e.printStackTrace();
				getGoodsByOrderIdListener.getGoodsResult(null, orpType);
			}
		} else {
			getGoodsByOrderIdListener.getGoodsResult(null, orpType);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (orpType == ContactsUtils.ORP_NONE) {
			if (loadingDialog == null) {
				loadingDialog = CommonView.LoadingDialog(context);
			}
			if (loadingDialog.isShowing() == false) {
				loadingDialog.show();
			}
		}
	}

	public GetGoodsByOrderIdListener getGetGoodsByOrderIdListener() {
		return getGoodsByOrderIdListener;
	}

	public void setGetGoodsByOrderIdListener(GetGoodsByOrderIdListener getGoodsByOrderIdListener) {
		this.getGoodsByOrderIdListener = getGoodsByOrderIdListener;
	}

	public interface GetGoodsByOrderIdListener {
		public void getGoodsResult(GoodsListModel goodsListModel, int orpType);
	}
}
