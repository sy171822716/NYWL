/**   
 * @Title: FlagUnLockAsyncTask.java 
 * @Package com.resmanager.client.flag 
 * @Description: 标签解锁
 * @author ShenYang  
 * @date 2015-12-4 下午3:58:37 
 * @version V1.0   
 */
package com.resmanager.client.flag;

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
 * @ClassName: FlagUnLockAsyncTask
 * @Description: 标签解锁
 * @author ShenYang
 * @date 2015-12-4 下午3:58:37
 * 
 */
public class FlagUnLockAsyncTask extends AsyncTask<Void, Void, String> {

	private Dialog loadingDialog;
	private String userKey;
	private String flagStr;
	private FlagUnLockListener flagUnLockListener;
	private int orpType;

	public FlagUnLockAsyncTask(Context context, String userKey, String flagStr, int orpType) {
		this.loadingDialog = CommonView.LoadingDialog(context);
		this.userKey = userKey;
		this.flagStr = flagStr;
		this.orpType = orpType;
	}

	@Override
	protected String doInBackground(Void... arg0) {
		String method = ContactsUtils.LABEL_UNLOCKED;
		switch (orpType) {
		case ContactsUtils.FLAG_LOCK_RESULTCODE:
			// 锁定
			method = ContactsUtils.LABEL_LOCKED;
			break;
		case ContactsUtils.FLAG_UNLOCK_RESULTCODE:
			// 解锁
			method = ContactsUtils.LABEL_UNLOCKED;
			break;

		default:
			break;
		}
		WebServiceUtil ws = new WebServiceUtil(false, ContactsUtils.WS_URL, method);
		ws.addProperty("UserKey", userKey);
		ws.addProperty("LabelCode", flagStr);
		ws.addProperty("UserName", ContactsUtils.userDetailModel.getUserName());// 工号
		ws.addProperty("NickName", ContactsUtils.userDetailModel.getNickName());// 名字
		try {
			String jsonStr = ws.start();
			return jsonStr;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (this.loadingDialog.isShowing() == false) {
			this.loadingDialog.show();
		}
	}

	@Override
	protected void onPostExecute(String rv) {
		super.onPostExecute(rv);
		if (rv != null) {
			try {
				ResultModel resultModel = JSON.parseObject(rv, ResultModel.class);
				getFlagUnLockListener().flagUnLockResult(resultModel, flagStr);
			} catch (Exception e) {
				e.printStackTrace();
				getFlagUnLockListener().flagUnLockResult(null, flagStr);
			}
		} else {
			getFlagUnLockListener().flagUnLockResult(null, flagStr);
		}
		if (this.loadingDialog.isShowing()) {
			this.loadingDialog.cancel();
		}
	}

	public FlagUnLockListener getFlagUnLockListener() {
		return flagUnLockListener;
	}

	public void setFlagUnLockListener(FlagUnLockListener flagUnLockListener) {
		this.flagUnLockListener = flagUnLockListener;
	}

	public interface FlagUnLockListener {
		public void flagUnLockResult(ResultModel resultModel, String code);
	}

}
