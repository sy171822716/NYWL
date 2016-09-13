package com.resmanager.client.model;

import java.util.ArrayList;

public class ViewLabelListModel extends ResultModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<ViewLabelModel> data;
	public ArrayList<ViewLabelModel> getData() {
		return data;
	}
	public void setData(ArrayList<ViewLabelModel> data) {
		this.data = data;
	}

}
