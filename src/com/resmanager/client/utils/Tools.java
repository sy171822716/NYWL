/**
 *
 *
 * @className:com.resmanager.client.Utils.Tools
 * @description:工具类
 * 
 * @version:v1.0.0 
 * @author:ShenYang
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2014-10-27     ShenYang       v1.0.0        create
 *
 *
 */
package com.resmanager.client.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kobjects.base64.Base64;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class Tools {

	public static String PHONE_CHECK_PAT = "^((145|147)|(15[^4])|(17[6-8])|((13|18)[0-9]))\\d{8}$"; // 电话号码匹配正则表达式
	public static Tools tools = null;
	private static Toast mToast = null;

	public static Tools getInstance() {
		if (tools == null) {
			tools = new Tools();
		}
		return tools;
	}

	/**
	 * base64加密
	 * 
	 * @param imageView
	 * @return String
	 */
	public static String getImageByte(ImageView imageView) {
		imageView.setDrawingCacheEnabled(true);
		Bitmap bitMap = imageView.getDrawingCache();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitMap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] imageBytes = baos.toByteArray();
		// 将byte加密成base64格式
		String base64Buff = Base64.encode(imageBytes);
		return base64Buff;
	}

	/**
	 * base64加密
	 * 
	 * @param imageView
	 * @return String
	 */
	public static String getImageByte(Bitmap bitMap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitMap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] imageBytes = baos.toByteArray();
		// 将byte加密成base64格式
		String base64Buff = Base64.encode(imageBytes);
		return base64Buff;
	}

	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	/**
	 * 
	 * @Title: getVersionName
	 * @Description: 获取当前版本号
	 * @param @param context
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (PackageManager.NameNotFoundException e) {
			Log.e("NameNotFoundException", e.getMessage());
		}
		return "";
	}

	/**
	 * 
	 * @Description:弹出提示框
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2015-7-10 上午8:39:58
	 */
	public static void showToast(Context context, String text) {
		if (mToast == null) {
			mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	/**
	 * 
	 * @Description:判断手机号码合法性
	 * @param phone
	 * @return
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-10-27 下午3:36:25
	 */
	public static boolean checkPhone(String phone) {
		Pattern pat = Pattern.compile(PHONE_CHECK_PAT);// 正则校验电话号码合法性
		Matcher mat = pat.matcher(phone);
		return mat.find();
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 
	 * @Description:计算除法
	 * @param price
	 * @param cValue
	 * @param scale
	 * @return
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2015-3-19 下午4:03:31
	 */
	public static String div1(String price, int cValue, int scale) {
		if (price != null) {
			BigDecimal b1 = new BigDecimal(price);
			BigDecimal b2 = new BigDecimal(cValue);
			if (scale < 0) {
				throw new IllegalArgumentException("The scale must be a positive integer or zero");
			}
			double result = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
			String str = String.valueOf(result);
			String resultStr = "";
			if (str.contains(".")) {
				String xiaoshu = str.substring(str.lastIndexOf(".") + 1);
				if (xiaoshu.equals("0")) {
					resultStr = str.substring(0, str.lastIndexOf("."));
				} else {
					resultStr = str;
				}
			} else {
				resultStr = str;
			}
			return resultStr;
		} else {
			return "0";
		}

	}

	/**
	 * 
	 * @Description:格式化价格
	 * @param price
	 * @return
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2015-4-3 下午4:37:29
	 */
	public static String decimalPrice(float price) {
		DecimalFormat decimalFormat = new DecimalFormat("0.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
		return decimalFormat.format(price);
	}

	/**
	 * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
	 * 
	 * @param value
	 * @return Sting
	 */
	public static String formatFloatNumber(double value) {
		if (value != 0.00) {
			java.text.DecimalFormat df = new java.text.DecimalFormat("########0.00");
			return df.format(value);
		} else {
			return "0.00";
		}

	}

	/**
	 * 
	 * @Description:计算总价格
	 * @param price
	 *            现价
	 * @param num
	 *            数量
	 * @param flag
	 *            1：计算加法，2：计算减法，3：计算乘法 4：计算触发
	 * @return 结果
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2015-3-9 下午3:00:26
	 */
	public static String formatDoublePrice(double price, double price1, int num, int flag) {
		double totalPrice = 0f;
		BigDecimal b1 = new BigDecimal(price);
		BigDecimal b2 = new BigDecimal(price1);
		BigDecimal b3 = new BigDecimal(num);
		switch (flag) {
		case 1:
			// 商品相加
			totalPrice = b1.add(b2).doubleValue();
			break;
		case 2:
			// 商品相减
			totalPrice = b1.subtract(b2).doubleValue();
			break;
		case 3:
			// 相乘
			totalPrice = b1.multiply(b3).doubleValue();
			break;
		case 4:
			// totalPrice =
			// b1.divide(b2,BigDecimal.DEF_DIV_SCALE,BigDecimal.ROUND_HALF_UP).doubleValue();
			break;
		default:
			break;
		}
		if (Math.round(totalPrice) / 100.0 > 1) {
			return String.valueOf(Math.round(totalPrice) / 100.0);
		} else {
			return "0" + String.valueOf(Math.round(totalPrice) / 100.0);
		}
	}

	/*
	 * 计算MD5值
	 */
	private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * Generate MD5 sum for string.
	 * 
	 * @param string
	 * @return
	 */
	public static final String md5Sum(String string) {
		try {
			InputStream in_withcode = new ByteArrayInputStream(string.getBytes());
			return md5sum(in_withcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String md5sum(InputStream is) {
		try {
			int numRead = 0;
			byte[] buffer = new byte[1024];
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			while ((numRead = is.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			String str = encodeHex(md5.digest());
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static String encodeHex(final byte[] data) {
		final int l = data.length;
		final char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = hexDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = hexDigits[0x0F & data[i]];
		}
		return new String(out);
	}

	// /**
	// * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
	// *
	// * @param value
	// * @return Sting
	// */
	// public static String formatFloatNumber(double value) {
	// if (value != 0.00) {
	// if (value > 1.00) {
	// java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
	// return df.format(value);
	// } else {
	// DecimalFormat decimalFormat = new DecimalFormat("0.00");//
	// 构造方法的字符格式这里如果小数不足2位,会以0补足.
	// return decimalFormat.format(value);
	// }
	//
	// } else {
	// return "0.00";
	// }
	//
	// }

	/**
	 * 
	 * @Description:计算
	 * @param v1
	 * @param v2
	 * @return
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2015-5-26 上午10:17:20
	 */
	public static String getDoubleResult(double v1, double v2) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMaximumIntegerDigits(1000);
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		// DecimalFormat decimalFormat = new DecimalFormat("0.00");//
		// 构造方法的字符格式这里如果小数不足2位,会以0补足.
		return formatFloatNumber((b1.multiply(b2).doubleValue())).replaceAll(",", "");
		// System.out.println( b1.multiply(b2).doubleValue() );
		// System.out.println( nf.format( b1.multiply(b2).doubleValue() ) );
		// return nf.format(b1.multiply(b2).doubleValue()).replaceAll(",", "");

	}

	/**
	 * 
	 * @Description:获取小图
	 * @return
	 * @version:v1.0
	 * @author:ShenYang
	 * @param url
	 *            :原图链接，flag:0，原图，1,小图，2,中图,3,大图
	 * @date:2015-6-10 下午2:58:01
	 */
	public static String getSmallPicUrl(String url, int flag) {
		if (url != null && !url.equals("") && url.contains(".")) {
			String str = url.substring(0, url.lastIndexOf("."));
			String end = url.substring(url.lastIndexOf("."), url.length());
			String returnUrl = url;
			switch (flag) {
			case 1:
				returnUrl = str + "_s" + end;
				break;
			case 2:
				returnUrl = str + "_m" + end;
				break;
			case 3:
				returnUrl = str + "_b" + end;
				break;

			default:
				break;
			}
			return returnUrl;
		}
		return "";
	}

	/**
	 * 4.4以上获取图片地址
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	@SuppressLint("NewApi")
	public String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * 
	 * @Title: getDataColumn
	 * @Description: 获取列
	 * @param @param context
	 * @param @param uri
	 * @param @param selection
	 * @param @param selectionArgs
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
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

	/**
	 * 
	 * @Description:计算时间差
	 * @param dateStr
	 * @return
	 * @version:v1.0
	 * @author:FuHuiHui
	 * @date:2015-1-4 下午12:23:30
	 */
	public static String TimeDelay(String dateStr, String systemTime) {
		if (dateStr != null && !dateStr.equals("null")) {
			SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

			String[] dalayTime = dateStr.split(" ");
			String[] yearMonth = dalayTime[0].split("-");
			String[] hourMinStr = dalayTime[1].split(":");
			long between = 0;
			String delayStr = "";
			try {
				java.util.Date begin = dfs.parse(dateStr);
				java.util.Date end = df.parse(systemTime);
				between = (end.getTime() - begin.getTime());// 得到两者的毫秒数

				Calendar c1 = Calendar.getInstance();
				Calendar c2 = Calendar.getInstance();
				c1.setTime(begin);
				c2.setTime(end);
				int beginYear = c1.get(Calendar.YEAR);
				int endYear = c2.get(Calendar.YEAR);
				int beginDay = c1.get(Calendar.DAY_OF_MONTH);
				int endDay = c2.get(Calendar.DAY_OF_MONTH);
				int month = (int) ((between / (24 * 60 * 60 * 1000)) / 30);
				int day = (int) (between / (24 * 60 * 60 * 1000));
				int hour = (int) ((between / (60 * 60 * 1000) - day * 24));
				int minute = (int) (((between / (60 * 1000)) - day * 24 * 60 - hour * 60));

				if ((endYear - beginYear) == 0) {
					if (month == 0 && (endDay - beginDay) == 0) {
						if (hour == 0) {
							if (minute == 0) {
								delayStr = "刚刚";
							} else {
								delayStr = minute + "分钟前";
							}
						} else {
							delayStr = hourMinStr[0] + ":" + hourMinStr[1];
						}

					} else {
						delayStr = yearMonth[1] + "-" + yearMonth[2];
					}
				} else {
					delayStr = dalayTime[0];
				}

				return delayStr;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return "";
		} else {
			return "";
		}
	}

	/**
	 * 
	 * @Title: getStr
	 * @Description: 获取资源文件Str
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static String getStr(Context context, int strId) {
		return context.getResources().getString(strId);
	}

	/**
	 * 
	 * @Title: getIMEI
	 * @Description: 获取手机IMEI号
	 * @param @param context
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String getIMEI(Context context) {
		TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mTm.getDeviceId();
		return imei;
	}

	// public static void getNetWorkState(Context context) {
	// Activity activity = (Activity) context;
	// MyPhoneStateListener MyListener = new MyPhoneStateListener(context);
	// TelephonyManager Tel = (TelephonyManager)
	// activity.getSystemService(Context.TELEPHONY_SERVICE);
	// Tel.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	// }

	/**
	 * 
	 * @Title: GetNetworkType
	 * @Description: 获取当前网络类型
	 * @param @param context
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String GetNetworkType(Context context) {
		String strNetworkType = "";
		NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				strNetworkType = "WIFI";
			} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				String _strSubTypeName = networkInfo.getSubtypeName();
				// TD-SCDMA networkType is 17
				int networkType = networkInfo.getSubtype();
				switch (networkType) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_1xRTT:
				case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by
															// 11
					strNetworkType = "2G";
					break;
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
				case TelephonyManager.NETWORK_TYPE_HSUPA:
				case TelephonyManager.NETWORK_TYPE_HSPA:
				case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by
															// 14
				case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by
				case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by
					strNetworkType = "3G";
					break;
				case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by
					strNetworkType = "4G";
					break;
				default:
					// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
					if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA")
							|| _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
						strNetworkType = "3G";
					} else {
						strNetworkType = _strSubTypeName;
					}
					break;
				}
			}
		}
		return strNetworkType;
	}

	public static String getNowTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateNowStr = sdf.format(new Date());
		return dateNowStr;
	}

	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * 
	 * @param context
	 * @return true 表示开启
	 */
	public static final boolean isOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// //
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		// boolean network = locationManager
		// .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps) {
			return true;
		}

		return false;
	}

	/**
	 * 跳转至拨号界面
	 * 
	 * @param context
	 * @param phoneNumber
	 */
	public static void jumpToTelPhone(Context context, String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 
	 * @Title: takePhoto
	 * @Description: 调用相机拍照
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void takePhoto(Activity context) {
		String path = FileUtils.SDPATH + String.valueOf(System.currentTimeMillis()) + ".jpg";
		Intent takePhotoIntent = new Intent(context, UseCameraActivity.class);
		takePhotoIntent.putExtra("img_path", path);
		context.startActivityForResult(takePhotoIntent, ContactsUtils.TAKE_PHOTO_RESULT);
	}

	/**
	 * 
	 * @Title: getShowLabelCode
	 * @Description: 返回给用户看的二维码标签
	 * @param @param enLabelCode 加密后的二维码内容
	 * @return String 返回类型
	 * @throws
	 */
	public static String getShowLabelCode(String enLabelCode) {
		String decryptStr = DESUtils.decrypt(enLabelCode);// 解密后的二维码
		try {
			return decryptStr.substring(5, decryptStr.lastIndexOf("-"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * 
	 * @Title: getShowLabelCode
	 * @Description: 返回给用户看的二维码标签
	 * @param @param enLabelCode 完整二维码内容
	 * @return String 返回类型
	 * @throws
	 */
	public static String getParserLabelCode(String enLabelCode) {
		try {
			return enLabelCode.substring(5, enLabelCode.lastIndexOf("-"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * 
	 * @Title: getShowLabelCode
	 * @Description: 返回给用户看的二维码标签
	 * @param @param enLabelCode 加密后的二维码内容
	 * @return String 返回类型
	 * @throws
	 */
	public static String getShowLabelCodes(String enlabels) {
		String[] labels = enlabels.split(",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < labels.length; i++) {
			String labelCode = labels[i];
			if (labelCode.contains("-")) {
				if (i == (labels.length - 1)) {
					sb.append(labelCode.substring(5, labelCode.lastIndexOf("-")));
				} else {
					sb.append(labelCode.substring(5, labelCode.lastIndexOf("-")) + ",");
				}
			} else {
				if (i == (labels.length - 1)) {
					sb.append(labelCode);
				} else {
					sb.append(labelCode + ",");
				}
			}
		}
		return sb.toString();

	}

	/**
	 * 
	 * @Title: getGUID
	 * @Description: 返回GUID
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String getGUID() {
		return java.util.UUID.randomUUID().toString();
	}

	/**
	 * 
	 * @Title: getPkgName
	 * @Description: 获取程序包名
	 * @param @param context
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String getPkgName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 判断某个服务是否正在运行的方法
	 * 
	 * @param mContext
	 * @param serviceName
	 *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
	 * @return true代表正在运行，false代表服务没有正在运行
	 */
	public static boolean isServiceWork(Context mContext, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> myList = myAM.getRunningServices(40);
		if (myList.size() <= 0) {
			return false;
		}
		for (int i = 0; i < myList.size(); i++) {
			String mName = myList.get(i).service.getClassName().toString();
			if (mName.equals(serviceName)) {
				isWork = true;
				break;
			}
		}
		return isWork;
	}

	/**
	 * 
	 * @Title: formatNowDate
	 * @Description: 获取当前时间
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String formatNowDate() {
		// 初始化时设置 日期和时间模式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

		// 修改日期和时间模式
		// sdf.applyPattern("yyyy/MM/dd HH:mm:ss.SSS");

		return sdf.format(new Date());
	}
}
