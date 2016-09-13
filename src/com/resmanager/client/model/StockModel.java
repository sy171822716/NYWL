package com.resmanager.client.model;

import java.io.Serializable;

public class StockModel implements Serializable {

	/** 
	*/
	private static final long serialVersionUID = 1L;
	private String CustomerID;
	private String CustomerName;
	private String Stock;

	/**
	 * @return customerID
	 */
	public String getCustomerID() {
		return CustomerID;
	}

	/**
	 * @param customerID
	 *            要设置的 customerID
	 */
	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}

	/**
	 * @return customerName
	 */
	public String getCustomerName() {
		return CustomerName;
	}

	/**
	 * @param customerName
	 *            要设置的 customerName
	 */
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	/**
	 * @return stock
	 */
	public String getStock() {
		return Stock;
	}

	/**
	 * @param stock
	 *            要设置的 stock
	 */
	public void setStock(String stock) {
		Stock = stock;
	}

}
