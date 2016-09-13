package com.resmanager.client.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.map.GetDriverPositionListAsyncTask.GetUserLocationListListener;
import com.resmanager.client.model.UserLocationListModel;
import com.resmanager.client.model.UserLocationModel;
import com.resmanager.client.user.order.DispatchingOrderList;
import com.resmanager.client.utils.Tools;

@SuppressLint("InflateParams")
public class UserLocationMapView extends TopContainActivity implements OnClickListener, InfoWindowAdapter {
	private MapView mapView;
	private AMap amap;

	private String lat = null;
	private String lng = null;
	private Map<String, String> workIdMaps;
	private Map<String, UserLocationModel> userLocationModelMap;
	private boolean isFirst = true;

	// private UserModel userModel;

	/*
	 * (非 Javadoc) <p>Title: onClick</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_img:
			this.finish();
			break;
		default:
			break;
		}
	}

	/*
	 * (非 Javadoc) <p>Title: getTopView</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getTopView()
	 */
	@Override
	protected View getTopView() {
		View topView = inflater.inflate(R.layout.custom_title_bar, null);
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("所在位置");
		return topView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		workIdMaps = new HashMap<String, String>();
		userLocationModelMap = new HashMap<String, UserLocationModel>();
		mapView = (MapView) centerView.findViewById(R.id.amap_view);
		mapView.onCreate(savedInstanceState);// 必须要写
		amap = mapView.getMap();
		UiSettings mUiSettings = amap.getUiSettings();
		mUiSettings.setTiltGesturesEnabled(false);// 设置地图是否可以倾斜
		mUiSettings.setScaleControlsEnabled(true);// 设置地图默认的比例尺是否显示
		mUiSettings.setZoomControlsEnabled(false);
		// userModel = (UserModel)
		// getIntent().getExtras().getSerializable("userModel");
		// lat = getIntent().getExtras().getString("lat");
		// lng = getIntent().getExtras().getString("lng");
		// lat = "31.863125";
		// lng = "120.482941";
		// 在onCreat方法中给aMap对象赋值

		amap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker m) {
				m.showInfoWindow();
				return true;
			}
		});
		amap.setOnMapLoadedListener(new OnMapLoadedListener() {

			@Override
			public void onMapLoaded() {

				// 设置中心点和缩放比例
				// LatLng marker1 = new LatLng(Double.parseDouble(lat),
				// Double.parseDouble(lng));
				// amap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
				amap.moveCamera(CameraUpdateFactory.zoomTo(12));

			}
		});
		amap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		// getUserLocList();
		timer.schedule(task, 0, 1 * 1000); // 1s后执行task,经过1s再次执行
	}

	@Override
	protected View getCenterView() {
		View contentView = inflater.inflate(R.layout.user_location_map, null);
		return contentView;
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public View getInfoContents(Marker arg0) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
		infoWindow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String driverId = (String) v.getTag();
				if (driverId != null) {
					String workId = workIdMaps.get(driverId);
					if (workId != null && !workId.equals("")) {
						Intent intent = new Intent(UserLocationMapView.this, DispatchingOrderList.class);
						intent.putExtra("WorkId", workId);
						startActivity(intent);
					} else {
						Tools.showToast(UserLocationMapView.this, "该驾驶员当前没有运送中订单");
					}
				}
			}

		});
		render(marker, infoWindow);
		return infoWindow;
	}

	/**
	 * 自定义INFOWINDOW窗口
	 */
	public void render(Marker marker, View view) {
		String deviceId = marker.getSnippet();
		UserLocationModel userLocationModel = userLocationModelMap.get(deviceId);
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		TextView car_id = ((TextView) view.findViewById(R.id.car_id));
		TextView phone_num = ((TextView) view.findViewById(R.id.phone_num));
		TextView package_type = ((TextView) view.findViewById(R.id.package_type));
		TextView order_num = ((TextView) view.findViewById(R.id.order_num));
		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
		TextView status_txt = ((TextView) view.findViewById(R.id.status_txt));
		titleUi.setText(userLocationModel.getDriverName());
		car_id.setText("车牌:" + userLocationModel.getCID());
		phone_num.setText("电话:" + userLocationModel.getPhone());
		snippetUi.setText("工号:" + userLocationModel.getUserName());
		if (userLocationModel.getWorkID() != null && !userLocationModel.getWorkID().equals("")) {
			status_txt.setTextColor(getResources().getColor(R.color.green_color));
			status_txt.setText("[运送中]");
			String isstank = userLocationModel.getIstank();
			if (isstank.equals("1")) {
				package_type.setText("货物类型:油罐");
			} else {
				package_type.setText("货物类型:油桶");
			}
			order_num.setText("订单数:" + userLocationModel.getDdsl());
		} else {
			status_txt.setTextColor(getResources().getColor(R.color.red));
			status_txt.setText("[空闲]");
			package_type.setVisibility(View.GONE);
			order_num.setVisibility(View.GONE);
		}

		view.setTag(deviceId);
	}

	/**
	 * 
	 * @Title: getUserLocList
	 * @Description:获取位置列表
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getUserLocList() {
		GetDriverPositionListAsyncTask getDriverPositionListAsyncTask = new GetDriverPositionListAsyncTask(this, isFirst);
		getDriverPositionListAsyncTask.setGetUserLocationListListener(new GetUserLocationListListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onSuccess(UserLocationListModel userLocationListModel) {
				amap.clear();
				if (userLocationListModel.getData() != null) {
					ArrayList<UserLocationModel> userLocationModels = userLocationListModel.getData();
					for (int i = 0; i < userLocationModels.size(); i++) {
						UserLocationModel userLocationModel = userLocationModels.get(i);
						if (userLocationModel.getLatitude() != null && !userLocationModel.getLatitude().equals("") && userLocationModel.getLongitude() != null
								&& !userLocationModel.getLongitude().equals("")) {
							MarkerOptions mko = new MarkerOptions();

							if (lat == null) {
								lat = userLocationModels.get(i).getLatitude();
								lng = userLocationModels.get(i).getLongitude();
							}

							LatLng latlng = new LatLng(Double.parseDouble(userLocationModel.getLatitude()),
									Double.parseDouble(userLocationModel.getLongitude()));
							mko.position(latlng);
							// mko.title(userLocationModel.getDriverName());
							mko.snippet(userLocationModel.getUserName());
							mko.draggable(false);// 不允许移动标记
							mko.setFlat(false);// 是否贴地显示
							mko.setGps(true);
							mko.visible(true);
							mko.perspective(true);
							mko.anchor(0.5f, 0f);
							int drawableId = R.drawable.free;
							if (userLocationModel.getWorkID() != null && !userLocationModel.getWorkID().equals("")) {
								if (userLocationModel.getIstank().equals("1")) {
									drawableId = R.drawable.car_youguan;
								} else {
									drawableId = R.drawable.car_youtong;
								}
							}
							// mko.icon(BitmapDescriptorFactory.fromResource(drawableId));
							View view = View.inflate(UserLocationMapView.this, R.layout.define_map_marker, null);
							ImageView marker_img = (ImageView) view.findViewById(R.id.marker_img);
							marker_img.setImageResource(drawableId);
							TextView car_num = (TextView) view.findViewById(R.id.car_num);
							TextView order_num = (TextView) view.findViewById(R.id.order_num);
							car_num.setText("[" + userLocationModel.getCID() + "]");
							order_num.setText("订单数:" + userLocationModel.getDdsl());
							Bitmap bitmap = convertViewToBitmap(view);
							mko.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
							if (!workIdMaps.containsKey(userLocationModel.getUserName())) {
								workIdMaps.put(userLocationModel.getUserName(), userLocationModel.getWorkID());
							}
							if (!userLocationModelMap.containsKey(userLocationModel.getUserName())) {
								userLocationModelMap.put(userLocationModel.getUserName(), userLocationModel);
							}
							amap.addMarker(mko);

						}

					}
					if (isFirst) {
						LatLng marker1 = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
						amap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
					}
					isFirst = false;
				}
			}

			@Override
			public void onFaile() {
				Tools.showToast(UserLocationMapView.this, "位置信息获取失败，请检查网络");
			}
		});
		getDriverPositionListAsyncTask.execute();
	}

	/**
	 * 在地图上画marker
	 * 
	 * @param point
	 *            marker坐标点位置（example:LatLng point = new LatLng(39.963175,
	 *            116.400244); ）
	 * @param markerIcon
	 *            图标
	 * @return Marker对象
	 */
	private Marker drawMarkerOnMap(MarkerOptions mko) {
		if (amap != null && mko != null) {
			Marker marker = amap.addMarker(mko);
			return marker;
		}
		return null;
	}

	// view 转bitmap
	private Bitmap convertViewToBitmap(View view) {
		view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			getUserLocList();
			super.handleMessage(msg);
		};
	};
	Timer timer = new Timer();
	TimerTask task = new TimerTask() {

		@Override
		public void run() {
			// 需要做的事:发送消息
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};
}
