package com.resmanager.client.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.utils.Tools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class DefineCameraActivity extends Activity {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    Camera camera;
    Camera.Parameters parameters;
    /**
     * 控制相机方向
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);//旋转90
        ORIENTATIONS.append(Surface.ROTATION_90, 0);//旋转0
        ORIENTATIONS.append(Surface.ROTATION_180, 270);//旋转270
        ORIENTATIONS.append(Surface.ROTATION_270, 180);//旋转180°
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.define_camera);
        TextView titleContent = (TextView) findViewById(R.id.title_content);
        titleContent.setText("拍照");
        findViewById(R.id.title_left_img).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        surfaceView = (SurfaceView) findViewById(R.id.camera_preview);
        Button button = (Button) findViewById(R.id.camera_take_photo);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("tab");//System.out快捷键“sout+tab”
            }
        });
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setKeepScreenOn(true);
        surfaceView.setFocusable(true);
        surfaceView.setBackgroundColor(TRIM_MEMORY_BACKGROUND);
        surfaceHolder.addCallback(new Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                camera.stopPreview();
                camera.release();
                camera = null;

            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (null == camera) {
                    camera = Camera.open();
                    try {
                        camera.setPreviewDisplay(surfaceHolder);// 获取当前手机屏幕方向
                        int rotation = DefineCameraActivity.this.getWindowManager().getDefaultDisplay().getRotation();
                        // 调整相机方向
                        camera.setDisplayOrientation(ORIENTATIONS.get(rotation));
                        initCamera();
                        camera.startPreview();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // 实现自动对焦
                camera.autoFocus(new AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            initCamera();// 实现相机的参数初始化
                            camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。

                        }
                    }

                });
            }

        });
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    private void data2file(byte[] w, String fileName) throws Exception {// 将二进制数据转换为文件的函数
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
            out.write(w);
            out.close();
        } catch (Exception e) {
            if (out != null)
                out.close();
            throw e;
        }
    }

    // 相机参数的初始化设置
    private void initCamera() {
        parameters = camera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);

        Size maxPictureSize = parameters.getSupportedPictureSizes().get(0);
        Size maxPreviewSize = parameters.getSupportedPreviewSizes().get(0);
        for (int i = 0; i < parameters.getSupportedPictureSizes().size(); i++) {
            Size s = parameters.getSupportedPictureSizes().get(i);
            if (s.width > maxPictureSize.width) {
                maxPictureSize = s;
            }
            if (s.width == maxPictureSize.width && s.height > maxPictureSize.height) {
                maxPictureSize = s;
            }
        }
        for (int i = 0; i < parameters.getSupportedPreviewSizes().size(); i++) {
            Size s = parameters.getSupportedPreviewSizes().get(i);
            if (s.width > maxPreviewSize.width) {
                maxPreviewSize = s;
            }
            if (s.width == maxPreviewSize.width && s.height > maxPreviewSize.height) {
                maxPreviewSize = s;
            }
        }

        parameters.setPictureSize(maxPictureSize.width, maxPictureSize.height);
        parameters.setPreviewSize(maxPreviewSize.width, maxPreviewSize.height);
        // parameters.setPictureSize(surfaceView.getWidth(),
        // surfaceView.getHeight()); // 部分定制手机，无法正常识别该方法。
        // parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
        setDispaly(parameters, camera);
        camera.setParameters(parameters);
        camera.startPreview();
        camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上

    }

    // 控制图像的正确显示方向
    private void setDispaly(Camera.Parameters parameters, Camera camera) {
        if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
            setDisplayOrientation(camera, 90);
        } else {
            parameters.setRotation(90);
        }

    }

    // 实现的图像的正确显示
    private void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[]{int.class});
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[]{i});
            }
        } catch (Exception e) {
            Log.e("Came_e", "图像出错");
        }
    }

    /**
     * 将图片的旋转角度置为0 ，此方法可以解决某些机型拍照后图像，出现了旋转情况
     *
     * @param path
     * @return void
     * @Title: setPictureDegreeZero
     * @date 2012-12-10 上午10:54:46
     */
    private void setPictureDegreeZero(String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            // 修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
            // 例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "6");
            exifInterface.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (camera != null) {
                    camera.takePicture(null, null, new PictureCallback() {

                        @Override
                        public void onPictureTaken(byte[] data, Camera arg1) {

                            String path = Environment.getExternalStorageDirectory() + "/DCIM/Camera/" + System.currentTimeMillis() + "test133.png";
                            try {
                                data2file(data, path);
                                setPictureDegreeZero(path);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                Tools.showToast(DefineCameraActivity.this, "up");
                return true;
            case KeyEvent.KEYCODE_VOLUME_MUTE:
                Tools.showToast(DefineCameraActivity.this, "mute");
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}