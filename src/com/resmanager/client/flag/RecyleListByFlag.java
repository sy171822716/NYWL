package com.resmanager.client.flag;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.flag.GetRecyleByLabelAsyncTask.GetRecyleListByLabelListener;
import com.resmanager.client.model.RecyleListModel;
import com.resmanager.client.model.RecyleModel;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.PullToRefreshLayout;
import com.resmanager.client.view.PullToRefreshLayout.OnRefreshListener;
import com.resmanager.client.view.PullableListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class RecyleListByFlag extends LinearLayout implements OnRefreshListener {
	private LayoutInflater mInflater;
	private PullableListView orderList;
	private PullToRefreshLayout refreshView;
	private int currentPage = 1;// 当前页
	private Context context;
	private String searchStr;
	private RecyleListAdapter recyleListAdapter;
	private String startDate = "";
	private String endDate = "";
	private String DayType = "";
	private String ordercustomer = "";
	private String DriverID = "";
	private TextView tongji_txt;
	private String salername = "";

	public RecyleListByFlag(Context context, String searchStr, String startDate, String endDate, String DayType, String ordercustomer, String DriverID,
			String salername) {
		super(context);
		this.context = context;
		this.searchStr = searchStr;
		this.startDate = startDate;
		this.endDate = endDate;
		this.salername = salername;
		this.DayType = DayType;
		this.ordercustomer = ordercustomer;
		this.DriverID = DriverID;
		mInflater = LayoutInflater.from(context);
		View contentView = mInflater.inflate(R.layout.flag_search_order_list, null);
		this.addView(contentView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		orderList = (PullableListView) contentView.findViewById(R.id.order_list);
		refreshView = (PullToRefreshLayout) contentView.findViewById(R.id.refresh_view);
		refreshView.setOnRefreshListener(this);
		// orderList.setOnItemClickListener(this);
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
			recyleListAdapter = null;
		}
		GetRecyleByLabelAsyncTask getRecyleByLabelAsyncTask = new GetRecyleByLabelAsyncTask(context, currentPage, searchStr, startDate, endDate, DayType,
				ordercustomer, DriverID, salername, isShowDialog, orpType);
		getRecyleByLabelAsyncTask.setGetRecyleListByLabelListener(new GetRecyleListByLabelListener() {

			@Override
			public void getRecyleListByLabelResult(int orpType, RecyleListModel recyleListModel) {
				if (orpType == ContactsUtils.ORP_REFRESH) {
					refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
				} else if (orpType == ContactsUtils.ORP_LOAD) {
					refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
				if (recyleListModel != null) {
					currentPage += 1;
					if (recyleListModel.getResult().equals("true")) {
						ArrayList<RecyleModel> recyleModels = recyleListModel.getData();
						if (recyleListAdapter == null) {
							recyleListAdapter = new RecyleListAdapter(context, recyleModels);
							orderList.setAdapter(recyleListAdapter);
						} else {
							recyleListAdapter.loadMore(recyleModels);
						}
						setTongji(recyleListModel.getDescription());
					} else {
						Tools.showToast(context, recyleListModel.getDescription());
					}
				} else {
					Tools.showToast(context, "回收记录获取失败，请检查网络");
				}
			}
		});
		getRecyleByLabelAsyncTask.execute();
	}

	// @Override
	// public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3)
	// {
	// Order order = (Order) v.getAdapter().getItem(pos);
	// Intent intent = new Intent(context, OrderDetailActivity.class);
	// intent.putExtra("orderId", order.getOrderID());
	// context.startActivity(intent);
	// }

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
				tongji_txt.setText("从" + startDate + "到" + endDate + "为止，总计回收送该标签桶" + number + "次");
			} else {
				tongji_txt.setText("从" + startDate + "到目前为止，总计回收该标签桶" + number + "次");
			}
		} else {
			if (endDate != null && !endDate.equals("")) {
				tongji_txt.setText("截止" + endDate + "为止，总计回收该标签桶" + number + "次");
			} else {
				tongji_txt.setText("到目前为止，总计回收该标签桶" + number + "次");
			}
		}
	}
}
