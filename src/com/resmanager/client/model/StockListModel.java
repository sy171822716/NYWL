package com.resmanager.client.model;

import java.util.ArrayList;

public class StockListModel extends ResultModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<StockModel> data;

	public ArrayList<StockModel> getData() {
		return data;
	}

	public void setData(ArrayList<StockModel> data) {
		this.data = data;
	}
}
