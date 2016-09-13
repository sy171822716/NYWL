package com.resmanager.client.user.order;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.LocalActivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.TitleModel;
import com.resmanager.client.system.ResManagerApplication;
import com.resmanager.client.utils.ContactsUtils;

@SuppressWarnings("deprecation")
@SuppressLint("InflateParams")
public class MyOrderListNew extends TopContainActivity implements OnClickListener {
	private List<TitleModel> titleList = new ArrayList<TitleModel>();
	private LinearLayout layout;
	private ImageView mImageView;
	private HorizontalScrollView mHorizontalScrollView;// 上面的水平滚动控件
	private ViewPager mViewPager; // 下方的可横向拖动的控件
	private RadioGroup myRadioGroup;
	private int _id = 1000;
	private float mCurrentCheckedRadioLeft;// 当前被选中的RadioButton距离左侧的距离
	LocalActivityManager manager = null;
	private ArrayList<View> mViews;// 用来存放下方滚动的layout(layout_1,layout_2,layout_3)

	private void getTitleInfo() {
		TitleModel tm = new TitleModel();
		tm.setOrderState(ContactsUtils.ORDER_ALL);
		tm.setTitleName("全部运单");
		titleList.add(tm);

		tm = new TitleModel();
		tm.setOrderState(ContactsUtils.ORDER_DAISHENHE);
		tm.setTitleName("待审核");
		titleList.add(tm);

		tm = new TitleModel();
		tm.setOrderState(ContactsUtils.ORDER_YUNSONGZHONG);
		tm.setTitleName("运送中");
		titleList.add(tm);

		tm = new TitleModel();
		tm.setOrderState(ContactsUtils.ORDER_DAIJIESUAN);
		tm.setTitleName("待结算");
		titleList.add(tm);

		tm = new TitleModel();
		tm.setOrderState(ContactsUtils.ORDER_YIWANCHENG);
		tm.setTitleName("已完成");
		titleList.add(tm);
	}

	/*
	 * (非 Javadoc) <p>Title: onClick</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_img:
			this.finish();
			break;

		default:
			break;
		}
	}

	/*
	 * (非 Javadoc) <p>Title: getTopView</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getTopView()
	 */
	@Override
	protected View getTopView() {
		View topView = inflater.inflate(R.layout.custom_title_bar, null);
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("订单列表");
		return topView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);

	}

	@Override
	protected View getCenterView() {
		View contentView = inflater.inflate(R.layout.my_order_new, null);
		layout = (LinearLayout) contentView.findViewById(R.id.lay);
		mImageView = (ImageView) contentView.findViewById(R.id.img1);
		mHorizontalScrollView = (HorizontalScrollView) contentView.findViewById(R.id.horizontalScrollView);
		mViewPager = (ViewPager) contentView.findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(2);
		getTitleInfo();
		initGroup();
		iniListener();
		iniVariable();
		return contentView;
	}

	private void initGroup() {
		// mImageView = new ImageView(this);
		myRadioGroup = new RadioGroup(this);
		myRadioGroup.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		myRadioGroup.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(myRadioGroup);
		for (int i = 0; i < titleList.size(); i++) {
			TitleModel tm = titleList.get(i);
			RadioButton radio = new RadioButton(this);
			radio.setSingleLine(true);
			radio.setBackgroundResource(R.drawable.radiobtn_selector);
			radio.setButtonDrawable(android.R.color.transparent);
			LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(ResManagerApplication.SCREENWIDTH / titleList.size(), LayoutParams.MATCH_PARENT, 1);
			radio.setLayoutParams(l);
			radio.setGravity(Gravity.CENTER);
			radio.setPadding(0, 15, 0, 15);
			// radio.setPadding(left, top, right, bottom)
			radio.setId(_id + i);
			radio.setText(tm.getTitleName());
			radio.setTextColor(getResources().getColor(R.color.radiobtn_textcolor_selector));
			radio.setTag(tm);
			if (i == 0) {
				radio.setChecked(true);
				int itemWidth = (int) radio.getPaint().measureText(tm.getTitleName());
				mImageView.setLayoutParams(new LinearLayout.LayoutParams(itemWidth + radio.getPaddingLeft() + radio.getPaddingRight(), 4));
			}
			myRadioGroup.addView(radio);
		}
		myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// Map<String, Object> map = (Map<String, Object>)
				// group.getChildAt(checkedId).getTag();
				int radioButtonId = group.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) findViewById(radioButtonId);

				AnimationSet animationSet = new AnimationSet(true);
				TranslateAnimation translateAnimation;
				translateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, rb.getLeft(), 0f, 0f);
				animationSet.addAnimation(translateAnimation);
				animationSet.setFillBefore(true);
				animationSet.setFillAfter(true);
				animationSet.setDuration(300);

				mImageView.startAnimation(animationSet);// 开始上面蓝色横条图片的动画切换
				mViewPager.setCurrentItem(radioButtonId - _id);// 让下方ViewPager跟随上面的HorizontalScrollView切换
				mCurrentCheckedRadioLeft = rb.getLeft();// 更新当前蓝色横条距离左边的距离
				mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (int) getResources().getDimension(R.dimen.rdo2), 0);
				mImageView.setLayoutParams(new LinearLayout.LayoutParams(rb.getRight() - rb.getLeft(), 4));
			}
		});
	}

	private void iniVariable() {
		mViews = new ArrayList<View>();
		for (int i = 0; i < titleList.size(); i++) {
			TitleModel tm = titleList.get(i);
			OrderListView orderListView = new OrderListView(this);
			orderListView.setOrderState(tm.getOrderState());
			mViews.add(orderListView);
		}
		mViewPager.setAdapter(new MyPagerAdapter());// 设置ViewPager的适配器
	}

	private void iniListener() {
		mViewPager.setOnPageChangeListener(new MyPagerOnPageChangeListener());
	}

	/**
	 * ViewPager的适配器
	 */
	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View v, int position, Object obj) {
			((ViewPager) v).removeView(mViews.get(position));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mViews.size();
		}

		@Override
		public Object instantiateItem(View v, int position) {
			((ViewPager) v).addView(mViews.get(position));
			return mViews.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
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

	/**
	 * ViewPager的PageChangeListener(页面改变的监听器)
	 */
	private class MyPagerOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		/**
		 * 滑动ViewPager的时候,让上方的HorizontalScrollView自动切换
		 */
		@Override
		public void onPageSelected(int position) {
			RadioButton radioButton = (RadioButton) findViewById(_id + position);
			radioButton.performClick();

		}
	}
}
