package com.resmanager.client.model;

import java.io.Serializable;

public class OrderPicModel implements Serializable {

	/** 
	*/
	private static final long serialVersionUID = 1L;
	private String LabelCode;
	private String OrderID;
	private String Original_Path_fh;
	private String Thumb_Path_fh;
	private String Longitude_fh;
	private String Latitude_fh;
	private String MapLocationName_fh;
	private String MapSpecificLocation_fh;
	private String Original_Path_xh;
	private String Thumb_Path_xh;
	private String Longitude_xh;
	private String Latitude_xh;
	private String MapLocationName_xh;
	private String MapSpecificLocation_xh;
	private String GoodsName;
	private String fmodel;
	private String PackagetypeID;
	private String Packagetype;

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
	 * @return original_Path_fh
	 */
	public String getOriginal_Path_fh() {
		return Original_Path_fh;
	}

	/**
	 * @param original_Path_fh
	 *            要设置的 original_Path_fh
	 */
	public void setOriginal_Path_fh(String original_Path_fh) {
		Original_Path_fh = original_Path_fh;
	}

	/**
	 * @return thumb_Path_fh
	 */
	public String getThumb_Path_fh() {
		return Thumb_Path_fh;
	}

	/**
	 * @param thumb_Path_fh
	 *            要设置的 thumb_Path_fh
	 */
	public void setThumb_Path_fh(String thumb_Path_fh) {
		Thumb_Path_fh = thumb_Path_fh;
	}

	/**
	 * @return original_Path_xh
	 */
	public String getOriginal_Path_xh() {
		return Original_Path_xh;
	}

	/**
	 * @param original_Path_xh
	 *            要设置的 original_Path_xh
	 */
	public void setOriginal_Path_xh(String original_Path_xh) {
		Original_Path_xh = original_Path_xh;
	}

	/**
	 * @return thumb_Path_xh
	 */
	public String getThumb_Path_xh() {
		return Thumb_Path_xh;
	}

	/**
	 * @param thumb_Path_xh
	 *            要设置的 thumb_Path_xh
	 */
	public void setThumb_Path_xh(String thumb_Path_xh) {
		Thumb_Path_xh = thumb_Path_xh;
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
	 * @return fmodel
	 */
	public String getFmodel() {
		return fmodel;
	}

	/**
	 * @return longitude_fh
	 */
	public String getLongitude_fh() {
		return Longitude_fh;
	}

	/**
	 * @return latitude_fh
	 */
	public String getLatitude_fh() {
		return Latitude_fh;
	}

	/**
	 * @return mapLocationName_fh
	 */
	public String getMapLocationName_fh() {
		return MapLocationName_fh;
	}

	/**
	 * @return mapSpecificLocation_fh
	 */
	public String getMapSpecificLocation_fh() {
		return MapSpecificLocation_fh;
	}

	/**
	 * @return longitude_xh
	 */
	public String getLongitude_xh() {
		return Longitude_xh;
	}

	/**
	 * @return latitude_xh
	 */
	public String getLatitude_xh() {
		return Latitude_xh;
	}

	/**
	 * @return mapLocationName_xh
	 */
	public String getMapLocationName_xh() {
		return MapLocationName_xh;
	}

	/**
	 * @return mapSpecificLocation_xh
	 */
	public String getMapSpecificLocation_xh() {
		return MapSpecificLocation_xh;
	}

	/**
	 * @param longitude_fh
	 *            要设置的 longitude_fh
	 */
	public void setLongitude_fh(String longitude_fh) {
		Longitude_fh = longitude_fh;
	}

	/**
	 * @param latitude_fh
	 *            要设置的 latitude_fh
	 */
	public void setLatitude_fh(String latitude_fh) {
		Latitude_fh = latitude_fh;
	}

	/**
	 * @param mapLocationName_fh
	 *            要设置的 mapLocationName_fh
	 */
	public void setMapLocationName_fh(String mapLocationName_fh) {
		MapLocationName_fh = mapLocationName_fh;
	}

	/**
	 * @param mapSpecificLocation_fh
	 *            要设置的 mapSpecificLocation_fh
	 */
	public void setMapSpecificLocation_fh(String mapSpecificLocation_fh) {
		MapSpecificLocation_fh = mapSpecificLocation_fh;
	}

	/**
	 * @param longitude_xh
	 *            要设置的 longitude_xh
	 */
	public void setLongitude_xh(String longitude_xh) {
		Longitude_xh = longitude_xh;
	}

	/**
	 * @param latitude_xh
	 *            要设置的 latitude_xh
	 */
	public void setLatitude_xh(String latitude_xh) {
		Latitude_xh = latitude_xh;
	}

	/**
	 * @param mapLocationName_xh
	 *            要设置的 mapLocationName_xh
	 */
	public void setMapLocationName_xh(String mapLocationName_xh) {
		MapLocationName_xh = mapLocationName_xh;
	}

	/**
	 * @param mapSpecificLocation_xh
	 *            要设置的 mapSpecificLocation_xh
	 */
	public void setMapSpecificLocation_xh(String mapSpecificLocation_xh) {
		MapSpecificLocation_xh = mapSpecificLocation_xh;
	}

	/**
	 * @param fmodel
	 *            要设置的 fmodel
	 */
	public void setFmodel(String fmodel) {
		this.fmodel = fmodel;
	}

	/**
	 * @return packagetypeID
	 */
	public String getPackagetypeID() {
		return PackagetypeID;
	}

	/**
	 * @param packagetypeID
	 *            要设置的 packagetypeID
	 */
	public void setPackagetypeID(String packagetypeID) {
		PackagetypeID = packagetypeID;
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
}
