package com.resmanager.client.flag;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.DriverModel;
import com.resmanager.client.model.WarseHouseModel;
import com.resmanager.client.user.order.DiverListActivity;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.warehouse.WareHouseListActivity;

@SuppressLint("InflateParams")
public class FlagSearch extends TopContainActivity implements OnClickListener {
	private EditText search_input_edit, customer_name_edit, saleman_edit;
	private TextView endTime, startTime, canku_txt, diver_name;
	private static final int START_DATE_ID = 0;// 起始时间
	private static final int END_DATE_ID = 1;// 结束时间
	private int currentYear, currentMonth, currentDay;
	private String startDate = "";
	private String endDate = "";
	private RadioGroup mRadioGroup2, mRadioGroup3, mRadioGroup4;
	private RadioButton radioButton2, radioButton22;
	private RadioButton radioButton3, radioButton33;
	private RadioButton radioButton4, radioButton44;
	private String dayType = "";
	private String warsehouseName = "";
	private String driverId = "";
	private LinearLayout saler_layout;

	@Override
	protected View getTopView() {
		View topView = inflater.inflate(R.layout.custom_title_bar, null);
		return topView;
	}

	@Override
	protected View getCenterView() {
		View cententView = inflater.inflate(R.layout.flag_search, null);
		return cententView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageView leftImg = (ImageView) topView
				.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView
				.findViewById(R.id.title_content);
		titleContent.setText("标签查询");
		centerView.findViewById(R.id.search_btn).setOnClickListener(this);
		search_input_edit = (EditText) centerView
				.findViewById(R.id.search_input_edit);
		search_input_edit
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEND
								|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
							search();
							return false;
						}
						return false;
					}
				});
		startTime = (TextView) findViewById(R.id.start_time);
		endTime = (TextView) findViewById(R.id.end_time);
		canku_txt = (TextView) findViewById(R.id.canku_txt);
		diver_name = (TextView) findViewById(R.id.diver_name);
		diver_name.setOnClickListener(this);
		canku_txt.setOnClickListener(this);
		endTime.setOnClickListener(this);
		startTime.setOnClickListener(this);
		saler_layout = (LinearLayout) findViewById(R.id.saler_layout);
		customer_name_edit = (EditText) findViewById(R.id.customer_name_edit);
		saleman_edit = (EditText) findViewById(R.id.saleman_edit);
		mRadioGroup2 = (RadioGroup) findViewById(R.id.radiogroup2);
		mRadioGroup3 = (RadioGroup) findViewById(R.id.radiogroup3);
		mRadioGroup4 = (RadioGroup) findViewById(R.id.radiogroup4);
		radioButton2 = (RadioButton) findViewById(R.id.radio2);
		radioButton22 = (RadioButton) findViewById(R.id.radio22);
		radioButton3 = (RadioButton) findViewById(R.id.radio3);
		radioButton33 = (RadioButton) findViewById(R.id.radio33);
		radioButton4 = (RadioButton) findViewById(R.id.radio4);
		radioButton44 = (RadioButton) findViewById(R.id.radio44);
		if (ContactsUtils.userDetailModel != null
				&& ContactsUtils.userDetailModel.getUserType().equals("4")) {

			saleman_edit.setText(ContactsUtils.userDetailModel.getNickName());
			// saleman_edit.setText("");
			saleman_edit.setEnabled(false);
			// saler_layout.setVisibility(View.GONE);
		} else {
			saleman_edit.setEnabled(true);
			// saler_layout.setVisibility(View.VISIBLE);
		}
		initRadioButton();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.diver_name:
			Intent driverIntent = new Intent(this, DiverListActivity.class);
			startActivityForResult(driverIntent,
					ContactsUtils.CHOOSE_DRIVER_RESULT);
			break;
		case R.id.canku_txt:
			Intent cangkuIntent = new Intent(this, WareHouseListActivity.class);
			startActivityForResult(cangkuIntent,
					ContactsUtils.CHOOSE_WARSEHOUSE_RESULT);
			break;
		case R.id.title_left_img:
			this.finish();
			break;
		case R.id.search_btn:
			search();
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

	private void search() {
		String searchStr = search_input_edit.getText().toString();
		// if (searchStr.equals("")) {
		// Tools.showToast(this, "请输入要搜索的标签号");
		// } else {
		Intent searchIntent = new Intent(this, FlagSearchResultActivity.class);
		searchIntent.putExtra("searchStr", searchStr);
		searchIntent.putExtra("startDate", startDate);
		searchIntent.putExtra("endDate", endDate);
		searchIntent.putExtra("dayType", dayType);
		searchIntent.putExtra("customrname", customer_name_edit.getText()
				.toString());
		searchIntent.putExtra("saleman", saleman_edit.getText().toString());
		searchIntent.putExtra("driverid", driverId);
		searchIntent.putExtra("warsehousename", warsehouseName);
		startActivity(searchIntent);
		// }
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
				Toast.makeText(FlagSearch.this, "起始日期不能超过当前时间",
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
			final String birthStr = year + "-" + (monthOfYear + 1) + "-"
					+ dayOfMonth;
			endTime.setText(birthStr);
			endDate = birthStr;
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			switch (resultCode) {
			case ContactsUtils.CHOOSE_WARSEHOUSE_RESULT:
				WarseHouseModel warseHouseModel = (WarseHouseModel) data
						.getExtras().getSerializable("warseHouseModel");

				warsehouseName = warseHouseModel.getWarehouseName();
				canku_txt.setText(warsehouseName);
				break;
			case ContactsUtils.CHOOSE_DRIVER_RESULT:
				DriverModel driverModel = (DriverModel) data.getExtras()
						.getSerializable("driverModel");
				driverId = driverModel.getUserId();
				diver_name.setText(driverModel.getNickName());
				break;

			default:
				break;
			}
		}

	};

	/**
	 * @Description:
	 * @version:v1.0
	 * @author:FuHuiHui
	 * @date:2016-3-30 下午3:55:18
	 */
	private void initRadioButton() {
		// 0今日1昨日2本月3上月4当年-1全部

		mRadioGroup2
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == radioButton2.getId()) {
							if (radioButton2.isChecked()) {
								// search_day.setText("本月");
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
								radioButton2.setChecked(false);
								radioButton22.setChecked(false);
								radioButton3.setChecked(false);
								radioButton33.setChecked(false);
								radioButton4.setChecked(false);
								dayType = "";
							}
						}
					}
				});

	}
}
