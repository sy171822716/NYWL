package com.resmanager.client.model;

import java.util.ArrayList;

public class RecylePicListModel extends ResultModel {

	/** 
	*/
	private static final long serialVersionUID = 1L;
	private ArrayList<RecylePicModel> data;

	public ArrayList<RecylePicModel> getData() {
		return data;
	}

	public void setData(ArrayList<RecylePicModel> data) {
		this.data = data;
	}
}
