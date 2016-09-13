package com.resmanager.client.model;

import java.io.Serializable;

public class TempScanBimpModel implements Serializable{
	/** 
	*/ 
	private static final long serialVersionUID = 1L;
	private String WorkID;
	private String OrderID;
	private String LabelCode;
	private String GoodsName;
	private String Packagetype;
	private String Original_Path;
	private String Thumb_Path;

	public String getWorkID() {
		return WorkID;
	}

	public void setWorkID(String workID) {
		WorkID = workID;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getLabelCode() {
		return LabelCode;
	}

	public void setLabelCode(String labelCode) {
		LabelCode = labelCode;
	}

	public String getGoodsName() {
		return GoodsName;
	}

	public void setGoodsName(String goodsName) {
		GoodsName = goodsName;
	}

	public String getPackagetype() {
		return Packagetype;
	}

	public void setPackagetype(String packagetype) {
		Packagetype = packagetype;
	}

	public String getOriginal_Path() {
		return Original_Path;
	}

	public void setOriginal_Path(String original_Path) {
		Original_Path = original_Path;
	}

	public String getThumb_Path() {
		return Thumb_Path;
	}

	public void setThumb_Path(String thumb_Path) {
		Thumb_Path = thumb_Path;
	}

}
