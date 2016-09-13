/**   
 * @Title: OrderListModel.java 
 * @Package com.resmanager.client.model 
 * @author ShenYang  
 * @date 2015-12-1 下午12:48:21 
 * @version V1.0   
 */
package com.resmanager.client.model;

import java.util.ArrayList;

/**
 * @ClassName: OrderListModel
 * @author ShenYang
 * @date 2015-12-1 下午12:48:21
 * 
 */
public class OrderListModel extends ResultModel {

	private static final long serialVersionUID = 1L;
	private ArrayList<Order> data;

	public ArrayList<Order> getData() {
		return data;
	}

	public void setData(ArrayList<Order> data) {
		this.data = data;
	}
}
