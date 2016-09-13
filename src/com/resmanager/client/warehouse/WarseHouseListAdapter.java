/**   
 * @Title: OrderListAdapter.java 
 * @Package com.resmanager.client.order 
 * @Description: 订单列表适配器
 * @author ShenYang  
 * @date 2015-12-1 上午10:41:38 
 * @version V1.0   
 */
package com.resmanager.client.warehouse;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.model.WarseHouseModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @ClassName: OrderListAdapter
 * @Description: 订单列表适配器
 * @author ShenYang
 * @date 2015-12-1 上午10:41:38
 * 
 */
@SuppressLint("InflateParams")
public class WarseHouseListAdapter extends BaseAdapter {
	private ArrayList<WarseHouseModel> warseHouseModels;
	private LayoutInflater mInflater;

	public WarseHouseListAdapter(Context context, ArrayList<WarseHouseModel> warseHouseModels) {
		this.mInflater = LayoutInflater.from(context);
		this.warseHouseModels = warseHouseModels;
	}

	private class ViewHolder {
		TextView warsehouse_name_txt, warsehouse_address_txt;
	}

	/**
	 * 
	 * @Title: loadMore
	 * @Description: 加载更多
	 * @param @param tempOrders 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void loadMore(ArrayList<WarseHouseModel> tempModels) {
		for (int i = 0; i < tempModels.size(); i++) {
			warseHouseModels.add(tempModels.get(i));
		}
		this.notifyDataSetChanged();
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
		return warseHouseModels.size();
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
		return warseHouseModels.get(arg0);
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.warsehouse_list_item, null);
			viewHolder.warsehouse_name_txt = (TextView) convertView.findViewById(R.id.warsehouse_name_txt);
			viewHolder.warsehouse_address_txt = (TextView) convertView.findViewById(R.id.warsehouse_address_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		WarseHouseModel warseHouseModel = warseHouseModels.get(pos);
		viewHolder.warsehouse_name_txt.setText(warseHouseModel.getWarehouseName() + "");
		viewHolder.warsehouse_address_txt.setText(warseHouseModel.getWarehouseAddress() + "");
		return convertView;
	}

}
