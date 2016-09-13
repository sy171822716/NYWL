/**   
 * @Title: GoodsListAdapter.java 
 * @Package com.resmanager.client.order 
 * @Description: 发货物品列表 
 * @author ShenYang  
 * @date 2015-12-3 下午4:18:41 
 * @version V1.0   
 */
package com.resmanager.client.order;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.model.SourceModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @ClassName: GoodsListAdapter
 * @Description:发货物品列表
 * @author ShenYang
 * @date 2015-12-3 下午4:18:41
 * 
 */
@SuppressLint("InflateParams")
public class ResourceListAdapter extends BaseAdapter {

	private ArrayList<SourceModel> sourceModels;
	private LayoutInflater mInflater;

	public ResourceListAdapter(Context context, ArrayList<SourceModel> sourceModels) {
		this.mInflater = LayoutInflater.from(context);
		this.sourceModels = sourceModels;

	}

	private class ItemViewHolder {
		TextView goods_name_txt, package_type_txt, quantity_txt, qty_txt, wherehourse_txt;
	}

	/*
	 * (非 Javadoc) <p>Title: getCount</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return sourceModels.size();
	}

	/*
	 * (非 Javadoc) <p>Title: getItem</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @return
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return sourceModels.get(arg0);
	}

	/*
	 * (非 Javadoc) <p>Title: getItemId</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @return
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	/*
	 * (非 Javadoc) <p>Title: getView</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @param arg1
	 * 
	 * @param arg2
	 * 
	 * @return
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ItemViewHolder itemViewHolder = null;
		if (convertView == null) {
			itemViewHolder = new ItemViewHolder();
			convertView = mInflater.inflate(R.layout.goods_list_item, null);
			itemViewHolder.goods_name_txt = (TextView) convertView.findViewById(R.id.goods_name_txt);
			itemViewHolder.package_type_txt = (TextView) convertView.findViewById(R.id.package_type_txt);
			itemViewHolder.qty_txt = (TextView) convertView.findViewById(R.id.qty_txt);
			itemViewHolder.quantity_txt = (TextView) convertView.findViewById(R.id.quantity_txt);
			itemViewHolder.wherehourse_txt = (TextView) convertView.findViewById(R.id.wherehourse_txt);
			convertView.setTag(itemViewHolder);
		} else {
			itemViewHolder = (ItemViewHolder) convertView.getTag();
		}
		SourceModel sourceModel = sourceModels.get(pos);
		itemViewHolder.goods_name_txt.setText(sourceModel.getGoodsname());
		itemViewHolder.package_type_txt.setText("类型: " + sourceModel.getPackagetype());
		itemViewHolder.qty_txt.setText("数量: " + sourceModel.getQty());
		itemViewHolder.quantity_txt.setText("件数: " + sourceModel.getQuantity());
		itemViewHolder.wherehourse_txt.setText("仓库: " + sourceModel.getWarehouse());
		return convertView;
	}

}
