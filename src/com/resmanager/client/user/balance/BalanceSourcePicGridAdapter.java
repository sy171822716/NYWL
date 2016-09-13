package com.resmanager.client.user.balance;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.model.RecylePicModel;
import com.squareup.picasso.Picasso;

@SuppressLint("InflateParams")
public class BalanceSourcePicGridAdapter extends BaseAdapter {
	private ArrayList<RecylePicModel> recylePicModels;
	private LayoutInflater mInflater;
	private Context context;

	public BalanceSourcePicGridAdapter(Context context, ArrayList<RecylePicModel> recylePicModels) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.recylePicModels = recylePicModels;
	}

	/**
	 * 
	 * @Title: getImageUrls
	 * @Description: 获取urls
	 * @param @return 设定文件
	 * @return String[] 返回类型
	 * @throws
	 */
	public String[] getImageUrls() {
		String[] urls = new String[recylePicModels.size()];
		for (int i = 0; i < recylePicModels.size(); i++) {
			urls[i] = recylePicModels.get(i).getOriginal_Path();
		}
		return urls;
	}

	private class ViewHolder {
		public ImageView goods_img;
		private TextView label_code_txt, goods_type_name_txt, goods_package_type_txt;
	}

	@Override
	public int getCount() {
		return recylePicModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		return recylePicModels.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.delivery_source_grid_item, null);
			viewHolder = new ViewHolder();
			viewHolder.goods_img = (ImageView) convertView.findViewById(R.id.goods_img);
			viewHolder.goods_package_type_txt = (TextView) convertView.findViewById(R.id.goods_package_type_txt);
			viewHolder.goods_type_name_txt = (TextView) convertView.findViewById(R.id.goods_type_name_txt);
			viewHolder.label_code_txt = (TextView) convertView.findViewById(R.id.label_code_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		RecylePicModel recylePicModel = recylePicModels.get(pos);
		viewHolder.label_code_txt.setText(recylePicModel.getLabels());
		viewHolder.goods_type_name_txt.setVisibility(View.GONE);
		viewHolder.goods_package_type_txt.setVisibility(View.GONE);
		Picasso.with(context).load(recylePicModel.getThumb_Path()).placeholder(R.drawable.default_img).error(R.drawable.default_img).into(viewHolder.goods_img);
		return convertView;
	}
}
