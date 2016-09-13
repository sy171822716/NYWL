package com.resmanager.client.model;

import java.util.ArrayList;


public class RecyleTempListModel extends ResultModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<RecyleTempModel> data;

	public ArrayList<RecyleTempModel> getData() {
		return data;
	}

	public void setData(ArrayList<RecyleTempModel> data) {
		this.data = data;
	}
}
