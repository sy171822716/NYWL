package com.resmanager.client.order;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.map.ResourceMapView;
import com.resmanager.client.model.OrderPicListModel;
import com.resmanager.client.model.OrderPicModel;
import com.resmanager.client.photo.ImagePagerActivity;
import com.resmanager.client.view.DefineGridView;
import com.squareup.picasso.Picasso;

@SuppressLint("InflateParams")
public class ViewLabelActivity extends TopContainActivity implements OnClickListener {
	private OrderPicListModel orderPicListModel;
	private ImageView uploading_img;
	private TextView delivery_location_txt, uploading_location_txt;
	private DefineGridView delivery_goods_img_grid, uploading_goods_img_grid;
	private String locationAddress_fh = "", locationAddress_xh = "", locationName_fh = "", locationName_xh = "", lat_fh = "0", lng_fh = "0", lat_xh = "0",
			lng_xh = "0";
	private String[] fh_imgs;
	private String[] xh_imgs;
	private String[] xhd;

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
		case R.id.uploading_img:
			Intent photoIntent = new Intent(ViewLabelActivity.this, ImagePagerActivity.class);
			photoIntent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
			photoIntent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, xhd);
			ViewLabelActivity.this.startActivity(photoIntent);
			break;
		case R.id.delivery_location_txt:
			Intent mapIntent = new Intent(this, ResourceMapView.class);
			mapIntent.putExtra("type", 1);
			mapIntent.putExtra("lat_fh", lat_fh);
			mapIntent.putExtra("lng_fh", lng_fh);
			mapIntent.putExtra("lat_xh", lat_xh);
			mapIntent.putExtra("lng_xh", lng_xh);
			mapIntent.putExtra("locationFhStr", locationName_fh);
			mapIntent.putExtra("locationXhStr", locationName_xh);
			mapIntent.putExtra("locationAddress_fh", locationAddress_fh);
			mapIntent.putExtra("locationAddress_xh", locationAddress_xh);
			startActivity(mapIntent);
			break;
		case R.id.uploading_location_txt:
			Intent mapIntent1 = new Intent(this, ResourceMapView.class);
			mapIntent1.putExtra("type", 2);
			mapIntent1.putExtra("lat_fh", lat_fh);
			mapIntent1.putExtra("lng_fh", lng_fh);
			mapIntent1.putExtra("lat_xh", lat_xh);
			mapIntent1.putExtra("lng_xh", lng_xh);
			mapIntent1.putExtra("locationFhStr", locationName_fh);
			mapIntent1.putExtra("locationXhStr", locationName_xh);
			mapIntent1.putExtra("locationAddress_fh", locationAddress_fh);
			mapIntent1.putExtra("locationAddress_xh", locationAddress_xh);
			startActivity(mapIntent1);
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
		titleContent.setText("油桶列表");

		return topView;
	}

	@Override
	protected View getCenterView() {
		orderPicListModel = (OrderPicListModel) getIntent().getExtras().getSerializable("orderPicListModel");
		fh_imgs = new String[orderPicListModel.getData().size()];
		xh_imgs = new String[orderPicListModel.getData().size()];
		xhd = new String[1];
		xhd[0] = orderPicListModel.getOriginal_Path_xhd();// 卸货单图片
		View contentView = inflater.inflate(R.layout.view_label, null);
		uploading_img = (ImageView) contentView.findViewById(R.id.uploading_img);
		uploading_img.setOnClickListener(this);
		delivery_location_txt = (TextView) contentView.findViewById(R.id.delivery_location_txt);
		delivery_location_txt.setOnClickListener(this);
		uploading_location_txt = (TextView) contentView.findViewById(R.id.uploading_location_txt);
		uploading_location_txt.setOnClickListener(this);
		delivery_goods_img_grid = (DefineGridView) contentView.findViewById(R.id.delivery_goods_img_grid);
		uploading_goods_img_grid = (DefineGridView) contentView.findViewById(R.id.uploading_goods_img_grid);
		locationName_fh = orderPicListModel.getData().get(0).getMapLocationName_fh();
		locationName_xh = orderPicListModel.getData().get(0).getMapLocationName_xh();
		lat_fh = orderPicListModel.getData().get(0).getLatitude_fh();
		lat_xh = orderPicListModel.getData().get(0).getLatitude_xh();
		lng_fh = orderPicListModel.getData().get(0).getLongitude_fh();
		lng_xh = orderPicListModel.getData().get(0).getLongitude_xh();
		locationAddress_fh = orderPicListModel.getData().get(0).getMapSpecificLocation_fh();
		locationAddress_xh = orderPicListModel.getData().get(0).getMapSpecificLocation_xh();
		init();
		return contentView;
	}

	/**
	 * 
	 * @Title: INIT
	 * @Description:初始化
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void init() {
		ArrayList<OrderPicModel> orderPicModels = orderPicListModel.getData();
		for (int i = 0; i < orderPicModels.size(); i++) {
			fh_imgs[i] = orderPicModels.get(i).getOriginal_Path_fh();// 发货图片
			xh_imgs[i] = orderPicModels.get(i).getOriginal_Path_xh();// 卸货图片
		}
		Picasso.with(this).load(orderPicListModel.getThumb_Path_xhd()).error(R.drawable.default_img).placeholder(R.drawable.default_img).into(uploading_img);
		delivery_location_txt.setText(locationName_fh);
		uploading_location_txt.setText(locationName_xh);
		delivery_goods_img_grid.setAdapter(new ViewLabelGridAdapter(this, orderPicModels, 1));// 发货
		delivery_goods_img_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				Intent photoIntent = new Intent(ViewLabelActivity.this, ImagePagerActivity.class);
				photoIntent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, pos);
				photoIntent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, fh_imgs);
				ViewLabelActivity.this.startActivity(photoIntent);
			}
		});
		uploading_goods_img_grid.setAdapter(new ViewLabelGridAdapter(this, orderPicModels, 2));// 卸货
		uploading_goods_img_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				Intent photoIntent = new Intent(ViewLabelActivity.this, ImagePagerActivity.class);
				photoIntent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, pos);
				photoIntent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, xh_imgs);
				ViewLabelActivity.this.startActivity(photoIntent);
			}
		});
	}
}
