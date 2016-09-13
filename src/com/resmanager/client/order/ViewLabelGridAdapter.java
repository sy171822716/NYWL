package com.resmanager.client.order;

import java.util.ArrayList;

import com.resmanager.client.R;
import com.resmanager.client.model.OrderPicModel;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewLabelGridAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<OrderPicModel> orderPicModels;
	private Context context;
	private int type;// 1：发货，2：卸货

	public ViewLabelGridAdapter(Context context, ArrayList<OrderPicModel> orderPicModels, int type) {
		this.context = context;
		this.orderPicModels = orderPicModels;
		this.mInflater = LayoutInflater.from(context);
		this.type = type;
	}

	private class ViewHolder {
		private ImageView goods_img;
		private TextView label_code_txt, goods_type_name_txt, goods_package_type_txt;
	}

	@Override
	public int getCount() {
		return this.orderPicModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		return orderPicModels.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.delivery_source_grid_item, parent, false);
			holder = new ViewHolder();
			holder.goods_type_name_txt = (TextView) convertView.findViewById(R.id.goods_type_name_txt);
			holder.goods_img = (ImageView) convertView.findViewById(R.id.goods_img);
			holder.label_code_txt = (TextView) convertView.findViewById(R.id.label_code_txt);
			holder.goods_package_type_txt = (TextView) convertView.findViewById(R.id.goods_package_type_txt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		OrderPicModel orderPicModel = orderPicModels.get(position);
		holder.goods_type_name_txt.setText(orderPicModel.getGoodsName());
		String lable = orderPicModel.getLabelCode();
		holder.label_code_txt.setText(lable.substring(5, lable.lastIndexOf("-")));
		holder.goods_package_type_txt.setText(orderPicModel.getPackagetype());
		switch (type) {
		case 1:
			// 发货
			Picasso.with(context).load(orderPicModel.getThumb_Path_fh()).error(R.drawable.default_img).placeholder(R.drawable.default_img)
					.into(holder.goods_img);
			break;
		case 2:
			// 卸货
			Picasso.with(context).load(orderPicModel.getThumb_Path_xh()).error(R.drawable.default_img).placeholder(R.drawable.default_img)
					.into(holder.goods_img);
			break;
		default:
			break;
		}
		// ViewLabelModel viewLabelModel = viewLabelModels.get(position);
		// holder.scan_code_btn.setVisibility(View.GONE);
		// String labelStr = viewLabelModel.getLabelCode();
		// if (labelStr.contains("无")) {
		// labelStr = "无标签";
		// } else {
		// labelStr = labelStr.substring(5, labelStr.lastIndexOf("-"));
		// }
		// holder.scan_code_txt.setText(labelStr);
		// Picasso.with(context).load(viewLabelModel.getOriginal_Path()).error(R.drawable.default_img).placeholder(R.drawable.default_img).into(holder.image);

		return convertView;
	}

}
