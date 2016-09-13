/**   
 * @Title: OrderDetailInfo.java 
 * @Package com.resmanager.client.model 
 * @Description: 订单信息和明细列表
 * @author ShenYang  
 * @date 2015-12-3 下午2:54:30 
 * @version V1.0   
 */
package com.resmanager.client.model;

import java.util.ArrayList;

/**
 * @ClassName: OrderDetailInfo
 * @Description: 订单信息和明细列表
 * @author ShenYang
 * @date 2015-12-3 下午2:54:30
 * 
 */
public class OrderDetailInfo extends ResultModel {

	private static final long serialVersionUID = 1L;
	private Order head;
	private ArrayList<SourceModel> detail;

	/**
	 * @return head
	 */
	public Order getHead() {
		return head;
	}

	/**
	 * @param head
	 *            要设置的 head
	 */
	public void setHead(Order head) {
		this.head = head;
	}

	/**
	 * @return detail
	 */
	public ArrayList<SourceModel> getDetail() {
		return detail;
	}

	/**
	 * @param detail
	 *            要设置的 detail
	 */
	public void setDetail(ArrayList<SourceModel> detail) {
		this.detail = detail;
	}

}
