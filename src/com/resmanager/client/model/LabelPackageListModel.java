package com.resmanager.client.model;

import java.util.ArrayList;

public class LabelPackageListModel extends ResultModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<LabelPackageModel> data;

	public ArrayList<LabelPackageModel> getData() {
		return data;
	}

	public void setData(ArrayList<LabelPackageModel> data) {
		this.data = data;
	}
}
