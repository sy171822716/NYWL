/**   
 * @Title: AllOrderList.java 
 * @Package com.resmanager.client.order 
 * @Description: 全部订单列表 
 * @author ShenYang  
 * @date 2015-12-1 上午9:27:22 
 * @version V1.0   
 */
package com.resmanager.client.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.Order;
import com.resmanager.client.model.OrderListModel;
import com.resmanager.client.order.GetOrderListAsyncTask.GetOrderListListener;
import com.resmanager.client.user.order.SearchOrderActivity;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.PullToRefreshLayout;
import com.resmanager.client.view.PullToRefreshLayout.OnRefreshListener;
import com.resmanager.client.view.PullableListView;

/**
 * @ClassName: AllOrderList
 * @Description: 全部订单列表
 * @author ShenYang
 * @date 2015-12-1 上午9:27:22
 * 
 */
@SuppressLint("InflateParams")
public class AllOrderList extends TopContainActivity implements
		OnClickListener, OnRefreshListener, OnItemClickListener {
	private PullableListView orderList;
	private PullToRefreshLayout refreshView;
	private int currentPage = 1;// 当前页
	private int orderstate = -1;// 全部订单
	private OrderListAdapter orderListAdapter;
	private int isUsed = 0;
	private String DayType = "-1";
	private String Days = "";
	private String Town = "";
	private String Saleoid = "";
	private String ordercustomer = "";
	private String userId = "";
	private String packType = "";
	private String startDate = "";
	private String endDate = "";
	private String salername = "";

	// private CompleteOrderListAdapter completeOrderListAdapter;
	// private MyDaiyunOrderListAdapter myDaiyunOrderListAdapter;

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
		case R.id.title_right_img:
			Intent searchIntent = new Intent(this, SearchOrderActivity.class);
			startActivityForResult(searchIntent,
					ContactsUtils.SEARCH_ORDER_RESULT);
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
		ImageView leftImg = (ImageView) topView
				.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		ImageView rightImg = (ImageView) topView
				.findViewById(R.id.title_right_img);
		rightImg.setVisibility(View.VISIBLE);
		rightImg.setImageResource(R.drawable.search);
		rightImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView
				.findViewById(R.id.title_content);
		titleContent.setText("订单列表");
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected View getCenterView() {
		orderstate = getIntent().getExtras().getInt("orderState");
		isUsed = getIntent().getExtras().getInt("isUsed");
		salername = getIntent().getExtras().getString("saler");
		View contentView = inflater.inflate(R.layout.order_list, null);
		orderList = (PullableListView) contentView
				.findViewById(R.id.order_list);
		refreshView = (PullToRefreshLayout) contentView
				.findViewById(R.id.refresh_view);
		refreshView.setOnRefreshListener(this);
		orderList.setOnItemClickListener(this);
		getOrderList(true, true, ContactsUtils.ORP_NONE);
		return contentView;
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		getOrderList(true, false, ContactsUtils.ORP_REFRESH);

	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		getOrderList(false, false, ContactsUtils.ORP_LOAD);
	}

	public void getOrderList(boolean isRefresh, boolean isShowDialog,
			int orpType) {
		if (isRefresh) {
			this.currentPage = 1;
			orderListAdapter = null;
		}
		GetOrderListAsyncTask getOrderListAsyncTask = new GetOrderListAsyncTask(
				this, currentPage, String.valueOf(orderstate),
				ContactsUtils.USER_KEY, isShowDialog, orpType, userId, isUsed,
				ordercustomer, Saleoid, Town, Days, DayType, startDate,
				endDate, packType, salername);
		getOrderListAsyncTask
				.setGetOrderListListener(new GetOrderListListener() {

					@Override
					public void getOrderListResult(int orpType,
							OrderListModel orderListModel) {
						if (orpType == ContactsUtils.ORP_REFRESH) {
							refreshView
									.refreshFinish(PullToRefreshLayout.SUCCEED);
						} else if (orpType == ContactsUtils.ORP_LOAD) {
							refreshView
									.loadmoreFinish(PullToRefreshLayout.SUCCEED);
						}
						if (orderListModel != null) {
							if (orderListModel.getResult().equals("true")) {
								currentPage += 1;
								if (orderListAdapter == null) {
									orderListAdapter = new OrderListAdapter(
											AllOrderList.this, orderListModel
													.getData(), false);
									orderList.setAdapter(orderListAdapter);
								} else {
									orderListAdapter.loadMore(orderListModel
											.getData());
								}
							} else {
								Tools.showToast(AllOrderList.this,
										orderListModel.getDescription());
							}
						} else {
							Tools.showToast(AllOrderList.this, "订单获取失败，请检查网络");
						}
					}
				});

		getOrderListAsyncTask.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
		Order order = (Order) v.getAdapter().getItem(pos);
		Intent intent = new Intent(AllOrderList.this, OrderDetailActivity.class);
		intent.putExtra("orderId", order.getOrderID());
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case ContactsUtils.SEARCH_ORDER_RESULT:
			if (data != null) {
				DayType = data.getExtras().getString("DayType");
				Days = data.getExtras().getString("Days");
				Town = data.getExtras().getString("Town");
				Saleoid = data.getExtras().getString("Saleoid");
				ordercustomer = data.getExtras().getString("ordercustomer");
				userId = data.getExtras().getString("userId");
				startDate = data.getExtras().getString("startDate");
				endDate = data.getExtras().getString("endDate");
				packType = data.getExtras().getString("Packtype");
				salername = data.getExtras().getString("salername");
				getOrderList(true, true, ContactsUtils.ORP_NONE);
			}
			break;

		default:
			break;
		}
	}

}
