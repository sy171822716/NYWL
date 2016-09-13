/**
 *
 *
 * @description:下载对话框
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

import com.resmanager.client.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DownloadDialog extends Dialog implements android.view.View.OnClickListener {
	private LayoutInflater mInflater;
	private View dialogView;
	public static ProgressBar downloadProgress;
	private DownloadListener downloadListener;
	public static TextView downloadProgressText;

	@SuppressLint("InflateParams")
	public DownloadDialog(Context context, int theme) {
		super(context, theme);
		mInflater = LayoutInflater.from(context);
		dialogView = mInflater.inflate(R.layout.download_dialog_layout, null);
		setContentView(dialogView);
		this.setCanceledOnTouchOutside(false);
		initView();
	}

	private void initView() {
		downloadProgress = (ProgressBar) dialogView.findViewById(R.id.download_progress);
		downloadProgressText = (TextView) dialogView.findViewById(R.id.download_progress_text);
		downloadProgress.setMax(100);
		dialogView.findViewById(R.id.download_cancel_btn).setOnClickListener(this);
		if (downloadListener != null) {
			downloadListener.getProgress(downloadProgress);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.download_cancel_btn:
			this.dismiss();
			if (downloadListener != null) {
				downloadListener.cancelClick();
			}
			break;

		default:
			break;
		}
	}

	public DownloadListener getDownloadListener() {
		return downloadListener;
	}

	public void setDownloadListener(DownloadListener downloadListener) {
		this.downloadListener = downloadListener;
	}

	public interface DownloadListener {
		public void getProgress(ProgressBar progressBar);

		public void cancelClick();
	}

}
