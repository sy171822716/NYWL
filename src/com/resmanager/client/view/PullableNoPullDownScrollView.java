package com.resmanager.client.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PullableNoPullDownScrollView extends ScrollView implements Pullable {

	public PullableNoPullDownScrollView(Context context) {
		super(context);
	}

	public PullableNoPullDownScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableNoPullDownScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown() {
		if (getScrollY() == 0)
			return false;
		else
			return false;
	}

	@Override
	public boolean canPullUp() {
		if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
			return true;
		else
			return false;
	}

}
