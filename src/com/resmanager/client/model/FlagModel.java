package com.resmanager.client.model;

import java.io.Serializable;

public class FlagModel implements Serializable {

	/** 
	*/
	private static final long serialVersionUID = 1L;
	private String OrderID;
	private String LabelCode;
	private String Saleoid;
	private String Delivery_Date;
	private String DriverID;
	private String DriverName;
	private String ordercustomer;
	private String saler;

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
	 * @return saleoid
	 */
	public String getSaleoid() {
		return Saleoid;
	}

	/**
	 * @param saleoid
	 *            要设置的 saleoid
	 */
	public void setSaleoid(String saleoid) {
		Saleoid = saleoid;
	}

	/**
	 * @return delivery_Date
	 */
	public String getDelivery_Date() {
		return Delivery_Date;
	}

	/**
	 * @param delivery_Date
	 *            要设置的 delivery_Date
	 */
	public void setDelivery_Date(String delivery_Date) {
		Delivery_Date = delivery_Date;
	}

	/**
	 * @return driverID
	 */
	public String getDriverID() {
		return DriverID;
	}

	/**
	 * @param driverID
	 *            要设置的 driverID
	 */
	public void setDriverID(String driverID) {
		DriverID = driverID;
	}

	/**
	 * @return driverName
	 */
	public String getDriverName() {
		return DriverName;
	}

	/**
	 * @param driverName
	 *            要设置的 driverName
	 */
	public void setDriverName(String driverName) {
		DriverName = driverName;
	}

	/**
	 * @return ordercustomer
	 */
	public String getOrdercustomer() {
		return ordercustomer;
	}

	/**
	 * @param ordercustomer
	 *            要设置的 ordercustomer
	 */
	public void setOrdercustomer(String ordercustomer) {
		this.ordercustomer = ordercustomer;
	}

	/**
	 * @return saler
	 */
	public String getSaler() {
		return saler;
	}

	/**
	 * @param saler
	 *            要设置的 saler
	 */
	public void setSaler(String saler) {
		this.saler = saler;
	}

}
