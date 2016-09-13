/**   
 * @Title: ContactsUtils.java 
 * @Package com.resmanager.client.system 
 * @Description: 系统通用参数类
 * @author ShenYang  
 * @date 2015-11-30 上午8:55:46 
 * @version V1.0   
 */
package com.resmanager.client.utils;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.PoiItem;
import com.resmanager.client.model.UserDetailModel;

public class ContactsUtils {

	public static boolean ISUPLOAD_LOC = false;// 是否上传经纬度
	// public static final String BAIDU_API_KEY = "Q4BSvV6capalhEVUjqxgRgh1";//
	// 百度开放平台key
	public static final String DB_NAME = "RESMANAGER_DB";
	public static final int POWER_ALL_ORDER = 20000023;// 查看全部订单权限
	public static final int POWER_FLAG = 20000027;// 标签授权操作
	public static final int POWER_KUCUN = 20000028;// 库存查看
	public static final int POWER_DRIVE = 20000029;// 送货
	public static final int POWER_UPLOADING = 20000030;// 卸货
	public static final int POWER_RESOURCE_RECYLE = 20000031;// 回收
	public static final int POWER_MY_ORDER = 20000032;// 我的订单
	public static final int POWER_MY_MESSAGE = 20000033;// 我的消息
	public static final int POWER_MY_TRADE = 20000034;// 我的结算
	public static final int POWER_LOCATION = 20000055;// 位置查看
	public static final int POWER_DAIXIEHUO = 20000067;// 代卸货
	// 订单状态
	public static final int ORDER_ALL = -1;// 所有订单
	public static final int ORDER_DAICHULI = 0;// 待处理
	public static final int ORDER_DAIYUNSONG = 1;// 带运送
	public static final int ORDER_YUNSONGZHONG = 2;// 运送中
	public static final int ORDER_DAISHENHE = 3;// 待审核
	public static final int ORDER_DAIJIESUAN = 4;// 待结算
	public static final int ORDER_YIWANCHENG = 5;// 已完成

	// 订单是否有效
	public static final int ORDER_NOGUOLU = -1;// 全部
	public static final int ORDER_DAIGUOLU = 0;// 待过滤
	public static final int ORDER_YOUXIAO = 1;// 有效
	public static final int ORDER_XIUGAI = 2;// 修改
	public static final int ORDER_XINZENG = 3;// 新增
	public static final int ORDER_WUXIAO = 4;// 无效

	public static final int ORP_NONE = 0x001;// 无操作
	public static final int ORP_REFRESH = 0x002;// 刷新
	public static final int ORP_LOAD = 0x003;// 加载更多
	public static final int FLAG_LOCK_RESULTCODE = 0x004;// 标签上锁
	public static final int FLAG_UNLOCK_RESULTCODE = 0x005;// 标签解锁
	public static final int SCAN_RESULT = 0x006;// 标签扫描
	public static final int CHOOSE_LOCATION_RESULT = 0x007;// 选择地址返回
	public static final int ADD_RECYLE_RESULT = 0x008;// 添加回收返回
	public static final int ADD_RESOURCE_RESULT = 0x009;// 添加卸货发货返回
	public final static int TAKE_PHOTO_RESULT = 0x010;// 拍照返回
	public static final int PHOTO_TYPE_DELIVERY = 0x011;// 发货类型图片
	public static final int PHOTO_TYPE_UPLOADING = 0x012;// 卸货类型图片
	public static final int PHOTO_TYPE_RECYLE = 0x013;// 回收桶类型图片
	public static final int UPLOAD_WITH_SCANCODE = 0x014;// 带二维码上传
	public static final int UPLOAD_NO_SCANCODE = 0x015;// 无二维码上传
	public static final int CHOOSE_GOODS_RESULT = 0x016;// 选择产品返回
	public static final int CHOOSE_LABEL_RESULT = 0x017;// 选择标签返回
	public static final int GRID_TYPE_DELIVERY_UPLOADING = 0x018;// 发货卸货
	public static final int GRID_TYPE_RECYLE = 0x019;// 回收
	public static final int CHOOSE_DRIVER_RESULT = 0x020;// 选择驾驶员
	public static final int SEARCH_ORDER_RESULT = 0x021;// 搜索返回
	public static final int CHOOSE_WARSEHOUSE_RESULT = 0x022;// 选择仓库返回
	public static String USER_KEY = "";// 用户key
	public static UserDetailModel userDetailModel = null;
	public static final int PAGE_SIZE = 15;// 分页条数
	public static final int SEARCH_ROUND_PAGE_SIZE = 30;// 搜索周边分页条数
	public static int SPAN = 60 * 1000;// 定位时间间隔，2秒定位1次
	public static final String BUGLYID = "900015657";
	public static AMapLocation baseAMapLocation;// 定位数据
	public static PoiItem poiItem;// 热点数据
	// public static final String WS_URL =
	// "http://121.40.99.27:90/TMSWebService.asmx";// webService地址
	public static final String WS_URL = "http://nywl112233.6655.la:1050/TMSWebService.asmx";// 正式库
	// webService地址
//	public static final String WS_URL = "http://nywl112233.6655.la:91/TMSWebService.asmx";// 测试库
	// webService地址
	public static final String GET_VERSION_METHOD = "GetVersion";// 获取版本号
	public static final String USER_LOGIN_METHOD = "ManageLogin";// 用户登录
	public static final String GET_ORDER_LIST = "GetOrderInfo";// 获取订单列表
	public static final String GET_USERINFO = "GetUserInfo";// 获取用户详细信息
	public static final String GET_ORDER_DETAIL = "GetOrderDetail";// 获取订单明细
	public static final String GET_ORDER_MODEL = "GetOrder_ModelDetail";// 获取订单明细
	public static final String LABEL_UNLOCKED = "LabelUnlocked";// 标签解锁
	public static final String LABEL_LOCKED = "LabelLocked";// 标签锁定
	public static final String IMAGE_UPLOAD = "Delivery_ScanUpload";// 上传发货图片
	public static final String CONFIRMDELIVERY = "Delivery_Confirm";// 确认发货
	public static final String DELPICDELIVERY = "Delivery_DelPic";// 发货图片信息删除
	public static final String Delivery_DelPicAll = "Delivery_DelPicAll";// 删除所有发货图片
	public static final String DischargeCargo_ScanUpload = "DischargeCargo_ScanUpload";// 上传卸货图片
	public static final String DischargeCargo_DelPicAll = "DischargeCargo_DelPicAll";// 删除所有卸货时上传的图片
	public static final String DischargeCargo_DelPic = "DischargeCargo_DelPic";// 删除卸货图片，单张删除
	public static final String Recovery_ScanUpload = "Recovery_ScanUpload";// 桶回收标签图片扫描上传
	public static final String Discharge_Confirm = "Discharge_Confirm";// 确认卸货
	public static final String Recovery_DelPic = "Recovery_DelPic";// 删除回收图片
	public static final String ViewLabelsRecord = "ViewLabelsRecord";// 查看标签图片信息
	// public static final String Driver_Settlement_Sum =
	// "Driver_Settlement_Sum";// 驾驶员已结算未结算汇总
	public static final String Driver_TradeBill = "Driver_TradeBill";// 驾驶员结算明细
	public static final String GetTjEachOrderStatus = "GetTjEachOrderStatus";// 订单各个状态统计
	public static final String GetGoodsByOrderID = "GetGoodsByOrderID";// 根据订单找产品
	public static final String GetDeliveryLabelsInfo = "GetDeliveryLabelsInfo";// 根据标签获取发货时的信息
	public static final String GetOrderPicList = "GetOrderPicList";// 根据订单编号获取发货时，卸货时，卸货单图片
	public static final String GetCustomerList = "GetCustomerList";// 获取客户列表
	public static final String GetLabelByCustomerList = "GetLabelByCustomerList";// 获取客户那可回收标签
	public static final String Recovery_Confirm = "Recovery_Confirm";// 确认回收
	public static final String GetGoodsCountByOrderID = "GetGoodsCountByOrderID";// 根据订单统计各个产品数量
	public static final String GetOrderTradeBill = "GetOrderTradeBill";// 获取订单结算明细
	public static final String Discharge_Continue = "Discharge_Continue";// 卸货续传
	public static final String GetUnreadMailCount = "GetUnreadMailCount";// 获取未读消息数量
	public static final String GetMailList = "GetMailList";// 获取消息列表
	public static final String UpdateMailRead = "UpdateMailRead";// 更改消息已读标识
	public static final String Stock_CustomerList = "Stock_CustomerList";// 获取客户库存列表
	public static final String Stock_CompanyList = "Stock_CompanyList";// 获取公司库存
	public static final String Stock_CompanyLabelList = "Stock_CompanyLabelList";// 库存对应标签列表
	public static final String GetUserList = "GetUserList";// 获取用户列表
	public static final String UpdateDriver_Track = "UpdateDriver_Track";// 更新軌跡
	public static final String GetDriver_Track = "GetDriver_Track";// 获取位置
	public static final String UpdateMsgtoken = "UpdateMsgtoken";// 更新token
	public static final String RecoveryList = "RecoveryList";// 获取回收列表
	public static final String Recovery_LabelList = "Recovery_LabelList";// 回收标签列表
	public static final String Recovery_PicList = "Recovery_PicList ";// 回收图片列表
	public static final String Recovery_TradeBill = "Recovery_TradeBill  ";// 驾驶员结算明细
	public static final String Driver_Settlement_Sum = "Driver_Settlement_Sum  ";// 驾驶员结算汇总
	public static final String OrderFilter = "OrderFilter";// 订单过滤
	public static final String GetUserList_fp = "GetUserList_fp";// 获取驾驶员
	public static final String Order_fp = "Order_fp";// 订单分配
	public static final String Order_cxfp = "Order_cxfp";// 订单分配撤销
	public static final String Delivery_Continue = "Delivery_Continue";// 发货续传
	public static final String GetDriver_PositionList = "GetDriver_PositionList";// 获取驾驶员位置列表
	public static final String GetOrderInfoByWorkID = "GetOrderInfoByWorkID";// 获取当前车上订单列表
	public static final String GetOrderInfo_ByLabel = "GetOrderInfo_ByLabel";// 根据标签查询曾经使用过该标签的订单
	public static final String Recovery_ByLabel = "Recovery_ByLabel";// 获取标签的回收记录
	public static final String Delivery_Cancel = "Delivery_Cancel";// 发货取消
	public static final String Discharge_Cancel = "Discharge_Cancel";// 卸货取消
	public static final String Warehouse_List = "Warehouse_List";// 仓库列表
	public static final String Recovery_Cancel = "Recovery_Cancel";// 清空回收历史
	public static final String Recovery_Continue = "Recovery_Continue";// 回收续传

}
