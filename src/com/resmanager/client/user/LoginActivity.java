/**   
 * @Title: LoginActivity.java 
 * @Package com.resmanager.client.user 
 * @Description:用户登录
 * @author ShenYang  
 * @date 2015-11-24 下午3:08:25 
 * @version V1.0   
 */
package com.resmanager.client.user;

import com.resmanager.client.R;
import com.resmanager.client.home.HomePageActivity;
import com.resmanager.client.model.UserModel;
import com.resmanager.client.system.SPHelper;
import com.resmanager.client.user.UserLoginAsyncTask.LoginListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @ClassName: LoginActivity
 * @Description: 用户登录
 * @author ShenYang
 * @date 2015-11-24 下午3:08:25
 * 
 */
public class LoginActivity extends Activity implements OnClickListener {
	private EditText user_account_edit, user_pass_edit;
	private CheckBox auto_login_check;
	private TextView ver_txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login_page);
		findViewById(R.id.login_btn).setOnClickListener(this);
		user_account_edit = (EditText) findViewById(R.id.user_account_edit);
		user_pass_edit = (EditText) findViewById(R.id.user_pass_edit);
		auto_login_check = (CheckBox) findViewById(R.id.auto_login_check);
		ver_txt = (TextView) findViewById(R.id.ver_txt);
		ver_txt.setText("版本号:v" + Tools.getVersionName(this));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:

			Login();
			break;

		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: Login
	 * @Description: 登录
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void Login() {
		final String account = user_account_edit.getText().toString();
		final String pass = user_pass_edit.getText().toString();
		String imei = Tools.getIMEI(this);
		if (account.equals("")) {
			Tools.showToast(this, "请输入工号");
		} else if (pass.equals("")) {
			Tools.showToast(this, "请输入密码");
		} else {
			UserLoginAsyncTask userLoginAsyncTask = new UserLoginAsyncTask(this, account, pass, imei);
			userLoginAsyncTask.setLoginListener(new LoginListener() {

				@Override
				public void loginResult(UserModel userModel) {
					if (userModel != null) {
						if (userModel.getResult().equals("true")) {
							if (auto_login_check.isChecked()) {
								SPHelper.getInstance(LoginActivity.this).saveUserInfo(account, pass);
							}
							ContactsUtils.USER_KEY = userModel.getUserKey();
							Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
							startActivity(intent);
							LoginActivity.this.finish();
						} else {
							Tools.showToast(LoginActivity.this, userModel.getDescription());
						}
					} else {
						Tools.showToast(LoginActivity.this, "登录失败，请检查网络");
					}
				}
			});
			userLoginAsyncTask.execute();
		}
	}
}
