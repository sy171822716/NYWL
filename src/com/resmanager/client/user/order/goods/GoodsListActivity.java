/**   
 * @Title: GoodsListActivity.java 
 * @Package com.resmanager.client.user.order.goods 
 * @Description: 货物列表 
 * @author ShenYang   
 * @date 2015-12-26 上午9:51:35 
 * @version V1.0   
 */
package com.resmanager.client.user.order.goods;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.GoodsModel;
import com.resmanager.client.user.order.delivery.AddSourceInfoActivity;
import com.resmanager.client.user.order.delivery.DeliveryActivity;
import com.resmanager.client.user.order.delivery.DeliverySourceGrid;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;

/**
 * @ClassName: GoodsListActivity
 * @Description:货物列表
 * @author ShenYang
 * @date 2015-12-26 上午9:51:35
 * 
 */
@SuppressLint("InflateParams")
public class GoodsListActivity extends TopContainActivity implements OnClickListener, OnItemClickListener {
	private ListView goods_list;
	private ArrayList<GoodsModel> goodsModels;

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
		titleContent.setText("选择产品");
		return topView;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		goodsModels = (ArrayList<GoodsModel>) getIntent().getExtras().getSerializable("goodsModels");
		goods_list = (ListView) centerView.findViewById(R.id.goods_list);
		goods_list.setOnItemClickListener(this);
		// getGoodsByOrderIds();
		GoodsListAdapter goodsListAdapter = new GoodsListAdapter(GoodsListActivity.this, goodsModels);
		goods_list.setAdapter(goodsListAdapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getCenterView()
	 */
	@Override
	protected View getCenterView() {
		return inflater.inflate(R.layout.goods_list, null);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		GoodsModel goodsModel = (GoodsModel) arg0.getAdapter().getItem(pos);
		String goodsNameId = goodsModel.getGoodsnameID();
		if (DeliveryActivity.skuMap.containsKey(goodsNameId)) {
			int num = DeliveryActivity.skuMap.get(goodsNameId);
			if (DeliveryActivity.selectSkuMap != null) {
				if (DeliveryActivity.selectSkuMap.containsKey(goodsNameId)) {
					int selectNum = DeliveryActivity.selectSkuMap.get(goodsNameId);
					if (selectNum == num) {
						Tools.showToast(GoodsListActivity.this, "该类型货物已全部添加");
						return;
					}
				}
			}
			Intent chooseResultIntent = new Intent(GoodsListActivity.this, AddSourceInfoActivity.class);
			chooseResultIntent.putExtra("goodsModel", goodsModel);
			setResult(ContactsUtils.CHOOSE_GOODS_RESULT, chooseResultIntent);
			finish();

		} else {
			Tools.showToast(GoodsListActivity.this, "发货订单中不包含此货物型号");
		}

	}
}
