/**   
 * @Title: LocationListAdapter.java 
 * @Package com.resmanager.client.user.order 
 * @Description:地址列表适配器
 * @author ShenYang  
 * @date 2015-12-8 下午3:34:01 
 * @version V1.0   
 */
package com.resmanager.client.user.order;

import java.util.List;

import com.amap.api.services.core.PoiItem;
import com.resmanager.client.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @ClassName: LocationListAdapter
 * @Description: 地址列表适配器
 * @author ShenYang
 * @date 2015-12-8 下午3:34:01
 * 
 */
@SuppressLint("InflateParams")
public class LocationListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<PoiItem> poiInfos;

	public LocationListAdapter(Context context, List<PoiItem> poiInfos) {
		this.mInflater = LayoutInflater.from(context);
		this.poiInfos = poiInfos;
	}

	public void loadMore(List<PoiItem> tempPois) {
		for (int i = 0; i < tempPois.size(); i++) {
			this.poiInfos.add(tempPois.get(i));
		}
		notifyDataSetChanged();
	}

	private class ViewHolder {
		TextView company_name_txt, company_address_txt;
	}

	/*
	 * (非 Javadoc) <p>Title: getCount</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return poiInfos.size();
	}

	/*
	 * (非 Javadoc) <p>Title: getItem</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @return
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return poiInfos.get(arg0);
	}

	/*
	 * (非 Javadoc) <p>Title: getItemId</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @return
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	/*
	 * (非 Javadoc) <p>Title: getView</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @param arg1
	 * 
	 * @param arg2
	 * 
	 * @return
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.locaiton_item, null);
			viewHolder.company_address_txt = (TextView) convertView.findViewById(R.id.company_address_txt);
			viewHolder.company_name_txt = (TextView) convertView.findViewById(R.id.company_name_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PoiItem poiInfo = poiInfos.get(pos);
		viewHolder.company_name_txt.setText(poiInfo.getTitle());
		if (poiInfo.getSnippet().equals("")) {
			viewHolder.company_address_txt.setText(poiInfo.getTitle());
		} else {
			viewHolder.company_address_txt.setText(poiInfo.getSnippet());
		}
		return convertView;
	}

}
