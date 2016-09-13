/**   
 * @Title: OrderListFilter.java 
 * @Package com.resmanager.client.order 
 * @Description: 订单过滤 
 * @author ShenYang  
 * @date 2016-2-24 下午12:42:38 
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.Order;
import com.resmanager.client.model.OrderListModel;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.order.GetOrderListAsyncTask.GetOrderListListener;
import com.resmanager.client.order.OrderFilterAsyncTask.OrderFilterListener;
import com.resmanager.client.user.order.SearchOrderActivity;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.CustomDialog;
import com.resmanager.client.view.CustomDialog.ToDoListener;
import com.resmanager.client.view.PullToRefreshLayout;
import com.resmanager.client.view.PullToRefreshLayout.OnRefreshListener;
import com.resmanager.client.view.PullableListView;

/**
 * @ClassName: OrderListFilter
 * @Description: 订单过滤
 * @author ShenYang
 * @date 2016-2-24 下午12:42:38
 * 
 */
@SuppressLint("InflateParams")
public class OrderListFilter extends TopContainActivity implements
		OnClickListener, OnItemClickListener {

	private PullableListView orderList;
	private PullToRefreshLayout refreshView;
	private int currentPage = 1;// 当前页
	private int orderstate = -1;// 全部订单
	private OrderFilterListAdapter orderListAdapter;
	private int isUsed = 0;
	private Button yes, no;
	private CustomDialog customDialog;
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
	private LinearLayout filter_layout;

	/*
	 * (非 Javadoc) <p>Title: onClick</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @see android.view.View.OnClickListener#onClick(android .view.View)
	 */

	/**
	 * 
	 * @Title: showDialog
	 * @Description: 弹出提示对话框
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void showNoticeDialog(final int setUsed) {
		if (customDialog == null) {
			customDialog = new CustomDialog(this, R.style.myDialogTheme);
		}
		if (setUsed == 0) {
			customDialog.setContentText("是否将所选订单标为问题订单");
		} else {
			customDialog.setContentText("是否将所选订单标为有效订单");
		}
		customDialog.setToDoListener(new ToDoListener() {

			@Override
			public void doSomething() {
				OrderFilterAsyncTask orderFilterAsyncTask = new OrderFilterAsyncTask(
						OrderListFilter.this,
						orderListAdapter.getCheckedMaps(), setUsed);
				orderFilterAsyncTask
						.setOrderFilterListener(new OrderFilterListener() {

							@Override
							public void orderFilterResult(
									ResultModel resultModel) {
								if (resultModel != null) {
									if (resultModel.getResult().equals("true")) {
										Tools.showToast(OrderListFilter.this,
												"设置成功");
										getOrderList(true, true,
												ContactsUtils.ORP_NONE);
									} else {
										Tools.showToast(OrderListFilter.this,
												resultModel.getDescription());
									}
								} else {
									Tools.showToast(OrderListFilter.this,
											"设置失败，请重试");
								}
							}
						});
				orderFilterAsyncTask.execute();
			}
		});
		customDialog.show();
	}

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
		case R.id.yes:
			if (orderListAdapter != null
					&& orderListAdapter.getCheckedMaps().size() > 0) {
				showNoticeDialog(1);
			} else {
				Tools.showToast(OrderListFilter.this, "请选择需要设置的订单");
			}
			break;
		case R.id.no:
			if (orderListAdapter != null
					&& orderListAdapter.getCheckedMaps().size() > 0) {
				showNoticeDialog(4);
			} else {
				Tools.showToast(OrderListFilter.this, "请选择需要设置的订单");
			}
			break;

		default:
			break;
		}
	}

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
		titleContent.setText("订单过滤");
		return topView;
	}

	@Override
	protected View getCenterView() {
		View centerView = inflater.inflate(R.layout.order_list_filter, null);
		return centerView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		salername = getIntent().getExtras().getString("saler");
		orderstate = getIntent().getExtras().getInt("orderState");
		isUsed = getIntent().getExtras().getInt("isUsed");
		orderList = (PullableListView) centerView.findViewById(R.id.order_list);
		filter_layout = (LinearLayout) centerView
				.findViewById(R.id.filter_layout);
		refreshView = (PullToRefreshLayout) centerView
				.findViewById(R.id.refresh_view);
		refreshView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				getOrderList(true, false, ContactsUtils.ORP_REFRESH);
			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				getOrderList(false, false, ContactsUtils.ORP_LOAD);
			}
		});
		orderList.setOnItemClickListener(this);
		getOrderList(true, true, ContactsUtils.ORP_NONE);
		yes = (Button) centerView.findViewById(R.id.yes);
		no = (Button) centerView.findViewById(R.id.no);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);
		if (ContactsUtils.userDetailModel.getUserType() != null
				&& ContactsUtils.userDetailModel.getUserType().equals("4")) {
			filter_layout.setVisibility(View.GONE);
		} else {
			filter_layout.setVisibility(View.VISIBLE);
		}
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
									orderListAdapter = new OrderFilterListAdapter(
											OrderListFilter.this,
											orderListModel.getData(), true,
											false);
									orderList.setAdapter(orderListAdapter);
								} else {
									orderListAdapter.loadMore(orderListModel
											.getData());
								}
							} else {
								Tools.showToast(OrderListFilter.this,
										orderListModel.getDescription());
							}
						} else {
							Tools.showToast(OrderListFilter.this,
									"订单获取失败，请检查网络");
						}
					}
				});

		getOrderListAsyncTask.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
		Order order = (Order) v.getAdapter().getItem(pos);
		Intent intent = new Intent(OrderListFilter.this,
				OrderDetailActivity.class);
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
