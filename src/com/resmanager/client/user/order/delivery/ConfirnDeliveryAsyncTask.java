/**   
 * @Title: ConfirnDeliveryAsyncTask.java 
 * @Package com.resmanager.client.user.order.delivery 
 * @Description: 确认发货
 * @author ShenYang  
 * @date 2015-12-9 下午1:33:35 
 * @version V1.0   
 */
package com.resmanager.client.user.order.delivery;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * @ClassName: ConfirnDeliveryAsyncTask
 * @Description:确认发货
 * @author ShenYang
 * @date 2015-12-9 下午1:33:35
 * 
 */
public class ConfirnDeliveryAsyncTask extends AsyncTask<Void, Void, String> {
	private String workId;
	private String orderId;
	private String locaitonName;
	private String locationAddr;
	private String lon;
	private String lat;
	private Context context;
	private Dialog loadingDialog;
	private DeliveryListener deliveryListener;
	private String remark_txt;

	public ConfirnDeliveryAsyncTask(Context context, String workId, String orderId, String locationName, String locationAddr, String lon, String lat,
			String remark_txt) {
		this.context = context;
		this.workId = workId;
		this.orderId = orderId;
		this.lat = lat;
		this.locaitonName = locationName;
		this.locationAddr = locationAddr;
		this.lon = lon;
		this.remark_txt = remark_txt;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.CONFIRMDELIVERY);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("WorkID", this.workId);
		ws.addProperty("OrderID", this.orderId);
		ws.addProperty("MapLocationName", this.locaitonName);
		ws.addProperty("MapSpecificLocation", this.locationAddr);
		ws.addProperty("Longitude", this.lon);
		ws.addProperty("Latitude", this.lat);
		ws.addProperty("Delivery_Remark", this.remark_txt);
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
				ResultModel rm = JSON.parseObject(rv, ResultModel.class);
				deliveryListener.deliveryResult(rm);
			} catch (Exception e) {
				deliveryListener.deliveryResult(null);
				e.printStackTrace();
			}
		} else {
			deliveryListener.deliveryResult(null);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (loadingDialog == null) {
			loadingDialog = CommonView.LoadingDialog(context);
		}
		if (loadingDialog.isShowing() == false) {
			loadingDialog.show();
		}
	}

	public DeliveryListener getDeliveryListener() {
		return deliveryListener;
	}

	public void setDeliveryListener(DeliveryListener deliveryListener) {
		this.deliveryListener = deliveryListener;
	}

	public interface DeliveryListener {
		public void deliveryResult(ResultModel rm);
	}
}
