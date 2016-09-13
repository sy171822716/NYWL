/**   
 * @Title: TradeBillListModel.java 
 * @Package com.resmanager.client.model 
 * @Description: 结算单列表
 * @author ShenYang   
 * @date 2015-12-19 上午9:48:23 
 * @version V1.0   
 */
package com.resmanager.client.model;

import java.util.ArrayList;

/**
 * @ClassName: TradeBillListModel
 * @Description: 结算单列表
 * @author ShenYang
 * @date 2015-12-19 上午9:48:23
 * 
 */
public class TradeBillListModel extends ResultModel {

	/** 
	*/
	private static final long serialVersionUID = 1L;

	private ArrayList<TradeBillModel> data;

	public ArrayList<TradeBillModel> getData() {
		return data;
	}

	public void setData(ArrayList<TradeBillModel> data) {
		this.data = data;
	}

}
