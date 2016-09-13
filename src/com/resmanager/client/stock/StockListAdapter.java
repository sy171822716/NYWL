package com.resmanager.client.stock;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.model.StockModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class StockListAdapter extends BaseAdapter {
	private ArrayList<StockModel> stockModels = null;
	private Context context = null;

	@SuppressLint("UseSparseArrays")
	public StockListAdapter(Context context, ArrayList<StockModel> stockModels) {
		this.context = context;
		this.stockModels = stockModels;
	}

	/**
	 * 
	 * @Title: loadMore
	 * @Description: 加载更多
	 * @param @param userSysMessageModels 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void loadMore(ArrayList<StockModel> tempStockModels) {
		for (StockModel stockModel : tempStockModels) {
			this.stockModels.add(stockModel);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return stockModels.size();
	}

	@Override
	public Object getItem(int position) {
		return stockModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.stock_item, null);
			holder = new ViewHolder();
			holder.title_txt = (TextView) convertView.findViewById(R.id.title_txt);
			holder.stock_txt = (TextView) convertView.findViewById(R.id.stock_txt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		StockModel stockModel = stockModels.get(position);
		holder.title_txt.setText(stockModel.getCustomerName());
		holder.stock_txt.setText(stockModel.getStock());
		return convertView;
	}

	/**
	 * 
	 * @className:com.xtwl.jy.client.activity.mainpage.user.adapter.ViewHolder
	 * @description: item自定义View
	 * @version:v1.0.0
	 * @date:2014-11-3 下午7:12:38
	 * @author:FuHuiHui
	 */
	final class ViewHolder {
		private TextView title_txt;
		private TextView stock_txt;

	}
}
