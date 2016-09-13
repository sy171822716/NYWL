package com.resmanager.client.utils;

import android.content.Context;

/**
 * 检查权限的工具类
 */
public class PermissionsChecker {
	private final Context mContext;

	public PermissionsChecker(Context context) {
		mContext = context.getApplicationContext();
	}

	// 判断权限集合
	public boolean lacksPermissions(String... permissions) {
		for (String permission : permissions) {
			if (lacksPermission(permission)) {
				return true;
			}
		}
		return false;
	}

	// 判断是否缺少权限
	private boolean lacksPermission(String permission) {
		// return mContext.checkSelfPermission(mContext, permission) ==
		// PackageManager.PERMISSION_DENIED;
		return true; 
	}
}