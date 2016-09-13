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
import java.util.HashMap;
import java.util.Map;

import com.resmanager.client.R;
import com.resmanager.client.model.Order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * @ClassName: OrderListAdapter
 * @Description: 订单列表适配器
 * @author ShenYang
 * @date 2015-12-1 上午10:41:38
 * 
 */
@SuppressLint("InflateParams")
public class OrderRevokeListAdapter extends BaseAdapter {
	private ArrayList<Order> orders;
	private LayoutInflater mInflater;
	private Map<Integer, Order> checkedOrders;
	private boolean isShowCheckBox;
	private Context context;
	private boolean isShowMonery;

	public OrderRevokeListAdapter(Context context, ArrayList<Order> orders, boolean isShowCheckBox, boolean isShowMonery) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.orders = orders;
		this.checkedOrders = new HashMap<>();
		this.isShowCheckBox = isShowCheckBox;
		this.isShowMonery = isShowMonery;
	}

	private class ViewHolder {
		TextView order_state_txt, order_customer_txt, address_txt, weight_txt, request_date, delivery_standards_txt, order_date_txt, order_twon_txt,
				order_monery_txt, driver_name_txt, driver_tel_txt, order_num_txt;
		CheckBox order_checkbox;
		LinearLayout monery_layout;
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

	/**
	 * 
	 * @Title: getCheckedMaps
	 * @Description: 获取选中的订单
	 * @param @return 设定文件
	 * @return Map<Integer,Order> 返回类型
	 * @throws
	 */
	public Map<Integer, Order> getCheckedMaps() {
		return checkedOrders;
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
			convertView = mInflater.inflate(R.layout.user_order_list_item, null);
			viewHolder.address_txt = (TextView) convertView.findViewById(R.id.address_txt);
			viewHolder.order_customer_txt = (TextView) convertView.findViewById(R.id.order_customer_txt);
			viewHolder.weight_txt = (TextView) convertView.findViewById(R.id.weight_txt);
			viewHolder.request_date = (TextView) convertView.findViewById(R.id.request_date);
			viewHolder.delivery_standards_txt = (TextView) convertView.findViewById(R.id.delivery_standards_txt);
			viewHolder.order_date_txt = (TextView) convertView.findViewById(R.id.order_date_txt);
			viewHolder.order_checkbox = (CheckBox) convertView.findViewById(R.id.order_checkbox);
			viewHolder.order_twon_txt = (TextView) convertView.findViewById(R.id.order_twon_txt);
			viewHolder.order_monery_txt = (TextView) convertView.findViewById(R.id.order_monery_txt);
			viewHolder.order_state_txt = (TextView) convertView.findViewById(R.id.order_state_txt);
			viewHolder.driver_name_txt = (TextView) convertView.findViewById(R.id.driver_name_txt);
			viewHolder.driver_tel_txt = (TextView) convertView.findViewById(R.id.driver_tel_txt);
			viewHolder.monery_layout = (LinearLayout) convertView.findViewById(R.id.monery_layout);
			viewHolder.order_num_txt = (TextView) convertView.findViewById(R.id.order_num_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (isShowMonery) {
			viewHolder.monery_layout.setVisibility(View.VISIBLE);
		} else {
			viewHolder.monery_layout.setVisibility(View.GONE);
		}
		Order order = orders.get(pos);
		viewHolder.order_customer_txt.setText(order.getOrdercustomer());// 客户
		viewHolder.address_txt.setText(order.getShippingaddress());// 送货地址
		viewHolder.request_date.setText("要求到货日期:" + order.getRequestarrivedate());
		viewHolder.request_date.setVisibility(View.GONE);
		if (order.getDeliverystandards().equals("紧急")) {
			viewHolder.delivery_standards_txt.setTextColor(context.getResources().getColor(R.color.orange_color));
		} else {
			viewHolder.delivery_standards_txt.setTextColor(context.getResources().getColor(R.color.middle_gray));
		}
		viewHolder.delivery_standards_txt.setText(order.getDeliverystandards());

		viewHolder.weight_txt.setText(order.getShippingweight());// 重量
		viewHolder.order_twon_txt.setText("【" + order.getTown() + "】");
		viewHolder.order_twon_txt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));// 加粗
		viewHolder.order_twon_txt.getPaint().setFakeBoldText(true);// 加粗
		viewHolder.order_customer_txt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));// 加粗
		viewHolder.order_customer_txt.getPaint().setFakeBoldText(true);// 加粗
		viewHolder.order_monery_txt.setText("¥" + order.getOrdermoney());// 运费
		viewHolder.order_num_txt.setText(order.getQuantity() + "");
		viewHolder.driver_name_txt.setVisibility(View.VISIBLE);
		viewHolder.driver_tel_txt.setVisibility(View.VISIBLE);
		viewHolder.driver_name_txt.setText("驾驶员:"+order.getDriverName());
		viewHolder.driver_tel_txt.setText("手机:"+order.getDriverTel());
		viewHolder.order_date_txt.setText("订单日期:" + order.getOrderdate());
		viewHolder.order_state_txt.setText(order.getOrderStateName());
		final int position = pos;
		final Order tempOrder = order;
		if (isShowCheckBox) {
			viewHolder.order_checkbox.setVisibility(View.VISIBLE);
			viewHolder.order_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
					if (isChecked) {
						if (!checkedOrders.containsKey(position)) {
							checkedOrders.put(position, tempOrder);
						}
					} else {
						if (checkedOrders.containsKey(position)) {
							checkedOrders.remove(position);
						}
					}
				}
			});

			if (checkedOrders.containsKey(pos)) {
				viewHolder.order_checkbox.setChecked(true);
			} else {
				viewHolder.order_checkbox.setChecked(false);
			}
		} else {
			viewHolder.order_checkbox.setVisibility(View.GONE);
		}
		return convertView;
	}
}
