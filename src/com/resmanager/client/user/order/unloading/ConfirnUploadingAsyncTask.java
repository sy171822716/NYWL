/**   
 * @Title: ConfirnDeliveryAsyncTask.java 
 * @Package com.resmanager.client.user.order.delivery 
 * @Description: 确认卸货
 * @author ShenYang  
 * @date 2015-12-9 下午1:33:35 
 * @version V1.0   
 */
package com.resmanager.client.user.order.unloading;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.utils.CommonView;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.utils.WebServiceUtil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * @ClassName: ConfirnDeliveryAsyncTask
 * @Description:确认卸货
 * @author ShenYang
 * @date 2015-12-9 下午1:33:35
 * 
 */
public class ConfirnUploadingAsyncTask extends AsyncTask<Void, Void, String> {
	private String workId;
	private String saleoid;
	private String locaitonName;
	private String locationAddr;
	private String lon;
	private String lat;
	private Context context;
	private Dialog loadingDialog;
	private UploadingListener uploadingListener;
	private String discharge_Remark;// 卸货备注
	private String deliveryMan;
	private String deliveryTel;
	private Bitmap uploadingImg;// 卸货单
	private String orderId;
	private int IsInsteadXH;

	public ConfirnUploadingAsyncTask(Context context, String workId, String orderId, String saleoid, String locationName, String locationAddr, String lon,
			String lat, String discharge_Remark, String deliveryMan, String deliveryTel, Bitmap uploadingImg, int IsInsteadXH) {
		this.context = context;
		this.workId = workId;
		this.orderId = orderId;
		this.saleoid = saleoid;
		this.lat = lat;
		this.locaitonName = locationName;
		this.locationAddr = locationAddr;
		this.lon = lon;
		this.discharge_Remark = discharge_Remark;
		this.deliveryMan = deliveryMan;
		this.deliveryTel = deliveryTel;
		this.uploadingImg = uploadingImg;
		this.IsInsteadXH = IsInsteadXH;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Discharge_Confirm);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("WorkID", this.workId);
		ws.addProperty("OrderID", this.orderId);
		ws.addProperty("OrderNumber", this.saleoid);
		ws.addProperty("MapLocationName", this.locaitonName);
		ws.addProperty("MapSpecificLocation", this.locationAddr);
		ws.addProperty("Longitude", this.lon);
		ws.addProperty("Latitude", this.lat);
		ws.addProperty("Discharge_Remark", discharge_Remark);// 卸货备注
		ws.addProperty("DeliveryMan", deliveryMan);// 签收人
		ws.addProperty("DeliveryTel", deliveryTel);// 签收人电话
		ws.addProperty("image", Tools.getImageByte(uploadingImg));
		ws.addProperty("fileName", this.saleoid + ".jpg");
		ws.addProperty("IsInsteadXH", IsInsteadXH);
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
				uploadingListener.uploadingResult(rm);
			} catch (Exception e) {
				uploadingListener.uploadingResult(null);
				e.printStackTrace();
			}
		} else {
			uploadingListener.uploadingResult(null);
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

	public UploadingListener getUploadingListener() {
		return uploadingListener;
	}

	public void setUploadingListener(UploadingListener uploadingListener) {
		this.uploadingListener = uploadingListener;
	}

	public interface UploadingListener {
		public void uploadingResult(ResultModel rm);
	}
}
