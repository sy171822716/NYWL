package com.resmanager.client.model;

import java.util.ArrayList;


public class RecyleLabelListModel extends ResultModel {
	/** 
	*/
	private static final long serialVersionUID = 1L;
	private ArrayList<RecyleLabelModel> data;

	public ArrayList<RecyleLabelModel> getData() {
		return data;
	}

	public void setData(ArrayList<RecyleLabelModel> data) {
		this.data = data;
	}
}
