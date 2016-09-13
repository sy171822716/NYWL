package com.resmanager.client.model;

import java.util.ArrayList;

public class GoodsPackageQtyListModel extends ResultModel {

	/** 
	*/
	private static final long serialVersionUID = 1L;

	private ArrayList<GoodsPackageQtyModel> data;

	public ArrayList<GoodsPackageQtyModel> getData() {
		return data;
	}

	public void setData(ArrayList<GoodsPackageQtyModel> data) {
		this.data = data;
	}

}
