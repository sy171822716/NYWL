package com.resmanager.client.home;

import java.util.List;

import com.resmanager.client.R;
import com.resmanager.client.model.PowerModel;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrpGridAdapter extends BaseAdapter {

	private List<PowerModel> orpModels;
	private LayoutInflater mInflater;
	private Context context;

	public OrpGridAdapter(Context context, List<PowerModel> orpModels) {
		this.context = context;
		this.orpModels = orpModels;
		this.mInflater = LayoutInflater.from(context);
	}

	private class ItemViewHolder {
		private ImageView orp_img;
		private TextView orp_name, num_txt;
	}

	public void notifyMsgNum(String num) {
		for (int i = 0; i < orpModels.size(); i++) {
			PowerModel pm = orpModels.get(i);
			if (pm.getPowerID() == ContactsUtils.POWER_MY_MESSAGE) {
				try {
					pm.setNum(Integer.parseInt(num));
				} catch (Exception e) {
					pm.setNum(0);
					e.printStackTrace();
				}

			}
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return orpModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		return orpModels.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ItemViewHolder itemViewHolder = null;
		if (convertView == null) {
			itemViewHolder = new ItemViewHolder();
			convertView = mInflater.inflate(R.layout.orp_grid_item, null);
			itemViewHolder.orp_img = (ImageView) convertView.findViewById(R.id.orp_img);
			itemViewHolder.orp_name = (TextView) convertView.findViewById(R.id.orp_name);
			itemViewHolder.num_txt = (TextView) convertView.findViewById(R.id.num_txt);
			convertView.setTag(itemViewHolder);
		} else {
			itemViewHolder = (ItemViewHolder) convertView.getTag();
		}

		PowerModel pm = orpModels.get(pos);
		if (pm.isShowNum() && pm.getNum() > 0) {
			itemViewHolder.num_txt.setVisibility(View.VISIBLE);
		} else {
			itemViewHolder.num_txt.setVisibility(View.GONE);
		}
		itemViewHolder.num_txt.setText(pm.getNum() + "");
		String orpName = Tools.getStr(context, pm.getPowerName());
		int orpImg = pm.getPowerImg();
		itemViewHolder.orp_img.setImageResource(orpImg);
		itemViewHolder.orp_name.setText(orpName);
		return convertView;
	}
}
