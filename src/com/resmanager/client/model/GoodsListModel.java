package com.resmanager.client.model;

import java.util.ArrayList;

public class GoodsListModel extends ResultModel {
	private static final long serialVersionUID = 1L;

	private ArrayList<GoodsModel> data;

	public ArrayList<GoodsModel> getData() {
		return data;
	}

	public void setData(ArrayList<GoodsModel> data) {
		this.data = data;
	}

}
