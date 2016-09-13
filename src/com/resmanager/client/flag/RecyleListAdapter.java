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
import com.resmanager.client.model.RecyleModel;
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
public class RecyleListAdapter extends BaseAdapter {
	private ArrayList<RecyleModel> recyleModels;
	private LayoutInflater mInflater;

	public RecyleListAdapter(Context context, ArrayList<RecyleModel> recyleModels) {
		this.mInflater = LayoutInflater.from(context);
		this.recyleModels = recyleModels;
	}

	private class ViewHolder {
		TextView label_text, driver_name_txt, recyle_time_txt, recyle_customer_txt, recyle_remark_txt;
	}

	/**
	 * 
	 * @Title: loadMore
	 * @Description: 加载更多
	 * @param @param tempOrders 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void loadMore(ArrayList<RecyleModel> tempRecyleOrders) {
		for (int i = 0; i < tempRecyleOrders.size(); i++) {
			recyleModels.add(tempRecyleOrders.get(i));
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
		return recyleModels.size();
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
		return recyleModels.get(arg0);
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
			convertView = mInflater.inflate(R.layout.recyle_list_item, null);
			viewHolder.label_text = (TextView) convertView.findViewById(R.id.label_text);
			viewHolder.driver_name_txt = (TextView) convertView.findViewById(R.id.driver_name_txt);
			viewHolder.recyle_time_txt = (TextView) convertView.findViewById(R.id.recyle_time_txt);
			viewHolder.recyle_customer_txt = (TextView) convertView.findViewById(R.id.recyle_customer_txt);
			viewHolder.recyle_remark_txt = (TextView) convertView.findViewById(R.id.recyle_remark_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		RecyleModel recyleModel = recyleModels.get(pos);
		String labelCode = Tools.getParserLabelCode(recyleModel.getLabelCode());
		viewHolder.label_text.setText(labelCode);
		viewHolder.driver_name_txt.setText(recyleModel.getDriverName()+"");
		viewHolder.recyle_customer_txt.setText(recyleModel.getFromCustomerName()+"");
		viewHolder.recyle_time_txt.setText(recyleModel.getCreateTime()+"");
		viewHolder.recyle_remark_txt.setText(recyleModel.getRecovery_Remark()+"");
		return convertView;
	}

}
