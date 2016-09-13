package com.resmanager.client.model;

import java.io.Serializable;

public class RecyleTempModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String Original_Path;
	private String Thumb_Path;
	private String WorkID;
	private String Labels;
	private String FromCustomerID;
	private String DriverID;

	/**
	 * @return original_Path
	 */
	public String getOriginal_Path() {
		return Original_Path;
	}

	/**
	 * @param original_Path
	 *            要设置的 original_Path
	 */
	public void setOriginal_Path(String original_Path) {
		Original_Path = original_Path;
	}

	/**
	 * @return thumb_Path
	 */
	public String getThumb_Path() {
		return Thumb_Path;
	}

	/**
	 * @param thumb_Path
	 *            要设置的 thumb_Path
	 */
	public void setThumb_Path(String thumb_Path) {
		Thumb_Path = thumb_Path;
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
	 * @return labels
	 */
	public String getLabels() {
		return Labels;
	}

	/**
	 * @param labels
	 *            要设置的 labels
	 */
	public void setLabels(String labels) {
		Labels = labels;
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

}
