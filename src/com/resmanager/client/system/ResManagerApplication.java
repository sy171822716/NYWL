package com.resmanager.client.system;

import java.io.File;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.resmanager.client.R;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.FileUtils;
import com.resmanager.client.utils.Tools;
import com.tencent.bugly.crashreport.CrashReport;

import android.app.Application;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.view.WindowManager;

public class ResManagerApplication extends Application {
	public static RequestQueue mQueue;

	public Vibrator mVibrator;
	public static int SCREENWIDTH = 0;
	public static String PACKAGE_NAME = "";

	private void initRequestQueue() {
		mQueue = Volley.newRequestQueue(getApplicationContext());
	}

	/**
	 * 强制帮用户打开GPS
	 * 
	 * @param context
	 */
	public static final void openGPS(Context context) {
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}

	private void initImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_img)
				.showImageOnFail(R.drawable.default_img).cacheInMemory(true).cacheOnDisc(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultOptions)
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(100)// 缓存一百张图片
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		PACKAGE_NAME = Tools.getPkgName(getApplicationContext());
		WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		SCREENWIDTH = wm.getDefaultDisplay().getWidth();
		openGPS(getApplicationContext());
		if (!new File(FileUtils.SDPATH).exists()) {
			new File(FileUtils.SDPATH).mkdirs();
		}

		initImageLoader();
		initRequestQueue();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		CrashReport.initCrashReport(getApplicationContext(), ContactsUtils.BUGLYID, true);
	}

}
