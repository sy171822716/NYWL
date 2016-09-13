package com.resmanager.client.flag;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.flag.GetOrderListByLabelAsyncTask.GetOrderListByLabelListener;
import com.resmanager.client.model.FlagModel;
import com.resmanager.client.model.FlagModelResult;
import com.resmanager.client.model.Order;
import com.resmanager.client.model.OrderListModel;
import com.resmanager.client.order.OrderDetailActivity;
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
import android.widget.TextView;

@SuppressLint("InflateParams")
public class OrderListByFlag extends LinearLayout implements OnRefreshListener, OnItemClickListener {
	private LayoutInflater mInflater;
	private PullableListView orderList;
	private PullToRefreshLayout refreshView;
	private int currentPage = 1;// 当前页
	private Context context;
	private String searchStr;

	private SendListAdapter sendListAdapter;
	private String startDate = "";
	private String endDate = "";
	private TextView tongji_txt;
	private String DayType = "", Warehouse = "", DriverID = "", saler = "", ordercustomer = "";

	public OrderListByFlag(Context context, String searchStr, String startDate, String endDate, String DayType, String ordercustomer, String DriverID,
			String saler, String Warehouse) {
		super(context);
		this.context = context;
		this.searchStr = searchStr;
		this.startDate = startDate;
		this.endDate = endDate;
		this.DayType = DayType;
		this.Warehouse = Warehouse;
		this.DriverID = DriverID;
		this.saler = saler;
		this.ordercustomer = ordercustomer;
		mInflater = LayoutInflater.from(context);
		View contentView = mInflater.inflate(R.layout.flag_search_order_list, null);
		this.addView(contentView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		orderList = (PullableListView) contentView.findViewById(R.id.order_list);
		refreshView = (PullToRefreshLayout) contentView.findViewById(R.id.refresh_view);
		refreshView.setOnRefreshListener(this);
		orderList.setOnItemClickListener(this);
		tongji_txt = (TextView) contentView.findViewById(R.id.tongji_txt);
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
			sendListAdapter = null;
		}
		GetOrderListByLabelAsyncTask getOrderListByLabel = new GetOrderListByLabelAsyncTask(context, currentPage, searchStr, startDate, endDate, DayType,
				ordercustomer, saler, DriverID, Warehouse, isShowDialog, orpType);
		getOrderListByLabel.setGetOrderListByLabelListener(new GetOrderListByLabelListener() {

			@Override
			public void getOrderListByLabelResult(int orpType, FlagModelResult orderListModel) {
				if (orpType == ContactsUtils.ORP_REFRESH) {
					refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
				} else if (orpType == ContactsUtils.ORP_LOAD) {
					refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
				if (orderListModel != null) {
					currentPage += 1;
					if (orderListModel.getResult().equals("true")) {
						ArrayList<FlagModel> flagModels = orderListModel.getData();
						if (sendListAdapter == null) {

							sendListAdapter = new SendListAdapter(context, flagModels);
							orderList.setAdapter(sendListAdapter);
						} else {
							sendListAdapter.loadMore(flagModels);
						}	
						setTongji(orderListModel.getDescription());
					} else {
						Tools.showToast(context, orderListModel.getDescription());
					}
				} else {
					Tools.showToast(context, "数据获取失败，请检查网络");
				}
			}

		});
		getOrderListByLabel.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
		FlagModel order = (FlagModel) v.getAdapter().getItem(pos);
		Intent intent = new Intent(context, OrderDetailActivity.class);
		intent.putExtra("orderId", order.getOrderID());
		context.startActivity(intent);
	}

	/**
	 * 
	 * @Title: setTongji
	 * @Description: 设置统计
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void setTongji(String number) {
		if (startDate != null && !startDate.equals("")) {
			if (endDate != null && !endDate.equals("")) {
				tongji_txt.setText("从" + startDate + "到" + endDate + "为止，该标签总计运送桶" + number + "个");
			} else {
				tongji_txt.setText("从" + startDate + "到目前为止，该标签总计运送桶" + number + "个");
			}
		} else {
			if (endDate != null && !endDate.equals("")) {
				tongji_txt.setText("截止" + endDate + "为止，该标签总计运送桶" + number + "个");
			} else {
				tongji_txt.setText("到目前为止，该标签总计运送桶" + number + "个");
			}
		}
	}
}
