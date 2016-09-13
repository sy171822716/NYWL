/**   
 * @Title: FlagManager.java 
 * @Package com.resmanager.client.flag 
 * @Description:标签管理
 * @author ShenYang  
 * @date 2015-12-4 上午9:40:24 
 * @version V1.0   
 */
package com.resmanager.client.flag;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.flag.FlagUnLockAsyncTask.FlagUnLockListener;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.scan.CaptureActivity;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.DESUtils;
import com.resmanager.client.utils.Tools;

/**
 * @ClassName: FlagManager
 * @Description: 标签管理
 * @author ShenYang
 * @date 2015-12-4 上午9:40:24
 * 
 */
@SuppressLint("InflateParams")
public class FlagManager extends TopContainActivity implements OnClickListener {

	private TextView flag_lock_txt, flag_unlock_txt;
	private LinearLayout flag_Layout;
	private LayoutInflater mInflater;

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
		case R.id.flag_lock_txt:
			Intent lockIntent = new Intent(FlagManager.this, CaptureActivity.class);
			lockIntent.putExtra("flagType", ContactsUtils.FLAG_LOCK_RESULTCODE);
			startActivityForResult(lockIntent, ContactsUtils.SCAN_RESULT);
			break;
		case R.id.flag_unlock_txt:
			Intent unlockIntent = new Intent(FlagManager.this, CaptureActivity.class);
			unlockIntent.putExtra("flagType", ContactsUtils.FLAG_UNLOCK_RESULTCODE);
			startActivityForResult(unlockIntent, ContactsUtils.SCAN_RESULT);
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
		titleContent.setText("标签管理");
		return topView;
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
		View contentView = inflater.inflate(R.layout.flag_manager, null);
		flag_lock_txt = (TextView) contentView.findViewById(R.id.flag_lock_txt);
		flag_unlock_txt = (TextView) contentView.findViewById(R.id.flag_unlock_txt);
		flag_lock_txt.setOnClickListener(this);
		flag_unlock_txt.setOnClickListener(this);
		flag_Layout = (LinearLayout) contentView.findViewById(R.id.flags_layout);
		return contentView;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == ContactsUtils.SCAN_RESULT) {
			String flagContent = data.getExtras().getString("result");
			int flagType = data.getExtras().getInt("flagType");
			switch (flagType) {
			case ContactsUtils.SCAN_RESULT:
				// 上锁结果
				orpFlag(DESUtils.decrypt(flagContent), ContactsUtils.FLAG_LOCK_RESULTCODE);
				break;
			case ContactsUtils.FLAG_UNLOCK_RESULTCODE:
				// 解锁结果
				orpFlag(DESUtils.decrypt(flagContent), ContactsUtils.FLAG_UNLOCK_RESULTCODE);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 
	 * @Title: orpFlag
	 * @Description: 标签操作
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	private void orpFlag(String flagContent, int orpType) {
		FlagUnLockAsyncTask flagUnLockAsyncTask = new FlagUnLockAsyncTask(FlagManager.this, ContactsUtils.USER_KEY, flagContent, orpType);
		flagUnLockAsyncTask.setFlagUnLockListener(new FlagUnLockListener() {

			@Override
			public void flagUnLockResult(ResultModel resultModel, String flagStr) {
				if (resultModel != null) {
					if (resultModel.getResult().equals("true")) {
						addFlag(flagStr, ContactsUtils.FLAG_UNLOCK_RESULTCODE);
					} else {
						Tools.showToast(FlagManager.this, resultModel.getDescription());
					}
				} else {
					Tools.showToast(FlagManager.this, "标签解锁失败，请检查网络");
				}
			}
		});
		flagUnLockAsyncTask.execute();
	}

	/**
	 * 
	 * @Title: addFlag
	 * @Description: 添加标签
	 * @param @param flagContent
	 * @param @param flagStatus 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void addFlag(String flagContent, int flagStatus) {
		if (mInflater == null) {
			mInflater = LayoutInflater.from(this);
		}
		View flagView = mInflater.inflate(R.layout.flag_list_item, null);
		TextView flag_content_txt = (TextView) flagView.findViewById(R.id.flag_content_txt);
		TextView flag_status_txt = (TextView) flagView.findViewById(R.id.flag_status_txt);
		flag_content_txt.setText(flagContent.substring(5,flagContent.lastIndexOf("-")));
		switch (flagStatus) {
		case ContactsUtils.FLAG_LOCK_RESULTCODE:
			flag_status_txt.setText("已锁定");
			flag_status_txt.setTextColor(getResources().getColor(R.color.flag_lock_color));
			break;
		case ContactsUtils.FLAG_UNLOCK_RESULTCODE:
			flag_status_txt.setText("已解锁");
			flag_status_txt.setTextColor(getResources().getColor(R.color.flag_unlock_color));
			break;
		default:
			break;
		}
		flag_Layout.addView(flagView, 0);
	}

}
