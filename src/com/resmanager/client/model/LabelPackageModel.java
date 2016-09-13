package com.resmanager.client.model;

import java.io.Serializable;

public class LabelPackageModel implements Serializable {

	/** 
	*/
	private static final long serialVersionUID = 1L;
	private String LabelCode;

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

	private String Packagetype;
}
