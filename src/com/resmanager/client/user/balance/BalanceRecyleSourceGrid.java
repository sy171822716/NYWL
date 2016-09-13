package com.resmanager.client.user.balance;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.RecylePicListModel;
import com.resmanager.client.model.RecylePicModel;
import com.resmanager.client.photo.ImagePagerActivity;
import com.resmanager.client.user.balance.GetRecylePicAsyncTask.GetRecylePicListener;
import com.resmanager.client.utils.Tools;

@SuppressLint("InflateParams")
public class BalanceRecyleSourceGrid extends TopContainActivity implements OnClickListener, OnItemClickListener {
	private GridView source_grid;
	private Button add_btn;
	private String workId;
	private BalanceSourcePicGridAdapter balanceSourcePicGridAdapter;

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
		add_btn = (Button) centerView.findViewById(R.id.add_btn);
		add_btn.setVisibility(View.GONE);
		source_grid = (GridView) findViewById(R.id.source_grid);
		source_grid.setOnItemClickListener(this);
		getRecylePicList();
	}

	@Override
	public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
		if (balanceSourcePicGridAdapter != null) {
			Intent intent = new Intent(this, ImagePagerActivity.class);
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, pos);//
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, balanceSourcePicGridAdapter.getImageUrls());
			startActivity(intent);
		}
	}

	/**
	 * 
	 * @Title: getRecylePicList
	 * @Description: 获取回收图片
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getRecylePicList() {
		GetRecylePicAsyncTask getRecylePicAsyncTask = new GetRecylePicAsyncTask(this, this.workId);
		getRecylePicAsyncTask.setGetRecylePicListener(new GetRecylePicListener() {

			@Override
			public void getRecylePicResult(RecylePicListModel recylePicListModel) {
				if (recylePicListModel != null) {
					if (recylePicListModel.getResult().equals("true")) {
						ArrayList<RecylePicModel> recyleModels = recylePicListModel.getData();
						balanceSourcePicGridAdapter = new BalanceSourcePicGridAdapter(BalanceRecyleSourceGrid.this, recyleModels);
						source_grid.setAdapter(balanceSourcePicGridAdapter);
					} else {
						Tools.showToast(BalanceRecyleSourceGrid.this, recylePicListModel.getDescription());
					}
				} else {
					Tools.showToast(BalanceRecyleSourceGrid.this, "回收明细获取失败，请检查网络");
				}
			}
		});
		getRecylePicAsyncTask.execute();
	}
}
