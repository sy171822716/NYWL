package com.resmanager.client.warehouse;

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
import com.resmanager.client.flag.FlagSearch;
import com.resmanager.client.model.WarseHouseListModel;
import com.resmanager.client.model.WarseHouseModel;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.warehouse.GetWarseHouseListAsyncTask.GetWarseHouseListListener;

@SuppressLint("InflateParams")
public class WareHouseListActivity extends TopContainActivity implements OnClickListener, OnItemClickListener {
	private ListView warseHouseList;
	private WarseHouseListAdapter warseHouseListAdapter;

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

	@Override
	protected View getTopView() {
		View topView = inflater.inflate(R.layout.custom_title_bar, null);

		return topView;
	}

	@Override
	protected View getCenterView() {
		View contentView = inflater.inflate(R.layout.warsehouse_list, null);

		return contentView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		ImageView rightImg = (ImageView) topView.findViewById(R.id.title_right_img);
		rightImg.setVisibility(View.INVISIBLE);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("仓库列表");
		warseHouseList = (ListView) centerView.findViewById(R.id.warsehouse_list);
		warseHouseList.setOnItemClickListener(this);
		getWarseHouseList();
	}

	/**
	 * 
	 * @Title: getWarseHouseList
	 * @Description: 获取仓库列表
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getWarseHouseList() {
		GetWarseHouseListAsyncTask getWarseHouseListAsyncTask = new GetWarseHouseListAsyncTask(this);
		getWarseHouseListAsyncTask.setGetWarseHouseListListener(new GetWarseHouseListListener() {

			@Override
			public void getWarseHouseListResult(WarseHouseListModel warseHouseListModel) {
				if (warseHouseListModel != null) {
					if (warseHouseListModel.getResult().equals("true")) {
						warseHouseListAdapter = new WarseHouseListAdapter(WareHouseListActivity.this, warseHouseListModel.getData());
						warseHouseList.setAdapter(warseHouseListAdapter);
					} else {
						Tools.showToast(WareHouseListActivity.this, warseHouseListModel.getDescription());
					}
				} else {
					Tools.showToast(WareHouseListActivity.this, "仓库列表获取失败，请重试");
				}
			}
		});
		getWarseHouseListAsyncTask.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		WarseHouseModel warseHouseModel = (WarseHouseModel) arg0.getAdapter().getItem(arg2);
		Intent chooseWarseHouseIntent = new Intent(WareHouseListActivity.this, FlagSearch.class);
		chooseWarseHouseIntent.putExtra("warseHouseModel", warseHouseModel);
		setResult(ContactsUtils.CHOOSE_WARSEHOUSE_RESULT, chooseWarseHouseIntent);
		finish();
	}

}
