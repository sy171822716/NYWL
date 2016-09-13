package com.resmanager.client.user.order;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.model.DriverModel;

@SuppressLint("UseSparseArrays")
public class DiverListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<DriverModel> driverModels;

	public DiverListAdapter(Context context, ArrayList<DriverModel> driverModels) {
		this.mInflater = LayoutInflater.from(context);
		this.driverModels = driverModels;
	}

	private class ItemViewHolder {
		TextView user_name, user_phone, car_id, work_id_txt, car_type_txt;
	}

	@Override
	public int getCount() {
		return driverModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		return driverModels.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ItemViewHolder itemViewHolder = null;
		if (convertView == null) {
			itemViewHolder = new ItemViewHolder();
			convertView = mInflater.inflate(R.layout.driver_list_item_new, null);
			itemViewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
			itemViewHolder.work_id_txt = (TextView) convertView.findViewById(R.id.work_id_txt);
			itemViewHolder.user_phone = (TextView) convertView.findViewById(R.id.user_phone);
			itemViewHolder.car_id = (TextView) convertView.findViewById(R.id.car_id);
			itemViewHolder.car_type_txt = (TextView) convertView.findViewById(R.id.car_type_txt);
			convertView.setTag(itemViewHolder);
		} else {
			itemViewHolder = (ItemViewHolder) convertView.getTag();
		}
		DriverModel driverModel = driverModels.get(pos);
		itemViewHolder.user_name.setText(driverModel.getNickName());
		itemViewHolder.work_id_txt.setText("工号:" + driverModel.getUserName());
		itemViewHolder.user_phone.setText("手机:" + driverModel.getPhone());
		itemViewHolder.car_id.setText("车牌:" + driverModel.getCID());
		itemViewHolder.car_type_txt.setText("吨位:" + driverModel.getShippington());
		return convertView;
	}

}
