package com.resmanager.client.model;

import java.io.Serializable;

public class DriverModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String UserId;
	private String UserName;
	private String NickName;
	private String Phone;
	private String CID;
	private String shippington;

	/**
	 * @return userId
	 */
	public String getUserId() {
		return UserId;
	}

	/**
	 * @param userId
	 *            要设置的 userId
	 */
	public void setUserId(String userId) {
		UserId = userId;
	}

	/**
	 * @return userName
	 */
	public String getUserName() {
		return UserName;
	}

	/**
	 * @param userName
	 *            要设置的 userName
	 */
	public void setUserName(String userName) {
		UserName = userName;
	}

	/**
	 * @return nickName
	 */
	public String getNickName() {
		return NickName;
	}

	/**
	 * @param nickName
	 *            要设置的 nickName
	 */
	public void setNickName(String nickName) {
		NickName = nickName;
	}

	/**
	 * @return phone
	 */
	public String getPhone() {
		return Phone;
	}

	/**
	 * @param phone
	 *            要设置的 phone
	 */
	public void setPhone(String phone) {
		Phone = phone;
	}

	/**
	 * @return cID
	 */
	public String getCID() {
		return CID;
	}

	/**
	 * @param cID
	 *            要设置的 cID
	 */
	public void setCID(String cID) {
		CID = cID;
	}

	/**
	 * @return shippington
	 */
	public String getShippington() {
		return shippington;
	}

	/**
	 * @param shippington
	 *            要设置的 shippington
	 */
	public void setShippington(String shippington) {
		this.shippington = shippington;
	}

}
