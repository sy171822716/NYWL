package com.resmanager.client.model;

import java.io.Serializable;

public class RecylePicModel implements Serializable {

	/** 
	*/
	private static final long serialVersionUID = 1L;
	private String Original_Path;
	private String Thumb_Path;
	private String WorkID;
	private String Labels;

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

	public String getLabels() {
		return Labels;
	}

	public void setLabels(String labels) {
		Labels = labels;
	}
}
