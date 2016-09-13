package com.resmanager.client.system;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapCache implements ImageCache {
	private final ImageFileCache mFileCache;
	private final ImageLruCache mLruCache;

	private static BitmapCache sInstance;

	public static synchronized BitmapCache getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new BitmapCache(context);
		}

		return sInstance;
	}

	private BitmapCache(Context context) {
		mLruCache = new ImageLruCache(context);
		mFileCache = new ImageFileCache(context.getPackageName());
	}

	@Override
	public Bitmap getBitmap(String url) {
		// 内存缓存中获取图�?
		Bitmap result = mLruCache.getBitmapFromCache(url);
		if (result == null) {
			// 文件缓存中获取图�?
			result = mFileCache.getImage(url);
			if (result != null) {
				mLruCache.addBitmapToCache(url, result);
			}
		}

		return result;
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mLruCache.addBitmapToCache(url, bitmap);
		mFileCache.saveBitmap(bitmap, url);
	}

	public void clearCache() {
		mLruCache.clearCache();
		// 调用此函数并不能完全清理文件缓存
		mFileCache.clearCache();
	}

}
