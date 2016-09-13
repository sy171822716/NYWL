package com.resmanager.client.model;

import java.io.Serializable;

public class VersionModel implements Serializable {
	/** 
	*/
	private static final long serialVersionUID = 1L;
	private String VersionNum;
	private String UrlAddres;
	private String IsForceUpdate;

	/**
	 * @return versionNum
	 */
	public String getVersionNum() {
		return VersionNum;
	}

	/**
	 * @param versionNum
	 *            要设置的 versionNum
	 */
	public void setVersionNum(String versionNum) {
		VersionNum = versionNum;
	}

	/**
	 * @return isForceUpdate
	 */
	public String getIsForceUpdate() {
		return IsForceUpdate;
	}

	/**
	 * @param isForceUpdate
	 *            要设置的 isForceUpdate
	 */
	public void setIsForceUpdate(String isForceUpdate) {
		IsForceUpdate = isForceUpdate;
	}

	public String getUrlAddres() {
		return UrlAddres;
	}

	public void setUrlAddres(String urlAddres) {
		UrlAddres = urlAddres;
	}
}
