package com.resmanager.client.model;

import java.io.Serializable;

public class UserDetailModel implements Serializable {
	/** 
	* @Fields serialVersionUID : TODO
	*/ 
	private static final long serialVersionUID = 1L;
	private int UserId;
	private String UserName;
	private String Password;
	private String NickName;
	private String Email;
	private String Phone;
	private String UserType;
	private String DepartId;
	private String Sex;
	private String RegTime;
	private String Status;
	private String Remark;
	private String QQ;
	private String RoleId;
	private String IsSpecify;
	private String isdel;
	private String UserKey;
	private String RoleName;
	private String Power;

	/**
	 * @return userId
	 */
	public int getUserId() {
		return UserId;
	}

	/**
	 * @param userId
	 *            要设置的 userId
	 */
	public void setUserId(int userId) {
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
	 * @return password
	 */
	public String getPassword() {
		return Password;
	}

	/**
	 * @param password
	 *            要设置的 password
	 */
	public void setPassword(String password) {
		Password = password;
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
	 * @return email
	 */
	public String getEmail() {
		return Email;
	}

	/**
	 * @param email
	 *            要设置的 email
	 */
	public void setEmail(String email) {
		Email = email;
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

	/**
	 * @return departId
	 */
	public String getDepartId() {
		return DepartId;
	}

	/**
	 * @param departId
	 *            要设置的 departId
	 */
	public void setDepartId(String departId) {
		DepartId = departId;
	}

	/**
	 * @return sex
	 */
	public String getSex() {
		return Sex;
	}

	/**
	 * @param sex
	 *            要设置的 sex
	 */
	public void setSex(String sex) {
		Sex = sex;
	}

	/**
	 * @return regTime
	 */
	public String getRegTime() {
		return RegTime;
	}

	/**
	 * @param regTime
	 *            要设置的 regTime
	 */
	public void setRegTime(String regTime) {
		RegTime = regTime;
	}

	/**
	 * @return status
	 */
	public String getStatus() {
		return Status;
	}

	/**
	 * @param status
	 *            要设置的 status
	 */
	public void setStatus(String status) {
		Status = status;
	}

	/**
	 * @return remark
	 */
	public String getRemark() {
		return Remark;
	}

	/**
	 * @param remark
	 *            要设置的 remark
	 */
	public void setRemark(String remark) {
		Remark = remark;
	}

	/**
	 * @return qQ
	 */
	public String getQQ() {
		return QQ;
	}

	/**
	 * @param qQ
	 *            要设置的 qQ
	 */
	public void setQQ(String qQ) {
		QQ = qQ;
	}

	/**
	 * @return roleId
	 */
	public String getRoleId() {
		return RoleId;
	}

	/**
	 * @param roleId
	 *            要设置的 roleId
	 */
	public void setRoleId(String roleId) {
		RoleId = roleId;
	}

	/**
	 * @return isSpecify
	 */
	public String getIsSpecify() {
		return IsSpecify;
	}

	/**
	 * @param isSpecify
	 *            要设置的 isSpecify
	 */
	public void setIsSpecify(String isSpecify) {
		IsSpecify = isSpecify;
	}

	/**
	 * @return isdel
	 */
	public String getIsdel() {
		return isdel;
	}

	/**
	 * @param isdel
	 *            要设置的 isdel
	 */
	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

	/**
	 * @return userKey
	 */
	public String getUserKey() {
		return UserKey;
	}

	/**
	 * @param userKey
	 *            要设置的 userKey
	 */
	public void setUserKey(String userKey) {
		UserKey = userKey;
	}

	/**
	 * @return roleName
	 */
	public String getRoleName() {
		return RoleName;
	}

	/**
	 * @param roleName
	 *            要设置的 roleName
	 */
	public void setRoleName(String roleName) {
		RoleName = roleName;
	}

	/**
	 * @return power
	 */
	public String getPower() {
		return Power;
	}

	/**
	 * @param power
	 *            要设置的 power
	 */
	public void setPower(String power) {
		Power = power;
	}

}
