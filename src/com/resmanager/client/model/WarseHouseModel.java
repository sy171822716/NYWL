package com.resmanager.client.model;

import java.io.Serializable;

public class WarseHouseModel implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String warehouseID;
	private String warehouseName;
	private String warehouseAddress;
	private String isDel;

	/**
	 * @return warehouseID
	 */
	public String getWarehouseID() {
		return warehouseID;
	}

	/**
	 * @param warehouseID
	 *            要设置的 warehouseID
	 */
	public void setWarehouseID(String warehouseID) {
		this.warehouseID = warehouseID;
	}

	/**
	 * @return warehouseName
	 */
	public String getWarehouseName() {
		return warehouseName;
	}

	/**
	 * @param warehouseName
	 *            要设置的 warehouseName
	 */
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	/**
	 * @return warehouseAddress
	 */
	public String getWarehouseAddress() {
		return warehouseAddress;
	}

	/**
	 * @param warehouseAddress
	 *            要设置的 warehouseAddress
	 */
	public void setWarehouseAddress(String warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}

	/**
	 * @return isDel
	 */
	public String getIsDel() {
		return isDel;
	}

	/**
	 * @param isDel
	 *            要设置的 isDel
	 */
	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

}
