/**   
 * @Title: MessageDetail.java 
 * @Package com.resmanager.client.user.message 
 * @Description: 消息详情
 * @author ShenYang  
 * @date 2016-1-18 上午10:37:55 
 * @version V1.0   
 */
package com.resmanager.client.user.message;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.MessageModel;

/**
 * @ClassName: MessageDetail
 * @Description: 消息详情
 * @author ShenYang
 * @date 2016-1-18 上午10:37:55
 * 
 */
@SuppressLint("InflateParams")
public class MessageDetail extends TopContainActivity implements OnClickListener {
	/*
	 * (非 Javadoc) <p>Title: onClick</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	private MessageModel messageModel;
	private TextView title_txt, content_txt, author_txt, date_txt;

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
		return inflater.inflate(R.layout.message_detail, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		messageModel = (MessageModel) getIntent().getExtras().getSerializable("messageModel");
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("消息详情");
		title_txt = (TextView) centerView.findViewById(R.id.title_txt);
		content_txt = (TextView) centerView.findViewById(R.id.content_txt);
		author_txt = (TextView) centerView.findViewById(R.id.author_txt);
		date_txt = (TextView) centerView.findViewById(R.id.date_txt);
		title_txt.setText(messageModel.getInstationTitle());
		content_txt.setText(messageModel.getContents());
		author_txt.setText("宁盈物流");
		date_txt.setText(messageModel.getSenderTime());
		UpdateMailRead();
	}

	/**
	 * 
	 * @Title: UpdateMailRead
	 * @Description: 更改消息阅读状态
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void UpdateMailRead() {
		UpdateMailReadAsyncTask updateMailReadAsyncTask = new UpdateMailReadAsyncTask(messageModel.getReceiverId());
		updateMailReadAsyncTask.execute();
	}
}
