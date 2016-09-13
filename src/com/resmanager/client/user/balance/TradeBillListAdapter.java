/**   
 * @Title: TradeBillListAdapter.java 
 * @Package com.resmanager.client.user.balance 
 * @Description: 结算明细适配器
 * @author ShenYang   
 * @date 2015-12-19 上午10:16:28 
 * @version V1.0   
 */
package com.resmanager.client.user.balance;

import java.util.List;

import com.resmanager.client.R;
import com.resmanager.client.model.TradeBillModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @ClassName: TradeBillListAdapter
 * @Description: 结算明细适配器
 * @author ShenYang
 * @date 2015-12-19 上午10:16:28
 * 
 */
public class TradeBillListAdapter extends BaseAdapter {

	private List<TradeBillModel> bms;
	private LayoutInflater mInflater;

	public TradeBillListAdapter(Context context, List<TradeBillModel> bms) {
		this.bms = bms;
		this.mInflater = LayoutInflater.from(context);
	}

	public void loadMore(List<TradeBillModel> tempbms) {
		for (TradeBillModel tradeBillModel : tempbms) {
			bms.add(tradeBillModel);
		}
		notifyDataSetChanged();
	}

	private class ViewHolder {
		TextView receiver_name_txt, receiver_date_txt, trade_monery_txt, remark_txt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return bms.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return bms.get(arg0);
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
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder viewholder = null;
		if (convertView == null) {
			viewholder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.trade_bill_item, parent, false);
			viewholder.receiver_name_txt = (TextView) convertView.findViewById(R.id.receiver_name_txt);
			viewholder.receiver_date_txt = (TextView) convertView.findViewById(R.id.receiver_date_txt);
			viewholder.trade_monery_txt = (TextView) convertView.findViewById(R.id.trade_monery_txt);
			viewholder.remark_txt = (TextView) convertView.findViewById(R.id.remark_txt);
			convertView.setTag(viewholder);
		}
		viewholder = (ViewHolder) convertView.getTag();
		TradeBillModel bm = bms.get(pos);
		viewholder.receiver_name_txt.setText("结  算  人:" + bm.getReceiverName());
		viewholder.receiver_date_txt.setText("结算日期:" + bm.getTradeTime());
		viewholder.trade_monery_txt.setText("¥" + bm.getTradeMoney());
		if (bm.getReMark().equals("")) {
			viewholder.remark_txt.setText("无");
		} else {
			viewholder.remark_txt.setText(bm.getReMark());
		}
		return convertView;
	}

}
