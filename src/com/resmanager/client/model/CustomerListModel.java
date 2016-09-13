package com.resmanager.client.model;

import java.util.ArrayList;

public class CustomerListModel extends ResultModel {

	/** 
	*/
	private static final long serialVersionUID = 1L;
	private ArrayList<CustomerModel> data;

	public ArrayList<CustomerModel> getData() {
		return data;
	}

	public void setData(ArrayList<CustomerModel> data) {
		this.data = data;
	}

}
