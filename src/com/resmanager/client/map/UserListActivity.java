/**   
 * @Title: UserListActivity.java 
 * @Package com.resmanager.client.map 
 * @Description: 用户列表 
 * @author ShenYang  
 * @date 2016-1-19 下午1:57:45 
 * @version V1.0   
 */
package com.resmanager.client.map;

import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.map.GetUserListAsyncTask.GetUserListListener;
import com.resmanager.client.map.GetUserLocationAsyncTask.GetUserLocationListener;
import com.resmanager.client.model.UserListModel;
import com.resmanager.client.model.UserModel;
import com.resmanager.client.utils.Tools;

/**
 * @ClassName: UserListActivity
 * @Description: 用户列表
 * @author ShenYang
 * @date 2016-1-19 下午1:57:45
 * 
 */
@SuppressLint("InflateParams")
public class UserListActivity extends TopContainActivity implements OnClickListener {
	private TextView notice_txt;
	private ListView label_list;

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
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("用户列表");
		notice_txt = (TextView) centerView.findViewById(R.id.notice_txt);
		notice_txt.setVisibility(View.GONE);
		label_list = (ListView) centerView.findViewById(R.id.label_list);
		label_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
				final UserModel userModel = (UserModel) v.getAdapter().getItem(pos);
				GetUserLocationAsyncTask getUserLocationAsyncTask = new GetUserLocationAsyncTask(UserListActivity.this, userModel.getUserID());
				getUserLocationAsyncTask.setGetUserLocationListener(new GetUserLocationListener() {

					@Override
					public void getUserLocationResult(Map<String, String> locationMap) {
						if (locationMap != null) {
							if (locationMap.get("result").equals("true")) {
								String Longitude = locationMap.get("Longitude");
								String Latitude = locationMap.get("Latitude");
								Intent mapIntent = new Intent(UserListActivity.this, UserLocationMapView.class);
								mapIntent.putExtra("userModel", userModel);
								mapIntent.putExtra("lat", Latitude);
								mapIntent.putExtra("lng", Longitude);
								UserListActivity.this.startActivity(mapIntent);
							} else {
								Tools.showToast(UserListActivity.this, locationMap.get("description"));
							}
						} else {
							Tools.showToast(UserListActivity.this, "位置获取失败");
						}
					}
				});
				getUserLocationAsyncTask.execute();
			}
		});
		getUserList();
	}

	/**
	 * 
	 * @Title: getUserList
	 * @Description: 获取用户列表
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getUserList() {
		GetUserListAsyncTask getUserListAsyncTask = new GetUserListAsyncTask(this);
		getUserListAsyncTask.setGetUserListListener(new GetUserListListener() {

			@Override
			public void getUserListResult(UserListModel userListModel) {
				if (userListModel != null) {
					if (userListModel.getResult().equals("true") && userListModel.getData() != null) {
						UserListAdapter userListAdapter = new UserListAdapter(UserListActivity.this, userListModel.getData());
						label_list.setAdapter(userListAdapter);
					} else {
						Tools.showToast(UserListActivity.this, userListModel.getDescription());
					}
				} else {
					Tools.showToast(UserListActivity.this, "用户获取失败，请检查网络");
				}
			}
		});
		getUserListAsyncTask.execute();
	}
}
