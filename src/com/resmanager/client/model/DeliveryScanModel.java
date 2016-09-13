package com.resmanager.client.model;

import java.io.Serializable;

public class DeliveryScanModel implements Serializable {
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

	/**
	 * @return workID
	 */
	public String getWorkID() {
		return WorkID;
	}

	/**
	 * @param workID
	 *            要设置的 workID
	 */
	public void setWorkID(String workID) {
		WorkID = workID;
	}

	/**
	 * @return orderID
	 */
	public String getOrderID() {
		return OrderID;
	}

	/**
	 * @param orderID
	 *            要设置的 orderID
	 */
	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	/**
	 * @return labelCode
	 */
	public String getLabelCode() {
		return LabelCode;
	}

	/**
	 * @param labelCode
	 *            要设置的 labelCode
	 */
	public void setLabelCode(String labelCode) {
		LabelCode = labelCode;
	}

	/**
	 * @return goodsName
	 */
	public String getGoodsName() {
		return GoodsName;
	}

	/**
	 * @param goodsName
	 *            要设置的 goodsName
	 */
	public void setGoodsName(String goodsName) {
		GoodsName = goodsName;
	}

	/**
	 * @return packagetype
	 */
	public String getPackagetype() {
		return Packagetype;
	}

	/**
	 * @param packagetype
	 *            要设置的 packagetype
	 */
	public void setPackagetype(String packagetype) {
		Packagetype = packagetype;
	}

	/**
	 * @return original_Path
	 */
	public String getOriginal_Path() {
		return Original_Path;
	}

	/**
	 * @param original_Path
	 *            要设置的 original_Path
	 */
	public void setOriginal_Path(String original_Path) {
		Original_Path = original_Path;
	}

	/**
	 * @return thumb_Path
	 */
	public String getThumb_Path() {
		return Thumb_Path;
	}

	/**
	 * @param thumb_Path
	 *            要设置的 thumb_Path
	 */
	public void setThumb_Path(String thumb_Path) {
		Thumb_Path = thumb_Path;
	}
}
