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
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName: OrderListAdapter
 * @Description: 订单列表适配器
 * @author ShenYang
 * @date 2015-12-1 上午10:41:38
 * 
 */
@SuppressLint("InflateParams")
public class OrderListAdapter extends BaseAdapter {
	private ArrayList<Order> orders;
	private LayoutInflater mInflater;
	private Context context;
	private boolean isShowMonery;

	public OrderListAdapter(Context context, ArrayList<Order> orders, boolean isShowMonery) {
		this.mInflater = LayoutInflater.from(context);
		this.orders = orders;
		this.context = context;
		this.isShowMonery = isShowMonery;
	}

	private class ViewHolder {
		TextView order_customer_txt, order_num_txt, order_state_txt, address_txt, weight_txt, request_date, delivery_standards_txt, order_date_txt,
				order_twon_txt, monery_daijie_txt, monery_yijie_txt, order_monery_txt;
		LinearLayout monery_layout;
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
			viewHolder.order_state_txt = (TextView) convertView.findViewById(R.id.order_state_txt);
			viewHolder.order_num_txt = (TextView) convertView.findViewById(R.id.order_num_txt);
			viewHolder.monery_layout = (LinearLayout) convertView.findViewById(R.id.monery_layout);
			viewHolder.monery_daijie_txt = (TextView) convertView.findViewById(R.id.monery_daijie_txt);
			viewHolder.monery_yijie_txt = (TextView) convertView.findViewById(R.id.monery_yijie_txt);
			viewHolder.order_monery_txt = (TextView) convertView.findViewById(R.id.order_monery_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Order order = orders.get(pos);
		if (isShowMonery) {
			viewHolder.monery_layout.setVisibility(View.VISIBLE);
			viewHolder.monery_daijie_txt.setText("¥" + order.getNotSettled());
			viewHolder.monery_yijie_txt.setText("¥" + order.getHasSettled());
			viewHolder.order_monery_txt.setText("¥" + order.getTotalMoney());
		} else {
			viewHolder.monery_layout.setVisibility(View.GONE);
		}
		viewHolder.order_customer_txt.setText(order.getOrdercustomer());// 客户
		viewHolder.address_txt.setText(order.getShippingaddress());// 送货地址
		if (order.getDeliverystandards().equals("紧急")) {
			viewHolder.delivery_standards_txt.setTextColor(context.getResources().getColor(R.color.orange_color));
		} else {
			viewHolder.delivery_standards_txt.setTextColor(context.getResources().getColor(R.color.middle_gray));
		}
		viewHolder.delivery_standards_txt.setText("【" + order.getDeliverystandards() + "】");
		viewHolder.order_date_txt.setVisibility(View.GONE);
		viewHolder.order_date_txt.setText(order.getOrderdate());
		viewHolder.weight_txt.setText(order.getShippingweight());// 重量
		viewHolder.order_num_txt.setText(order.getQuantity() + "");
		String orderStateCode = order.getOrderStateCode();
		viewHolder.order_twon_txt.setText("【" + order.getTown() + "】");
		viewHolder.order_twon_txt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));// 加粗
		viewHolder.order_twon_txt.getPaint().setFakeBoldText(true);// 加粗
		viewHolder.order_customer_txt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));// 加粗
		viewHolder.order_customer_txt.getPaint().setFakeBoldText(true);// 加粗
		if (orderStateCode.equals("0")) {
			// 待处理
			viewHolder.order_status_view.setBackgroundColor(context.getResources().getColor(R.color.order_daichuli));
			viewHolder.order_state_txt.setTextColor(context.getResources().getColor(R.color.order_daichuli));
			viewHolder.delivery_standards_txt.setVisibility(View.VISIBLE);// 显示配送是否紧急的状态
			viewHolder.request_date.setText("订单日期:" + order.getOrderdate());
		} else if (orderStateCode.equals("1")) {
			// 待运送
			viewHolder.order_status_view.setBackgroundColor(context.getResources().getColor(R.color.order_daiyunsong));
			viewHolder.order_state_txt.setTextColor(context.getResources().getColor(R.color.order_daiyunsong));
			viewHolder.delivery_standards_txt.setVisibility(View.VISIBLE);// 显示配送是否紧急的状态
			viewHolder.request_date.setText("派单日期:" + order.getRDT());
		} else if (orderStateCode.equals("2")) {
			// 运送中
			viewHolder.order_status_view.setBackgroundColor(context.getResources().getColor(R.color.order_yunsongzhong));
			viewHolder.order_state_txt.setTextColor(context.getResources().getColor(R.color.order_yunsongzhong));
			viewHolder.delivery_standards_txt.setVisibility(View.VISIBLE);// 显示配送是否紧急的状态
			viewHolder.request_date.setText("发货日期:" + order.getDelivery_Date());
		} else if (orderStateCode.equals("3")) {
			// 待审核
			viewHolder.order_status_view.setBackgroundColor(context.getResources().getColor(R.color.order_daishenhe));
			viewHolder.order_state_txt.setTextColor(context.getResources().getColor(R.color.order_daishenhe));
			viewHolder.delivery_standards_txt.setVisibility(View.VISIBLE);// 显示配送是否紧急的状态
			viewHolder.request_date.setText("发货日期:" + order.getDelivery_Date());
		} else if (orderStateCode.equals("4")) {
			// 待结算
			viewHolder.order_status_view.setBackgroundColor(context.getResources().getColor(R.color.order_daijiesuan));
			viewHolder.order_state_txt.setTextColor(context.getResources().getColor(R.color.order_daijiesuan));
			viewHolder.delivery_standards_txt.setVisibility(View.GONE);// 由于已经送到，所以不显示配送是否紧急的状态
			viewHolder.request_date.setText("发货日期:" + order.getDelivery_Date());//"审核日期:" + order.getDischargeAudit_Date()
		} else if (orderStateCode.equals("5")) {
			// 已完成
			viewHolder.order_status_view.setBackgroundColor(context.getResources().getColor(R.color.order_yiwancheng));
			viewHolder.order_state_txt.setTextColor(context.getResources().getColor(R.color.order_yiwancheng));
			viewHolder.delivery_standards_txt.setVisibility(View.GONE);// 由于已经送到，所以不显示配送是否紧急的状态
			viewHolder.request_date.setText("结算日期:" + order.getTradeTime());
		}
		viewHolder.order_state_txt.setText(order.getOrderStateName());
		return convertView;
	}

}
