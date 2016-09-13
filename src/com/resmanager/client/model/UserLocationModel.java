package com.resmanager.client.model;

import java.io.Serializable;

public class UserLocationModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String WorkID;
	private String Longitude;
	private String Latitude;
	private String DriverID;
	private String UserName;
	private String DriverName;
	private String Phone;
	private String CID;//车牌
	private String ddsl;//订单数量
	private String istank;//是否是油桶，1是0不是

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
	 * @return longitude
	 */
	public String getLongitude() {
		return Longitude;
	}

	/**
	 * @param longitude
	 *            要设置的 longitude
	 */
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	/**
	 * @return latitude
	 */
	public String getLatitude() {
		return Latitude;
	}

	/**
	 * @param latitude
	 *            要设置的 latitude
	 */
	public void setLatitude(String latitude) {
		Latitude = latitude;
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

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	/** 
	 * @return phone 
	 */
	public String getPhone() {
		return Phone;
	}

	/** 
	 * @param phone 要设置的 phone 
	 */
	public void setPhone(String phone) {
		Phone = phone;
	}

	/** 
	 * @return cID 
	 */
	public String getCID() {
		return CID;
	}

	/** 
	 * @param cID 要设置的 cID 
	 */
	public void setCID(String cID) {
		CID = cID;
	}

	/** 
	 * @return ddsl 
	 */
	public String getDdsl() {
		return ddsl;
	}

	/** 
	 * @param ddsl 要设置的 ddsl 
	 */
	public void setDdsl(String ddsl) {
		this.ddsl = ddsl;
	}

	/** 
	 * @return istank 
	 */
	public String getIstank() {
		return istank;
	}

	/** 
	 * @param istank 要设置的 istank 
	 */
	public void setIstank(String istank) {
		this.istank = istank;
	}

}
