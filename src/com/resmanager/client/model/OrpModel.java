/**   
 * @Title: OrpModel.java 
 * @Package com.resmanager.client.model 
 * @Description: 操作类
 * @author ShenYang  
 * @date 2015-11-27 上午10:40:07 
 * @version V1.0   
 */
package com.resmanager.client.model;

/**
 * @ClassName: OrpModel
 * @Description: 操作类
 * @author ShenYang
 * @date 2015-11-27 上午10:40:07
 * 
 */
public class OrpModel {
	private int orpId;
	private int orpName;
	private int orpImg;

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
	public int getOrpName() {
		return orpName;
	}

	/**
	 * @param orpName
	 *            要设置的 orpName
	 */
	public void setOrpName(int orpName) {
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
}
