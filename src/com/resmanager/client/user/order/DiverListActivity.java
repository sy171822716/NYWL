/**
 * @Description:驾驶员列表主界面
 * @version:v1.0
 * @author:FuHuiHui
 * @date:2016-3-30 下午2:37:34
 */
package com.resmanager.client.user.order;

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
import com.resmanager.client.order.GetDriverListAsyncTask;
import com.resmanager.client.order.GetDriverListAsyncTask.GetDriverListListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;

@SuppressLint("InflateParams")
public class DiverListActivity extends TopContainActivity implements OnClickListener {
	private ListView driver_list;
	private DiverListAdapter diverListAdapter;

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
			DriverModel driverModel = (DriverModel) v.getAdapter().getItem(pos);
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("driverModel", driverModel);
			intent.putExtras(bundle);
			setResult(ContactsUtils.CHOOSE_DRIVER_RESULT, intent);
			finish();
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
		GetDriverListAsyncTask getDriverListAsyncTask = new GetDriverListAsyncTask(DiverListActivity.this);
		getDriverListAsyncTask.setGetDriverListListener(new GetDriverListListener() {

			@Override
			public void getDriverResult(DriverListResultModel driverListResultModel) {
				if (driverListResultModel != null) {
					if (driverListResultModel.getResult().equals("true")) {
						if (diverListAdapter == null) {
							diverListAdapter = new DiverListAdapter(DiverListActivity.this, driverListResultModel.getData());
							driver_list.setAdapter(diverListAdapter);
						}
					} else {
						Tools.showToast(DiverListActivity.this, driverListResultModel.getDescription());
					}
				} else {
					Tools.showToast(DiverListActivity.this, "消息列表获取失败，请检查网络");
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
