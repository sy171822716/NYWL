/**   
 * @Title: OrderListAdapter.java 
 * @Package com.resmanager.client.order 
 * @Description: 订单列表适配器
 * @author ShenYang  
 * @date 2015-12-1 上午10:41:38 
 * @version V1.0   
 */
package com.resmanager.client.order;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.model.Order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
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
public class CompleteOrderListAdapter extends BaseAdapter {
	private ArrayList<Order> orders;
	private LayoutInflater mInflater;
	private Context context;

	public CompleteOrderListAdapter(Context context, ArrayList<Order> orders) {
		this.mInflater = LayoutInflater.from(context);
		this.orders = orders;
		this.context = context;
	}

	private class ViewHolder {
		TextView order_customer_txt, address_txt, weight_txt, request_date, delivery_standards_txt, order_date_txt, order_twon_txt;
		View order_status_view;
	}

	/**
	 * 
	 * @Title: loadMore
	 * @Description: 加载更多
	 * @param @param tempOrders 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void loadMore(ArrayList<Order> tempOrders) {
		for (int i = 0; i < tempOrders.size(); i++) {
			orders.add(tempOrders.get(i));
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
		return orders.size();
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
		return orders.get(arg0);
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
			convertView = mInflater.inflate(R.layout.order_list_item, null);
			viewHolder.address_txt = (TextView) convertView.findViewById(R.id.address_txt);
			viewHolder.order_customer_txt = (TextView) convertView.findViewById(R.id.order_customer_txt);
			viewHolder.weight_txt = (TextView) convertView.findViewById(R.id.weight_txt);
			viewHolder.request_date = (TextView) convertView.findViewById(R.id.request_date);
			viewHolder.delivery_standards_txt = (TextView) convertView.findViewById(R.id.delivery_standards_txt);
			viewHolder.order_date_txt = (TextView) convertView.findViewById(R.id.order_date_txt);
			viewHolder.order_status_view = convertView.findViewById(R.id.order_status_view);
			viewHolder.order_twon_txt = (TextView) convertView.findViewById(R.id.order_twon_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Order order = orders.get(pos);
		viewHolder.order_customer_txt.setText(order.getOrdercustomer());// 客户
		viewHolder.address_txt.setText(order.getShippingaddress());// 送货地址
		viewHolder.request_date.setText("要求到货日期:" + order.getRequestarrivedate());
		viewHolder.delivery_standards_txt.setText("收货日期:" + order.getDeliveryDate());
		viewHolder.order_date_txt.setText("收货人:"+order.getDeliveryMan());
		viewHolder.weight_txt.setText("重量:" + order.getShippingweight());// 重量
		String orderStateCode = order.getOrderStateCode();
		viewHolder.order_twon_txt.setText("【"+order.getTown()+"】");
		viewHolder.order_twon_txt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));// 加粗
		viewHolder.order_twon_txt.getPaint().setFakeBoldText(true);// 加粗
		viewHolder.order_customer_txt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));// 加粗
		viewHolder.order_customer_txt.getPaint().setFakeBoldText(true);// 加粗
		if (orderStateCode.equals("0")) {
			// 待处理
			viewHolder.order_status_view.setBackgroundColor(context.getResources().getColor(R.color.red));
		} else if (orderStateCode.equals("1")) {
			// 待运送
		} else if (orderStateCode.equals("2")) {
			// 运送中
		} else if (orderStateCode.equals("3")) {
			// 待审核
		} else if (orderStateCode.equals("4")) {
			// 待结算
		} else if (orderStateCode.equals("5")) {
			// 已完成
			viewHolder.order_status_view.setBackgroundColor(context.getResources().getColor(R.color.drak_gray));
		}
		return convertView;
	}

}
