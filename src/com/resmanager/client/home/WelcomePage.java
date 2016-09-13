package com.resmanager.client.home;

import com.resmanager.client.R;
import com.resmanager.client.home.UpdateNoticeDialog.CancelBtnListener;
import com.resmanager.client.home.UpdateNoticeDialog.ToDoListener;
import com.resmanager.client.home.UpdateUtils.DownloadListener;
import com.resmanager.client.model.UserModel;
import com.resmanager.client.model.VersionModel;
import com.resmanager.client.system.GetVersionAsyncTask;
import com.resmanager.client.system.SPHelper;
import com.resmanager.client.system.GetVersionAsyncTask.GetVersionCodeListener;
import com.resmanager.client.user.LoginActivity;
import com.resmanager.client.user.UserLoginAsyncTask;
import com.resmanager.client.user.UserLoginAsyncTask.LoginListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.FileUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.CustomDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;

public class WelcomePage extends Activity {
	private UpdateNoticeDialog updateNoticeDialog;
	private CustomDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_page);
		FileUtils.deleteDir();
		if (Tools.isOPen(this)) {
			getVersion();
		} else {
			isGpsOpenDialog();
		}

	}

	/**
	 * 
	 */
	private void isGpsOpenDialog() {
		if (dialog == null) {
			dialog = new CustomDialog(this, R.style.myDialogTheme);
			dialog.setContentText("GPS未打开，请前往设置打开GPS");
			dialog.setToDoListener(new CustomDialog.ToDoListener() {

				@Override
				public void doSomething() {
					Intent intent = new Intent(
							Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
					dialog.dismiss();
				}
			});
			dialog.setCancelBtnListener(new CustomDialog.CancelBtnListener() {

				@Override
				public void cancel() {
					dialog.dismiss();
					finish();
				}
			});
		}
		dialog.show();
	}

	/**
	 * 
	 * @ClassName: LoadingAsyncTask
	 * @Description: 加载
	 * @author ShenYang
	 * @date 2015-11-26 下午5:00:10
	 * 
	 */
	private class LoadingAsyncTask extends AsyncTask<Void, Void, UserModel> {

		@Override
		protected UserModel doInBackground(Void... arg0) {
			try {
				Thread.sleep(2000);
				UserModel userModel = SPHelper.getInstance(WelcomePage.this)
						.getUserInfo();
				return userModel;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(UserModel userModel) {
			super.onPostExecute(userModel);
			if (userModel != null && userModel.getPass() != null
					&& !userModel.getPass().equals("")) {
				loginUser(userModel.getAccount(), userModel.getPass());
			} else {
				Intent intent = new Intent(WelcomePage.this,
						LoginActivity.class);
				startActivity(intent);
				WelcomePage.this.finish();
			}

		}
	}

	/**
	 * 
	 * @Title: getVersion
	 * @Description: 获取版本号异步线程
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getVersion() {

		GetVersionAsyncTask getVersionAsyncTask = new GetVersionAsyncTask();
		getVersionAsyncTask
				.setGetVersionCodeListener(new GetVersionCodeListener() {

					@Override
					public void GetVersionCodeResult(VersionModel versionModel) {
						if (versionModel != null) {
							String localVersion = Tools
									.getVersionName(WelcomePage.this);
							// 当本地版本号和服务器版本号不同时，弹出更新对话框
							if (!localVersion.equals(versionModel
									.getVersionNum())) {
								// 弹出更新对话框
								showUpdateNoticeDialog("",
										versionModel.getVersionNum(),
										versionModel.getUrlAddres(),
										versionModel.getIsForceUpdate());
							} else {
								new LoadingAsyncTask().execute();
							}
						} else {
							new LoadingAsyncTask().execute();
						}
					}
				});
		getVersionAsyncTask.execute();
	}

	/**
	 * 
	 * @Title: showUpdateNoticeDialog
	 * @Description: 弹出更新对话框
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void showUpdateNoticeDialog(String versionDesc, String versionCode,
			String updateUrl, String isForce) {
		if (updateNoticeDialog == null) {
			updateNoticeDialog = new UpdateNoticeDialog(this,
					R.style.myDialogTheme);
			updateNoticeDialog.setCanceledOnTouchOutside(false);
			updateNoticeDialog.setToDoListener(new ToDoListener() {

				@Override
				public void doSomething() {
					UpdateUtils updateUtils = new UpdateUtils(WelcomePage.this,
							updateNoticeDialog.getUrl());
					updateUtils.setDownlaodListener(new DownloadListener() {

						@Override
						public void downloadCancel() {
							String isForce = updateNoticeDialog.getIsForce();
							if (isForce.equals("1")) {
								WelcomePage.this.finish();
							} else {
								new LoadingAsyncTask().execute((Void) null);
							}

						}
					});
					updateUtils.showDownloadDialog();
					updateNoticeDialog.dismiss();
				}
			});
			updateNoticeDialog.setCancelBtnListener(new CancelBtnListener() {

				@Override
				public void cancel() {
					updateNoticeDialog.dismiss();
					String isForce = updateNoticeDialog.getIsForce();
					if (isForce.equals("1")) {
						WelcomePage.this.finish();
					} else {
						new LoadingAsyncTask().execute();
					}
				}
			});
		}
		updateNoticeDialog.setContentText(versionDesc, versionCode, isForce);
		updateNoticeDialog.setUrl(updateUrl);
		updateNoticeDialog.show();
	}

	/**
	 * 
	 * @Title: loginUser
	 * @Description: 登录用户
	 * @param @param account
	 * @param @param pass
	 * @param @param imei 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void loginUser(String account, String pass) {
		UserLoginAsyncTask userLoginAsyncTask = new UserLoginAsyncTask(this,
				account, pass, Tools.getIMEI(WelcomePage.this));
		userLoginAsyncTask.setLoginListener(new LoginListener() {

			@Override
			public void loginResult(UserModel userModel) {
				if (userModel != null) {
					if (userModel.getResult().equals("true")) {
						ContactsUtils.USER_KEY = userModel.getUserKey();
						Intent intent = new Intent(WelcomePage.this,
								HomePageActivity.class);
						startActivity(intent);
						WelcomePage.this.finish();
					} else {
						Tools.showToast(WelcomePage.this,
								userModel.getDescription());
						Intent intent = new Intent(WelcomePage.this,
								LoginActivity.class);
						startActivity(intent);
						WelcomePage.this.finish();
					}
				} else {
					Intent intent = new Intent(WelcomePage.this,
							LoginActivity.class);
					startActivity(intent);
					WelcomePage.this.finish();
				}
			}
		});
		userLoginAsyncTask.execute();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (Tools.isOPen(this)) {
			getVersion();
		} else {
			isGpsOpenDialog();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}
}
