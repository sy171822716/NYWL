/**   
 * @Title: MessageList.java 
 * @Package com.resmanager.client.user.message 
 * @Description: 消息列表
 * @author ShenYang  
 * @date 2016-1-18 上午9:44:21 
 * @version V1.0   
 */
package com.resmanager.client.user.message;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.MessageListModel;
import com.resmanager.client.model.MessageModel;
import com.resmanager.client.user.message.GetMessageListAsyncTask.GetMessageListListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.PullToRefreshLayout;
import com.resmanager.client.view.PullToRefreshLayout.OnRefreshListener;
import com.resmanager.client.view.PullableListView;

/**
 * @ClassName: MessageList
 * @Description: 消息列表
 * @author ShenYang
 * @date 2016-1-18 上午9:44:21
 * 
 */
@SuppressLint("InflateParams")
public class MessageList extends TopContainActivity implements OnClickListener, OnRefreshListener {
	private PullableListView orderList;
	private PullToRefreshLayout refreshView;
	private int currentPage = 1;// 当前页
	private MessageListAdapter messageListAdapter;

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

	@Override
	protected View getCenterView() {
		return inflater.inflate(R.layout.order_list, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("我的消息");
		orderList = (PullableListView) centerView.findViewById(R.id.order_list);
		refreshView = (PullToRefreshLayout) centerView.findViewById(R.id.refresh_view);
		refreshView.setOnRefreshListener(this);
		orderList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
				MessageModel messageModel = (MessageModel) v.getAdapter().getItem(pos);
				Intent msgIntent = new Intent(MessageList.this, MessageDetail.class);
				msgIntent.putExtra("messageModel", messageModel);
				startActivity(msgIntent);
			}
		});
		getMessageList(true, ContactsUtils.ORP_NONE);
	}

	/**
	 * 
	 * @Title: getMessageList
	 * @Description: 获取消息列表
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getMessageList(boolean isRefresh, int orpType) {
		if (isRefresh) {
			this.currentPage = 1;
			messageListAdapter = null;
		}
		GetMessageListAsyncTask getMessageListAsyncTask = new GetMessageListAsyncTask(this, currentPage, orpType);
		getMessageListAsyncTask.setGetMessageListListener(new GetMessageListListener() {

			@Override
			public void getMessageResult(MessageListModel messageListModel, int orpType) {
				if (orpType == ContactsUtils.ORP_REFRESH) {
					refreshView.refreshFinish(PullToRefreshLayout.SUCCEED);
				} else if (orpType == ContactsUtils.ORP_LOAD) {
					refreshView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
				if (messageListModel != null) {
					if (messageListModel.getResult().equals("true")) {
						currentPage += 1;
						if (messageListAdapter == null) {
							messageListAdapter = new MessageListAdapter(MessageList.this, messageListModel.getData());
							orderList.setAdapter(messageListAdapter);
						} else {
							messageListAdapter.loadMore(messageListModel.getData());
						}
					} else {
						Tools.showToast(MessageList.this, messageListModel.getDescription());
					}
				} else {
					Tools.showToast(MessageList.this, "消息列表获取失败，请检查网络");
				}
			}
		});
		getMessageListAsyncTask.execute();
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		getMessageList(true, ContactsUtils.ORP_REFRESH);
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		getMessageList(false, ContactsUtils.ORP_LOAD);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		getMessageList(true, ContactsUtils.ORP_NONE);
	}
}
