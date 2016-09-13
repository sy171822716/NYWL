package com.resmanager.client.user.order;

import com.resmanager.client.R;
import com.resmanager.client.model.Order;
import com.resmanager.client.model.OrderListModel;
import com.resmanager.client.order.GetOrderListAsyncTask;
import com.resmanager.client.order.OrderDetailActivity;
import com.resmanager.client.order.GetOrderListAsyncTask.GetOrderListListener;
import com.resmanager.client.order.OrderListAdapter;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.PullToRefreshLayout;
import com.resmanager.client.view.PullToRefreshLayout.OnRefreshListener;
import com.resmanager.client.view.PullableListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("InflateParams")
public class OrderListView extends LinearLayout implements OnRefreshListener, OnItemClickListener {
	private LayoutInflater mInflater;
	private PullableListView orderList;
	private PullToRefreshLayout refreshView;
	private int currentPage = 1;// 当前页
	private int orderstate = ContactsUtils.ORDER_ALL;// 全部订单
	private OrderListAdapter orderListAdapter;
	private Context context;

	public OrderListView(Context context) {
		super(context);
		this.context = context;
		mInflater = LayoutInflater.from(context);
		View contentView = mInflater.inflate(R.layout.order_list, null);
		this.addView(contentView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		orderList = (PullableListView) contentView.findViewById(R.id.order_list);
		refreshView = (PullToRefreshLayout) contentView.findViewById(R.id.refresh_view);
		refreshView.setOnRefreshListener(this);
		orderList.setOnItemClickListener(this);
	}

	public void setOrderState(int orderState) {
		this.orderstate = orderState;
		getOrderList(true, true, ContactsUtils.ORP_NONE);
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		getOrderList(true, false, ContactsUtils.ORP_REFRESH);

	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		getOrderList(false, false, ContactsUtils.ORP_LOAD);
	}

	public void getOrderList(boolean isRefresh, boolean isShowDialog, int orpType) {
		if (isRefresh) {
			this.currentPage = 1;
			orderListAdapter = null;
		}

		GetOrderListAsyncTask getOrderListAsyncTask = new GetOrderListAsyncTask(context, currentPage, String.valueOf(orderstate), ContactsUtils.USER_KEY,
				isShowDialog, orpType, String.valueOf(ContactsUtils.userDetailModel.getUserId()), ContactsUtils.ORDER_YOUXIAO, "", "", "", "", "-1", "", "", "","");
		getOrderListAsyncTask.setGetOrderListListener(new GetOrderListListener() {

			@Override
			public void getOrderListResult(int orpType, OrderListModel orderListModel) {
				if (orpType == ContactsUtils.ORP_REFRESH) {
					refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
				} else if (orpType == ContactsUtils.ORP_LOAD) {
					refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
				if (orderListModel != null) {
					if (orderListModel.getResult().equals("true")) {
						currentPage += 1;
						if (orderListAdapter == null) {
							boolean isShowMonery = false;
							if (orderstate == ContactsUtils.ORDER_DAIJIESUAN || orderstate == ContactsUtils.ORDER_YIWANCHENG) {
								isShowMonery = true;
							}
							orderListAdapter = new OrderListAdapter(context, orderListModel.getData(), isShowMonery);
							orderList.setAdapter(orderListAdapter);
						} else {
							orderListAdapter.loadMore(orderListModel.getData());
						}
					} else {
						Tools.showToast(context, orderListModel.getDescription());
					}
				} else {
					Tools.showToast(context, "订单获取失败，请检查网络");
				}
			}
		});

		getOrderListAsyncTask.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
		Order order = (Order) v.getAdapter().getItem(pos);
		Intent intent = new Intent(context, OrderDetailActivity.class);
		intent.putExtra("orderId", order.getOrderID());
		context.startActivity(intent);
	}
}
