package com.resmanager.client.model;

public class OrderOrpModel {

	private int orpId;
	private int isused;
	private String orpName;
	private int orpImg;
	private int num;
	/**
	 * @return orpId
	 */
	public int getOrpId() {
		return orpId;
	}

	/**
	 * @param orpId
	 *            要设置的 orpId
	 */
	public void setOrpId(int orpId) {
		this.orpId = orpId;
	}

	/**
	 * @return orpName
	 */
	public String getOrpName() {
		return orpName;
	}

	/**
	 * @param orpName
	 *            要设置的 orpName
	 */
	public void setOrpName(String orpName) {
		this.orpName = orpName;
	}

	/**
	 * @return orpImg
	 */
	public int getOrpImg() {
		return orpImg;
	}

	/**
	 * @param orpImg
	 *            要设置的 orpImg
	 */
	public void setOrpImg(int orpImg) {
		this.orpImg = orpImg;
	}

	public int getIsused() {
		return isused;
	}

	public void setIsused(int isused) {
		this.isused = isused;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
