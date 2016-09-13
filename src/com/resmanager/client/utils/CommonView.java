/**   
 * @Title: CommonView.java 
 * @Package com.resmanager.client.Utils 
 * @Description: 通用View
 * @author ShenYang  
 * @date 2015-11-30 下午3:22:30 
 * @version V1.0   
 */
package com.resmanager.client.utils;

import com.resmanager.client.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @ClassName: CommonView
 * @Description: 通用View
 * @author ShenYang
 * @date 2015-11-30 下午3:22:30
 * 
 */
public class CommonView {

	@SuppressLint("InflateParams")
	public static Dialog LoadingDialog(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialogview, null);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);
		// main.xml中的ImageView
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.animation);
		// 使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		Dialog dialog = new Dialog(context, R.style.CustomDialog);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(layout, new LinearLayout.LayoutParams(180, LinearLayout.LayoutParams.WRAP_CONTENT));
		return dialog;
	}

}
