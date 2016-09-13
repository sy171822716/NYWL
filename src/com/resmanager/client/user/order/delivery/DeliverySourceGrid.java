package com.resmanager.client.user.order.delivery;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.main.photoalbum.PhotoActivity2;
import com.resmanager.client.user.order.UploadCache;
import com.resmanager.client.utils.ContactsUtils;

@SuppressLint("InflateParams")
public class DeliverySourceGrid extends TopContainActivity implements OnClickListener, OnItemClickListener {
	private String workId;
	private String orderIds;
	private GridView source_grid;
	private SourceGridAdapter sourceGridAdapter;
	private Button add_btn;


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_img:
			this.finish();
			break;
		case R.id.add_btn:
			if (add_btn.getText().toString().equals("完成")) {
				this.finish();
			} else {
				Intent addIntent = new Intent(DeliverySourceGrid.this, AddSourceInfoActivity.class);
				addIntent.putExtra("workId", this.workId);
				addIntent.putExtra("orderIds", this.orderIds);
				startActivity(addIntent);
			}
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
		titleContent.setText("已添加货物");
		return topView;
	}

	@Override
	protected View getCenterView() {
		return inflater.inflate(R.layout.delivery_source_grid_act, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		workId = getIntent().getExtras().getString("workId");
		orderIds = getIntent().getExtras().getString("orderIds");
		add_btn = (Button) centerView.findViewById(R.id.add_btn);
		add_btn.setOnClickListener(this);
		source_grid = (GridView) findViewById(R.id.source_grid);
		source_grid.setOnItemClickListener(this);
		sourceGridAdapter = new SourceGridAdapter(this, UploadCache.scanBimpModels, ContactsUtils.GRID_TYPE_DELIVERY_UPLOADING);
		source_grid.setAdapter(sourceGridAdapter);
	}

	@Override
	protected void onRestart() {
		super.onRestart();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (sourceGridAdapter != null) {
			sourceGridAdapter.notifyDataSetChanged();
			if (DeliveryActivity.NUM == UploadCache.scanBimpModels.size()) {
				add_btn.setText("完成");
			} else {
				add_btn.setText("添加(" + UploadCache.scanBimpModels.size() + ")");
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
		Intent intent = new Intent(this, PhotoActivity2.class);
		intent.putExtra("photoType", ContactsUtils.PHOTO_TYPE_DELIVERY);// 发货照片
		intent.putExtra("workId", this.workId);
		intent.putExtra("count", pos);
		startActivity(intent);
	}
}
