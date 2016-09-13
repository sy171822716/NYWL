/**
 * Copyright (C) 2014 XUNTIAN NETWORK
 *
 *
 * @description:下载
 * 
 * @version:v1.0.0 
 * @author:ShenYang
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2014-11-20     ShenYang       v1.0.0        create
 *
 *
 */
package com.resmanager.client.home;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.resmanager.client.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;

public class UpdateUtils implements com.resmanager.client.home.DownloadDialog.DownloadListener {
	private Context context;
	private String downloadUrl;
	private DownloadDialog downloadDialog;

	public static final String ROOT_DIR = Environment.getExternalStorageDirectory() + "/WUXILIFE/";// 根目录
	private String saveFileName = ROOT_DIR + "WuxiLife.apk";
	private int progress = 0;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private boolean interceptFlag = false;
	private DownloadListener downlaodListener;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				DownloadDialog.downloadProgress.setProgress(progress);
				DownloadDialog.downloadProgressText.setText("正在下载.." + progress + "%");
				break;
			case DOWN_OVER:
				if (downloadDialog != null)
					downloadDialog.dismiss();
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateUtils(Context context, String downloadUrl) {
		this.context = context;
		this.downloadUrl = downloadUrl;
		this.progress = 0;
		interceptFlag = false;
	}

	public void showDownloadDialog() {
		downloadDialog = new DownloadDialog(context, R.style.myDialogTheme);
		downloadDialog.setDownloadListener(this);
		downloadDialog.show();
		DownloadDialog.downloadProgress.setProgress(0);
		downloadApk();
	}

	/**
	 * 启动线程下载apk
	 */
	public void downloadApk() {

		Thread downLoadThread = new Thread(downApkRunnable);
		downLoadThread.start();

	}

	/**
	 * 安装包下载线程
	 */
	private Runnable downApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(downloadUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("Accept-Encoding", "identity");
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				File file = new File(ROOT_DIR);
				if (!file.exists()) {
					file.mkdirs();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[2048];
				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					handler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						handler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 安装APK
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		context.startActivity(i);
	}

	@Override
	public void getProgress(ProgressBar progressBar) {
		// this.progressBar = progressBar;
	}

	@Override
	public void cancelClick() {
		interceptFlag = true;
		if (downlaodListener != null) {
			downlaodListener.downloadCancel();
		}
	}

	public DownloadListener getDownlaodListener() {
		return downlaodListener;
	}

	public void setDownlaodListener(DownloadListener downlaodListener) {
		this.downlaodListener = downlaodListener;
	}

	public interface DownloadListener {
		public void downloadCancel();
	}

}
