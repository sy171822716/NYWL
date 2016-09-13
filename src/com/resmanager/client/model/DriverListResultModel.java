package com.resmanager.client.model;

import java.util.ArrayList;

public class DriverListResultModel extends ResultModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<DriverModel> data;

	public ArrayList<DriverModel> getData() {
		return data;
	}

	public void setData(ArrayList<DriverModel> data) {
		this.data = data;
	}

}
