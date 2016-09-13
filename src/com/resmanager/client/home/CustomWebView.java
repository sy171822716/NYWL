package com.resmanager.client.home;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.utils.CommonView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class CustomWebView extends TopContainActivity implements OnClickListener {

	private String url;
	// private String title;
	private Dialog loadingDialog;
	private WebView webView;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wuxi.life.main.base.TopContainActivity#getTopView()
	 */
	@Override
	protected View getTopView() {
		View topView = inflater.inflate(R.layout.custom_title_bar, null);
		topView.findViewById(R.id.title_right_img).setVisibility(View.INVISIBLE);
		topView.findViewById(R.id.title_left_img).setOnClickListener(this);
		TextView contentText = (TextView) topView.findViewById(R.id.title_content);
		contentText.setText("");
		return topView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wuxi.life.main.base.TopContainActivity#getCenterView()
	 */
	@Override
	protected View getCenterView() {
		this.loadingDialog = CommonView.LoadingDialog(this);
		url = getIntent().getExtras().getString("url");
		// title = getIntent().getExtras().getString("title");
		webView = new WebView(this);
		webView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		webView.loadUrl(url);
		webView.setWebViewClient(new WebViewClient() {
			// 当点击链接时,希望覆盖而不是打开新窗口
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url); // 加载新的url
				return true; // 返回true,代表事件已处理,事件流到此终止
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if (loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

				if (loadingDialog.isShowing() == false) {
					loadingDialog.show();
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				if (loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}
			}
		});
		//
		// // 点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
		webView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
						webView.goBack(); // 后退
						return true; // 已处理
					}
				}
				return false;
			}
		});
		return webView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_img:
			if (this.webView.canGoBack()) {
				this.webView.goBack();
			} else {
				this.finish();
			}
			break;

		default:
			break;
		}
	}

}
