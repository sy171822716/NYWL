/**
 * Copyright (C) 2014 XUNTIAN NETWORK
 *
 *
 * @className:com.xtwl.jy.client.adapter.AdViewPagerAdapter
 * @description:广告轮播Viewpager adapter
 * 
 * @version:v1.0.0 
 * @author:shenyang
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2014-10-10     shenyang       v1.0.0        create
 *
 *
 */
package com.resmanager.client.home;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class AdViewPagerAdapter extends PagerAdapter {
	private List<View> adViews;

	// 构造方法传参
	public AdViewPagerAdapter(List<View> adViews) {
		this.adViews = adViews;
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	/**
	 * 
	 * @Description:获取某个View
	 * @param pos
	 * @return
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2015-4-22 上午9:43:28
	 */
	public View getItemView(int pos) {
		return adViews.get(pos);
	}

	// 获取数据数量
	@Override
	public int getCount() {
		return adViews.size(); // 显示内容的个数
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(adViews.get(arg1), 0);
		return adViews.get(arg1); // 预加载下一个View
	}

	@Override
	public void finishUpdate(View arg0) {

	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(adViews.get(arg1)); // 删除View
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

}
