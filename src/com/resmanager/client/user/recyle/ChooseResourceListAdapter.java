package com.resmanager.client.user.recyle;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.model.RecyleLabelModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ChooseResourceListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<RecyleLabelModel> recyleLabelModels;
	private boolean isshowSelect;

	public ChooseResourceListAdapter(Context context, ArrayList<RecyleLabelModel> recyleLabelModels, boolean isshowSelect) {
		this.mInflater = LayoutInflater.from(context);
		this.recyleLabelModels = recyleLabelModels;
		this.isshowSelect = isshowSelect;
	}

	private class ItemViewHolder {
		TextView label_txt, packagename_txt;
		ImageView is_select_img;
	}

	@Override
	public int getCount() {
		return recyleLabelModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		return recyleLabelModels.get(arg0);
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
			convertView = mInflater.inflate(R.layout.label_package_list_item, null);
			itemViewHolder.is_select_img = (ImageView) convertView.findViewById(R.id.is_select_img);
			itemViewHolder.label_txt = (TextView) convertView.findViewById(R.id.label_txt);
			itemViewHolder.packagename_txt = (TextView) convertView.findViewById(R.id.packagename_txt);
			convertView.setTag(itemViewHolder);
		} else {
			itemViewHolder = (ItemViewHolder) convertView.getTag();
		}
		RecyleLabelModel recyleLabelModel = recyleLabelModels.get(pos);
		String label = recyleLabelModel.getLabelCode();
		itemViewHolder.label_txt.setText(label.substring(5, label.lastIndexOf("-")));
		itemViewHolder.packagename_txt.setText(recyleLabelModel.getPackagetype());
		if (isshowSelect) {
			if (recyleLabelModel.isSelect()) {
				itemViewHolder.is_select_img.setVisibility(View.VISIBLE);
			} else {
				itemViewHolder.is_select_img.setVisibility(View.GONE);
			}
		} else {
			itemViewHolder.is_select_img.setVisibility(View.GONE);
		}
		return convertView;
	}

}
