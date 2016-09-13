package com.resmanager.client.user.balance;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.model.Order;
import com.resmanager.client.model.TradeBillListModel;
import com.resmanager.client.model.TradeBillModel;
import com.resmanager.client.user.balance.GetDriverTradeBillAsyncTask.GetTradeBillListener;
import com.resmanager.client.utils.Tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class BalanceOrderListAdapter extends BaseAdapter {
	private ArrayList<Order> orders;
	private LayoutInflater mInflater;
	private Context context;

	public BalanceOrderListAdapter(Context context, ArrayList<Order> orders) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.orders = orders;
	}

	private class ItemViewHolder {
		private TextView order_number_txt, order_customer_txt, address_txt, order_date_txt, order_state_txt, order_num_txt, weight_txt, monery_yijie_txt,
				order_monery_txt, monery_daijie_txt, read_more_txt;

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

	@Override
	public int getCount() {
		return orders.size();
	}

	@Override
	public Object getItem(int arg0) {
		return orders.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ItemViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ItemViewHolder();
			convertView = mInflater.inflate(R.layout.balance_order_list_item, null);
			viewHolder.address_txt = (TextView) convertView.findViewById(R.id.address_txt);
			viewHolder.order_number_txt = (TextView) convertView.findViewById(R.id.order_number_txt);
			viewHolder.order_customer_txt = (TextView) convertView.findViewById(R.id.order_customer_txt);
			viewHolder.order_date_txt = (TextView) convertView.findViewById(R.id.order_date_txt);
			viewHolder.order_state_txt = (TextView) convertView.findViewById(R.id.order_state_txt);
			viewHolder.order_num_txt = (TextView) convertView.findViewById(R.id.order_num_txt);
			viewHolder.weight_txt = (TextView) convertView.findViewById(R.id.weight_txt);
			viewHolder.monery_yijie_txt = (TextView) convertView.findViewById(R.id.monery_yijie_txt);
			viewHolder.order_monery_txt = (TextView) convertView.findViewById(R.id.order_monery_txt);
			viewHolder.monery_daijie_txt = (TextView) convertView.findViewById(R.id.monery_daijie_txt);
			viewHolder.read_more_txt = (TextView) convertView.findViewById(R.id.read_more_txt);
//			viewHolder.notice_str = (TextView) convertView.findViewById(R.id.notice_str);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ItemViewHolder) convertView.getTag();
		}
		Order order = orders.get(pos);
		viewHolder.order_number_txt.setText("订单编号:" + order.getSaleoid());
		viewHolder.monery_daijie_txt.setText("¥" + order.getNotSettled());
		viewHolder.monery_yijie_txt.setText("¥" + order.getHasSettled());
		viewHolder.order_monery_txt.setText("¥" + order.getTotalMoney());
		viewHolder.order_customer_txt.setText(order.getOrdercustomer());// 客户
//		String noticeStr = "截止到"+Tools.formatNowDate()+",剩余结算金额为:¥"+order.getNotSettled();
//		viewHolder.notice_str.setText(noticeStr);
		viewHolder.address_txt.setText(order.getShippingaddress());// 送货地址
		viewHolder.weight_txt.setText(order.getShippingweight());// 重量
		viewHolder.order_num_txt.setText(order.getQuantity() + "");
		String orderStateCode = order.getOrderStateCode();
		if (orderStateCode.equals("0")) {
			// 待处理
			viewHolder.order_state_txt.setTextColor(context.getResources().getColor(R.color.order_daichuli));
		} else if (orderStateCode.equals("1")) {
			// 待运送
			viewHolder.order_state_txt.setTextColor(context.getResources().getColor(R.color.order_daiyunsong));
		} else if (orderStateCode.equals("2")) {
			// 运送中
			viewHolder.order_state_txt.setTextColor(context.getResources().getColor(R.color.order_yunsongzhong));
		} else if (orderStateCode.equals("3")) {
			// 待审核
			viewHolder.order_state_txt.setTextColor(context.getResources().getColor(R.color.order_daishenhe));
		} else if (orderStateCode.equals("4")) {
			// 待结算
			viewHolder.order_state_txt.setTextColor(context.getResources().getColor(R.color.order_daijiesuan));
		} else if (orderStateCode.equals("5")) {
			// 已完成
			viewHolder.order_state_txt.setTextColor(context.getResources().getColor(R.color.order_yiwancheng));
		}
		viewHolder.order_state_txt.setText(order.getOrderStateName());
		viewHolder.order_date_txt.setText("订单审核日期:" + order.getDischargeAudit_Date());
		viewHolder.read_more_txt.setTag(order.getOrderID());
		viewHolder.read_more_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String orderId = (String) v.getTag();
				getTradeBill(orderId);
			}
		});
		return convertView;
	}

	private void getTradeBill(String orderId) {
		GetDriverTradeBillAsyncTask getDriverTradeBillAsyncTask = new GetDriverTradeBillAsyncTask(context, orderId);
		getDriverTradeBillAsyncTask.setGetTradeBillListener(new GetTradeBillListener() {

			@Override
			public void getTraderBillResult(TradeBillListModel tradeBillListModel) {
				if (tradeBillListModel != null) {
					if (tradeBillListModel.getResult().equals("true")) {
						ArrayList<TradeBillModel> bms = tradeBillListModel.getData();
						Intent tradeBillIntent = new Intent(context, UserTradeBillActivity.class);
						tradeBillIntent.putExtra("bms", bms);
						context.startActivity(tradeBillIntent);
					} else {
						Tools.showToast(context, tradeBillListModel.getDescription());
					}
				} else {
					Tools.showToast(context, "结算明细获取失败，请检查网络");
				}
			}
		});
		getDriverTradeBillAsyncTask.execute();
	}
}
