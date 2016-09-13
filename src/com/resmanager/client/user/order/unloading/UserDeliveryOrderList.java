/**   
 * @Title: AllOrderList.java 
 * @Package com.resmanager.client.order 
 * @Description: 全部订单列表 
 * @author ShenYang  
 * @date 2015-12-1 上午9:27:22 
 * @version V1.0   
 */
package com.resmanager.client.user.order.unloading;

import java.util.ArrayList;
import java.util.Map;

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
import com.resmanager.client.order.OrderListAdapter;
import com.resmanager.client.user.order.SearchOrderActivity;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.PullToRefreshLayout;
import com.resmanager.client.view.PullToRefreshLayout.OnRefreshListener;
import com.resmanager.client.view.PullableListView;

/**
 * @ClassName: AllOrderList
 * @Description:用戶运送中订单
 * @author ShenYang
 * @date 2015-12-1 上午9:27:22
 * 
 */
@SuppressLint("InflateParams")
public class UserDeliveryOrderList extends TopContainActivity implements OnClickListener, OnRefreshListener, OnItemClickListener {
	private PullableListView orderList;
	private PullToRefreshLayout refreshView;
	private int currentPage = 1;// 当前页
	private MyUploadOrderListAdapter orderListAdapter;
	private String userId = "";

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
			startActivityForResult(searchIntent, ContactsUtils.SEARCH_ORDER_RESULT);
			break;
		case R.id.add_source_btn:
			if (orderListAdapter != null) {
				Map<Integer, Order> daiyunMap = orderListAdapter.getCheckedMaps();// 获取用户选中订单
				ArrayList<Order> orders = new ArrayList<Order>();
				if (daiyunMap.size() > 0) {
					for (Map.Entry<Integer, Order> entry : daiyunMap.entrySet()) {
						orders.add(entry.getValue());
					}
					Intent addSourceIntent = new Intent(UserDeliveryOrderList.this, UploadingActivity.class);
					addSourceIntent.putExtra("orders", orders);
					addSourceIntent.putExtra("IsInsteadXH", 1);//是否代卸货0：不是，1：是
					startActivity(addSourceIntent);
				} else {
					Tools.showToast(this, "请选择您想要卸货的订单");
				}
			} else {
				Tools.showToast(this, "您当前没有需要卸货的订单，请刷新后再试");
			}
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
		ImageView rightImg = (ImageView) topView.findViewById(R.id.title_right_img);
		rightImg.setVisibility(View.INVISIBLE);
		rightImg.setImageResource(R.drawable.search);
		rightImg.setOnClickListener(this);
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
		userId = getIntent().getExtras().getString("userId");
		View contentView = inflater.inflate(R.layout.order_uploading_list, null);
		orderList = (PullableListView) contentView.findViewById(R.id.order_list);
		refreshView = (PullToRefreshLayout) contentView.findViewById(R.id.refresh_view);
		refreshView.setOnRefreshListener(this);
		orderList.setOnItemClickListener(this);
		getOrderList(true, true, ContactsUtils.ORP_NONE);
		contentView.findViewById(R.id.add_source_btn).setOnClickListener(this);
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
		GetOrderListAsyncTask getOrderListAsyncTask = new GetOrderListAsyncTask(this, currentPage, String.valueOf(ContactsUtils.ORDER_YUNSONGZHONG),
				ContactsUtils.USER_KEY, isShowDialog, orpType, userId, ContactsUtils.ORDER_YOUXIAO, "", "", "", "", "-1", "", "", "","");
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
							orderListAdapter = new MyUploadOrderListAdapter(UserDeliveryOrderList.this, orderListModel.getData());
							orderList.setAdapter(orderListAdapter);
						} else {
							orderListAdapter.loadMore(orderListModel.getData());
						}
					} else {
						Tools.showToast(UserDeliveryOrderList.this, orderListModel.getDescription());
					}
				} else {
					Tools.showToast(UserDeliveryOrderList.this, "订单获取失败，请检查网络");
				}
			}
		});

		getOrderListAsyncTask.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
		Order order = (Order) v.getAdapter().getItem(pos);
		Intent intent = new Intent(UserDeliveryOrderList.this, OrderDetailActivity.class);
		intent.putExtra("orderId", order.getOrderID());
		startActivity(intent);
	}

}
