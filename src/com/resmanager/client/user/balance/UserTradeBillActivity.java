/**
 * 
 */
package com.resmanager.client.user.balance;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.TradeBillModel;

/**
 * @author 17182
 * 
 */
@SuppressLint("InflateParams")
public class UserTradeBillActivity extends TopContainActivity implements OnClickListener {
	private ListView trade_bill_list;
	private TradeBillListAdapter tradeBillListAdapter;
	private ArrayList<TradeBillModel> bms;

	/*
	 * (非 Javadoc) <p>Title: onClick</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_img:
			this.finish();
			break;

		default:
			break;
		}
	}

	/*
	 * (非 Javadoc) <p>Title: getTopView</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getTopView()
	 */
	@Override
	protected View getTopView() {
		View topView = inflater.inflate(R.layout.custom_title_bar, null);
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("结算明细");
		return topView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getCenterView()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected View getCenterView() {
		bms = (ArrayList<TradeBillModel>) getIntent().getExtras().getSerializable("bms");
		View contentView = inflater.inflate(R.layout.user_trade_bill, null);
		trade_bill_list = (ListView) contentView.findViewById(R.id.trade_bill_list);
		trade_bill_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
				// TradeBillModel tbm = (TradeBillModel)
				// v.getAdapter().getItem(pos);
				// Intent orderDetailIntent = new
				// Intent(UserTradeBillActivity.this,
				// OrderDetailActivity.class);
				// orderDetailIntent.putExtra("orderId", tbm.getOrderID());
				// startActivity(orderDetailIntent);

			}
		});
		if (tradeBillListAdapter == null) {
			tradeBillListAdapter = new TradeBillListAdapter(UserTradeBillActivity.this, bms);
			trade_bill_list.setAdapter(tradeBillListAdapter);
		}
		return contentView;
	}

}
