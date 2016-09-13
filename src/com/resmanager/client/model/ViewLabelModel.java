package com.resmanager.client.model;

import java.io.Serializable;

public class ViewLabelModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String LabelUseID;// 操作员ID
	private String WorkID;
	private String OrderID;
	private String LabelCode;
	private String Label_StatusCode;
	private String Label_StatusName;
	private String Barrel_StatusCode;
	private String Barrel_StatusName;
	private String RDT;
	private String Longitude;
	private String Latitude;
	private String NetworkType;
	private String NetworkStrength;
	private String Original_Path;
	private String Thumb_Path;
	private String ExerID;
	private String ExerName;
	private String PicState;
	private String MapLocationName;
	private String MapSpecificLocation;

	public String getLabelUseID() {
		return LabelUseID;
	}

	public void setLabelUseID(String labelUseID) {
		LabelUseID = labelUseID;
	}

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

	public String getLabel_StatusCode() {
		return Label_StatusCode;
	}

	public void setLabel_StatusCode(String label_StatusCode) {
		Label_StatusCode = label_StatusCode;
	}

	public String getLabel_StatusName() {
		return Label_StatusName;
	}

	public void setLabel_StatusName(String label_StatusName) {
		Label_StatusName = label_StatusName;
	}

	public String getBarrel_StatusCode() {
		return Barrel_StatusCode;
	}

	public void setBarrel_StatusCode(String barrel_StatusCode) {
		Barrel_StatusCode = barrel_StatusCode;
	}

	public String getBarrel_StatusName() {
		return Barrel_StatusName;
	}

	public void setBarrel_StatusName(String barrel_StatusName) {
		Barrel_StatusName = barrel_StatusName;
	}

	public String getRDT() {
		return RDT;
	}

	public void setRDT(String rDT) {
		RDT = rDT;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getNetworkType() {
		return NetworkType;
	}

	public void setNetworkType(String networkType) {
		NetworkType = networkType;
	}

	public String getNetworkStrength() {
		return NetworkStrength;
	}

	public void setNetworkStrength(String networkStrength) {
		NetworkStrength = networkStrength;
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

	public String getExerID() {
		return ExerID;
	}

	public void setExerID(String exerID) {
		ExerID = exerID;
	}

	public String getExerName() {
		return ExerName;
	}

	public void setExerName(String exerName) {
		ExerName = exerName;
	}

	public String getPicState() {
		return PicState;
	}

	public void setPicState(String picState) {
		PicState = picState;
	}

	public String getMapLocationName() {
		return MapLocationName;
	}

	public void setMapLocationName(String mapLocationName) {
		MapLocationName = mapLocationName;
	}

	public String getMapSpecificLocation() {
		return MapSpecificLocation;
	}

	public void setMapSpecificLocation(String mapSpecificLocation) {
		MapSpecificLocation = mapSpecificLocation;
	}

}
