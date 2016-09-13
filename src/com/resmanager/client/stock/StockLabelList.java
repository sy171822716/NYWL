/**   
 * @Title: StockLabelList.java 
 * @Package com.resmanager.client.stock 
 * @Description: 获取库存标签列表
 * @author ShenYang  
 * @date 2016-1-19 下午12:51:40 
 * @version V1.0   
 */
package com.resmanager.client.stock;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.LabelPackageListModel;
import com.resmanager.client.stock.GetStockLabelsByCustomerId.GetLabelPackageListener;
import com.resmanager.client.utils.Tools;

/**
 * @ClassName: StockLabelList
 * @Description:获取库存标签列表
 * @author ShenYang
 * @date 2016-1-19 下午12:51:40
 * 
 */
@SuppressLint("InflateParams")
public class StockLabelList extends TopContainActivity implements OnClickListener {

	/*
	 * (非 Javadoc) <p>Title: onClick</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	private ListView label_list;
	private String customerId;
	private StockLabelListAdapter stockLabelListAdapter;

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
		return inflater.inflate(R.layout.custom_title_bar, null);
	}

	/*
	 * (非 Javadoc) <p>Title: getCenterView</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getCenterView()
	 */
	@Override
	protected View getCenterView() {
		return inflater.inflate(R.layout.stock_label_list, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		customerId = getIntent().getExtras().getString("customerId");
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("库存标签");
		label_list = (ListView) centerView.findViewById(R.id.label_list);
		getLabelPackageList();
	}

	private void getLabelPackageList() {
		GetStockLabelsByCustomerId getStockLabelsByCustomerId = new GetStockLabelsByCustomerId(this, customerId);
		getStockLabelsByCustomerId.setGetLabelPackageListener(new GetLabelPackageListener() {

			@Override
			public void getLabelPackageResult(LabelPackageListModel labelPackageListModel) {
				if (labelPackageListModel != null) {
					if (labelPackageListModel.getResult().equals("true") && labelPackageListModel.getData() != null) {
						stockLabelListAdapter = new StockLabelListAdapter(StockLabelList.this, labelPackageListModel.getData());
						label_list.setAdapter(stockLabelListAdapter);
					} else {
						Tools.showToast(StockLabelList.this, labelPackageListModel.getDescription());
					}
				} else {
					Tools.showToast(StockLabelList.this, "标签获取失败，请检查网络");
				}
			}
		});
		getStockLabelsByCustomerId.execute();
	}
}
