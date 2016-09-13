/**
 * 
 */
package com.resmanager.client.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.resmanager.client.map.UpdateUserLocationAsyncTask;

/**
 * 辅助工具类
 * 
 * @创建时间： 2015年11月24日 上午11:46:50
 * @项目名称： AMapLocationDemo2.x
 * @author hongming.wang
 * @文件名称: Utils.java
 * @类型名称: Utils
 */
@SuppressLint("HandlerLeak")
public class LocationUtils implements AMapLocationListener {
	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	/**
	 * 开始定位
	 */
	public final static int MSG_LOCATION_START = 0;
	/**
	 * 定位完成
	 */
	public final static int MSG_LOCATION_FINISH = 1;
	/**
	 * 停止定位
	 */
	public final static int MSG_LOCATION_STOP = 2;
	private PoiSearchListener poiSearchListener;
	Handler mHandler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			// 开始定位
			case LocationUtils.MSG_LOCATION_START:
				break;
			// 定位完成
			case LocationUtils.MSG_LOCATION_FINISH:
				AMapLocation loc = (AMapLocation) msg.obj;
				ContactsUtils.baseAMapLocation = loc;
				if (loc != null) {
					if (ContactsUtils.USER_KEY != null && ContactsUtils.userDetailModel != null) {
						UpdateUserLocationAsyncTask updateUserLocationAsyncTask = new UpdateUserLocationAsyncTask(String.valueOf(loc.getLongitude()),
								String.valueOf(loc.getLatitude()));
						updateUserLocationAsyncTask.execute();// 更新用户坐标
					}
					if (ContactsUtils.baseAMapLocation == null) {
						ContactsUtils.baseAMapLocation = loc;
					} else if (ContactsUtils.baseAMapLocation.getLatitude() != loc.getLatitude()
							|| ContactsUtils.baseAMapLocation.getLongitude() != loc.getLongitude()) {
						searchRound(context, ContactsUtils.baseAMapLocation.getLatitude(), ContactsUtils.baseAMapLocation.getLongitude(), 0,
								ContactsUtils.ORP_NONE, new PoiSearchListener() {

									@Override
									public void onPoiSearched(PoiResult pois, int rcode, int orpType) {
										if (rcode == 0 && pois.getPois().size() > 0) {
											ContactsUtils.poiItem = pois.getPois().get(0);
										}
									}
								});
					}
					if (getLocationListener != null) {
						getLocationListener.onGetSucceed(loc);
					}
				} else {
					if (getLocationListener != null) {
						getLocationListener.onGetFailed();
					}
				}
				// 停止定位
			case LocationUtils.MSG_LOCATION_STOP:
				break;
			default:
				break;
			}
		};
	};
	private Context context;
	private static GetLocationListener getLocationListener = null;

	public interface GetLocationListener {
		void onGetSucceed(AMapLocation loc);

		void onGetFailed();
	}

	public LocationUtils(Context context) {
		this.context = context;
		locationClient = new AMapLocationClient(context);
		locationOption = new AMapLocationClientOption();
		// 设置定位模式为高精度模式
		locationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// 设置定位监听
		locationClient.setLocationListener(this);
		initOption();
		// 设置定位参数
		locationClient.setLocationOption(locationOption);
	}

	@Override
	public void onLocationChanged(AMapLocation loc) {
		if (null != loc) {
			Message msg = mHandler.obtainMessage();
			msg.obj = loc;
			msg.what = LocationUtils.MSG_LOCATION_FINISH;
			mHandler.sendMessage(msg);
		} else {
			if (getLocationListener != null) {
				getLocationListener.onGetFailed();
			}
		}
	}

	// 根据控件的选择，重新设置定位参数
	private void initOption() {
		// 设置是否需要显示地址信息
		locationOption.setNeedAddress(true);
		/**
		 * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位 注意：只有在高精度模式下的单次定位有效，其他方式无效
		 */
		locationOption.setGpsFirst(true);
		// 设置发送定位请求的时间间隔,最小值为2000，如果小于2000，按照2000算
		locationOption.setInterval(ContactsUtils.SPAN);// 定位间隔
	}

	/**
	 * 
	 * @Title: startLoc
	 * @Description:开始定位
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void startLoc() {
		// initOption();

		// 启动定位
		locationClient.startLocation();
		mHandler.sendEmptyMessage(LocationUtils.MSG_LOCATION_START);
	}

	/**
	 * 
	 * @Description: 初始化location信息
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void destory() {
		if (null != locationClient) {
			/**
			 * 如果AMapLocationClient是在当前Activity实例化的，
			 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
			 */
			locationClient.onDestroy();
			locationClient = null;
			locationOption = null;
		}
	}

	/**
	 * 
	 * @Title: searchRound
	 * @Description: 搜索周边
	 * @param @param context
	 * @param @param Lat
	 * @param @param lng
	 * @param @param currentPage 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void searchRound(Context context, double lat, double lng, int currentPage, final int orpType, PoiSearchListener poiSearchListener) {
		this.poiSearchListener = poiSearchListener;
		String searchTypeStr = "汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";
		PoiSearch.Query query = new PoiSearch.Query("", searchTypeStr, ContactsUtils.baseAMapLocation.getCityCode());
		query.setPageSize(ContactsUtils.SEARCH_ROUND_PAGE_SIZE);
		query.setPageNum(currentPage);// 设置查第一页
		PoiSearch poiSearch = new PoiSearch(context, query);
		poiSearch.setBound(new SearchBound(new LatLonPoint(lat, lng), 500));// 设置周边搜索的中心点以及区域
		poiSearch.setOnPoiSearchListener(new OnPoiSearchListener() {

			@Override
			public void onPoiSearched(PoiResult poiResult, int rCode) {
				// rCode返回结果成功或者失败的响应码。0为成功，其他为失败（详细信息参见网站开发指南-错误码对照表）
				LocationUtils.this.poiSearchListener.onPoiSearched(poiResult, rCode, orpType);
			}
		});// 设置数据返回的监听器
		poiSearch.searchPOIAsyn();// 开始搜索
	}

	public static GetLocationListener getGetLocationListener() {
		return getLocationListener;
	}

	public static void setGetLocationListener(GetLocationListener getLocationListener) {
		LocationUtils.getLocationListener = getLocationListener;
	}

	public interface PoiSearchListener {
		public void onPoiSearched(PoiResult arg0, int arg1, int orpType);
	}

	// /**
	// * 根据定位结果返回定位信息的字符串
	// *
	// * @param loc
	// * @return
	// */
	// public synchronized static String getLocationStr(AMapLocation location) {
	// if (null == location) {
	// return null;
	// }
	// ContactsUtils.baseAMapLocation = location;
	// StringBuffer sb = new StringBuffer();
	// // errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
	// if (location.getErrorCode() == 0) {
	// sb.append("定位成功" + "\n");
	// sb.append("定位类型: " + location.getLocationType() + "\n");
	// sb.append("经    度    : " + location.getLongitude() + "\n");
	// sb.append("纬    度    : " + location.getLatitude() + "\n");
	// sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
	// sb.append("提供者    : " + location.getProvider() + "\n");
	//
	// if
	// (location.getProvider().equalsIgnoreCase(android.location.LocationManager.GPS_PROVIDER))
	// {
	// // 以下信息只有提供者是GPS时才会有
	// sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
	// sb.append("角    度    : " + location.getBearing() + "\n");
	// // 获取当前提供定位服务的卫星个数
	// sb.append("星    数    : " + location.getExtras().getInt("satellites", 0) +
	// "\n");
	// } else {
	// // 供者是GPS时是没有以下信息的
	// sb.append("国    家    : " + location.getCountry() + "\n");
	// sb.append("省            : " + location.getProvince() + "\n");
	// sb.append("市            : " + location.getCity() + "\n");
	// sb.append("区            : " + location.getDistrict() + "\n");
	// sb.append("地    址    : " + location.getAddress() + "\n");
	// }
	// } else {
	// // 定位失败
	// sb.append("定位为失败" + "\n");
	// sb.append("错误码:" + location.getErrorCode() + "\n");
	// sb.append("错误信息:" + location.getErrorInfo() + "\n");
	// sb.append("错误描述:" + location.getLocationDetail() + "\n");
	// }
	// return sb.toString();
	// }
}
