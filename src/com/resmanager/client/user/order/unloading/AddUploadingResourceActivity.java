package com.resmanager.client.user.order.unloading;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.main.photoalbum.AlbumActivity;
import com.resmanager.client.main.photoalbum.AlbumHelper;
import com.resmanager.client.main.photoalbum.Bimp;
import com.resmanager.client.main.photoalbum.ImageBucket;
import com.resmanager.client.main.photoalbum.ImageGridActivity;
import com.resmanager.client.main.photoalbum.PhotoActivity;
import com.resmanager.client.scan.CaptureActivity;
import com.resmanager.client.user.order.unloading.AddUploadingResourceActivity.GridAdapter.ViewHolder;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.DESUtils;
import com.resmanager.client.utils.FileUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.utils.UseCameraActivity;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//(该类暂未使用)
@SuppressLint({ "HandlerLeak", "UseSparseArrays", "DefaultLocale" })
public class AddUploadingResourceActivity extends Activity implements OnClickListener {

	private GridView noScrollgridview;
	private GridAdapter adapter;
	private TextView send_comment_txt;
	// private RatingBar score_rating;
	private LinearLayout choose_photo_layout, photo_layout, take_photo_layout;
	private List<ImageBucket> dataList;
	private AlbumHelper helper;
	// private Dialog loadingDialog;
	private Map<Integer, ViewHolder> holderMaps;
	private Map<Integer, Bitmap> bmps;
	private String workId;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		workId = getIntent().getStringExtra("workId");
		setContentView(R.layout.send_topic);
		findViewById(R.id.title_left_img).setOnClickListener(this);
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		dataList = helper.getImagesBucketList(false);
		choose_photo_layout = (LinearLayout) findViewById(R.id.choose_photo_layout);
		choose_photo_layout.setOnClickListener(this);
		take_photo_layout = (LinearLayout) findViewById(R.id.take_photo_layout);
		take_photo_layout.setOnClickListener(this);
		photo_layout = (LinearLayout) findViewById(R.id.photo_layout);
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		findViewById(R.id.ok_btn).setOnClickListener(this);
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		this.holderMaps = new HashMap<Integer, ViewHolder>();
		this.bmps = new HashMap<Integer, Bitmap>();
		// score_rating = (RatingBar) findViewById(R.id.score_rating);
		Init();
	}

	/**
	 * 
	 * @Title: INIT
	 * @Description:初始化数据
	 * @param
	 * @return void 返回类型
	 * @throws
	 */
	public void Init() {
		if (Bimp.drr.size() > 0) {
			photo_layout.setVisibility(View.GONE);
			noScrollgridview.setVisibility(View.VISIBLE);
		} else {
			photo_layout.setVisibility(View.VISIBLE);
			noScrollgridview.setVisibility(View.GONE);
		}
		send_comment_txt = (TextView) findViewById(R.id.send_comment_txt);
		send_comment_txt.setOnClickListener(this);
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return arg0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
				holder = new ViewHolder();
				holder.scan_code_btn = (Button) convertView.findViewById(R.id.scan_code_btn);
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				holder.scan_code_txt = (TextView) convertView.findViewById(R.id.scan_code_txt);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (!holderMaps.containsKey(position)) {
				holderMaps.put(position, holder);
			}

			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
				if (position == Bimp.MAX_SELECTED) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				Bitmap tempBmp = Bimp.bmp.get(position);
				holder.image.setImageBitmap(tempBmp);
				bmps.put(position, Bimp.bmp.get(position));
				if (Bimp.strBmps.containsKey(tempBmp)) {
					holder.scan_code_txt.setVisibility(View.VISIBLE);
					String showStr = Bimp.strBmps.get(tempBmp).substring(5, Bimp.strBmps.get(tempBmp).lastIndexOf("-"));
					holder.scan_code_txt.setText(showStr);
					holder.scan_code_btn.setEnabled(false);
					holder.scan_code_btn.setBackgroundResource(R.drawable.update_cancel_bg);
					holder.scan_code_btn.setText("二维码已添加");
					holder.scan_code_btn.setTextColor(getResources().getColor(R.color.middle_gray));
				} else {
					holder.scan_code_txt.setVisibility(View.INVISIBLE);
					holder.scan_code_txt.setText("");
					holder.scan_code_btn.setEnabled(true);
					holder.scan_code_btn.setBackgroundResource(R.drawable.update_btn);
					holder.scan_code_btn.setText("点击扫描二维码");
					holder.scan_code_btn.setTextColor(getResources().getColor(R.color.white));
				}
			}

			holder.scan_code_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent scanIntent = new Intent(AddUploadingResourceActivity.this, CaptureActivity.class);
					scanIntent.putExtra("flagType", position);
					AddUploadingResourceActivity.this.startActivityForResult(scanIntent, ContactsUtils.SCAN_RESULT);
				}
			});

			if (position == Bimp.bmp.size()) {
				holder.scan_code_btn.setVisibility(View.INVISIBLE);
				holder.scan_code_txt.setVisibility(View.INVISIBLE);
			} else {
				holder.scan_code_btn.setVisibility(View.VISIBLE);
				holder.scan_code_txt.setVisibility(View.VISIBLE);
			}

			holder.image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (position == Bimp.bmp.size()) {
						new PopupWindows(AddUploadingResourceActivity.this, noScrollgridview);
					} else {
						Intent intent = new Intent(AddUploadingResourceActivity.this, PhotoActivity.class);
						intent.putExtra("ID", position);
						intent.putExtra("workId", workId);
						intent.putExtra("photoType", ContactsUtils.PHOTO_TYPE_UPLOADING);
						startActivity(intent);
					}
				}
			});
			return convertView;
		}

		class ViewHolder {
			private ImageView image;
			private Button scan_code_btn;
			private TextView scan_code_txt;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								if (Bimp.drr.size() > Bimp.max) {
									String path = Bimp.drr.get(Bimp.max);
									Bitmap bm = Bimp.revitionImageSize(path);
									Bimp.bmp.add(bm);
									String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
									FileUtils.saveBitmap(bm, "" + newStr);
									Bimp.max += 1;
									Message message = new Message();
									message.what = 1;
									handler.sendMessage(message);
								}
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	public class PopupWindows extends PopupWindow {

		@SuppressWarnings("deprecation")
		public PopupWindows(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					takePhoto();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					ImageBucket ib = dataList.get(0);
					for (int i = 0; i < dataList.size(); i++) {
						String albumName = dataList.get(i).bucketName;
						if (albumName.toLowerCase().equals("dcim") || albumName.toLowerCase().equals("camera")) {
							ib = dataList.get(i);
						}
					}
					Intent intent = new Intent(AddUploadingResourceActivity.this, ImageGridActivity.class);
					intent.putExtra(AlbumActivity.EXTRA_IMAGE_LIST, (Serializable) ib.imageList);
					startActivityForResult(intent, ImageGridActivity.IMAGE_CHOOSE_RESULT);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	private String path = "";

	/**
	 * 
	 * @Title: takePhoto
	 * @Description: 调用相机拍照
	 * @param
	 * @return void 返回类型
	 * @throws
	 */
	public void takePhoto() {
		path = FileUtils.SDPATH + String.valueOf(System.currentTimeMillis()) + ".jpg";
		Intent takePhotoIntent = new Intent(AddUploadingResourceActivity.this, UseCameraActivity.class);
		takePhotoIntent.putExtra("img_path", path);
		startActivityForResult(takePhotoIntent, ContactsUtils.TAKE_PHOTO_RESULT);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ContactsUtils.TAKE_PHOTO_RESULT:
			if (data != null) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					path = extras.getString("image_path");
					if (Bimp.drr.size() < Bimp.MAX_SELECTED) {
						Bimp.drr.add(path);
					}
					Init();
				}
			}
			break;

		}

		switch (resultCode) {
		case ImageGridActivity.IMAGE_CHOOSE_RESULT:
			Init();
			break;
		case ContactsUtils.SCAN_RESULT:
			if (data != null) {
				int pos = data.getIntExtra("flagType", -1);
				String flagContent = data.getStringExtra("result");
				uploadImg(bmps.get(pos), DESUtils.decrypt(flagContent), pos);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.choose_photo_layout:
			if (dataList != null && dataList.size() > 0) {
				ImageBucket ib = dataList.get(0);
				for (int i = 0; i < dataList.size(); i++) {
					String albumName = dataList.get(i).bucketName;
					if (albumName.toLowerCase().equals("dcim") || albumName.toLowerCase().equals("camera")) {
						ib = dataList.get(i);
					}
				}
				Intent intent = new Intent(AddUploadingResourceActivity.this, ImageGridActivity.class);
				intent.putExtra(AlbumActivity.EXTRA_IMAGE_LIST, (Serializable) ib.imageList);
				startActivityForResult(intent, ImageGridActivity.IMAGE_CHOOSE_RESULT);
			} else {
				Tools.showToast(this, "相册暂无图片");
			}
			break;
		case R.id.take_photo_layout:
			takePhoto();
			break;
		case R.id.send_comment_txt:
			AddUploadingResourceActivity.this.finish();
			break;
		case R.id.title_left_img:
			this.finish();
			break;
		case R.id.ok_btn:
			AddUploadingResourceActivity.this.finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: uploadImg
	 * @Description: 上传图片
	 * @param @param path 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void uploadImg(Bitmap bmp, String flagContent, int pos) {
		// UploadingImageAsyncTask uploadingImageAsyncTask = new
		// UploadingImageAsyncTask(this, orderId, sourceoid, workId,
		// flagContent, bmp, pos);
		// uploadingImageAsyncTask.setUploadUploadingResourceListener(new
		// UploadUploadingResourceListener() {
		//
		// @Override
		// public void uploadUploadingResult(ResultModel resultModel, Bitmap
		// bmp, String flagContent, int pos) {
		// if (resultModel != null) {
		// if (resultModel.getResult().equals("true")) {
		// Bimp.strBmps.put(bmp, flagContent);
		// if (adapter != null) {
		// adapter.notifyDataSetChanged();
		// }
		// Tools.showToast(AddUploadingResourceActivity.this, "货物添加成功");
		// } else {
		// Tools.showToast(AddUploadingResourceActivity.this,
		// resultModel.getDescription());
		// }
		// } else {
		// Tools.showToast(AddUploadingResourceActivity.this, "货物添加失败，请检查网络");
		// }
		// }
		// });
		// uploadingImageAsyncTask.execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
