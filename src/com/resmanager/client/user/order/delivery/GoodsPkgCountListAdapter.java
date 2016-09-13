package com.resmanager.client.user.order.delivery;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.model.GoodsPackageQtyModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class GoodsPkgCountListAdapter extends BaseAdapter {
	private ArrayList<GoodsPackageQtyModel> goodsPackageQtyModels;
	private LayoutInflater mInflater;

	public GoodsPkgCountListAdapter(Context context, ArrayList<GoodsPackageQtyModel> goodsPackageQtyModels) {
		this.mInflater = LayoutInflater.from(context);
		this.goodsPackageQtyModels = goodsPackageQtyModels;
	}

	private class ItemViewHolder {
		private TextView goods_name_txt, packagename_txt, goods_count;
	}

	@Override
	public int getCount() {
		return goodsPackageQtyModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		return goodsPackageQtyModels.get(arg0);
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
			convertView = mInflater.inflate(R.layout.goods_pkg_count_list_item, null);
			itemViewHolder.goods_count = (TextView) convertView.findViewById(R.id.goods_count);
			itemViewHolder.packagename_txt = (TextView) convertView.findViewById(R.id.packagename_txt);
			itemViewHolder.goods_name_txt = (TextView) convertView.findViewById(R.id.goods_name_txt);
			convertView.setTag(itemViewHolder);
		}else{
			itemViewHolder = (ItemViewHolder) convertView.getTag();
		}
		GoodsPackageQtyModel goodsPackageQtyModel = goodsPackageQtyModels.get(pos);
		itemViewHolder.goods_name_txt.setText(goodsPackageQtyModel.getGoodsname());
		itemViewHolder.packagename_txt.setText(goodsPackageQtyModel.getPackagetype());
		itemViewHolder.goods_count.setText("(数量:"+goodsPackageQtyModel.getQuantity()+")");
		return convertView;
	}

}
