package com.resmanager.client.common;

import com.resmanager.client.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class TopContainActivity extends Activity {
	/**
	 * 顶部title文字
	 */
	public TextView titleContent;
	/**
	 * 左边返回键
	 */
	public Button titleLeftBtn;
	/**
	 * 右边按键
	 */
	public Button titleRightBtn;
	/**
	 * 顶部视图
	 */
	public View topView;
	/**
	 * 中间视图
	 */
	public View centerView;

	/** 页面 上方布局，中间视图布局 */
	protected LinearLayout topLay, centerLay;
	protected LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
		setContentView(R.layout.top_contain);
		doBusiness();
	}

	private void doBusiness() {
		initView();
	}

	/**
	 * 初始化视图
	 */
	public void initView() {
		topLay = (LinearLayout) findViewById(R.id.top_lay);
		// 页面中间布局
		centerLay = (LinearLayout) findViewById(R.id.center_lay);
		topView = getTopView();// 得到最上面的视图
		centerView = getCenterView();// 得到中间的视图
		if (topView != null) {
			topLay.removeAllViews();
			topLay.addView(topView, new ViewPager.LayoutParams());
		} else {
			topLay.removeAllViews();
			topLay.addView(topView, new ViewPager.LayoutParams());
		}
		if (centerView != null) {
			centerLay.removeAllViews();
			centerLay.addView(centerView, new ViewPager.LayoutParams());
		}

	}

	/**
	 * 得到最上面的视图
	 * 
	 */
	protected abstract View getTopView();

	/**
	 * 得到中间的视图
	 * 
	 */
	protected abstract View getCenterView();

	/**
	 * 设置中间视图
	 * 
	 * @param layout
	 */
	public void setCenterView(int layout) {
		centerView = inflater.inflate(layout, null);
	}

}
