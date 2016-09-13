package com.resmanager.client.model;

import java.util.ArrayList;

public class UserLocationListModel extends ResultModel {

	private static final long serialVersionUID = 1L;

	private ArrayList<UserLocationModel> data;

	public ArrayList<UserLocationModel> getData() {
		return data;
	}

	public void setData(ArrayList<UserLocationModel> data) {
		this.data = data;
	}

}
