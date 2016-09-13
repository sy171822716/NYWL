/**   
 * @Title: ChooseLocationActivity.java 
 * @Package com.resmanager.client.user.order 
 * @Description:选择当前位置 
 * @author ShenYang  
 * @date 2015-12-8 下午3:21:17 
 * @version V1.0   
 */
package com.resmanager.client.user.order;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.user.order.delivery.DeliveryActivity;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.LocationUtils;
import com.resmanager.client.utils.LocationUtils.PoiSearchListener;
import com.resmanager.client.view.PullToRefreshLayout;
import com.resmanager.client.view.PullToRefreshLayout.OnRefreshListener;
import com.resmanager.client.view.PullableListView;

/**
 * @ClassName: ChooseLocationActivity
 * @Description: 选择当前位置
 * @author ShenYang
 * @date 2015-12-8 下午3:21:17
 * 
 */
@SuppressLint({ "InflateParams", "HandlerLeak" })
public class ChooseLocationActivity extends TopContainActivity implements OnClickListener, OnRefreshListener {
	private PullableListView location_list;
	private PullToRefreshLayout refreshView;
	private TextView auto_location_txt;
	private String current_location;
	private int currentPage = 0;
	private LocationListAdapter locationListAdapter;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				@SuppressWarnings("unchecked")
				List<PoiItem> poiList = (List<PoiItem>) msg.obj;
				if (locationListAdapter == null) {
					locationListAdapter = new LocationListAdapter(ChooseLocationActivity.this, poiList);
					location_list.setAdapter(locationListAdapter);
				} else {
					locationListAdapter.loadMore(poiList);
				}
				break;

			default:
				break;
			}
		};
	};

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

		}
	}

	/*
	 * (非 Javadoc) <p>Title: getTopView</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getTopView()
	 */
	@SuppressLint("InflateParams")
	@Override
	protected View getTopView() {
		View topView = inflater.inflate(R.layout.custom_title_bar, null);
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("我的订单");
		return topView;
	}

	/*
	 * (非 Javadoc) <p>Title: getCenterView</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getCenterView()
	 */
	@Override
	protected View getCenterView() {
		current_location = getIntent().getExtras().getString("current_location");
		View contentView = inflater.inflate(R.layout.choose_location, null);
		location_list = (PullableListView) contentView.findViewById(R.id.location_list);
		refreshView = (PullToRefreshLayout) contentView.findViewById(R.id.refresh_view);
		refreshView.setOnRefreshListener(this);
		auto_location_txt = (TextView) contentView.findViewById(R.id.auto_location_txt);
		auto_location_txt.setText(current_location);
		location_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				PoiItem poiInfo = (PoiItem) arg0.getAdapter().getItem(pos);
				Intent data = new Intent(ChooseLocationActivity.this, DeliveryActivity.class);
				data.putExtra("poiInfo", poiInfo);
				ChooseLocationActivity.this.setResult(ContactsUtils.CHOOSE_LOCATION_RESULT, data);
				finish();
			}
		});
		getNearAddress(ContactsUtils.ORP_NONE);
		return contentView;
	}

	/**
	 * 
	 * @Title: getNearAddress
	 * @Description: 获取附近地址
	 * @param @param orpType 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getNearAddress(int orpType) {
		if (orpType == ContactsUtils.ORP_NONE || orpType == ContactsUtils.ORP_REFRESH) {
			locationListAdapter = null;
			currentPage = 0;
		}
		if (ContactsUtils.baseAMapLocation != null) {
			LocationUtils locationUtils = new LocationUtils(this);
			locationUtils.searchRound(this, ContactsUtils.baseAMapLocation.getLatitude(), ContactsUtils.baseAMapLocation.getLongitude(), currentPage, orpType,
					new PoiSearchListener() {

						@Override
						public void onPoiSearched(PoiResult poiResult, int resultCode, int orpType) {

							switch (orpType) {
							case ContactsUtils.ORP_REFRESH:
								refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
								break;
							case ContactsUtils.ORP_LOAD:
								refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
								break;
							default:
								break;
							}
							if (resultCode == 0) {
								currentPage += 1;
								List<PoiItem> pois = poiResult.getPois();
								Message msg = new Message();
								msg.what = 0;
								msg.obj = pois;
								mHandler.sendMessage(msg);
							}
						}
					});
		}
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		getNearAddress(ContactsUtils.ORP_REFRESH);
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		getNearAddress(ContactsUtils.ORP_LOAD);
	}
}
