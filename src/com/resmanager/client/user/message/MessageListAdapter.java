package com.resmanager.client.user.message;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.model.MessageModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class MessageListAdapter extends BaseAdapter {
	private ArrayList<MessageModel> messageModels = null;
	private Context context = null;

	@SuppressLint("UseSparseArrays")
	public MessageListAdapter(Context context, ArrayList<MessageModel> messageModels) {
		this.context = context;
		this.messageModels = messageModels;
	}

	/**
	 * 
	 * @Title: loadMore
	 * @Description: 加载更多
	 * @param @param userSysMessageModels 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void loadMore(ArrayList<MessageModel> userSysMessageModels) {
		for (MessageModel userSysMessageModel : userSysMessageModels) {
			this.messageModels.add(userSysMessageModel);
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return messageModels.size();
	}

	@Override
	public Object getItem(int position) {
		return messageModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.message_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title_txt);
			holder.date = (TextView) convertView.findViewById(R.id.time_txt);
			holder.content = (TextView) convertView.findViewById(R.id.content_txt);
			holder.deleteIcon = (TextView) convertView.findViewById(R.id.delete_icon);
			holder.isLookUp = (TextView) convertView.findViewById(R.id.is_look);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.deleteIcon.setVisibility(View.GONE);
		MessageModel messageModel = messageModels.get(position);
		String titleStr = messageModel.getInstationTitle();
		String date = messageModel.getSenderTime();
		String msgDes = messageModel.getContents();
		String isLook = messageModel.getUnread();
		holder.title.setText(titleStr);
		holder.date.setText(date.substring(0, 16));
		holder.content.setText(msgDes);
		holder.deleteIcon.setTag(messageModel);
		if (isLook.equals("0")) {
			// 未读
			holder.isLookUp.setVisibility(View.VISIBLE);
		} else {
			// 已读
			holder.isLookUp.setVisibility(View.GONE);
		}
		return convertView;
	}

	/**
	 * 
	 * @className:com.xtwl.jy.client.activity.mainpage.user.adapter.ViewHolder
	 * @description: item自定义View
	 * @version:v1.0.0
	 * @date:2014-11-3 下午7:12:38
	 * @author:FuHuiHui
	 */
	final class ViewHolder {
		private TextView title;
		private TextView date;
		private TextView content;
		private TextView deleteIcon;
		private TextView isLookUp;

	}
}
