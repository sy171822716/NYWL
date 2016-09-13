package com.resmanager.client.model;

import java.util.ArrayList;

public class DeliveryScanListModel extends ResultModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<DeliveryScanModel> data;

	public ArrayList<DeliveryScanModel> getData() {
		return data;
	}

	public void setData(ArrayList<DeliveryScanModel> data) {
		this.data = data;
	}
}
