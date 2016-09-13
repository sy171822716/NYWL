/**   
 * @Title: AllOrderList.java 
 * @Package com.resmanager.client.order 
 * @Description: 我的订单列表 
 * @author ShenYang  
 * @date 2015-12-1 上午9:27:22 
 * @version V1.0   
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
import com.resmanager.client.model.Order;
import com.resmanager.client.model.OrderListModel;
import com.resmanager.client.order.GetOrderListAsyncTask;
import com.resmanager.client.order.GetOrderListAsyncTask.GetOrderListListener;
import com.resmanager.client.order.OrderDetailActivity;
import com.resmanager.client.user.order.delivery.MyDaiyunOrderListAdapter;
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
public class MyAllOrderList extends TopContainActivity implements OnClickListener, OnRefreshListener, OnItemClickListener {
	private PullableListView orderList;
	private PullToRefreshLayout refreshView;
	private int currentPage = 1;// 当前页
	private int orderstate = ContactsUtils.ORDER_ALL;// 全部订单
	private MyDaiyunOrderListAdapter orderListAdapter;

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
		orderstate = getIntent().getExtras().getInt("orderstate");
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
			this.currentPage = 1;
			orderListAdapter = null;
		}
		GetOrderListAsyncTask getOrderListAsyncTask = new GetOrderListAsyncTask(this, currentPage, String.valueOf(orderstate), ContactsUtils.USER_KEY,
				isShowDialog, orpType, String.valueOf(ContactsUtils.userDetailModel.getUserId()), ContactsUtils.ORDER_YOUXIAO, "", "", "", "", "-1","","","","");
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
							orderListAdapter = new MyDaiyunOrderListAdapter(MyAllOrderList.this, orderListModel.getData(), false, false);
							orderList.setAdapter(orderListAdapter);
						} else {
							orderListAdapter.loadMore(orderListModel.getData());
						}
					} else {
						Tools.showToast(MyAllOrderList.this, orderListModel.getDescription());
					}
				} else {
					Tools.showToast(MyAllOrderList.this, "订单获取失败，请检查网络");
				}
			}
		});

		getOrderListAsyncTask.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
		Order order = (Order) v.getAdapter().getItem(pos);
		Intent intent = new Intent(MyAllOrderList.this, OrderDetailActivity.class);
		intent.putExtra("orderId", order.getOrderID());
		startActivity(intent);
	}

}
