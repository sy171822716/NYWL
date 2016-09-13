package com.resmanager.client.utils;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

/**
 * 
 * @author baozi
 * 
 */
@SuppressLint("InlinedApi")
public class UseCameraActivity extends Activity {
	private static String mImageFilePath;
	public static final String IMAGEFILEPATH = "ImageFilePath";
	public final static String IMAGE_PATH = "image_path";
	static Activity mContext;
	public final static int GET_IMAGE_REQ = 5000;
	private static Context applicationContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageFilePath = getIntent().getExtras().getString("img_path");
		// 判断 activity被销毁后 有没有数据被保存下来
		if (savedInstanceState != null) {

			mImageFilePath = savedInstanceState.getString(IMAGEFILEPATH);

			Log.i("123---savedInstanceState", mImageFilePath);

			File mFile = new File(IMAGEFILEPATH);
			if (mFile.exists()) {
				Intent rsl = new Intent();
				rsl.putExtra(IMAGE_PATH, mImageFilePath);
				setResult(ContactsUtils.TAKE_PHOTO_RESULT, rsl);
				Log.i("123---savedInstanceState", "图片拍摄成功");
				finish();
			} else {
				Log.i("123---savedInstanceState", "图片拍摄失败");
			}
		}

		mContext = this;
		applicationContext = getApplicationContext();
		if (savedInstanceState == null) {
			initialUI();
		}

	}

	/**
	 * 
	 * @Description:设置图片地址
	 * @param imagePath
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-11-12 下午6:27:47
	 */
	public static void setImagePath(String imagePath) {
		mImageFilePath = imagePath;
	}

	public void initialUI() {
		// 根据时间生成 后缀为 .jpg 的图片
		// mImageFilePath = getCameraPath() + ts + ".jpg";
		File out = new File(mImageFilePath);
		showCamera(out);

	}

	/**
	 * 
	 * @Description:启动相机
	 * @param out
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-11-12 下午6:27:58
	 */
	private void showCamera(File out) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out)); // set
		startActivityForResult(intent, GET_IMAGE_REQ);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (GET_IMAGE_REQ == requestCode) {
			Intent rsl = new Intent();
			rsl.putExtra(IMAGE_PATH, mImageFilePath);
			mContext.setResult(ContactsUtils.TAKE_PHOTO_RESULT, rsl);
			mContext.finish();
		} else {
			mContext.finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("ImageFilePath", mImageFilePath + "");

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

	}

	// public static String getCameraPath()
	// {
	// String filePath = getImageRootPath() + "/camera/";
	// File file = new File(filePath);
	// if (!file.isDirectory())
	// {
	// file.mkdirs();
	// }
	// file = null;
	// return filePath;
	// }

	public static String getImageRootPath() {
		String filePath = getAppRootPath() + "/image";
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = null;
		return filePath;
	}

	public static String getAppRootPath() {
		String filePath = "/a";
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			filePath = Environment.getExternalStorageDirectory() + filePath;
		} else {
			filePath = applicationContext.getCacheDir() + filePath;
		}
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = null;
		File nomedia = new File(filePath + "/.nomedia");
		if (!nomedia.exists())
			try {
				nomedia.createNewFile();
			} catch (IOException e) {
			}
		return filePath;
	}

}
// ┏┓　　　┏┓
// ┏┛┻━━━┛┻┓
// ┃　　　　　　　┃ 　
// ┃　　　━　　　┃
// ┃　┳┛　┗┳　┃
// ┃　　　　　　　┃
// ┃　　　┻　　　┃
// ┃　　　　　　　┃
// ┗━┓　　　┏━┛
// ┃　　　┃ 神兽保佑　　　　　　　　
// ┃　　　┃ 代码无BUG！
// ┃　　　┗━━━┓
// ┃　　　　　　　┣┓
// ┃　　　　　　　┏┛
// ┗┓┓┏━┳┓┏┛
// ┃┫┫　┃┫┫
// ┗┻┛　┗┻┛
