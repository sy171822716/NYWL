package com.resmanager.client.model;

import java.util.ArrayList;

public class FlagModelResult extends ResultModel {

	/** 
	*/
	private static final long serialVersionUID = 1L;
	private ArrayList<FlagModel> data;

	public ArrayList<FlagModel> getData() {
		return data;
	}

	public void setData(ArrayList<FlagModel> data) {
		this.data = data;
	}

}
