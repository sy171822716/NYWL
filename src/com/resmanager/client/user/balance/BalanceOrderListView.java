package com.resmanager.client.user.balance;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.model.Order;
import com.resmanager.client.model.OrderListModel;
import com.resmanager.client.order.GetOrderListAsyncTask;
import com.resmanager.client.order.OrderDetailActivity;
import com.resmanager.client.order.GetOrderListAsyncTask.GetOrderListListener;
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
public class BalanceOrderListView extends LinearLayout implements OnRefreshListener, OnItemClickListener {
	private LayoutInflater mInflater;
	private PullableListView orderList;
	private PullToRefreshLayout refreshView;
	private int currentPage = 1;// 当前页
	private String orderstate = ContactsUtils.ORDER_DAIJIESUAN + "," + ContactsUtils.ORDER_YIWANCHENG;// 待运送单
	private Context context;
	private BalanceOrderListAdapter balanceOrderListAdapter;

	public BalanceOrderListView(Context context) {
		super(context);
		this.context = context;
		mInflater = LayoutInflater.from(context);
		View contentView = mInflater.inflate(R.layout.order_list, null);
		this.addView(contentView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		orderList = (PullableListView) contentView.findViewById(R.id.order_list);
		refreshView = (PullToRefreshLayout) contentView.findViewById(R.id.refresh_view);
		refreshView.setOnRefreshListener(this);
		orderList.setOnItemClickListener(this);
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
			balanceOrderListAdapter = null;
		}
		GetOrderListAsyncTask getOrderListAsyncTask = new GetOrderListAsyncTask(context, currentPage, orderstate, ContactsUtils.USER_KEY, isShowDialog,
				orpType, String.valueOf(ContactsUtils.userDetailModel.getUserId()), ContactsUtils.ORDER_YOUXIAO, "", "", "", "", "-1","","","","");
		getOrderListAsyncTask.setGetOrderListListener(new GetOrderListListener() {

			@Override
			public void getOrderListResult(int orpType, OrderListModel orderListModel) {
				if (orpType == ContactsUtils.ORP_REFRESH) {
					refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
				} else if (orpType == ContactsUtils.ORP_LOAD) {
					refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
				if (orderListModel != null) {
					currentPage += 1;
					if (orderListModel.getResult().equals("true")) {
						ArrayList<Order> orders = orderListModel.getData();
						if (balanceOrderListAdapter == null) {
							balanceOrderListAdapter = new BalanceOrderListAdapter(context, orders);
							orderList.setAdapter(balanceOrderListAdapter);
						} else {
							balanceOrderListAdapter.loadMore(orders);
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
