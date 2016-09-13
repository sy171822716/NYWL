/**   
 * @Title: BalanceOrderList.java 
 * @Package com.resmanager.client.user.balance 
 * @Description:待结算列表
 * @author ShenYang  
 * @date 2016-1-15 下午12:47:51 
 * @version V1.0   
 */
package com.resmanager.client.user.balance;

import java.util.ArrayList;

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
import com.resmanager.client.order.GetOrderListAsyncTask;
import com.resmanager.client.order.GetOrderListAsyncTask.GetOrderListListener;
import com.resmanager.client.order.OrderDetailActivity;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.PullToRefreshLayout;
import com.resmanager.client.view.PullToRefreshLayout.OnRefreshListener;
import com.resmanager.client.view.PullableListView;

/**
 * @ClassName: BalanceOrderList
 * @Description: 待结算订单列表
 * @author ShenYang
 * @date 2016-1-15 下午12:47:51
 * 
 */
@SuppressLint("InflateParams")
public class BalanceOrderList extends TopContainActivity implements OnRefreshListener, OnClickListener {
	private int currentPage = 1;// 当前页
	private String orderstate = ContactsUtils.ORDER_DAIJIESUAN + "," + ContactsUtils.ORDER_YIWANCHENG;// 待运送单
	private PullableListView orderList;
	private PullToRefreshLayout refreshView;
	private BalanceOrderListAdapter balanceOrderListAdapter;

	@Override
	protected View getTopView() {
		return inflater.inflate(R.layout.custom_title_bar, null);
	}

	@Override
	protected View getCenterView() {
		return inflater.inflate(R.layout.order_list, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		orderList = (PullableListView) centerView.findViewById(R.id.order_list);
		refreshView = (PullToRefreshLayout) centerView.findViewById(R.id.refresh_view);
		refreshView.setOnRefreshListener(this);
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("我的结算");
		getOrderList(true, true, ContactsUtils.ORP_NONE);
		orderList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
				Order order = (Order) v.getAdapter().getItem(pos);
				Intent orderDetailIntent = new Intent(BalanceOrderList.this, OrderDetailActivity.class);
				orderDetailIntent.putExtra("orderId", order.getOrderID());
				startActivity(orderDetailIntent);
			}
		});
	}

	public void getOrderList(boolean isRefresh, boolean isShowDialog, int orpType) {
		if (isRefresh) {
			this.currentPage = 1;
			balanceOrderListAdapter = null;
		}
		GetOrderListAsyncTask getOrderListAsyncTask = new GetOrderListAsyncTask(this, currentPage, orderstate, ContactsUtils.USER_KEY, isShowDialog, orpType,
				String.valueOf(ContactsUtils.userDetailModel.getUserId()), ContactsUtils.ORDER_YOUXIAO, "", "", "", "", "-1", "", "", "","");
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
							balanceOrderListAdapter = new BalanceOrderListAdapter(BalanceOrderList.this, orders);
							orderList.setAdapter(balanceOrderListAdapter);
						} else {
							balanceOrderListAdapter.loadMore(orders);
						}
					} else {
						Tools.showToast(BalanceOrderList.this, orderListModel.getDescription());
					}
				} else {
					Tools.showToast(BalanceOrderList.this, "订单获取失败，请检查网络");
				}
			}
		});
		getOrderListAsyncTask.execute();
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		getOrderList(true, false, ContactsUtils.ORP_REFRESH);
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		getOrderList(false, false, ContactsUtils.ORP_LOAD);
	}

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

}
