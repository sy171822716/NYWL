/**   
 * @Title: Order.java 
 * @Package com.resmanager.client.model 
 * @Description: 订单实体
 * @author ShenYang  
 * @date 2015-12-1 上午10:06:43 
 * @version V1.0   
 */
package com.resmanager.client.model;

/**
 * @ClassName: Order
 * @Description: 订单实体
 * @author ShenYang
 * @date 2015-12-1 上午10:06:43
 * 
 */
public class Order extends ResultModel {
	private static final long serialVersionUID = 1L;
	private String RowNumber;
	private String OrderID;// 内部生成订单编号，只用于数据库关联
	private String Sendoid;
	private String Businessdate;
	private String Referenceinfo;
	private String Sourceoid;
	private String Saleoid;// 核心单据编号
	private String Orderdate;
	private String Shippingweight;
	private String ordercustomer;
	private String saler;
	private String Shippingaddress;
	private String Town;
	private String Deliverystandards;
	private String Requestarrivedate;
	private int Quantity;
	private String Transmissiontime;
	private String flag;
	private String flastupdatetime;
	private String now;
	private String DeliveryDate;
	private String DeliveryTel;
	private String DeliveryMan;
	private String SN;
	private String orgnumber;
	private String Longitude;
	private String Latitude;
	private String OrderStateCode;
	private String OrderStateName;
	private String AreaPrice_ID;
	private String AreaPrice;
	private String id;
	private String ordermoney;// 订单价格
	private String DriverID = "";// 驾驶员ID
	private String CarNumber = "";// 车牌
	private String DriverName = "";// 驾驶员姓名
	private String DriverTel = "";// 驾驶员电话
	private String shippington = "";// 汽车吨位
	private String ArrangeNum = "";// 排车号
	private String ArrangeDate = "";// 排车时间
	private String Delivery_Date;// 发货时间
	private String TradeTime;// 结算时间
	private String Discharge_Date;// 卸货时间
	private String DischargeAudit_Date;// 卸货审核时间
	private String RDT;// 派单日期
	private String TotalMoney;// 总运费
	private String HasSettled;// 已结算
	private String NotSettled;// 未结算

	/**
	 * @return delivery_Date
	 */
	public String getDelivery_Date() {
		return Delivery_Date;
	}

	/**
	 * @param delivery_Date
	 *            要设置的 delivery_Date
	 */
	public void setDelivery_Date(String delivery_Date) {
		Delivery_Date = delivery_Date;
	}

	/**
	 * @return tradeTime
	 */
	public String getTradeTime() {
		return TradeTime;
	}

	/**
	 * @param tradeTime
	 *            要设置的 tradeTime
	 */
	public void setTradeTime(String tradeTime) {
		TradeTime = tradeTime;
	}

	/**
	 * @return discharge_Date
	 */
	public String getDischarge_Date() {
		return Discharge_Date;
	}

	/**
	 * @param discharge_Date
	 *            要设置的 discharge_Date
	 */
	public void setDischarge_Date(String discharge_Date) {
		Discharge_Date = discharge_Date;
	}

	/**
	 * @return dischargeAudit_Date
	 */
	public String getDischargeAudit_Date() {
		return DischargeAudit_Date;
	}

	/**
	 * @param dischargeAudit_Date
	 *            要设置的 dischargeAudit_Date
	 */
	public void setDischargeAudit_Date(String dischargeAudit_Date) {
		DischargeAudit_Date = dischargeAudit_Date;
	}

	/**
	 * @return ordermoney
	 */
	public String getOrdermoney() {
		return ordermoney;
	}

	/**
	 * @param ordermoney
	 *            要设置的 ordermoney
	 */
	public void setOrdermoney(String ordermoney) {
		this.ordermoney = ordermoney;
	}

	/**
	 * @return workID
	 */
	public String getWorkID() {
		return WorkID;
	}

	/**
	 * @param workID
	 *            要设置的 workID
	 */
	public void setWorkID(String workID) {
		WorkID = workID;
	}

	private String WorkID;

	/**
	 * @return rowNumber
	 */
	public String getRowNumber() {
		return RowNumber;
	}

	/**
	 * @param rowNumber
	 *            要设置的 rowNumber
	 */
	public void setRowNumber(String rowNumber) {
		RowNumber = rowNumber;
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
	 * @return businessdate
	 */
	public String getBusinessdate() {
		return Businessdate;
	}

	/**
	 * @param businessdate
	 *            要设置的 businessdate
	 */
	public void setBusinessdate(String businessdate) {
		Businessdate = businessdate;
	}

	/**
	 * @return referenceinfo
	 */
	public String getReferenceinfo() {
		return Referenceinfo;
	}

	/**
	 * @param referenceinfo
	 *            要设置的 referenceinfo
	 */
	public void setReferenceinfo(String referenceinfo) {
		Referenceinfo = referenceinfo;
	}

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
	 * @return saleoid
	 */
	public String getSaleoid() {
		return Saleoid;
	}

	/**
	 * @return totalMoney
	 */
	public String getTotalMoney() {
		return TotalMoney;
	}

	/**
	 * @param totalMoney
	 *            要设置的 totalMoney
	 */
	public void setTotalMoney(String totalMoney) {
		TotalMoney = totalMoney;
	}

	/**
	 * @return hasSettled
	 */
	public String getHasSettled() {
		return HasSettled;
	}

	/**
	 * @param hasSettled
	 *            要设置的 hasSettled
	 */
	public void setHasSettled(String hasSettled) {
		HasSettled = hasSettled;
	}

	/**
	 * @return notSettled
	 */
	public String getNotSettled() {
		return NotSettled;
	}

	/**
	 * @param notSettled
	 *            要设置的 notSettled
	 */
	public void setNotSettled(String notSettled) {
		NotSettled = notSettled;
	}

	/**
	 * @param saleoid
	 *            要设置的 saleoid
	 */
	public void setSaleoid(String saleoid) {
		Saleoid = saleoid;
	}

	/**
	 * @return orderdate
	 */
	public String getOrderdate() {
		return Orderdate;
	}

	/**
	 * @param orderdate
	 *            要设置的 orderdate
	 */
	public void setOrderdate(String orderdate) {
		Orderdate = orderdate;
	}

	/**
	 * @return shippingweight
	 */
	public String getShippingweight() {
		return Shippingweight;
	}

	/**
	 * @param shippingweight
	 *            要设置的 shippingweight
	 */
	public void setShippingweight(String shippingweight) {
		Shippingweight = shippingweight;
	}

	/**
	 * @return ordercustomer
	 */
	public String getOrdercustomer() {
		return ordercustomer;
	}

	/**
	 * @param ordercustomer
	 *            要设置的 ordercustomer
	 */
	public void setOrdercustomer(String ordercustomer) {
		this.ordercustomer = ordercustomer;
	}

	/**
	 * @return saler
	 */
	public String getSaler() {
		return saler;
	}

	/**
	 * @param saler
	 *            要设置的 saler
	 */
	public void setSaler(String saler) {
		this.saler = saler;
	}

	/**
	 * @return shippingaddress
	 */
	public String getShippingaddress() {
		return Shippingaddress;
	}

	/**
	 * @param shippingaddress
	 *            要设置的 shippingaddress
	 */
	public void setShippingaddress(String shippingaddress) {
		Shippingaddress = shippingaddress;
	}

	/**
	 * @return town
	 */
	public String getTown() {
		return Town;
	}

	/**
	 * @param town
	 *            要设置的 town
	 */
	public void setTown(String town) {
		Town = town;
	}

	/**
	 * @return deliverystandards
	 */
	public String getDeliverystandards() {
		return Deliverystandards;
	}

	/**
	 * @param deliverystandards
	 *            要设置的 deliverystandards
	 */
	public void setDeliverystandards(String deliverystandards) {
		Deliverystandards = deliverystandards;
	}

	/**
	 * @return requestarrivedate
	 */
	public String getRequestarrivedate() {
		return Requestarrivedate;
	}

	/**
	 * @param requestarrivedate
	 *            要设置的 requestarrivedate
	 */
	public void setRequestarrivedate(String requestarrivedate) {
		Requestarrivedate = requestarrivedate;
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
	 * @return flag
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param flag
	 *            要设置的 flag
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * @return flastupdatetime
	 */
	public String getFlastupdatetime() {
		return flastupdatetime;
	}

	/**
	 * @param flastupdatetime
	 *            要设置的 flastupdatetime
	 */
	public void setFlastupdatetime(String flastupdatetime) {
		this.flastupdatetime = flastupdatetime;
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

	/**
	 * @return deliveryDate
	 */
	public String getDeliveryDate() {
		return DeliveryDate;
	}

	/**
	 * @param deliveryDate
	 *            要设置的 deliveryDate
	 */
	public void setDeliveryDate(String deliveryDate) {
		DeliveryDate = deliveryDate;
	}

	/**
	 * @return deliveryMan
	 */
	public String getDeliveryMan() {
		return DeliveryMan;
	}

	/**
	 * @param deliveryMan
	 *            要设置的 deliveryMan
	 */
	public void setDeliveryMan(String deliveryMan) {
		DeliveryMan = deliveryMan;
	}

	/**
	 * @return sN
	 */
	public String getSN() {
		return SN;
	}

	/**
	 * @param sN
	 *            要设置的 sN
	 */
	public void setSN(String sN) {
		SN = sN;
	}

	/**
	 * @return orgnumber
	 */
	public String getOrgnumber() {
		return orgnumber;
	}

	/**
	 * @param orgnumber
	 *            要设置的 orgnumber
	 */
	public void setOrgnumber(String orgnumber) {
		this.orgnumber = orgnumber;
	}

	/**
	 * @return longitude
	 */
	public String getLongitude() {
		return Longitude;
	}

	/**
	 * @param longitude
	 *            要设置的 longitude
	 */
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	/**
	 * @return latitude
	 */
	public String getLatitude() {
		return Latitude;
	}

	/**
	 * @param latitude
	 *            要设置的 latitude
	 */
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	/**
	 * @return orderStateCode
	 */
	public String getOrderStateCode() {
		return OrderStateCode;
	}

	/**
	 * @param orderStateCode
	 *            要设置的 orderStateCode
	 */
	public void setOrderStateCode(String orderStateCode) {
		OrderStateCode = orderStateCode;
	}

	/**
	 * @return orderStateName
	 */
	public String getOrderStateName() {
		return OrderStateName;
	}

	/**
	 * @param orderStateName
	 *            要设置的 orderStateName
	 */
	public void setOrderStateName(String orderStateName) {
		OrderStateName = orderStateName;
	}

	/**
	 * @return areaPrice_ID
	 */
	public String getAreaPrice_ID() {
		return AreaPrice_ID;
	}

	/**
	 * @param areaPrice_ID
	 *            要设置的 areaPrice_ID
	 */
	public void setAreaPrice_ID(String areaPrice_ID) {
		AreaPrice_ID = areaPrice_ID;
	}

	/**
	 * @return areaPrice
	 */
	public String getAreaPrice() {
		return AreaPrice;
	}

	/**
	 * @param areaPrice
	 *            要设置的 areaPrice
	 */
	public void setAreaPrice(String areaPrice) {
		AreaPrice = areaPrice;
	}

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            要设置的 id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return driverID
	 */
	public String getDriverID() {
		return DriverID;
	}

	/**
	 * @param driverID
	 *            要设置的 driverID
	 */
	public void setDriverID(String driverID) {
		DriverID = driverID;
	}

	/**
	 * @return carNumber
	 */
	public String getCarNumber() {
		return CarNumber;
	}

	/**
	 * @param carNumber
	 *            要设置的 carNumber
	 */
	public void setCarNumber(String carNumber) {
		CarNumber = carNumber;
	}

	/**
	 * @return driverName
	 */
	public String getDriverName() {
		return DriverName;
	}

	/**
	 * @param driverName
	 *            要设置的 driverName
	 */
	public void setDriverName(String driverName) {
		DriverName = driverName;
	}

	/**
	 * @return driverTel
	 */
	public String getDriverTel() {
		return DriverTel;
	}

	/**
	 * @param driverTel
	 *            要设置的 driverTel
	 */
	public void setDriverTel(String driverTel) {
		DriverTel = driverTel;
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

	/**
	 * @return arrangeNum
	 */
	public String getArrangeNum() {
		return ArrangeNum;
	}

	/**
	 * @param arrangeNum
	 *            要设置的 arrangeNum
	 */
	public void setArrangeNum(String arrangeNum) {
		ArrangeNum = arrangeNum;
	}

	/**
	 * @return arrangeDate
	 */
	public String getArrangeDate() {
		return ArrangeDate;
	}

	/**
	 * @param arrangeDate
	 *            要设置的 arrangeDate
	 */
	public void setArrangeDate(String arrangeDate) {
		ArrangeDate = arrangeDate;
	}

	public String getDeliveryTel() {
		return DeliveryTel;
	}

	public void setDeliveryTel(String deliveryTel) {
		DeliveryTel = deliveryTel;
	}

	public String getRDT() {
		return RDT;
	}

	public void setRDT(String rDT) {
		RDT = rDT;
	}

}
