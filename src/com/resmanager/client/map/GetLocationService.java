package com.resmanager.client.map;

import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.LocationUtils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GetLocationService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LocationUtils lu = new LocationUtils(getApplicationContext());
		lu.startLoc();
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		if (ContactsUtils.USER_KEY != null && !ContactsUtils.USER_KEY.equals("")) {
			Intent localIntent = new Intent();
			localIntent.setClass(this, GetLocationService.class); // 销毁时重新启动Service
			this.startService(localIntent);
		}
	}
}
