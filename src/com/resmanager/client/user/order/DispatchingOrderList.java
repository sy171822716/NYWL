/**
 * @Description:正在运送中的订单主界面
 * @version:v1.0
 * @author:ShenYang
 * @date:2016-3-22 下午1:46:11
 */
package com.resmanager.client.user.order;

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
import com.resmanager.client.map.GetOrderInfoByWorkIdAsyncTask;
import com.resmanager.client.map.GetOrderInfoByWorkIdAsyncTask.GetOnDispatchOrderListener;
import com.resmanager.client.model.Order;
import com.resmanager.client.model.OrderListModel;
import com.resmanager.client.order.OrderDetailActivity;
import com.resmanager.client.user.order.delivery.MyDaiyunOrderListAdapter;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.PullToRefreshLayout;
import com.resmanager.client.view.PullToRefreshLayout.OnRefreshListener;
import com.resmanager.client.view.PullableListView;

@SuppressLint("InflateParams")
public class DispatchingOrderList extends TopContainActivity implements OnClickListener, OnRefreshListener, OnItemClickListener {
	private PullableListView orderList;
	private PullToRefreshLayout refreshView;
	private MyDaiyunOrderListAdapter orderListAdapter;
	private String WordId = "";

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

	@Override
	protected View getTopView() {
		View topView = inflater.inflate(R.layout.custom_title_bar, null);
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("运送中订单");

		return topView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected View getCenterView() {
		WordId = getIntent().getExtras().getString("WorkId");
		View contentView = inflater.inflate(R.layout.order_list, null);
		orderList = (PullableListView) contentView.findViewById(R.id.order_list);
		refreshView = (PullToRefreshLayout) contentView.findViewById(R.id.refresh_view);
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

	public void getOrderList(boolean isRefresh, boolean isShowDialog, int orpType) {
		if (isRefresh) {
			orderListAdapter = null;
		}

		GetOrderInfoByWorkIdAsyncTask getOrderInfoByWorkIdAsyncTask = new GetOrderInfoByWorkIdAsyncTask(DispatchingOrderList.this, WordId, orpType);
		getOrderInfoByWorkIdAsyncTask.setGetOnDispatchOrderListener(new GetOnDispatchOrderListener() {

			@Override
			public void getDispatchListResult(int orpType, OrderListModel orderListModel) {

				if (orpType == ContactsUtils.ORP_REFRESH) {
					refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
				} else if (orpType == ContactsUtils.ORP_LOAD) {
					refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
				if (orderListModel != null) {
					if (orderListModel.getResult().equals("true")) {
						if (orderListAdapter == null) {
							orderListAdapter = new MyDaiyunOrderListAdapter(DispatchingOrderList.this, orderListModel.getData(), false, true);
							orderList.setAdapter(orderListAdapter);
						} else {
							orderListAdapter.loadMore(orderListModel.getData());
						}
					} else {
						Tools.showToast(DispatchingOrderList.this, orderListModel.getDescription());
					}
				} else {
					Tools.showToast(DispatchingOrderList.this, "订单获取失败，请检查网络");
				}

			}
		});
		getOrderInfoByWorkIdAsyncTask.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
		Order order = (Order) v.getAdapter().getItem(pos);
		Intent intent = new Intent(DispatchingOrderList.this, OrderDetailActivity.class);
		intent.putExtra("orderId", order.getOrderID());
		startActivity(intent);
	}

}
