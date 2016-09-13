package com.resmanager.client.model;

import java.io.Serializable;

public class LocationModel implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String address;// 地址
	private String name;// 热点名字
	private Double lat;// 经度
	private Double lng;// 纬度

	/**
	 * @return address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            要设置的 address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            要设置的 name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return lat
	 */
	public Double getLat() {
		return lat;
	}

	/**
	 * @param lat
	 *            要设置的 lat
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * @return lng
	 */
	public Double getLng() {
		return lng;
	}

	/**
	 * @param lng
	 *            要设置的 lng
	 */
	public void setLng(Double lng) {
		this.lng = lng;
	}

	/**
	 * @return serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
