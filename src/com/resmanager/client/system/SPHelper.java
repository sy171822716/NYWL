package com.resmanager.client.system;

import com.resmanager.client.model.UserModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 
 */
public class SPHelper {
	private static SPHelper instance;
	private Context context;

	/**
	 * 
	 * @param context
	 */
	private SPHelper(Context context) {
		this.context = context;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static SPHelper getInstance(Context context) {
		if (instance == null) {
			instance = new SPHelper(context);
		}
		return instance;
	}

	/**
	 * 
	 * @Description:保存用户信息
	 * @param reqUserInfo
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2015-5-19 下午4:58:31
	 */
	public void saveUserInfo(String userAccount, String userPass) {
		Editor editor = getEditor();
		editor.putString("account", userAccount);
		editor.putString("password", userPass);
		editor.commit();
	}

	/**
	 * 
	 * @Description:获取用户信息
	 * @param reqUserInfo
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2015-5-19 下午4:58:31
	 */
	public UserModel getUserInfo() {
		SharedPreferences pref = getSharedPreferences();
		UserModel userModel = new UserModel();
		userModel.setAccount(pref.getString("account", ""));
		userModel.setPass(pref.getString("password", ""));
		return userModel;
	}

	/**
	 * get Editor
	 * 
	 * @return
	 */
	private Editor getEditor() {
		SharedPreferences pref = getSharedPreferences();
		return pref.edit();
	}

	/**
	 * get SharedPreferences
	 * 
	 * @return
	 */
	private SharedPreferences getSharedPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * 
	 * @Title: deleteSp
	 * @Description:删除缓存数据
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void deleteSp() {
		SharedPreferences sp = getSharedPreferences();
		sp.edit().clear().commit();
	}
}
