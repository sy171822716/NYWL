/**
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
package com.resmanager.client.view;

import com.resmanager.client.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class CustomDialog extends Dialog implements View.OnClickListener {
	private View dialogView;
	private TextView contentView;// 内容View
	private ToDoListener toDoListener;
	private CancelBtnListener cancelBtnListener;
	private LayoutInflater mInflater;
	private String userKey;// 对象key
	private String objectKey;
	private int pos;
	private String url;
	private String phoneNumber;
	private Button ok_btn, cancelButton;

	public CustomDialog(Context context, int theme) {
		super(context, theme);
		mInflater = LayoutInflater.from(context);
		dialogView = mInflater.inflate(R.layout.exit_dialog_layout, null);
		setContentView(dialogView);
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
			if (this.isShowing()) {
				this.dismiss();
			}
			if (cancelBtnListener != null) {
				cancelBtnListener.cancel();
			}
			break;
		case R.id.ok_btn:
			if (this.isShowing()) {
				this.dismiss();
			}
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
	/**
	 * @Title: setContentText
	 * @Description: TODO
	 * @param @param content
	 * @param @param isCenter 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void setContentText(String content) {
		this.contentView.setText(Html.fromHtml(content));
	}

	public void setContentText(String content, boolean isCenter) {
		if (isCenter) {
			this.contentView.setGravity(Gravity.CENTER);
		} else {
			this.contentView.setGravity(Gravity.CENTER_VERTICAL);
		}
		this.contentView.setText(Html.fromHtml(content));
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

	/**
	 * 
	 * @className:com.xtwl.jy.client.common.view.ToDoListener
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
