package com.resmanager.client.home;

/**
 *
 * @className:com.xtwl.jy.base.view.ExitDialog
 * @description:更新提示对话框
 * 
 * @version:v1.0.0 
 * @author:ShenYang
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2014-11-7     ShenYang       v1.0.0        create
 *
 *
 */

import com.resmanager.client.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class UpdateNoticeDialog extends Dialog implements View.OnClickListener {
	private View dialogView;
	private TextView contentView, this_version;// 内容View
	private ToDoListener toDoListener;
	private CancelBtnListener cancelBtnListener;
	private LayoutInflater mInflater;
	private String userKey;// 对象key
	private String objectKey;
	private int pos;
	private String url;
	private String phoneNumber;
	private String isForce = "1";// 0：不强制更新，1：强制更新
	private Button ok_btn, cancelButton;

	@SuppressWarnings("deprecation")
	public UpdateNoticeDialog(Context context, int theme) {
		super(context, theme);
		mInflater = LayoutInflater.from(context);
		dialogView = mInflater.inflate(R.layout.update_notice_dialog_layout, null);
		setContentView(dialogView);
		WindowManager m = this.getWindow().getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = this.getWindow().getAttributes(); // 获取对话框当前的参数值
		p.height = LayoutParams.WRAP_CONTENT; // 高度设置为屏幕的0.6
		p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.65
		this.getWindow().setAttributes(p);
		initView();
	}

	/**
	 * 
	 * @Description:初始化View
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-11-7 下午2:51:17
	 */
	private void initView() {
		contentView = (TextView) dialogView.findViewById(R.id.dialog_content_text);
		this_version = (TextView) dialogView.findViewById(R.id.this_version);
		ok_btn = (Button) dialogView.findViewById(R.id.ok_btn);
		ok_btn.setOnClickListener(this);
		cancelButton = (Button) dialogView.findViewById(R.id.cancel_btn);
		cancelButton.setOnClickListener(this);
	}

	/**
	 * 
	 * @Description:设置按钮文本
	 * @param okText
	 * @param cancelText
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2015-5-13 下午3:48:38
	 */
	public void setOkCancelBtnText(String okText, String cancelText) {
		ok_btn.setText(okText);

		cancelButton.setText(cancelText);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_btn:
			this.dismiss();
			if (cancelBtnListener != null) {
				cancelBtnListener.cancel();
			}
			break;
		case R.id.ok_btn:
			if (toDoListener != null) {
				toDoListener.doSomething();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @Description:获取监听
	 * @return
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-11-7 下午2:54:35
	 */
	public ToDoListener getToDoListener() {
		return toDoListener;
	}

	/**
	 * 
	 * @Description:设置监听
	 * @param toDoListener
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-11-7 下午2:54:54
	 */
	public void setToDoListener(ToDoListener toDoListener) {
		this.toDoListener = toDoListener;
	}

	/**
	 * 
	 * @Description:设置内容
	 * @param contentView
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-11-7 下午2:56:23
	 */
	public void setContentText(String content, String currentVersion, String isForce) {
		this.contentView.setText(Html.fromHtml(content));
		this.this_version.setText("最新版本v" + currentVersion);
		this.setIsForce(isForce);
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public String getObjectKey() {
		return objectKey;
	}

	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}

	public CancelBtnListener getCancelBtnListener() {
		return cancelBtnListener;
	}

	public void setCancelBtnListener(CancelBtnListener cancelBtnListener) {
		this.cancelBtnListener = cancelBtnListener;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIsForce() {
		return isForce;
	}

	public void setIsForce(String isForce) {
		this.isForce = isForce;
	}

	/**
	 * 
	 * @className:com.xtwl.hz.client.common.view.ToDoListener
	 * @description:点击OK监听
	 * @version:v1.0.0
	 * @date:2014-11-7 下午2:53:45
	 * @author:ShenYang
	 */
	public interface ToDoListener {
		public void doSomething();

	}

	public interface CancelBtnListener {
		public void cancel();
	}
}
