/**   
 * @Title: GoodsListAdapter.java 
 * @Package com.resmanager.client.user.order.goods 
 * @Description: 产品列表适配器
 * @author ShenYang   
 * @date 2015-12-26 上午10:03:14 
 * @version V1.0   
 */
package com.resmanager.client.user.order.goods;

import com.resmanager.client.R;
import com.resmanager.client.model.GoodsModel;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @ClassName: GoodsListAdapter
 * @Description: 产品列表适配器
 * @author ShenYang
 * @date 2015-12-26 上午10:03:14
 * 
 */
@SuppressLint("InflateParams")
public class GoodsListAdapter extends BaseAdapter {

	private List<GoodsModel> goodsModels;
	private LayoutInflater mInflater;

	public GoodsListAdapter(Context context, List<GoodsModel> goodsModels) {
		this.mInflater = LayoutInflater.from(context);
		this.goodsModels = goodsModels;
	}

	private class ViewHolder {
		TextView goods_name_txt, package_typename_txt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return goodsModels.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return goodsModels.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.locaiton_item, null);
			viewHolder.package_typename_txt = (TextView) convertView.findViewById(R.id.company_address_txt);
			viewHolder.goods_name_txt = (TextView) convertView.findViewById(R.id.company_name_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		GoodsModel goodsModel = goodsModels.get(pos);
		viewHolder.package_typename_txt.setText(goodsModel.getPackagetype());
		viewHolder.goods_name_txt.setText(goodsModel.getGoodsName());
		return convertView;
	}

}
