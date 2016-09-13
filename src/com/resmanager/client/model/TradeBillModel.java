/**   
 * @Title: TradeBillModel.java 
 * @Package com.resmanager.client.model 
 * @Description: 结算单类
 * @author ShenYang   
 * @date 2015-12-19 上午9:46:17 
 * @version V1.0   
 */
package com.resmanager.client.model;

import java.io.Serializable;

/**
 * @ClassName: TradeBillModel
 * @Description: 结算单类
 * @author ShenYang
 * @date 2015-12-19 上午9:46:17
 * 
 */
public class TradeBillModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String BillId;
	private String ReceiverId;
	private String ReceiverName;
	private String TradeMoney;
	private String TradeTime;
	private String OrderID;
	private String IsDel;
	private String ReMark;

	public String getBillId() {
		return BillId;
	}

	public void setBillId(String billId) {
		BillId = billId;
	}

	public String getReceiverId() {
		return ReceiverId;
	}

	public void setReceiverId(String receiverId) {
		ReceiverId = receiverId;
	}

	public String getReceiverName() {
		return ReceiverName;
	}

	public void setReceiverName(String receiverName) {
		ReceiverName = receiverName;
	}

	public String getTradeMoney() {
		return TradeMoney;
	}

	public void setTradeMoney(String tradeMoney) {
		TradeMoney = tradeMoney;
	}

	public String getTradeTime() {
		return TradeTime;
	}

	public void setTradeTime(String tradeTime) {
		TradeTime = tradeTime;
	}

	public String getOrderID() {
		return OrderID;
	}

	public void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public String getIsDel() {
		return IsDel;
	}

	public void setIsDel(String isDel) {
		IsDel = isDel;
	}

	public String getReMark() {
		return ReMark;
	}

	public void setReMark(String reMark) {
		ReMark = reMark;
	}
}
