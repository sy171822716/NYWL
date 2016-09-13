package com.resmanager.client.model;

import java.io.Serializable;

public class SettledModel implements Serializable {

	/** 
	*/
	private static final long serialVersionUID = 1L;

	private String DriverID;
	private String TotalMoney, HasSettled, NotSettled;
	private String total_Quantity, total_Qty, hscount;

	public String getDriverID() {
		return DriverID;
	}

	public void setDriverID(String driverID) {
		DriverID = driverID;
	}

	public String getTotalMoney() {
		return TotalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		TotalMoney = totalMoney;
	}

	public String getHasSettled() {
		return HasSettled;
	}

	public void setHasSettled(String hasSettled) {
		HasSettled = hasSettled;
	}

	public String getNotSettled() {
		return NotSettled;
	}

	public void setNotSettled(String notSettled) {
		NotSettled = notSettled;
	}

	public String getHscount() {
		return hscount;
	}

	public void setHscount(String hscount) {
		this.hscount = hscount;
	}


	public String getTotal_Quantity() {
		return total_Quantity;
	}

	public void setTotal_Quantity(String total_Quantity) {
		this.total_Quantity = total_Quantity;
	}

	public String getTotal_Qty() {
		return total_Qty;
	}

	public void setTotal_Qty(String total_Qty) {
		this.total_Qty = total_Qty;
	}

}
