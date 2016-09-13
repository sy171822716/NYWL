/**   
 * @Title: OrderListFilter.java 
 * @Package com.resmanager.client.order 
 * @Description: 订单分配 
 * @author ShenYang  
 * @date 2016-2-24 下午12:42:38 
 * @version V1.0   
 */
package com.resmanager.client.order;

import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

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
import com.resmanager.client.view.PullToRefreshLayout;
import com.resmanager.client.view.CustomDialog.ToDoListener;
import com.resmanager.client.view.PullToRefreshLayout.OnRefreshListener;
import com.resmanager.client.view.PullableListView;

/**
 * @ClassName: OrderListFilter
 * @Description: 订单分配
 * @author ShenYang
 * @date 2016-2-24 下午12:42:38
 * 
 */
@SuppressLint("InflateParams")
public class OrderListSend extends TopContainActivity implements OnClickListener, OnItemClickListener {

	private PullableListView orderList;
	private PullToRefreshLayout refreshView;
	private int currentPage = 1;// 当前页
	private int orderstate = -1;// 全部订单
	private OrderSendListAdapter orderListAdapter;
	private int isUsed = 0;
	private Button send_btn, reset_filter;
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
	private String salername="";
	private LinearLayout filter_layout;
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
		case R.id.send_btn:
			if (orderListAdapter != null && orderListAdapter.getCheckedMaps().size() > 0) {
				StringBuffer sb = new StringBuffer();
				for (Map.Entry<Integer, Order> entry : orderListAdapter.getCheckedMaps().entrySet()) {
					Order order = entry.getValue();
					sb.append(order.getOrderID() + ",");
				}
				Intent intent = new Intent(this, ChooseDriverActivity.class);
				intent.putExtra("orderIds", sb.toString());
				startActivityForResult(intent, ContactsUtils.CHOOSE_DRIVER_RESULT);
			} else {
				Tools.showToast(this, "请先选择要分配的订单");
			}
			break;
		case R.id.reset_filter:
			if (orderListAdapter != null && orderListAdapter.getCheckedMaps().size() > 0) {
				showNoticeDialog(0);
			} else {
				Tools.showToast(OrderListSend.this, "请先选择要处理的订单");
			}
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
		ImageView rightImg = (ImageView) topView.findViewById(R.id.title_right_img);
		rightImg.setVisibility(View.VISIBLE);
		rightImg.setImageResource(R.drawable.search);
		rightImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("派单");
		return topView;
	}

	@Override
	protected View getCenterView() {
		View centerView = inflater.inflate(R.layout.order_list_send, null);
		return centerView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		orderstate = getIntent().getExtras().getInt("orderState");
		isUsed = getIntent().getExtras().getInt("isUsed");
		salername = getIntent().getExtras().getString("saler");
		orderList = (PullableListView) centerView.findViewById(R.id.order_list);
		filter_layout = (LinearLayout) centerView.findViewById(R.id.filter_layout);
		refreshView = (PullToRefreshLayout) centerView.findViewById(R.id.refresh_view);
		reset_filter = (Button) centerView.findViewById(R.id.reset_filter);
		reset_filter.setOnClickListener(this);
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
		send_btn = (Button) centerView.findViewById(R.id.send_btn);
		send_btn.setOnClickListener(this);
		if (ContactsUtils.userDetailModel.getUserType() != null
				&& ContactsUtils.userDetailModel.getUserType().equals("4")) {
			filter_layout.setVisibility(View.GONE);
		} else {
			filter_layout.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 
	 * @Title: getOrderList
	 * @Description: 获取订单列表
	 * @param @param isRefresh
	 * @param @param isShowDialog
	 * @param @param orpType 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getOrderList(boolean isRefresh, boolean isShowDialog, int orpType) {
		if (isRefresh) {
			this.currentPage = 1;
			orderListAdapter = null;
		}
		GetOrderListAsyncTask getOrderListAsyncTask = new GetOrderListAsyncTask(this, currentPage, String.valueOf(orderstate), ContactsUtils.USER_KEY,
				isShowDialog, orpType, userId, isUsed, ordercustomer, Saleoid, Town, Days, DayType, startDate, endDate, packType,salername);
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
							orderListAdapter = new OrderSendListAdapter(OrderListSend.this, orderListModel.getData(), true, false);
							orderList.setAdapter(orderListAdapter);
						} else {
							orderListAdapter.loadMore(orderListModel.getData());
						}
					} else {
						Tools.showToast(OrderListSend.this, orderListModel.getDescription());
					}
				} else {
					Tools.showToast(OrderListSend.this, "订单获取失败，请检查网络");
				}
			}
		});

		getOrderListAsyncTask.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
		Order order = (Order) v.getAdapter().getItem(pos);
		Intent intent = new Intent(OrderListSend.this, OrderDetailActivity.class);
		intent.putExtra("orderId", order.getOrderID());
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case ContactsUtils.CHOOSE_DRIVER_RESULT:
			getOrderList(true, true, ContactsUtils.ORP_NONE);
			break;
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
		customDialog.setContentText("是否重新过滤所选订单?");
		customDialog.setToDoListener(new ToDoListener() {

			@Override
			public void doSomething() {
				OrderFilterAsyncTask orderFilterAsyncTask = new OrderFilterAsyncTask(OrderListSend.this, orderListAdapter.getCheckedMaps(), setUsed);
				orderFilterAsyncTask.setOrderFilterListener(new OrderFilterListener() {

					@Override
					public void orderFilterResult(ResultModel resultModel) {
						if (resultModel != null) {
							if (resultModel.getResult().equals("true")) {
								Tools.showToast(OrderListSend.this, "设置成功");
								getOrderList(true, true, ContactsUtils.ORP_NONE);
							} else {
								Tools.showToast(OrderListSend.this, resultModel.getDescription());
							}
						} else {
							Tools.showToast(OrderListSend.this, "设置失败，请重试");
						}
					}
				});
				orderFilterAsyncTask.execute();
			}
		});
		customDialog.show();
	}

}
