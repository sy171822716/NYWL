package com.resmanager.client.model;

import java.io.Serializable;

public class AdModel implements Serializable {

	/** 
	*/
	private static final long serialVersionUID = 1L;
	private int AID;
	private String ADName;
	private String ADPicUrl;
	private String ADUrl;

	/**
	 * @return aID
	 */
	public int getAID() {
		return AID;
	}

	/**
	 * @param aID
	 *            要设置的 aID
	 */
	public void setAID(int aID) {
		AID = aID;
	}

	/**
	 * @return aDName
	 */
	public String getADName() {
		return ADName;
	}

	/**
	 * @param aDName
	 *            要设置的 aDName
	 */
	public void setADName(String aDName) {
		ADName = aDName;
	}

	/**
	 * @return aDPicUrl
	 */
	public String getADPicUrl() {
		return ADPicUrl;
	}

	/**
	 * @param aDPicUrl
	 *            要设置的 aDPicUrl
	 */
	public void setADPicUrl(String aDPicUrl) {
		ADPicUrl = aDPicUrl;
	}

	/**
	 * @return aDUrl
	 */
	public String getADUrl() {
		return ADUrl;
	}

	/**
	 * @param aDUrl
	 *            要设置的 aDUrl
	 */
	public void setADUrl(String aDUrl) {
		ADUrl = aDUrl;
	}
}
