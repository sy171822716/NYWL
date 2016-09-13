package com.resmanager.client.model;

import java.util.ArrayList;

public class RecyleListModel extends ResultModel {

	/** 
	*/
	private static final long serialVersionUID = 1L;

	private ArrayList<RecyleModel> data;

	public ArrayList<RecyleModel> getData() {
		return data;
	}

	public void setData(ArrayList<RecyleModel> data) {
		this.data = data;
	}

}
