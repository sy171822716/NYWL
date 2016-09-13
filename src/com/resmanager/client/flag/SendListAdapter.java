/**   
 * @Title: OrderListAdapter.java 
 * @Package com.resmanager.client.order 
 * @Description: 回收列表适配器
 * @author ShenYang  
 * @date 2015-12-1 上午10:41:38 
 * @version V1.0   
 */
package com.resmanager.client.flag;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.model.FlagModel;
import com.resmanager.client.utils.Tools;

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
public class SendListAdapter extends BaseAdapter {
	private ArrayList<FlagModel> flagModels;
	private LayoutInflater mInflater;

	public SendListAdapter(Context context, ArrayList<FlagModel> flagModels) {
		this.mInflater = LayoutInflater.from(context);
		this.flagModels = flagModels;
	}

	private class ViewHolder {
		TextView label_text, driver_name_txt, send_time_txt, order_id_txt, kehu_txt;
	}

	/**
	 * 
	 * @Title: loadMore
	 * @Description: 加载更多
	 * @param @param tempOrders 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void loadMore(ArrayList<FlagModel> tempRecyleOrders) {
		for (int i = 0; i < tempRecyleOrders.size(); i++) {
			flagModels.add(tempRecyleOrders.get(i));
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
		return flagModels.size();
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
		return flagModels.get(arg0);
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
			convertView = mInflater.inflate(R.layout.send_list_item, null);
			viewHolder.label_text = (TextView) convertView.findViewById(R.id.label_text);
			viewHolder.driver_name_txt = (TextView) convertView.findViewById(R.id.driver_name_txt);
			viewHolder.send_time_txt = (TextView) convertView.findViewById(R.id.send_time_txt);
			viewHolder.order_id_txt = (TextView) convertView.findViewById(R.id.order_id_txt);
			viewHolder.kehu_txt = (TextView) convertView.findViewById(R.id.kehu_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		FlagModel flagModel = flagModels.get(pos);
		String orderId = flagModel.getOrderID();
		String labelCode = Tools.getParserLabelCode(flagModel.getLabelCode());
		viewHolder.label_text.setText(labelCode + "");
		viewHolder.driver_name_txt.setText(flagModel.getDriverName() + "");
		viewHolder.send_time_txt.setText(flagModel.getDelivery_Date() + "");
		if (orderId != null && !orderId.equals("")) {
			viewHolder.order_id_txt.setText(flagModel.getSaleoid() + "");
			viewHolder.kehu_txt.setText(flagModel.getOrdercustomer() + "");
		} else {
			viewHolder.order_id_txt.setText("正在运送中..");
			viewHolder.kehu_txt.setText("正在运送中..");
		}
		return convertView;
	}

}
