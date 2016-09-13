/**
 * Copyright (C) 2014 OPENARM NETWORK
 *
 *
 * @className:com.xtwl.jy.base.utils.ImageCacheUtils
 * @description:图片操作工具类
 * 
 * @version:v1.0.0 
 * @author:ShenYang
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2014-9-23     ShenYang       v1.0.0        create
 *
 *
 */
package com.resmanager.client.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.provider.MediaStore;

public class ImageUtils {
	public static ImageUtils imageUtils = null;

	public static ImageUtils getInstance() {
		if (imageUtils == null) {
			imageUtils = new ImageUtils();
		}
		return imageUtils;
	}

	public Bitmap compressBmpFromBmp(Bitmap image) {
		int options = 80;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, options, baos);
		while (baos.toByteArray().length / 1024 > 100 && options > 10) {
			baos.reset();
			options -= 10;
			image.compress(Bitmap.CompressFormat.PNG, options, baos);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}

	/**
	 * 下载图片
	 * 
	 * @author sy
	 * @param url
	 * @param path
	 * @return Bitmap
	 */

	public Bitmap downloadImage(String url, String path) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
			if (null != bitmap) {
				File file = new File(path);
				FileOutputStream os = new FileOutputStream(file);
				bitmap.compress(CompressFormat.PNG, 100, os);
			}
		} catch (Exception e) {
			bitmap = null;
		} catch (OutOfMemoryError error) {
			System.gc();
			System.runFinalization();
			return null;
		}

		return bitmap;
	}

	/**
	 * 根据路径获取图片
	 * 
	 * @author sy
	 * @param imgpath
	 * @return Bitmap
	 */
	public Bitmap getImageByPath(String path) {
		if (null == path) {
			return null;
		}
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		try {
			FileInputStream is = new FileInputStream(file);
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			System.gc();
			System.runFinalization();
			return null;
		}
	}

	/**
	 * 保存图片
	 * 
	 * @author sy
	 * @param bitmap
	 * @param path
	 * @return path
	 */
	public String saveBitmap(Bitmap bitmap, String path) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				if (file.isDirectory()) {
					file.mkdirs();
				} else if (file.isFile()) {
					file.createNewFile();
				} else {
					file.createNewFile();
				}
			}

			FileOutputStream os = new FileOutputStream(path);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return path;
	}

	/**
	 * 另存为
	 * 
	 * @author sy
	 * @param oldPath
	 * @param newPath
	 */
	public void resaveImageFile(String oldPath, String newPath) {
		File oldFile = new File(oldPath);
		if (!oldFile.exists()) {
			return;
		}
		File newFile = new File(newPath);
		if (newFile.exists()) {
			newFile.delete();
		}

		oldFile.renameTo(newFile);
	}

	/**
	 * 得到图片真实路径
	 * 
	 * @author sy
	 * @param context
	 * @param contentUri
	 * @return String
	 */
	public String getRealPathFromURI(Context context, Uri contentUri) {
		String filePath = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(contentUri, proj, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
		}
		return filePath;
	}

	/**
	 * 旋转图片
	 * 
	 * @author sy
	 * @param degree
	 *            旋转角度
	 * @param bitmap
	 * @return
	 */
	public Bitmap rotaingImage(int degree, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 计算图片压缩比
	 * 
	 * @author sy
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * 获取小图
	 * 
	 * @author sy
	 * @param filePath
	 * @return
	 */
	public Bitmap getSmallBitmap(String filePath) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, 320, 480);
		options.inJustDecodeBounds = false;
		Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
		return compressZLImage(bmp);
	}

	/**
	 * 获取小图
	 * 
	 * @author sy
	 * @param filePath
	 * @return
	 */
	public Bitmap getShareSmallBitmap(String filePath) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, 80, 80);
		options.inJustDecodeBounds = false;
		Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
		return compressZLImage(bmp);
	}

	/**
	 * 
	 * @Description:图片比例压缩
	 * @param image
	 * @return
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-10-29 下午3:57:29
	 */
	public Bitmap getSmallBitMapFromBmp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.PNG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		float hh = 320f;// 这里设置高度为800f
		float ww = 240f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		newOpts.inPreferredConfig = Config.RGB_565;// 降低图片从ARGB888到RGB565
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * 
	 * @Description:质量压缩
	 * @param image
	 * @return
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-10-29 下午3:58:12
	 */
	private Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 生成圆角图片
	 * 
	 * @author sy
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;

	}

	/**
	 * 质量压缩
	 * 
	 * @author sy
	 * @param image
	 * @return
	 */
	public Bitmap compressZLImage(Bitmap image) {
		if (image != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.PNG, 100, baos);
			int options = 100;
			while (baos.toByteArray().length / 1024 > 250 && options > 0) {
				baos.reset();//
				image.compress(Bitmap.CompressFormat.PNG, options, baos);
				options -= 10;//
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
			return bitmap;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @Description:图片转为byte
	 * @param bm
	 * @return
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-10-29 下午3:03:04
	 */
	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 
	 * @Description:byte 转为图片
	 * @param b
	 * @return
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-10-29 下午3:07:42
	 */
	public Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public Bitmap toRoundBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			float roundPx;
			float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
			if (width <= height) {
				roundPx = width / 2;
				top = 0;
				bottom = width;
				left = 0;
				right = width;
				height = width;
				dst_left = 0;
				dst_top = 0;
				dst_right = width;
				dst_bottom = width;
			} else {
				roundPx = height / 2;
				float clip = (width - height) / 2;
				left = clip;
				right = width - clip;
				top = 0;
				bottom = height;
				width = height;
				dst_left = 0;
				dst_top = 0;
				dst_right = height;
				dst_bottom = height;
			}

			Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
			final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
			final RectF rectF = new RectF(dst);

			paint.setAntiAlias(true);

			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, src, dst, paint);
			return output;
		} else {
			return null;
		}
	}

	// /**
	// * 4.4以上获取图片地址
	// *
	// * @param context
	// * @param uri
	// * @return
	// */
	// public String getPath(final Context context, final Uri uri) {
	//
	// final boolean isKitKat = Build.VERSION.SDK_INT >=
	// Build.VERSION_CODES.KITKAT;
	//
	// // DocumentProvider
	// if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	// // ExternalStorageProvider
	// if (isExternalStorageDocument(uri)) {
	// final String docId = DocumentsContract.getDocumentId(uri);
	// final String[] split = docId.split(":");
	// final String type = split[0];
	//
	// if ("primary".equalsIgnoreCase(type)) {
	// return Environment.getExternalStorageDirectory() + "/" + split[1];
	// }
	//
	// }
	// // DownloadsProvider
	// else if (isDownloadsDocument(uri)) {
	//
	// final String id = DocumentsContract.getDocumentId(uri);
	// final Uri contentUri =
	// ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
	// Long.valueOf(id));
	//
	// return getDataColumn(context, contentUri, null, null);
	// }
	// // MediaProvider
	// else if (isMediaDocument(uri)) {
	// final String docId = DocumentsContract.getDocumentId(uri);
	// final String[] split = docId.split(":");
	// final String type = split[0];
	//
	// Uri contentUri = null;
	// if ("image".equals(type)) {
	// contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	// } else if ("video".equals(type)) {
	// contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	// } else if ("audio".equals(type)) {
	// contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	// }
	//
	// final String selection = "_id=?";
	// final String[] selectionArgs = new String[] { split[1] };
	//
	// return getDataColumn(context, contentUri, selection, selectionArgs);
	// }
	// }
	// // MediaStore (and general)
	// else if ("content".equalsIgnoreCase(uri.getScheme())) {
	//
	// // Return the remote address
	// if (isGooglePhotosUri(uri))
	// return uri.getLastPathSegment();
	//
	// return getDataColumn(context, uri, null, null);
	// }
	// // File
	// else if ("file".equalsIgnoreCase(uri.getScheme())) {
	// return uri.getPath();
	// }
	//
	// return null;
	// }

	public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}
}
