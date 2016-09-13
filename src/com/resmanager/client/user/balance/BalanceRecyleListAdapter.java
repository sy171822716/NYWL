package com.resmanager.client.user.balance;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.model.RecyleModel;
import com.resmanager.client.model.TradeBillListModel;
import com.resmanager.client.model.TradeBillModel;
import com.resmanager.client.user.balance.GetBalanceRecyleDetailAsyncTask.GetRecyleTradeBillListener;
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
public class BalanceRecyleListAdapter extends BaseAdapter {
	private ArrayList<RecyleModel> recyleModels;
	private LayoutInflater mInflater;
	private Context context;

	public BalanceRecyleListAdapter(Context context, ArrayList<RecyleModel> recyleModels) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.recyleModels = recyleModels;
	}

	private class ItemViewHolder {
		private TextView recyle_customer_txt, recyle_date_txt, audit_date_txt, coast_monery_txt, monery_yijie_txt, monery_daijie_txt, recyle_state_txt,
				audit_username_txt, aduit_remark_txt, recyle_detail_txt, balance_detail_txt;
		private View remark_fg;

	}

	/**
	 * 
	 * @Title: loadMore
	 * @Description: 加载更多
	 * @param @param tempOrders 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void loadMore(ArrayList<RecyleModel> tempOrders) {
		for (int i = 0; i < tempOrders.size(); i++) {
			recyleModels.add(tempOrders.get(i));
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return recyleModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		return recyleModels.get(arg0);
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
			convertView = mInflater.inflate(R.layout.balance_recyle_list_item, null);
			viewHolder.remark_fg = convertView.findViewById(R.id.remark_fg);
			viewHolder.recyle_customer_txt = (TextView) convertView.findViewById(R.id.recyle_customer_txt);
			viewHolder.recyle_date_txt = (TextView) convertView.findViewById(R.id.recyle_date_txt);
			viewHolder.audit_date_txt = (TextView) convertView.findViewById(R.id.audit_date_txt);
			viewHolder.coast_monery_txt = (TextView) convertView.findViewById(R.id.coast_monery_txt);
			viewHolder.recyle_state_txt = (TextView) convertView.findViewById(R.id.recyle_state_txt);
			viewHolder.audit_username_txt = (TextView) convertView.findViewById(R.id.audit_username_txt);
			viewHolder.aduit_remark_txt = (TextView) convertView.findViewById(R.id.aduit_remark_txt);
			viewHolder.monery_yijie_txt = (TextView) convertView.findViewById(R.id.monery_yijie_txt);
			viewHolder.recyle_detail_txt = (TextView) convertView.findViewById(R.id.recyle_detail_txt);
			viewHolder.monery_daijie_txt = (TextView) convertView.findViewById(R.id.monery_daijie_txt);
			viewHolder.balance_detail_txt = (TextView) convertView.findViewById(R.id.balance_detail_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ItemViewHolder) convertView.getTag();
		}
		RecyleModel recyleModel = recyleModels.get(pos);
		viewHolder.recyle_customer_txt.setText("客户:" + recyleModel.getFromCustomerName());
		viewHolder.monery_daijie_txt.setText("¥" + recyleModel.getDjsmoney());
		viewHolder.monery_yijie_txt.setText("¥" + recyleModel.getYjsmoney());
		viewHolder.coast_monery_txt.setText("¥" + recyleModel.getCostMoney());
//		String noticeStr = "截止到"+Tools.formatNowDate()+",剩余结算金额为:¥"+recyleModel.getDjsmoney();
//		viewHolder.notice_str.setText(noticeStr);
		if (recyleModel.getStatus().equals("1")) {
			viewHolder.recyle_state_txt.setText("待结算");
		} else {
			viewHolder.recyle_state_txt.setText("已完成");
		}
		viewHolder.audit_username_txt.setText(recyleModel.getAudit_UserName());// 客户
		if (recyleModel.getAudit_Remark() != null && !recyleModel.getAudit_Remark().equals("")) {
			viewHolder.aduit_remark_txt.setVisibility(View.VISIBLE);
			viewHolder.remark_fg.setVisibility(View.VISIBLE);
			viewHolder.aduit_remark_txt.setText("备注:" + recyleModel.getAudit_Remark());
		} else {
			viewHolder.remark_fg.setVisibility(View.GONE);
			viewHolder.aduit_remark_txt.setVisibility(View.GONE);
		}
		viewHolder.recyle_date_txt.setText("回收时间:" + recyleModel.getCreateTime());
		viewHolder.audit_date_txt.setText("审核时间:" + recyleModel.getAudit_Date());
		if (recyleModel.getStatus().equals("1")) {
			// 待结算
			viewHolder.recyle_state_txt.setTextColor(context.getResources().getColor(R.color.order_daijiesuan));
		} else if (recyleModel.getStatus().equals("2")) {
			// 已完成
			viewHolder.recyle_state_txt.setTextColor(context.getResources().getColor(R.color.order_yiwancheng));
		}
		viewHolder.recyle_detail_txt.setTag(recyleModel);
		viewHolder.recyle_detail_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RecyleModel recyleModel = (RecyleModel) v.getTag();
				Intent picGridIntent = new Intent(context, BalanceRecyleSourceGrid.class);
				picGridIntent.putExtra("workId", recyleModel.getWorkID());
				context.startActivity(picGridIntent);
			}
		});
		viewHolder.balance_detail_txt.setTag(recyleModel);
		viewHolder.balance_detail_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RecyleModel recyleModel = (RecyleModel) v.getTag();
				getTradeBill(recyleModel.getRID());

			}
		});
		return convertView;
	}

	/**
	 * 
	 * @Title: getTradeBill
	 * @Description: 获取明细
	 * @param @param RID 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getTradeBill(String RID) {
		GetBalanceRecyleDetailAsyncTask getBalanceRecyleDetailAsyncTask = new GetBalanceRecyleDetailAsyncTask(context, RID);
		getBalanceRecyleDetailAsyncTask.setGetRecyleTradeBillListener(new GetRecyleTradeBillListener() {

			@Override
			public void getRecyleBillResult(TradeBillListModel tradeBillListModel) {
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
		getBalanceRecyleDetailAsyncTask.execute();
	}
}
