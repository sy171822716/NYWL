package com.resmanager.client.map;

import com.resmanager.client.utils.ContactsUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceBroadCast extends BroadcastReceiver {

	// 重写onReceive方法
	@Override
	public void onReceive(Context context, Intent intent) {
		if (ContactsUtils.USER_KEY != null && !ContactsUtils.USER_KEY.equals("")) {
			Intent service = new Intent(context, GetLocationService.class);
			context.startService(service);
		}
	}

}
