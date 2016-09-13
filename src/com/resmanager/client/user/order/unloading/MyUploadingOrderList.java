package com.resmanager.client.user.order.unloading;

import java.util.ArrayList;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.Order;
import com.resmanager.client.model.OrderListModel;
import com.resmanager.client.order.GetOrderListAsyncTask;
import com.resmanager.client.order.GetOrderListAsyncTask.GetOrderListListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.PullToRefreshLayout;
import com.resmanager.client.view.PullToRefreshLayout.OnRefreshListener;
import com.resmanager.client.view.PullableListView;

@SuppressLint("InflateParams")
public class MyUploadingOrderList extends TopContainActivity implements OnClickListener, OnRefreshListener {
	private PullableListView orderList;
	private PullToRefreshLayout refreshView;
	private int currentPage = 1;// 当前页
	private int orderstate = ContactsUtils.ORDER_YUNSONGZHONG;// 待运送单
	private MyUploadOrderListAdapter myUploadOrderListAdapter;

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
		case R.id.add_source_btn:
			if (myUploadOrderListAdapter != null) {
				Map<Integer, Order> daiyunMap = myUploadOrderListAdapter.getCheckedMaps();// 获取用户选中订单
				ArrayList<Order> orders = new ArrayList<Order>();
				if (daiyunMap.size() > 0) {
					for (Map.Entry<Integer, Order> entry : daiyunMap.entrySet()) {
						orders.add(entry.getValue());
					}
					Intent addSourceIntent = new Intent(MyUploadingOrderList.this, UploadingActivity.class);
					addSourceIntent.putExtra("orders", orders);
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
	@SuppressLint("InflateParams")
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
	protected View getCenterView() {
		View contentView = inflater.inflate(R.layout.order_uploading_list, null);
		orderList = (PullableListView) contentView.findViewById(R.id.order_list);
		contentView.findViewById(R.id.add_source_btn).setOnClickListener(this);
		refreshView = (PullToRefreshLayout) contentView.findViewById(R.id.refresh_view);
		refreshView.setOnRefreshListener(this);

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
		if (ContactsUtils.userDetailModel != null) {
			if (isRefresh) {
				this.currentPage = 1;
				myUploadOrderListAdapter = null;

			}

			GetOrderListAsyncTask getOrderListAsyncTask = new GetOrderListAsyncTask(this, currentPage, String.valueOf(orderstate), ContactsUtils.USER_KEY,
					isShowDialog, orpType, String.valueOf(ContactsUtils.userDetailModel.getUserId()), ContactsUtils.ORDER_YOUXIAO, "", "", "", "", "-1", "",
					"", "","");
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
							if (myUploadOrderListAdapter == null) {
								myUploadOrderListAdapter = new MyUploadOrderListAdapter(MyUploadingOrderList.this, orderListModel.getData());
								orderList.setAdapter(myUploadOrderListAdapter);
							} else {
								myUploadOrderListAdapter.loadMore(orderListModel.getData());
							}
						} else {
							Tools.showToast(MyUploadingOrderList.this, orderListModel.getDescription());
						}
					} else {
						Tools.showToast(MyUploadingOrderList.this, "订单获取失败，请检查网络");
					}
				}
			});
			getOrderListAsyncTask.execute();
		} else {
			Tools.showToast(MyUploadingOrderList.this, "用户信息丢失，请重新登录");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getOrderList(true, true, ContactsUtils.ORP_NONE);
	}
}
