/**   
 * @Title: TongModel.java 
 * @Package com.resmanager.client.model 
 * @Description: 资源信息
 * @author ShenYang  
 * @date 2015-12-3 下午3:00:57 
 * @version V1.0   
 */
package com.resmanager.client.model;

import java.io.Serializable;

/**
 * @ClassName: TongModel
 * @Description: 资源信息
 * @author ShenYang
 * @date 2015-12-3 下午3:00:57
 * 
 */
public class SourceModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String Sourceoid;
	private String Sendoid;
	private String DID;
	private String OrderID;
	private String Goodsname;
	private int Quantity;
	private String Qty;
	private String Warehouse;
	private String Packagetype;
	private String Transmissiontime;
	private String fid;
	private String now;
	private String id;

	/**
	 * @return sourceoid
	 */
	public String getSourceoid() {
		return Sourceoid;
	}

	/**
	 * @param sourceoid
	 *            要设置的 sourceoid
	 */
	public void setSourceoid(String sourceoid) {
		Sourceoid = sourceoid;
	}

	/**
	 * @return sendoid
	 */
	public String getSendoid() {
		return Sendoid;
	}

	/**
	 * @param sendoid
	 *            要设置的 sendoid
	 */
	public void setSendoid(String sendoid) {
		Sendoid = sendoid;
	}

	/**
	 * @return dID
	 */
	public String getDID() {
		return DID;
	}

	/**
	 * @param dID
	 *            要设置的 dID
	 */
	public void setDID(String dID) {
		DID = dID;
	}

	/**
	 * @return orderID
	 */
	public String getOrderID() {
		return OrderID;
	}

	/**
	 * @param orderID
	 *            要设置的 orderID
	 */
	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	/**
	 * @return goodsname
	 */
	public String getGoodsname() {
		return Goodsname;
	}

	/**
	 * @param goodsname
	 *            要设置的 goodsname
	 */
	public void setGoodsname(String goodsname) {
		Goodsname = goodsname;
	}

	/**
	 * @return quantity
	 */
	public int getQuantity() {
		return Quantity;
	}

	/**
	 * @param quantity
	 *            要设置的 quantity
	 */
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}

	/**
	 * @return qty
	 */
	public String getQty() {
		return Qty;
	}

	/**
	 * @param qty
	 *            要设置的 qty
	 */
	public void setQty(String qty) {
		Qty = qty;
	}

	/**
	 * @return warehouse
	 */
	public String getWarehouse() {
		return Warehouse;
	}

	/**
	 * @param warehouse
	 *            要设置的 warehouse
	 */
	public void setWarehouse(String warehouse) {
		Warehouse = warehouse;
	}

	/**
	 * @return packagetype
	 */
	public String getPackagetype() {
		return Packagetype;
	}

	/**
	 * @param packagetype
	 *            要设置的 packagetype
	 */
	public void setPackagetype(String packagetype) {
		Packagetype = packagetype;
	}

	/**
	 * @return transmissiontime
	 */
	public String getTransmissiontime() {
		return Transmissiontime;
	}

	/**
	 * @param transmissiontime
	 *            要设置的 transmissiontime
	 */
	public void setTransmissiontime(String transmissiontime) {
		Transmissiontime = transmissiontime;
	}

	/**
	 * @return fid
	 */
	public String getFid() {
		return fid;
	}

	/**
	 * @param fid
	 *            要设置的 fid
	 */
	public void setFid(String fid) {
		this.fid = fid;
	}

	/**
	 * @return now
	 */
	public String getNow() {
		return now;
	}

	/**
	 * @param now
	 *            要设置的 now
	 */
	public void setNow(String now) {
		this.now = now;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
