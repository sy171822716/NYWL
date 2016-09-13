/**   
 * @Title: ChooseDriverActivity.java 
 * @Package com.resmanager.client.order 
 * @Description: 选择驾驶员
 * @author ShenYang  
 * @date 2016-2-25 下午3:50:54 
 * @version V1.0   
 */
package com.resmanager.client.user.order.unloading;

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
import com.resmanager.client.model.DriverListResultModel;
import com.resmanager.client.model.DriverModel;
import com.resmanager.client.order.DriverListAdapter;
import com.resmanager.client.order.GetDriverListAsyncTask;
import com.resmanager.client.order.GetDriverListAsyncTask.GetDriverListListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;

/**
 * @ClassName: ChooseDriverActivity
 * @Description: 选择驾驶员
 * @author ShenYang
 * @date 2016-2-25 下午3:50:54
 * 
 */
@SuppressLint("InflateParams")
public class DriverList extends TopContainActivity implements OnClickListener {
	private ListView driver_list;

	@Override
	protected View getTopView() {
		View topView = inflater.inflate(R.layout.custom_title_bar, null);
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("驾驶员列表");
		return topView;
	}

	@Override
	protected View getCenterView() {
		return inflater.inflate(R.layout.driver_list, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		driver_list = (ListView) centerView.findViewById(R.id.driver_list);
		driver_list.setOnItemClickListener(driverListItemClick);
		getDriverList();
	}

	/**
	 * 驾驶员点击事件
	 */
	private OnItemClickListener driverListItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> v, View arg, int pos, long arg3) {
			// DriverModel driverModel = (DriverModel)
			// v.getAdapter().getItem(pos);
			DriverModel driverModel = (DriverModel) v.getAdapter().getItem(pos);
			Intent intent = new Intent(DriverList.this, UserDeliveryOrderList.class);
			intent.putExtra("userId", driverModel.getUserId());
			startActivity(intent);

		}
	};

	/**
	 * 
	 * @Title: getDriverList
	 * @Description: 获取驾驶员列表
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getDriverList() {
		GetDriverListAsyncTask getDriverListAsyncTask = new GetDriverListAsyncTask(this);
		getDriverListAsyncTask.setGetDriverListListener(new GetDriverListListener() {

			@Override
			public void getDriverResult(DriverListResultModel driverListResultModel) {
				if (driverListResultModel != null) {
					if (driverListResultModel.getResult().equals("true")) {
						if (driverListResultModel.getData() != null) {
							DriverListAdapter driverListAdapter = new DriverListAdapter(DriverList.this, driverListResultModel.getData());
							driver_list.setAdapter(driverListAdapter);
						} else {
							Tools.showToast(DriverList.this, "驾驶员列表获取失败,请重试");
						}
					} else {
						Tools.showToast(DriverList.this, driverListResultModel.getDescription());
					}
				} else {
					Tools.showToast(DriverList.this, "驾驶员列表获取失败，请检查网络");
				}
			}
		});
		getDriverListAsyncTask.execute();
	}

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

}
