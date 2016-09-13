package com.resmanager.client.model;

import java.io.Serializable;

public class RecyleModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String RID;
	private String Recovery_Remark;
	private String FromCustomerID;// 客户ID
	private String FromCustomerName;// 客户名字
	private String CreateTime;// 回收时间
	private String DriverID;
	private String DriverName;
	private String MapLocationName;
	private String MapSpecificLocation;
	private String Longitude;
	private String Latitude;
	private String WorkID;
	private String Audit_UserID;
	private String Audit_UserName;
	private String Audit_Remark;
	private String Audit_Date;
	private String Status;
	private String CostMoney;
	private String yjsmoney;
	private String djsmoney;
	private String LabelCode;
	/**
	 * @return rID
	 */
	public String getRID() {
		return RID;
	}

	/**
	 * @param rID
	 *            要设置的 rID
	 */
	public void setRID(String rID) {
		RID = rID;
	}

	/**
	 * @return recovery_Remark
	 */
	public String getRecovery_Remark() {
		return Recovery_Remark;
	}

	/**
	 * @param recovery_Remark
	 *            要设置的 recovery_Remark
	 */
	public void setRecovery_Remark(String recovery_Remark) {
		Recovery_Remark = recovery_Remark;
	}

	/**
	 * @return fromCustomerID
	 */
	public String getFromCustomerID() {
		return FromCustomerID;
	}

	/**
	 * @param fromCustomerID
	 *            要设置的 fromCustomerID
	 */
	public void setFromCustomerID(String fromCustomerID) {
		FromCustomerID = fromCustomerID;
	}

	/**
	 * @return fromCustomerName
	 */
	public String getFromCustomerName() {
		return FromCustomerName;
	}

	/**
	 * @param fromCustomerName
	 *            要设置的 fromCustomerName
	 */
	public void setFromCustomerName(String fromCustomerName) {
		FromCustomerName = fromCustomerName;
	}

	/**
	 * @return createTime
	 */
	public String getCreateTime() {
		return CreateTime;
	}

	/**
	 * @param createTime
	 *            要设置的 createTime
	 */
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
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
	 * @return mapLocationName
	 */
	public String getMapLocationName() {
		return MapLocationName;
	}

	/**
	 * @param mapLocationName
	 *            要设置的 mapLocationName
	 */
	public void setMapLocationName(String mapLocationName) {
		MapLocationName = mapLocationName;
	}

	/**
	 * @return mapSpecificLocation
	 */
	public String getMapSpecificLocation() {
		return MapSpecificLocation;
	}

	/**
	 * @param mapSpecificLocation
	 *            要设置的 mapSpecificLocation
	 */
	public void setMapSpecificLocation(String mapSpecificLocation) {
		MapSpecificLocation = mapSpecificLocation;
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
	 * @return audit_UserID
	 */
	public String getAudit_UserID() {
		return Audit_UserID;
	}

	/**
	 * @param audit_UserID
	 *            要设置的 audit_UserID
	 */
	public void setAudit_UserID(String audit_UserID) {
		Audit_UserID = audit_UserID;
	}

	/**
	 * @return audit_UserName
	 */
	public String getAudit_UserName() {
		return Audit_UserName;
	}

	/**
	 * @param audit_UserName
	 *            要设置的 audit_UserName
	 */
	public void setAudit_UserName(String audit_UserName) {
		Audit_UserName = audit_UserName;
	}

	/**
	 * @return yjsmoney
	 */
	public String getYjsmoney() {
		return yjsmoney;
	}

	/**
	 * @param yjsmoney
	 *            要设置的 yjsmoney
	 */
	public void setYjsmoney(String yjsmoney) {
		this.yjsmoney = yjsmoney;
	}

	/**
	 * @return djsmoney
	 */
	public String getDjsmoney() {
		return djsmoney;
	}

	/**
	 * @param djsmoney
	 *            要设置的 djsmoney
	 */
	public void setDjsmoney(String djsmoney) {
		this.djsmoney = djsmoney;
	}

	/**
	 * @return audit_Remark
	 */
	public String getAudit_Remark() {
		return Audit_Remark;
	}

	/**
	 * @param audit_Remark
	 *            要设置的 audit_Remark
	 */
	public void setAudit_Remark(String audit_Remark) {
		Audit_Remark = audit_Remark;
	}

	/**
	 * @return audit_Date
	 */
	public String getAudit_Date() {
		return Audit_Date;
	}

	/**
	 * @param audit_Date
	 *            要设置的 audit_Date
	 */
	public void setAudit_Date(String audit_Date) {
		Audit_Date = audit_Date;
	}

	/**
	 * @return status
	 */
	public String getStatus() {
		return Status;
	}

	/**
	 * @param status
	 *            要设置的 status
	 */
	public void setStatus(String status) {
		Status = status;
	}

	/**
	 * @return costMoney
	 */
	public String getCostMoney() {
		return CostMoney;
	}

	/**
	 * @param costMoney
	 *            要设置的 costMoney
	 */
	public void setCostMoney(String costMoney) {
		CostMoney = costMoney;
	}

	public String getLabelCode() {
		return LabelCode;
	}

	public void setLabelCode(String labelCode) {
		LabelCode = labelCode;
	}

}
