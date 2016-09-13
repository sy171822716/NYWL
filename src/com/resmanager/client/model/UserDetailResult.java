/**   
 * @Title: UserDetailResult.java 
 * @Package com.resmanager.client.model 
 * @author ShenYang  
 * @date 2015-12-3 下午4:44:57 
 * @version V1.0   
 */
package com.resmanager.client.model;

/**
 * @ClassName: UserDetailResult
 * @Description:
 * @author ShenYang
 * @date 2015-12-3 下午4:44:57
 * 
 */
public class UserDetailResult extends ResultModel {

	private static final long serialVersionUID = 1L;
	private UserDetailModel data;
	public UserDetailModel getData() {
		return data;
	}
	public void setData(UserDetailModel data) {
		this.data = data;
	}
}
