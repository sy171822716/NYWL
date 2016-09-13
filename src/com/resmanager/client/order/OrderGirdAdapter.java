package com.resmanager.client.order;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.model.OrderOrpModel;
import com.resmanager.client.utils.ContactsUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class OrderGirdAdapter extends BaseAdapter {

	private ArrayList<OrderOrpModel> orderOrpModels;
	private LayoutInflater mInflater;

	public OrderGirdAdapter(Context context,
			ArrayList<OrderOrpModel> orderOrpModels) {
		this.mInflater = LayoutInflater.from(context);
		this.orderOrpModels = orderOrpModels;

	}

	private class ViewHolder {
		private TextView order_num, orp_name;
		private ImageView order_orp_img;
	}

	@Override
	public int getCount() {
		return orderOrpModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		return orderOrpModels.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.order_main_grid_item, null);
			viewHolder = new ViewHolder();
			viewHolder.order_num = (TextView) convertView
					.findViewById(R.id.order_num);
			viewHolder.order_orp_img = (ImageView) convertView
					.findViewById(R.id.order_orp_img);
			viewHolder.orp_name = (TextView) convertView
					.findViewById(R.id.orp_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		OrderOrpModel orderOrpModel = orderOrpModels.get(pos);
		viewHolder.orp_name.setText(orderOrpModel.getOrpName());
		if (ContactsUtils.userDetailModel != null
				&& ContactsUtils.userDetailModel.getUserType().equals("4")) {
			viewHolder.order_num.setVisibility(View.GONE);
		} else {
			viewHolder.order_num.setVisibility(View.VISIBLE);
			viewHolder.order_num.setText("(" + orderOrpModel.getNum() + ")");
		}
		viewHolder.order_orp_img.setImageResource(orderOrpModel.getOrpImg());
		return convertView;
	}

}
