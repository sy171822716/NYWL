package com.resmanager.client.main.photoalbum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.resmanager.client.R;
import com.resmanager.client.main.photoalbum.ImageGridAdapter.TextCallback;
import com.resmanager.client.utils.Tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

@SuppressLint("HandlerLeak")
public class ImageGridActivity extends Activity implements OnClickListener {
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static final int IMAGE_CHOOSE_RESULT = 0x0015;
	// ArrayList<Entity> dataList;
	List<ImageItem> dataList;
	GridView gridView;
	ImageGridAdapter adapter;//
	AlbumHelper helper;
	Button bt;
	private TextView left_cancel_txt, right_album_txt;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(ImageGridActivity.this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_grid);
		left_cancel_txt = (TextView) findViewById(R.id.left_cancel_txt);
		left_cancel_txt.setOnClickListener(this);
		right_album_txt = (TextView) findViewById(R.id.right_album_txt);
		right_album_txt.setOnClickListener(this);
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(EXTRA_IMAGE_LIST);

		initView();
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}
				if (list.size() > 0) {
					if (Bimp.act_bool) {
//						Intent intent = new Intent(ImageGridActivity.this, SendComment.class);
//						setResult(IMAGE_CHOOSE_RESULT, intent);
//						startActivity(intent);
						setResult(IMAGE_CHOOSE_RESULT);
						Bimp.act_bool = false;
					}
					for (int i = 0; i < list.size(); i++) {
						if (Bimp.drr.size() < Bimp.MAX_SELECTED) {
							Bimp.drr.add(list.get(i));
						}
					}
					finish();
				} else {
					Tools.showToast(ImageGridActivity.this, "请先选择图片");
				}
			}

		});
	}

	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new ImageGridAdapter(ImageGridActivity.this, dataList, mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.notifyDataSetChanged();
			}

		});

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case AlbumActivity.CHOOSE_ALBUM_RESULT:
			dataList = (List<ImageItem>) data.getSerializableExtra(EXTRA_IMAGE_LIST);
			initView();
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_cancel_txt:
			this.finish();
			break;
		case R.id.right_album_txt:
			Intent albumIntent = new Intent(this, AlbumActivity.class);
			startActivityForResult(albumIntent, AlbumActivity.CHOOSE_ALBUM_RESULT);
			break;

		default:
			break;
		}
	}
}
