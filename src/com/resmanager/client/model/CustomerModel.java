/**   
 * @Title: CustomerModel.java 
 * @Package com.resmanager.client.model 
 * @Description: 客户信息model 
 * @author ShenYang  
 * @date 2016-1-5 上午9:49:46 
 * @version V1.0   
 */
package com.resmanager.client.model;

import java.io.Serializable;

/**
 * @ClassName: CustomerModel
 * @Description: 客户信息model
 * @author ShenYang
 * @date 2016-1-5 上午9:49:46
 * 
 */
public class CustomerModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String sortLetters; // 显示数据拼音的首字母
	private String CustomerID;
	private String CustomerName;

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

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

}
