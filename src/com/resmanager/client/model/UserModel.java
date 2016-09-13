package com.resmanager.client.model;

import java.io.Serializable;

public class UserModel extends ResultModel implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String UserKey;
	private String account;
	private String pass;
	private String UserName;
	private String NickName;
	private String UserType;
	private int UserID;

	public String getUserKey() {
		return UserKey;
	}

	public void setUserKey(String userKey) {
		UserKey = userKey;
	}

	/**
	 * @return account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            要设置的 account
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @param pass
	 *            要设置的 pass
	 */
	public void setPass(String pass) {
		this.pass = pass;
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
	 * @return userType
	 */
	public String getUserType() {
		return UserType;
	}

	/**
	 * @param userType
	 *            要设置的 userType
	 */
	public void setUserType(String userType) {
		UserType = userType;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

}
