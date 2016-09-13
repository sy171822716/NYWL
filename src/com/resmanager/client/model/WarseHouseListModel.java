package com.resmanager.client.model;

import java.util.ArrayList;

public class WarseHouseListModel extends ResultModel {

	/**
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<WarseHouseModel> data;

	public ArrayList<WarseHouseModel> getData() {
		return data;
	}

	public void setData(ArrayList<WarseHouseModel> data) {
		this.data = data;
	}

}
