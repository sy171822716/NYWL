/**
 * @Description:订单搜索主界面
 * @version:v1.0
 * @author:FuHuiHui
 * @date:2016-3-30 上午10:45:20
 */
package com.resmanager.client.user.order;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.resmanager.client.R;
import com.resmanager.client.model.DriverModel;
import com.resmanager.client.order.OrderMainNewActivity;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.CustomDialog;
import com.resmanager.client.view.CustomDialog.ToDoListener;

public class SearchOrderActivity extends Activity implements OnClickListener {

	private EditText customer_name, order_code, belong_area, saler_code;
	private TextView endTime, startTime;
	private TextView diver_name;
	private Button searchBtn, reset_btn;
	private String customerNameStr = "";
	private String orderCodeStr = "";
	private String belongAreaStr = "";
	private String daysStr = "";
	private String dayType = "-1";
	private RadioGroup mRadioGroup1, mRadioGroup2, mRadioGroup3, mRadioGroup4,
			packagetype_rg;
	private RadioButton radioButton1, radioButton11;
	private RadioButton radioButton2, radioButton22;
	private RadioButton radioButton3, radioButton33;
	private RadioButton radioButton4, radioButton44;
	private RadioButton youtong_radio, youguan_radio, all_radio;
	private String userID = "";
	private CustomDialog cm;
	private DatePickerDialog datePickerDialog = null;
	private static final int START_DATE_ID = 0;// 起始时间
	private static final int END_DATE_ID = 1;// 结束时间
	private int currentYear, currentMonth, currentDay;
	private String startDate = "";
	private String endDate = "";
	private final Calendar c = Calendar.getInstance();
	private String packType = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_order_main);
		initView();
	}

	/**
	 * @Description:初始化界面信息
	 * @version:v1.0
	 * @author:FuHuiHui
	 * @date:2016-3-30 下午12:23:08
	 */
	private void initView() {
		TextView titleContent = (TextView) findViewById(R.id.title_content);
		titleContent.setText("订单查询");
		findViewById(R.id.title_left_img).setOnClickListener(this);
		searchBtn = (Button) findViewById(R.id.search_btn);
		searchBtn.setOnClickListener(this);
		customer_name = (EditText) findViewById(R.id.customer_name);
		order_code = (EditText) findViewById(R.id.order_code);
		diver_name = (TextView) findViewById(R.id.diver_name);
		belong_area = (EditText) findViewById(R.id.belong_area);
		saler_code = (EditText) findViewById(R.id.saler_code);
		startTime = (TextView) findViewById(R.id.start_time);
		endTime = (TextView) findViewById(R.id.end_time);
		diver_name.setOnClickListener(this);
		endTime.setOnClickListener(this);
		startTime.setOnClickListener(this);

		mRadioGroup1 = (RadioGroup) findViewById(R.id.radiogroup1);
		mRadioGroup2 = (RadioGroup) findViewById(R.id.radiogroup2);
		mRadioGroup3 = (RadioGroup) findViewById(R.id.radiogroup3);
		mRadioGroup4 = (RadioGroup) findViewById(R.id.radiogroup4);
		packagetype_rg = (RadioGroup) findViewById(R.id.packagetype_rg);
		radioButton1 = (RadioButton) findViewById(R.id.radio1);
		radioButton11 = (RadioButton) findViewById(R.id.radio11);
		radioButton2 = (RadioButton) findViewById(R.id.radio2);
		radioButton22 = (RadioButton) findViewById(R.id.radio22);
		radioButton3 = (RadioButton) findViewById(R.id.radio3);
		radioButton33 = (RadioButton) findViewById(R.id.radio33);
		radioButton4 = (RadioButton) findViewById(R.id.radio4);
		radioButton44 = (RadioButton) findViewById(R.id.radio44);
		reset_btn = (Button) findViewById(R.id.reset_btn);
		youtong_radio = (RadioButton) findViewById(R.id.youtong_radio);
		youguan_radio = (RadioButton) findViewById(R.id.youguan_radio);
		all_radio = (RadioButton) findViewById(R.id.all_radio);

		reset_btn.setOnClickListener(this);
		initRadioButton();

		// 只有当用户为2的时候才能查询其他驾驶员订单
		if (ContactsUtils.userDetailModel.getUserType().equals("2")
				|| ContactsUtils.userDetailModel.getUserType().equals("0")
				|| ContactsUtils.userDetailModel.getUserType().equals("4")) {
			diver_name.setText("请选择驾驶员");
		} else {
			userID = String.valueOf(ContactsUtils.userDetailModel.getUserId());// 设置为当前登录用户ID
			diver_name.setText(ContactsUtils.userDetailModel.getNickName());
		}

		if (ContactsUtils.userDetailModel.getUserType().equals("4")) {
			saler_code.setText(ContactsUtils.userDetailModel.getNickName());
			saler_code.setEnabled(false);
		}

	}

	/**
	 * @Description:
	 * @version:v1.0
	 * @author:FuHuiHui
	 * @date:2016-3-30 下午3:55:18
	 */
	private void initRadioButton() {
		// 0今日1昨日2本月3上月4当年-1全部
		mRadioGroup1
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == radioButton1.getId()) {
							if (radioButton1.isChecked()) {
								// search_day.setText("今日");
								radioButton11.setChecked(false);
								radioButton2.setChecked(false);
								radioButton22.setChecked(false);
								radioButton3.setChecked(false);
								radioButton33.setChecked(false);
								radioButton4.setChecked(false);
								radioButton44.setChecked(false);
								dayType = "0";
							}
						}
						if (checkedId == radioButton11.getId()) {
							if (radioButton11.isChecked()) {
								// search_day.setText("昨日");
								radioButton1.setChecked(false);
								radioButton2.setChecked(false);
								radioButton22.setChecked(false);
								radioButton3.setChecked(false);
								radioButton33.setChecked(false);
								radioButton4.setChecked(false);
								radioButton44.setChecked(false);
								dayType = "1";
							}
						}

					}
				});
		mRadioGroup2
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == radioButton2.getId()) {
							if (radioButton2.isChecked()) {
								// search_day.setText("本月");
								radioButton1.setChecked(false);
								radioButton11.setChecked(false);
								radioButton22.setChecked(false);
								radioButton3.setChecked(false);
								radioButton33.setChecked(false);
								radioButton4.setChecked(false);
								radioButton44.setChecked(false);
								dayType = "2";
							}
						}
						if (checkedId == radioButton22.getId()) {
							if (radioButton22.isChecked()) {
								// search_day.setText("上月");
								radioButton1.setChecked(false);
								radioButton11.setChecked(false);
								radioButton2.setChecked(false);
								radioButton3.setChecked(false);
								radioButton33.setChecked(false);
								radioButton4.setChecked(false);
								radioButton44.setChecked(false);
								dayType = "3";
							}
						}
					}
				});

		mRadioGroup3
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == radioButton3.getId()) {
							if (radioButton3.isChecked()) {
								// search_day.setText("本周");
								radioButton1.setChecked(false);
								radioButton11.setChecked(false);
								radioButton22.setChecked(false);
								radioButton2.setChecked(false);
								radioButton33.setChecked(false);
								radioButton4.setChecked(false);
								radioButton44.setChecked(false);
								dayType = "5";
							}
						}
						if (checkedId == radioButton33.getId()) {
							if (radioButton33.isChecked()) {
								// search_day.setText("上周");
								radioButton1.setChecked(false);
								radioButton11.setChecked(false);
								radioButton2.setChecked(false);
								radioButton3.setChecked(false);
								radioButton22.setChecked(false);
								radioButton4.setChecked(false);
								radioButton44.setChecked(false);
								dayType = "6";
							}
						}
					}
				});

		mRadioGroup4
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						if (checkedId == radioButton4.getId()) {
							if (radioButton4.isChecked()) {
								// search_day.setText("当年");
								radioButton1.setChecked(false);
								radioButton11.setChecked(false);
								radioButton2.setChecked(false);
								radioButton22.setChecked(false);
								radioButton3.setChecked(false);
								radioButton33.setChecked(false);
								radioButton44.setChecked(false);
								dayType = "4";
							}
						}
						if (checkedId == radioButton44.getId()) {
							if (radioButton44.isChecked()) {
								// search_day.setText("全部");
								radioButton1.setChecked(false);
								radioButton11.setChecked(false);
								radioButton2.setChecked(false);
								radioButton22.setChecked(false);
								radioButton3.setChecked(false);
								radioButton33.setChecked(false);
								radioButton4.setChecked(false);
								dayType = "-1";
							}
						}
					}
				});

		packagetype_rg
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						if (checkedId == youtong_radio.getId()) {
							packType = "0";
						} else if (checkedId == youguan_radio.getId()) {
							packType = "1";
						} else {
							packType = "";
						}
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case ContactsUtils.CHOOSE_DRIVER_RESULT:
			DriverModel driverModel = (DriverModel) data
					.getSerializableExtra("driverModel");
			diver_name.setText(driverModel.getNickName());
			userID = driverModel.getUserId();
			break;

		default:
			break;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_btn:
			if (!daysStr.equals("")) {
				dayType = "-1";
			}
			// 0普通用户,1驾驶员,2管理员如果当前登录用户是驾驶员,把待分配,待过滤,全部订单隐藏掉待运送可以查看,但是顶部的操作按钮要隐藏掉不能让驾驶员操作
			customerNameStr = customer_name.getText().toString();
			orderCodeStr = order_code.getText().toString();
			belongAreaStr = belong_area.getText().toString();
			daysStr = startTime.getText().toString();
			Intent myOrderIntent = new Intent(SearchOrderActivity.this,
					OrderMainNewActivity.class);
			myOrderIntent.putExtra("UserType",
					ContactsUtils.userDetailModel.getUserType());
			myOrderIntent.putExtra("DayType", dayType);
			myOrderIntent.putExtra("Days", "");
			myOrderIntent.putExtra("Town", belongAreaStr);
			myOrderIntent.putExtra("Saleoid", orderCodeStr);// 订单号
			myOrderIntent.putExtra("ordercustomer", customerNameStr);// 订单号
			myOrderIntent.putExtra("userId", userID);// 用户ID
			myOrderIntent.putExtra("startDate", startDate);// 开始日期
			myOrderIntent.putExtra("endDate", endDate);// 结束日期
			myOrderIntent.putExtra("Packtype", packType);// 包装物类型
			myOrderIntent
					.putExtra("salername", saler_code.getText().toString());// 包装物类型
			setResult(ContactsUtils.SEARCH_ORDER_RESULT, myOrderIntent);
			finish();
			break;
		case R.id.diver_name:
			if (ContactsUtils.userDetailModel.getUserType().equals("2")
					|| ContactsUtils.userDetailModel.getUserType().equals("0")
					|| ContactsUtils.userDetailModel.getUserType().equals("4")) {
				Intent dirverIntent = new Intent(SearchOrderActivity.this,
						DiverListActivity.class);
				startActivityForResult(dirverIntent,
						ContactsUtils.CHOOSE_DRIVER_RESULT);
			} else {
				Tools.showToast(SearchOrderActivity.this, "只可查询当前登录用户的订单");
			}
			break;
		case R.id.title_left_img:
			SearchOrderActivity.this.finish();
			break;
		case R.id.reset_btn:
			resetConfirmDialog();
			break;
		case R.id.start_time:
			showDialog(START_DATE_ID);
			break;
		case R.id.end_time:
			showDialog(END_DATE_ID);
			break;
		default:
			break;
		}

	}

	/**
	 * 
	 * @Description:弹出选择日期对话框
	 * @version:v1.0
	 * @author:FuHuiHui
	 * @date:2014-11-19 下午3:43:41
	 */
	public void showDatePicker1() {
		datePickerDialog.show();
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case START_DATE_ID:
			Calendar c = Calendar.getInstance();
			currentYear = c.get(Calendar.YEAR);// 2015
			currentMonth = c.get(Calendar.MONTH);// 3
			currentDay = c.get(Calendar.DAY_OF_MONTH);// 28
			return new DatePickerDialog(this, onDateSetListener, currentYear,
					currentMonth, currentDay);
		case END_DATE_ID:
			Calendar c1 = Calendar.getInstance();
			currentYear = c1.get(Calendar.YEAR);// 2015
			currentMonth = c1.get(Calendar.MONTH);// 3
			currentDay = c1.get(Calendar.DAY_OF_MONTH);// 28
			return new DatePickerDialog(this, onDateSetListener1, currentYear,
					currentMonth, currentDay);
		}
		return super.onCreateDialog(id);
	}

	DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			boolean isOk = true;
			if (year > currentYear) {
				isOk = false;
			} else if (year == currentYear && monthOfYear > currentMonth) {
				isOk = false;
			} else if (year == currentYear && monthOfYear == currentMonth
					&& dayOfMonth > currentDay) {
				isOk = false;
			}
			if (isOk == false) {
				Toast.makeText(SearchOrderActivity.this, "起始日期不能超过当前时间",
						Toast.LENGTH_SHORT).show();
			} else {
				final String birthStr = year + "-" + (monthOfYear + 1) + "-"
						+ dayOfMonth;
				startTime.setText(birthStr);
				startDate = birthStr;
			}
		}

	};

	DatePickerDialog.OnDateSetListener onDateSetListener1 = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// boolean isOk = true;
			// if (year > currentYear) {
			// isOk = false;
			// } else if (year == currentYear && monthOfYear > currentMonth) {
			// isOk = false;
			// } else if (year == currentYear && monthOfYear == currentMonth &&
			// dayOfMonth > currentDay) {
			// isOk = false;
			// }
			// if (isOk == false) {
			// Toast.makeText(SearchOrderActivity.this, "结束日期不能超过当前时间",
			// Toast.LENGTH_SHORT).show();
			// } else {
			// final String birthStr = year + "-" + (monthOfYear + 1) + "-" +
			// dayOfMonth;
			// // int current = c.get(dayOfMonth);
			// // c.set(Calendar.DAY_OF_MONTH, current + 3);
			// // current = c.get(Calendar.DAY_OF_MONTH);
			// endTime.setText(birthStr);
			// }
			final String birthStr = year + "-" + (monthOfYear + 1) + "-"
					+ dayOfMonth;
			endTime.setText(birthStr);
			endDate = birthStr;
		}

	};

	/**
	 * 
	 * @Title: resetConfirmDialog
	 * @Description: 确认重置
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void resetConfirmDialog() {
		if (cm == null) {
			cm = new CustomDialog(this, R.style.myDialogTheme);
			cm.setContentText("确认清空所有查询条件？");
			cm.setToDoListener(new ToDoListener() {

				@Override
				public void doSomething() {
					reset();
				}
			});
		}
		cm.show();
	}

	/**
	 * 
	 * @Title: reset
	 * @Description:清空
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void reset() {
		customer_name.setText("");
		order_code.setText("");
		belong_area.setText("");
		startTime.setText("");
		endTime.setText("");
		// 只有当用户为2的时候才能查询其他驾驶员订单
		if (ContactsUtils.userDetailModel.getUserType().equals("2")
				|| ContactsUtils.userDetailModel.getUserType().equals("0")) {
			userID = "";
			diver_name.setText("请选择驾驶员");
		} else {
			userID = String.valueOf(ContactsUtils.userDetailModel.getUserId());// 设置为当前登录用户ID
			diver_name.setText(ContactsUtils.userDetailModel.getNickName());
		}
		radioButton1.setChecked(false);
		radioButton11.setChecked(false);
		radioButton2.setChecked(false);
		radioButton22.setChecked(false);
		radioButton3.setChecked(false);
		radioButton33.setChecked(false);
		radioButton3.setChecked(false);
		radioButton33.setChecked(false);
		startTime.setText("请选择要查询的开始时间");
		endTime.setText("请选择要查询的结束时间");
		startDate = "";
		endDate = "";
		youtong_radio.setChecked(false);
		youguan_radio.setChecked(false);
		all_radio.setChecked(true);
		packType = "";
		customerNameStr = "";
		orderCodeStr = "";
		belongAreaStr = "";
		daysStr = "";
		dayType = "-1";

	}
}
