package com.resmanager.client.scan;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.utils.ContactsUtils;

@SuppressLint({ "NewApi", "InflateParams" })
public class CatpureActivity extends TopContainActivity implements OnClickListener {
	// 相机
	private Camera mCamera;
	// 预览视图
	private CameraPreview mPreview;
	// 自动聚焦
	private Handler mAutoFocusHandler;
	// 图片扫描器
	private ImageScanner mScanner;
	// 是否扫描完毕
	private boolean IsScanned = false;
	// 是否处于预览状态
	private boolean IsPreview = true;
	// 是否显示弹出层
	private int FLAG_TYPE;// 上锁还是解锁
	static {
		System.loadLibrary("iconv");
	}

	public void onCreate(Bundle savedInstanceState) {
		FLAG_TYPE = getIntent().getExtras().getInt("flagType", -10);
		super.onCreate(savedInstanceState);
		// 自动聚焦线程
		mAutoFocusHandler = new Handler();
		// 获取相机实例
		mCamera = getCameraInstance();
		if (mCamera == null) {
			// 在这里写下获取相机失败的代码
			AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
			mBuilder.setTitle("ZBar4Android");
			mBuilder.setMessage("ZBar4Android获取相机失败，请重试！");
			mBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface mDialogInterface, int mIndex) {
					CatpureActivity.this.finish();
				}
			});
			AlertDialog mDialog = mBuilder.create();
			mDialog.show();
		}
		// 实例化Scanner
		mScanner = new ImageScanner();
		mScanner.setConfig(0, Config.X_DENSITY, 3);
		mScanner.setConfig(0, Config.Y_DENSITY, 3);
		// 设置相机预览视图
		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		FrameLayout preview = (FrameLayout) centerView.findViewById(R.id.cameraPreview);
		preview.addView(mPreview);
		if (IsScanned) {
			IsScanned = false;
			mCamera.setPreviewCallback(previewCb);
			mCamera.startPreview();
			IsPreview = true;
			mCamera.autoFocus(autoFocusCB);
		}

		TextView contentView = (TextView) topView.findViewById(R.id.title_content);
		contentView.setText("二维码扫描");
		topView.findViewById(R.id.title_left_img).setOnClickListener(this);
	}

	// 实现Pause方法
	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	// 获取照相机的方法
	public static Camera getCameraInstance() {
		Camera mCamera = null;
		try {
			mCamera = Camera.open();
			// 没有后置摄像头，尝试打开前置摄像头*******************
			if (mCamera == null) {
				Camera.CameraInfo mCameraInfo = new Camera.CameraInfo();
				int cameraCount = Camera.getNumberOfCameras();
				for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
					Camera.getCameraInfo(camIdx, mCameraInfo);
					if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
						mCamera = Camera.open(camIdx);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mCamera;
	}

	// 释放照相机
	private void releaseCamera() {
		if (mCamera != null) {
			IsPreview = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (IsPreview)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			// 获取扫描图片的大小
			Size mSize = parameters.getPreviewSize();
			// 构造存储图片的Image
			Image mResult = new Image(mSize.width, mSize.height, "Y800");// 第三个参数不知道是干嘛的
			// 设置Image的数据资源
			mResult.setData(data);
			// 获取扫描结果的代码
			int mResultCode = mScanner.scanImage(mResult);
			// 如果代码不为0，表示扫描成功
			if (mResultCode != 0) {
				// 停止扫描
				IsPreview = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();
				// 开始解析扫描图片
				SymbolSet Syms = mScanner.getResults();
				for (Symbol mSym : Syms) {
					// mSym.getType()方法可以获取扫描的类型，ZBar支持多种扫描类型,这里实现了条形码、二维码、ISBN码的识别
					if (mSym.getType() == Symbol.CODE128 || mSym.getType() == Symbol.QRCODE || mSym.getType() == Symbol.CODABAR
							|| mSym.getType() == Symbol.ISBN10 || mSym.getType() == Symbol.ISBN13 || mSym.getType() == Symbol.DATABAR
							|| mSym.getType() == Symbol.DATABAR_EXP || mSym.getType() == Symbol.I25)

					{
						// 添加震动效果，提示用户扫描完成
						Vibrator mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
						mVibrator.vibrate(400);
						// Intent intent = new Intent(CatpureActivity.this,
						// ResultActivity.class);
						// intent.putExtra("ScanResult", "扫描类型:" +
						// GetResultByCode(mSym.getType()) + "\n" +
						// mSym.getData());
						Intent it = new Intent();
						it.putExtra("result", mSym.getData());
						it.putExtra("flagType", FLAG_TYPE);
						setResult(ContactsUtils.SCAN_RESULT, it);
						finish();
						// 这里需要注意的是，getData方法才是最终返回识别结果的方法
						// 但是这个方法是返回一个标识型的字符串，换言之，返回的值中包含每个字符串的含义
						// 例如N代表姓名，URL代表一个Web地址等等，其它的暂时不清楚，如果可以对这个进行一个较好的分割
						// 效果会更好，如果需要返回扫描的图片，可以对Image做一个合适的处理
						IsScanned = true;
					} else {
						// 否则继续扫描
						IsScanned = false;
						mCamera.setPreviewCallback(previewCb);
						mCamera.startPreview();
						IsPreview = true;
						mCamera.autoFocus(autoFocusCB);
					}
				}
			}
		}
	};

	// 用于刷新自动聚焦的方法
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			mAutoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	// 根据返回的代码值来返回相应的格式化数据
	public String GetResultByCode(int CodeType) {
		String mResult = "";
		switch (CodeType) {
		// 条形码
		case Symbol.CODABAR:
			mResult = "条形码";
			break;
		// 128编码格式二维码)
		case Symbol.CODE128:
			mResult = "二维码";
			break;
		// QR码二维码
		case Symbol.QRCODE:
			mResult = "二维码";
			break;
		// ISBN10图书查询
		case Symbol.ISBN10:
			mResult = "图书ISBN号";
			break;
		// ISBN13图书查询
		case Symbol.ISBN13:
			mResult = "图书ISBN号";
			break;
		}
		return mResult;
	}

	@Override
	protected View getTopView() {
		return inflater.inflate(R.layout.custom_title_bar, null);
	}

	@Override
	protected View getCenterView() {
		return inflater.inflate(R.layout.zbar_capture_view, null);
	}

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

}
