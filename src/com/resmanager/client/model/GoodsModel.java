package com.resmanager.client.model;

import java.io.Serializable;

public class GoodsModel implements Serializable {
	/** 
	*/
	private static final long serialVersionUID = 1L;
	private String GoodsnameID;// 产品ID
	private String GoodsName;// 产品名字
	private String Packagetype;// 包装物名称

	public String getGoodsnameID() {
		return GoodsnameID;
	}

	public void setGoodsnameID(String goodsnameID) {
		GoodsnameID = goodsnameID;
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

}
