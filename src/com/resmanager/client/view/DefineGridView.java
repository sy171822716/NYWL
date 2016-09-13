/**
 * Copyright (C) 2014 XUNTIAN NETWORK
 *
 *
 * @className:com.xtwl.jy.base.utils.ImageCacheUtils
 * @description:自定义GridView
 * 
 * @version:v1.0.0 
 * @author:ShenYang
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2014-9-23     shenyang       v1.0.0        create
 *
 *
 */
package com.resmanager.client.view;

import com.resmanager.client.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class DefineGridView extends GridView {
	public DefineGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DefineGridView(Context context) {
		super(context);
		this.setSelector(R.drawable.transparent);
	}

	public DefineGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}