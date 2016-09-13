package com.resmanager.client.model;

import java.util.ArrayList;

public class MessageListModel extends ResultModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<MessageModel> data;

	public ArrayList<MessageModel> getData() {
		return data;
	}

	public void setData(ArrayList<MessageModel> data) {
		this.data = data;
	}
}
