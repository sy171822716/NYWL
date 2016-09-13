package com.resmanager.client.model;

import java.io.Serializable;

public class GoodsPackageQtyModel implements Serializable {

	/** 
	*/
	private static final long serialVersionUID = 1L;
	private String GoodsnameID;
	private String Goodsname;
	private int Quantity;
	private String Packagetype;

	public String getGoodsnameID() {
		return GoodsnameID;
	}

	public void setGoodsnameID(String goodsnameID) {
		GoodsnameID = goodsnameID;
	}

	public String getGoodsname() {
		return Goodsname;
	}

	public void setGoodsname(String goodsname) {
		Goodsname = goodsname;
	}

	public int getQuantity() {
		return Quantity;
	}

	public void setQuantity(int quantity) {
		Quantity = quantity;
	}

	public String getPackagetype() {
		return Packagetype;
	}

	public void setPackagetype(String packagetype) {
		Packagetype = packagetype;
	}

}
