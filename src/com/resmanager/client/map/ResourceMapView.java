package com.resmanager.client.map;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.resmanager.client.utils.Tools;

@SuppressLint("InflateParams")
public class ResourceMapView extends TopContainActivity implements OnClickListener, InfoWindowAdapter {
	private MapView mapView;
	private AMap amap;
	private String lat_fh;
	private String lng_fh;
	private String lat_xh;
	private String lng_xh;
	private String locationFhStr;
	private String locationXhStr;
	private String locationAddress_fh;
	private String locationAddress_xh;
	private int type;
	private TextView delivery_location_txt, uploading_location_txt;

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
		case R.id.delivery_location_txt:
			LatLng marker1 = new LatLng(Double.parseDouble(lat_fh), Double.parseDouble(lng_fh));
			amap.animateCamera(CameraUpdateFactory.changeLatLng(marker1));
			break;
		case R.id.uploading_location_txt:
			LatLng marker2 = new LatLng(Double.parseDouble(lat_xh), Double.parseDouble(lng_xh));
			amap.animateCamera(CameraUpdateFactory.changeLatLng(marker2));
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

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mapView = (MapView) centerView.findViewById(R.id.amap_view);
		delivery_location_txt = (TextView) centerView.findViewById(R.id.delivery_location_txt);
		uploading_location_txt = (TextView) centerView.findViewById(R.id.uploading_location_txt);
		delivery_location_txt.setOnClickListener(this);
		uploading_location_txt.setOnClickListener(this);
		mapView.onCreate(savedInstanceState);// 必须要写
		amap = mapView.getMap();
		UiSettings mUiSettings = amap.getUiSettings();
		mUiSettings.setTiltGesturesEnabled(false);// 设置地图是否可以倾斜
		mUiSettings.setScaleControlsEnabled(true);// 设置地图默认的比例尺是否显示
		mUiSettings.setZoomControlsEnabled(false);
		type = getIntent().getExtras().getInt("type");
		lat_fh = getIntent().getExtras().getString("lat_fh");
		lng_fh = getIntent().getExtras().getString("lng_fh");
		lat_xh = getIntent().getExtras().getString("lat_xh");
		lng_xh = getIntent().getExtras().getString("lng_xh");
		if (lat_fh == null || lng_fh == null || lat_xh == null || lng_xh == null) {
			Tools.showToast(this, "地址位置获取失败");
			this.finish();
		} else {
			locationFhStr = getIntent().getExtras().getString("locationFhStr");
			locationXhStr = getIntent().getExtras().getString("locationXhStr");
			locationAddress_fh = getIntent().getExtras().getString("locationAddress_fh");
			locationAddress_xh = getIntent().getExtras().getString("locationAddress_xh");
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

					switch (type) {
					case 1:
						// 设置中心点和缩放比例
						LatLng marker1 = new LatLng(Double.parseDouble(lat_fh), Double.parseDouble(lng_fh));
						amap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
						amap.moveCamera(CameraUpdateFactory.zoomTo(17));
						break;
					case 2:
						LatLng marker2 = new LatLng(Double.parseDouble(lat_xh), Double.parseDouble(lng_xh));
						amap.moveCamera(CameraUpdateFactory.changeLatLng(marker2));
						amap.moveCamera(CameraUpdateFactory.zoomTo(17));
						break;
					default:
						break;
					}

				}
			});
			amap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
			MarkerOptions mko = new MarkerOptions();
			mko.position(new LatLng(Double.parseDouble(lat_fh), Double.parseDouble(lng_fh)));
			mko.title(locationFhStr);
			mko.snippet(locationAddress_fh);
			mko.draggable(false);// 不允许移动标记
			mko.setFlat(false);// 是否贴地显示
			mko.visible(true);
			mko.perspective(true);
			mko.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_youguan));
			amap.addMarker(mko);

			MarkerOptions mko1 = new MarkerOptions();
			mko1.position(new LatLng(Double.parseDouble(lat_xh), Double.parseDouble(lng_xh)));
			mko1.title(locationXhStr);
			mko1.snippet(locationAddress_xh);
			mko1.draggable(false);// 不允许移动标记
			mko1.setFlat(false);// 是否贴地显示
			mko1.visible(true);
			mko1.perspective(true);
			mko1.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_youguan));
			amap.addMarker(mko1);
		}
		// m.showInfoWindow();
	}

	@Override
	protected View getCenterView() {
		View contentView = inflater.inflate(R.layout.resource_map, null);
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

		render(marker, infoWindow);
		return infoWindow;
	}

	/**
	 * 自定义INFOWINDOW窗口
	 */
	public void render(Marker marker, View view) {
		String title = marker.getTitle();
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		if (title != null) {
			titleUi.setText(title);
		} else {
			titleUi.setText("");
		}
		String snippet = marker.getSnippet();
		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
		if (snippet != null) {
			snippetUi.setText(snippet);
		} else {
			snippetUi.setText("");
		}
	}

}
