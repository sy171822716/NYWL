/**   
 * @Title: GetVersionAsyncTask.java 
 * @Package com.resmanager.client.system 
 * @Description:获取版本号
 * @author ShenYang  
 * @date 2015-11-30 上午8:59:01 
 * @version V1.0   
 */
package com.resmanager.client.system;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.resmanager.client.model.VersionModel;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.WebServiceUtil;

/**
 * @ClassName: GetVersionAsyncTask
 * @Description:获取版本号
 * @author ShenYang
 * @date 2015-11-30 上午8:59:01
 * 
 */
public class GetVersionAsyncTask extends AsyncTask<Void, Void, String> {

	private GetVersionCodeListener getVersionCodeListener;

	@Override
	protected String doInBackground(Void... arg0) {
		return getVersion();
	}

	@Override
	protected void onPostExecute(String rv) {
		super.onPostExecute(rv);
		if (rv != null) {
			try {

				VersionModel versionModel = JSON.parseObject(rv, VersionModel.class);
				if (versionModel != null) {
					getVersionCodeListener.GetVersionCodeResult(versionModel);
				} else {
					getVersionCodeListener.GetVersionCodeResult(null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				getVersionCodeListener.GetVersionCodeResult(null);
			}

		} else {
			getVersionCodeListener.GetVersionCodeResult(null);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	/**
	 * 
	 * @Title: getVersion
	 * @Description: 获取版本号
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public String getVersion() {
		WebServiceUtil webServiceUtil = new WebServiceUtil(false, ContactsUtils.WS_URL, ContactsUtils.GET_VERSION_METHOD);
		String jsonStr = null;
		try {
			jsonStr = webServiceUtil.start();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}

	public GetVersionCodeListener getGetVersionCodeListener() {
		return getVersionCodeListener;
	}

	public void setGetVersionCodeListener(GetVersionCodeListener getVersionCodeListener) {
		this.getVersionCodeListener = getVersionCodeListener;
	}

	/**
	 * 
	 * @ClassName: GetVersionCodeListener
	 * @Description: 获取服务器版本号
	 * @author ShenYang
	 * @date 2015-11-30 上午9:42:46
	 * 
	 */
	public interface GetVersionCodeListener {
		public void GetVersionCodeResult(VersionModel versionModel);
	}

}
