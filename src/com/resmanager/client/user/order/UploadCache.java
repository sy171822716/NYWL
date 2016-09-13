package com.resmanager.client.user.order;

import java.util.ArrayList;
import com.resmanager.client.model.ScanBimpModel;
import com.resmanager.client.utils.FileUtils;

public class UploadCache {
	public static ArrayList<ScanBimpModel> scanBimpModels = new ArrayList<ScanBimpModel>();

	/**
	 * 
	 * @Title: resetBimp
	 * @Description: 重置
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void resetBimp() {
//		FileUtils.deleteDir();
		UploadCache.scanBimpModels.clear();// 清空
	}
}
