package com.resmanager.client.user.order.unloading;

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
 * 
 * @ClassName: DeleteDeliveryPhotoAsyncTask
 * @Description: 删除卸货上传的照片
 * @author ShenYang
 * @date 2015-12-10 下午4:11:55
 * 
 */
public class DeleteRecylePhotoAsyncTask extends AsyncTask<Void, Void, String> {

	private Context context;
	private Dialog loadingDilog;
	private String RecPID;
	private DelRecyleListener delRecyleListener;

	public DeleteRecylePhotoAsyncTask(Context context, String RecPID) {
		this.context = context;
		this.RecPID = RecPID;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.Recovery_DelPic);
		ws.addProperty("UserKey", ContactsUtils.USER_KEY);
		ws.addProperty("RecPID", RecPID);
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
		if (this.loadingDilog.isShowing()) {
			this.loadingDilog.cancel();
			this.loadingDilog = null;
		}
		if (rv != null) {
			try {
				ResultModel resultModel = JSON.parseObject(rv, ResultModel.class);
				delRecyleListener.delRecyleResult(resultModel);
			} catch (Exception e) {
				e.printStackTrace();
				delRecyleListener.delRecyleResult(null);
			}
		} else {
			delRecyleListener.delRecyleResult(null);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (this.loadingDilog == null) {
			this.loadingDilog = CommonView.LoadingDialog(context);
		}
		if (this.loadingDilog.isShowing() == false) {
			this.loadingDilog.show();
		}
	}

	public DelRecyleListener getDelRecyleListener() {
		return delRecyleListener;
	}

	public void setDelRecyleListener(DelRecyleListener delRecyleListener) {
		this.delRecyleListener = delRecyleListener;
	}

	public interface DelRecyleListener {
		public void delRecyleResult(ResultModel resultModel);
	}
}
