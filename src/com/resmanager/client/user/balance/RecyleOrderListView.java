package com.resmanager.client.user.balance;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.resmanager.client.R;
import com.resmanager.client.model.RecyleListModel;
import com.resmanager.client.model.RecyleModel;
import com.resmanager.client.user.balance.GetUserRecyleList.GetRecyleListListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.PullToRefreshLayout;
import com.resmanager.client.view.PullToRefreshLayout.OnRefreshListener;
import com.resmanager.client.view.PullableListView;

@SuppressLint("InflateParams")
public class RecyleOrderListView extends LinearLayout implements OnRefreshListener {
	private LayoutInflater mInflater;
	private PullableListView orderList;
	private PullToRefreshLayout refreshView;
	private int currentPage = 1;// 当前页
	private String orderstate = "1,2";// 待结算和已完成状态的回收单
	private Context context;
	private BalanceRecyleListAdapter balanceRecyleListAdapter;

	public RecyleOrderListView(Context context) {
		super(context);
		this.context = context;
		mInflater = LayoutInflater.from(context);
		View contentView = mInflater.inflate(R.layout.order_list, null);
		this.addView(contentView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		orderList = (PullableListView) contentView.findViewById(R.id.order_list);
		refreshView = (PullToRefreshLayout) contentView.findViewById(R.id.refresh_view);
		refreshView.setOnRefreshListener(this);
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
			balanceRecyleListAdapter = null;
		}
		GetUserRecyleList getUserRecyleList = new GetUserRecyleList(context, orderstate, currentPage, orpType);
		getUserRecyleList.setGetRecyleListListener(new GetRecyleListListener() {

			@Override
			public void getRecyleListResult(RecyleListModel recyleListModel, int orpType) {
				if (orpType == ContactsUtils.ORP_REFRESH) {
					refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
				} else if (orpType == ContactsUtils.ORP_LOAD) {
					refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
				if (recyleListModel != null) {
					currentPage += 1;
					if (recyleListModel.getResult().equals("true")) {
						ArrayList<RecyleModel> recyleModels = recyleListModel.getData();
						if (balanceRecyleListAdapter == null) {
							balanceRecyleListAdapter = new BalanceRecyleListAdapter(context, recyleModels);
							orderList.setAdapter(balanceRecyleListAdapter);
						} else {
							balanceRecyleListAdapter.loadMore(recyleModels);
						}
					} else {
						Tools.showToast(context, recyleListModel.getDescription());
					}
				} else {
					Tools.showToast(context, "回收记录获取失败，请检查网络");
				}
			}
		});
		getUserRecyleList.execute();
	}
}
