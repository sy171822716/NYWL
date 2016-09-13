package com.resmanager.client.map;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.model.UserModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class UserListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<UserModel> userModels;

	public UserListAdapter(Context context, ArrayList<UserModel> userModels) {
		this.mInflater = LayoutInflater.from(context);
		this.userModels = userModels;
	}

	private class ItemViewHolder {
		TextView user_name, work_id_txt, user_type_txt;
	}

	@Override
	public int getCount() {
		return userModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		return userModels.get(arg0);
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
			convertView = mInflater.inflate(R.layout.user_list_item, null);
			itemViewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
			itemViewHolder.work_id_txt = (TextView) convertView.findViewById(R.id.work_id_txt);
			itemViewHolder.user_type_txt = (TextView) convertView.findViewById(R.id.user_type_txt);
			convertView.setTag(itemViewHolder);
		} else {
			itemViewHolder = (ItemViewHolder) convertView.getTag();
		}
		UserModel userModel = userModels.get(pos);
		itemViewHolder.user_name.setText("姓名:" + userModel.getNickName());
		itemViewHolder.work_id_txt.setText("工号:" + userModel.getUserName());
		String userType = userModel.getUserType();
		String leavelName = "其他";
		if (userType.equals("0")) {
			leavelName = "管理员";
		} else if (userType.equals("1")) {
			leavelName = "驾驶员";
		} else if (userType.equals("2")) {
			leavelName = "统计员";
		}
		itemViewHolder.user_type_txt.setText("级别:"+leavelName);
		return convertView;
	}

}
