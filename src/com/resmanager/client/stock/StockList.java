/**   
 * @Title: StockList.java 
 * @Package com.resmanager.client.stock 
 * @Description:库存列表
 * @author ShenYang  
 * @date 2016-1-19 上午9:03:12 
 * @version V1.0   
 */
package com.resmanager.client.stock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.StockListModel;
import com.resmanager.client.model.StockModel;
import com.resmanager.client.stock.GetCustomerStockAsyncTask.GetCustomerStocListListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.PullToRefreshLayout;
import com.resmanager.client.view.PullToRefreshLayout.OnRefreshListener;
import com.resmanager.client.view.PullableListView;

/**
 * @ClassName: StockList
 * @Description: 库存列表
 * @author ShenYang
 * @date 2016-1-19 上午9:03:12
 * 
 */
@SuppressLint("InflateParams")
public class StockList extends TopContainActivity implements OnClickListener, OnRefreshListener {
	private PullableListView stockList;
	private PullToRefreshLayout refreshView;
	private int currentPage = 1;// 当前页
	private EditText searchStockEdit;
	private StockListAdapter stockListAdapter = null;
	private String searchStr = "";

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
		View topView = inflater.inflate(R.layout.title_bar_new, null);
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		searchStockEdit = (EditText) topView.findViewById(R.id.search_input_edit);
		searchStockEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				searchStr = s.toString();
				getCustomerStockList(true, ContactsUtils.ORP_NONE, searchStr, false);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
		return topView;
	}

	@Override
	protected View getCenterView() {
		return inflater.inflate(R.layout.stock_list, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stockList = (PullableListView) centerView.findViewById(R.id.stock_list);
		refreshView = (PullToRefreshLayout) centerView.findViewById(R.id.refresh_view);
		refreshView.setOnRefreshListener(this);
		stockList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
				StockModel stockModel = (StockModel) v.getAdapter().getItem(pos);
				Intent labelIntent = new Intent(StockList.this, StockLabelList.class);
				labelIntent.putExtra("customerId", stockModel.getCustomerID());
				StockList.this.startActivity(labelIntent);
			}
		});
		getCustomerStockList(true, ContactsUtils.ORP_NONE, searchStr, true);

	}

	/**
	 * 
	 * @Title: getCustomerStockList
	 * @Description: 获取客户库存
	 * @param @param isRefresh
	 * @param @param orpType 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getCustomerStockList(boolean isRefresh, int orpType, String searchStr, boolean flag) {
		if (isRefresh) {
			this.currentPage = 1;
			stockListAdapter = null;
		}
		GetCustomerStockAsyncTask getCustomerStockAsyncTask = new GetCustomerStockAsyncTask(this, currentPage, orpType, searchStr, flag);
		getCustomerStockAsyncTask.setGetCustomerStocListListener(new GetCustomerStocListListener() {

			@Override
			public void getCustomerStockResult(int orpType, StockListModel stockListModel) {
				if (orpType == ContactsUtils.ORP_REFRESH) {
					refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
				} else if (orpType == ContactsUtils.ORP_LOAD) {
					refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
				if (stockListModel != null) {
					if (stockListModel.getResult().equals("true")) {
						currentPage += 1;
						if (stockListAdapter == null) {
							stockListAdapter = new StockListAdapter(StockList.this, stockListModel.getData());
							stockList.setAdapter(stockListAdapter);
						} else {
							stockListAdapter.loadMore(stockListModel.getData());
						}
					} else {
						Tools.showToast(StockList.this, stockListModel.getDescription());
					}
				} else {
					Tools.showToast(StockList.this, "客户库存获取失败，请检查网络");
				}
			}
		});
		getCustomerStockAsyncTask.execute();
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		getCustomerStockList(true, ContactsUtils.ORP_REFRESH, searchStr, true);
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		getCustomerStockList(false, ContactsUtils.ORP_LOAD, searchStr, true);
	}

}
