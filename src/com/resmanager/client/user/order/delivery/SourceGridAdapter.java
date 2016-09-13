package com.resmanager.client.user.order.delivery;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.model.ScanBimpModel;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.squareup.picasso.Picasso;

@SuppressLint("InflateParams")
public class SourceGridAdapter extends BaseAdapter {
	private List<ScanBimpModel> scanBimpModels;
	private LayoutInflater mInflater;
	private int gridType;// GRID_TYPE_DELIVERY_UPLOADING 发货卸货 GRID_TYPE_RECYLE
							// 回收
	private Context context;

	public SourceGridAdapter(Context context, List<ScanBimpModel> scanBimpModels, int gridType) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.scanBimpModels = scanBimpModels;
		this.gridType = gridType;
	}

	private class ViewHolder {
		public ImageView goods_img;
		private TextView label_code_txt, goods_type_name_txt, goods_package_type_txt;
	}

	@Override
	public int getCount() {
		return scanBimpModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		return scanBimpModels.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.delivery_source_grid_item, null);
			viewHolder = new ViewHolder();
			viewHolder.goods_img = (ImageView) convertView.findViewById(R.id.goods_img);
			viewHolder.goods_package_type_txt = (TextView) convertView.findViewById(R.id.goods_package_type_txt);
			viewHolder.goods_type_name_txt = (TextView) convertView.findViewById(R.id.goods_type_name_txt);
			viewHolder.label_code_txt = (TextView) convertView.findViewById(R.id.label_code_txt);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ScanBimpModel scanBimpModel = scanBimpModels.get(pos);
		String labelCode = scanBimpModel.getLabelCode();// 加密后的二维码内容，解密后传递给接口进行校验
		if (scanBimpModel.getThumbPath() != null && !scanBimpModel.getThumbPath().equals("")) {
			// 有缩略图，代表该图片是从上一次上传获取
			String path = scanBimpModel.getThumbPath();
			Picasso.with(context).load(path).placeholder(R.drawable.default_img).error(R.drawable.default_img)
					.into(viewHolder.goods_img);
		} else {
			viewHolder.goods_img.setImageBitmap(scanBimpModel.getBmp());
		}
		switch (gridType) {
		case ContactsUtils.GRID_TYPE_DELIVERY_UPLOADING:
			viewHolder.label_code_txt.setText(Tools.getShowLabelCode(labelCode));
			viewHolder.goods_type_name_txt.setText(scanBimpModel.getResourceTypeName());
			viewHolder.goods_package_type_txt.setText(scanBimpModel.getPackageType());
			viewHolder.goods_type_name_txt.setVisibility(View.VISIBLE);
			viewHolder.goods_package_type_txt.setVisibility(View.VISIBLE);
			break;
		case ContactsUtils.GRID_TYPE_RECYLE:
			viewHolder.label_code_txt.setText(Tools.getShowLabelCodes(labelCode));
			viewHolder.goods_type_name_txt.setVisibility(View.GONE);
			viewHolder.goods_package_type_txt.setVisibility(View.GONE);
			break;
		default:
			break;
		}

		return convertView;
	}
}
