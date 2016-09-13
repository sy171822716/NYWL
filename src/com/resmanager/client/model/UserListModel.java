package com.resmanager.client.model;

import java.util.ArrayList;

public class UserListModel extends ResultModel {

	private static final long serialVersionUID = 1L;

	private ArrayList<UserModel> data;

	public ArrayList<UserModel> getData() {
		return data;
	}

	public void setData(ArrayList<UserModel> data) {
		this.data = data;
	}

}
