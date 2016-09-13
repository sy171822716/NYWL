/**   
 * @Title: ChooseDriverActivity.java 
 * @Package com.resmanager.client.order 
 * @Description: 选择驾驶员
 * @author ShenYang  
 * @date 2016-2-25 下午3:50:54 
 * @version V1.0   
 */
package com.resmanager.client.order;

import android.annotation.SuppressLint;
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
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.order.GetDriverListAsyncTask.GetDriverListListener;
import com.resmanager.client.order.OrderFPAsyncTask.OrderSendListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.CustomDialog;
import com.resmanager.client.view.CustomDialog.ToDoListener;

/**
 * @ClassName: ChooseDriverActivity
 * @Description: 选择驾驶员
 * @author ShenYang
 * @date 2016-2-25 下午3:50:54
 * 
 */
@SuppressLint("InflateParams")
public class ChooseDriverActivity extends TopContainActivity implements OnClickListener {
	private ListView driver_list;
	private CustomDialog noticeDialog;
	private String orderIds;

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
		orderIds = getIntent().getExtras().getString("orderIds");
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
			showNoticeDialog(driverModel);
		}
	};

	/**
	 * 
	 * @Title: showNoticeDialog
	 * @Description:弹出提示框
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void showNoticeDialog(final DriverModel driverModel) {
		if (noticeDialog == null) {
			noticeDialog = new CustomDialog(this, R.style.myDialogTheme);
		}
		noticeDialog.setContentText("是否确定分配?");
		noticeDialog.setToDoListener(new ToDoListener() {

			@Override
			public void doSomething() {
				OrderFPAsyncTask orderFPAsyncTask = new OrderFPAsyncTask(ChooseDriverActivity.this, orderIds, driverModel);
				orderFPAsyncTask.setOrderSendListener(new OrderSendListener() {

					@Override
					public void orderSendResult(ResultModel resultModel) {
						if (resultModel != null) {
							if (resultModel.getResult().equals("true")) {
								Tools.showToast(ChooseDriverActivity.this, "分配成功");
								setResult(ContactsUtils.CHOOSE_DRIVER_RESULT);
								ChooseDriverActivity.this.finish();
							} else {
								Tools.showToast(ChooseDriverActivity.this, resultModel.getDescription());
							}
						} else {
							Tools.showToast(ChooseDriverActivity.this, "分配失败，请检查网络");
						}
					}
				});
				orderFPAsyncTask.execute();
			}
		});
		this.noticeDialog.show();
	}

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
							DriverListAdapter driverListAdapter = new DriverListAdapter(ChooseDriverActivity.this, driverListResultModel.getData());
							driver_list.setAdapter(driverListAdapter);
						} else {
							Tools.showToast(ChooseDriverActivity.this, "驾驶员列表获取失败,请重试");
						}
					} else {
						Tools.showToast(ChooseDriverActivity.this, driverListResultModel.getDescription());
					}
				} else {
					Tools.showToast(ChooseDriverActivity.this, "驾驶员列表获取失败，请检查网络");
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
