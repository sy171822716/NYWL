/**   

 * @Title: HomePageActivity.java 
 * @Package com.resmanager.client.home 
 * @Description: 主界面
 * @author ShenYang  
 * @date 2015-11-24 下午4:56:08 
 * @version V1.0   
 */
package com.resmanager.client.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.resmanager.client.R;
import com.resmanager.client.camera.DefineCameraActivity;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.flag.FlagSearch;
import com.resmanager.client.map.GetLocationService;
import com.resmanager.client.map.UserLocationMapView;
import com.resmanager.client.model.AdModel;
import com.resmanager.client.model.PowerModel;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.model.UserDetailModel;
import com.resmanager.client.model.UserDetailResult;
import com.resmanager.client.order.OrderMainNewActivity;
import com.resmanager.client.stock.StockList;
import com.resmanager.client.system.SPHelper;
import com.resmanager.client.user.GetUserDetailAsyncTask;
import com.resmanager.client.user.GetUserDetailAsyncTask.GetUserDetailListener;
import com.resmanager.client.user.LoginActivity;
import com.resmanager.client.user.UpdateTokenAsyncTask;
import com.resmanager.client.user.balance.BalanceActivity;
import com.resmanager.client.user.message.GetUnReadMsgNumAsyncTask;
import com.resmanager.client.user.message.GetUnReadMsgNumAsyncTask.GetUnReadListenr;
import com.resmanager.client.user.order.MyOrderListNew;
import com.resmanager.client.user.order.delivery.MyDaiYunOrderList;
import com.resmanager.client.user.order.unloading.DriverList;
import com.resmanager.client.user.order.unloading.MyUploadingOrderList;
import com.resmanager.client.user.recyle.ChooseCustomerActivity;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.CustomDialog;
import com.resmanager.client.view.CustomDialog.ToDoListener;
import com.squareup.picasso.Picasso;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName: HomePageActivity
 * @Description:主界面
 * @author ShenYang
 * @date 2015-11-24 下午4:56:08
 * 
 */
@SuppressLint({ "InflateParams", "HandlerLeak", "ClickableViewAccessibility" })
public class HomePageActivity extends TopContainActivity implements OnTouchListener, OnItemClickListener {
	private GridView orpGrid;
	private OrpGridAdapter orpGridAdapter;
	private long mPressedTime = 0;
	private TextView name_txt, work_number_txt, leavel_txt;
	private ViewPager AdViewPager;
	private ImageView[] imageViews = null;
	private ImageView imageView = null;
	private ViewGroup group;
	private TextView desc;
	private AtomicInteger what = new AtomicInteger(0);
	private boolean isContinue = true;
	private final Handler viewHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			AdViewPager.setCurrentItem(msg.what);
			super.handleMessage(msg);
		}
	};
	private List<AdModel> baseAdModels;
	private List<PowerModel> pms = new ArrayList<PowerModel>();
	private CustomDialog cm, noticeDialog;

	/*
	 * (非 Javadoc) <p>Title: getTopView</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getTopView()
	 */
	@Override
	protected View getTopView() {
		View topView = inflater.inflate(R.layout.custom_title_bar, null);
		topView.findViewById(R.id.title_left_img).setVisibility(View.INVISIBLE);
		ImageView titleRightImg = (ImageView) topView.findViewById(R.id.title_right_img);
		titleRightImg.setVisibility(View.VISIBLE);
		titleRightImg.setImageResource(R.drawable.exit);
		titleRightImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				signOut();
			}
		});
		return topView;
	}

	/**
	 * 
	 * @Title: signOut
	 * @Description: 退出登录
	 * @return void 返回类型
	 * @throws
	 */
	private void signOut() {
		if (cm == null) {
			cm = new CustomDialog(this, R.style.myDialogTheme);
			cm.setContentText("是否退出当前登录用户");
			cm.setToDoListener(new ToDoListener() {

				@Override
				public void doSomething() {
					SPHelper.getInstance(HomePageActivity.this).deleteSp();
					Intent loginIntent = new Intent(HomePageActivity.this, LoginActivity.class);
					startActivity(loginIntent);
					startOrStopService(1);
					finish();
				}
			});
		}
		cm.show();
	}

	/**
	 * 
	 * @Title: showNoticeDialog
	 * @Description: 提示框
	 * @return void 返回类型
	 * @throws
	 */
	public void showNoticeDialog() {
		if (noticeDialog == null) {
			noticeDialog = new CustomDialog(this, R.style.myDialogTheme);
			noticeDialog.setContentText("当前模块正在吐血开发中。。。");
			noticeDialog.setToDoListener(new ToDoListener() {

				@Override
				public void doSomething() {
					noticeDialog.dismiss();
				}
			});
		}
		noticeDialog.show();
	}

	/*
	 * (非 Javadoc) <p>Title: getCenterView</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getCenterView()
	 */
	@Override
	protected View getCenterView() {
		View contentView = inflater.inflate(R.layout.home_page, null);
		orpGrid = (GridView) contentView.findViewById(R.id.orp_grid);
		name_txt = (TextView) contentView.findViewById(R.id.name_txt);
		work_number_txt = (TextView) contentView.findViewById(R.id.work_number_txt);
		leavel_txt = (TextView) contentView.findViewById(R.id.leavel_txt);
		AdViewPager = (ViewPager) contentView.findViewById(R.id.adv_pager);
		group = (ViewGroup) contentView.findViewById(R.id.view_group);
		desc = (TextView) contentView.findViewById(R.id.desc);
		getUserDetail();
		// Tools.getNetWorkState(this);
		baseAdModels = initAdData();
		initAdViewPager(baseAdModels);
		return contentView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 2.36（不包括）之前的版本需要调用以下2行代码
		Intent service = new Intent(this, XGPushService.class);
		startService(service);

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			long mNowTime = System.currentTimeMillis();// 获取第一次按键时间
			if ((mNowTime - mPressedTime) > 2000) {// 比较两次按键时间差
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mPressedTime = mNowTime;
			} else {
				// if (PushManager.isPushEnabled(this)) {
				// PushManager.stopWork(this);
				// }
				this.finish();
				System.exit(0);
			}
		}
		return false;
	}

	/**
	 * 
	 * @Title: getUserDetail
	 * @Description: 获取用户信息
	 * @param  
	 * @return void 返回类型
	 * @throws
	 */
	private void getUserDetail() {
		GetUserDetailAsyncTask getUserDetailAsyncTask = new GetUserDetailAsyncTask(this, ContactsUtils.USER_KEY);
		getUserDetailAsyncTask.setGetUserDetailListener(new GetUserDetailListener() {

			@Override
			public void getUserDetailResult(UserDetailResult userDetailModel) {
				if (userDetailModel != null) {
					if (userDetailModel.getResult().equals("true")) {
						startOrStopService(0);
						ContactsUtils.userDetailModel = userDetailModel.getData();

						// 开启logcat输出，方便debug，发布时请关闭
						XGPushConfig.enableDebug(HomePageActivity.this, true);
						// 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(),
						// XGIOperateCallback)带callback版本
						// 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
						// 具体可参考详细的开发指南
						// 传递的参数为ApplicationContext
						XGPushManager.registerPush(HomePageActivity.this, new XGIOperateCallback() {

							@Override
							public void onSuccess(Object arg0, int arg1) {
								new UpdateTokenAsyncTask(arg0).execute();
							}

							@Override
							public void onFail(Object arg0, int arg1, String arg2) {

							}
						});
						XGPushManager.setTag(HomePageActivity.this, String.valueOf(ContactsUtils.userDetailModel.getUserId()));
						setUserInfo(userDetailModel.getData());
						String[] powers = userDetailModel.getData().getPower().split(",");
						for (int i = 0; i < powers.length; i++) {
							PowerModel pm;
							int powerId = 0;
							if (!powers[i].equals("")) {
								powerId = Integer.parseInt(powers[i]);
							}
							if (powerId == ContactsUtils.POWER_ALL_ORDER) {
								pm = new PowerModel();
								pm.setPowerID(ContactsUtils.POWER_ALL_ORDER);
								pm.setPowerName(R.string.all_order_str);
								pm.setShowNum(false);
								pm.setPowerImg(R.drawable.all_order_ico);
								pms.add(pm);
							} else if (powerId == ContactsUtils.POWER_FLAG) {
								pm = new PowerModel();
								pm.setPowerID(ContactsUtils.POWER_FLAG);
								pm.setShowNum(false);
								pm.setPowerName(R.string.search_tag_str);
								pm.setPowerImg(R.drawable.tag_search);
								pms.add(pm);
							} else if (powerId == ContactsUtils.POWER_KUCUN) {
								pm = new PowerModel();
								pm.setPowerID(ContactsUtils.POWER_KUCUN);
								pm.setShowNum(false);
								pm.setPowerName(R.string.t_stock_str);
								pm.setPowerImg(R.drawable.t_stock_ico);
								pms.add(pm);
							} else if (powerId == ContactsUtils.POWER_DRIVE) {
								pm = new PowerModel();
								pm.setPowerID(ContactsUtils.POWER_DRIVE);
								pm.setShowNum(false);
								pm.setPowerName(R.string.delivery_str);
								pm.setPowerImg(R.drawable.delivery_ico);
								pms.add(pm);
							} else if (powerId == ContactsUtils.POWER_UPLOADING) {
								pm = new PowerModel();
								pm.setPowerID(ContactsUtils.POWER_UPLOADING);
								pm.setPowerName(R.string.unloading_str);
								pm.setShowNum(false);
								pm.setPowerImg(R.drawable.unloading_ico);
								pms.add(pm);
							} else if (powerId == ContactsUtils.POWER_RESOURCE_RECYLE) {
								pm = new PowerModel();
								pm.setPowerID(ContactsUtils.POWER_RESOURCE_RECYLE);
								pm.setPowerName(R.string.recyle_str);
								pm.setPowerImg(R.drawable.path_ico);
								pm.setShowNum(false);
								pms.add(pm);
							} else if (powerId == ContactsUtils.POWER_MY_ORDER) {
								pm = new PowerModel();
								pm.setPowerID(ContactsUtils.POWER_MY_ORDER);
								pm.setShowNum(false);
								pm.setPowerName(R.string.my_order_str);
								pm.setPowerImg(R.drawable.my_order_ico);
								pms.add(pm);
							} else if (powerId == ContactsUtils.POWER_MY_MESSAGE) {
								pm = new PowerModel();
								pm.setPowerID(ContactsUtils.POWER_MY_MESSAGE);
								pm.setPowerName(R.string.message_str);
								pm.setShowNum(true);
								pm.setPowerImg(R.drawable.message_ico);
								pms.add(pm);
							} else if (powerId == ContactsUtils.POWER_MY_TRADE) {
								pm = new PowerModel();
								pm.setPowerID(ContactsUtils.POWER_MY_TRADE);
								pm.setPowerName(R.string.balance_str);
								pm.setShowNum(false);
								pm.setPowerImg(R.drawable.balance_ico);
								pms.add(pm);
							} else if (powerId == ContactsUtils.POWER_LOCATION) {
								pm = new PowerModel();
								pm.setPowerID(ContactsUtils.POWER_LOCATION);
								pm.setPowerName(R.string.location_str);
								pm.setShowNum(false);
								pm.setPowerImg(R.drawable.user_location);
								pms.add(pm);
							} else if (powerId == ContactsUtils.POWER_DAIXIEHUO) {
								pm = new PowerModel();
								pm.setPowerID(ContactsUtils.POWER_DAIXIEHUO);
								pm.setPowerName(R.string.daixuehuo_str);
								pm.setShowNum(false);
								pm.setPowerImg(R.drawable.daixiehuo);
								pms.add(pm);
							}
						}
						if (orpGridAdapter == null) {
							orpGridAdapter = new OrpGridAdapter(HomePageActivity.this, pms);
							orpGrid.setAdapter(orpGridAdapter);
							orpGrid.setOnItemClickListener(HomePageActivity.this);
						}
					} else {
						Tools.showToast(HomePageActivity.this, userDetailModel.getDescription());
						finish();
					}
				} else {
					Tools.showToast(HomePageActivity.this, "用户信息获取失败，请检查网络");
					finish();
				}
			}
		});
		getUserDetailAsyncTask.execute();
	}

	/**
	 * 
	 * @Title: getUnReadMsgNum
	 * @Description: 获取未读消息数量
	 * @param  
	 * @return void 返回类型
	 * @throws
	 */
	private void getUnReadMsgNum() {
		GetUnReadMsgNumAsyncTask getUnReadMsgNumAsyncTask = new GetUnReadMsgNumAsyncTask();
		getUnReadMsgNumAsyncTask.setGetUnReadListenr(new GetUnReadListenr() {

			@Override
			public void getUnReadResult(ResultModel resultModel) {
				if (resultModel != null) {
					if (resultModel.getResult().equals("true")) {
						String resultNum = resultModel.getDescription();
						if (orpGridAdapter != null) {
							orpGridAdapter.notifyMsgNum(resultNum);
						}
					}
				}
			}
		});
		getUnReadMsgNumAsyncTask.execute();
	}

	/**
	 * 
	 * @Title: setUserInfo
	 * @Description: 设置用户信息
	 * @param @param userDetailModel  
	 * @return void 返回类型
	 * @throws
	 */
	private void setUserInfo(UserDetailModel userDetailModel) {
		getUnReadMsgNum();
		name_txt.setText("姓名:" + userDetailModel.getNickName());
		work_number_txt.setText("工号:" + userDetailModel.getUserName());
		// String uleavel = "";
		// if (userDetailModel.getUserType().equals("0")) {
		// uleavel = "普通用户";
		//
		// } else {
		// uleavel = "驾驶员";
		// }
		leavel_txt.setText("级别:" + userDetailModel.getRoleName());
	}

	private List<AdModel> initAdData() {
		List<AdModel> adModels = new ArrayList<AdModel>();
		String[] strs = { "春节放假通知", "杭州线路修路通知" };
		String[] urls = { "http://lxs.cncn.com/61420/n523930", "http://zjnews.zjol.com.cn/system/2015/04/16/020605147.shtml" };
		String[] picUrls = { "http://cdn.pcbeta.attachment.inimc.com/data/attachment/forum/201302/09/1201219iyq6dd45iqgylgg.jpg",
				"http://img.jdzj.com/UserDocument/2014a/aoyuwuliu/Picture/201441412150.jpg" };
		int[] ids = { 1, 2 };
		for (int i = 0; i < ids.length; i++) {
			AdModel adModel = new AdModel();
			adModel.setAID(ids[i]);
			adModel.setADName(strs[i]);
			adModel.setADUrl(urls[i]);
			adModel.setADPicUrl(picUrls[i]);
			adModels.add(adModel);
		}
		return adModels;
	}

	/**
	 * 
	 * @Description:初始化顶部广告轮播ViewPager
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-10-30 上午8:33:36
	 */
	private void initAdViewPager(List<AdModel> adModels) {// 用于存放需要显示的View
		List<View> advPics = new ArrayList<View>();
		for (int i = 0; i < adModels.size(); i++) {
			desc.setText(adModels.get(0).getADName());
			final AdModel homeCarousel = adModels.get(i);
			ImageView networkImageView = new ImageView(this);
			networkImageView.setScaleType(ImageView.ScaleType.FIT_XY);
			Picasso.with(this).load(homeCarousel.getADPicUrl()).error(R.drawable.default_img).placeholder(R.drawable.default_img).into(networkImageView);
			networkImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent webViewIntent = new Intent(HomePageActivity.this, CustomWebView.class);
					webViewIntent.putExtra("url", homeCarousel.getADUrl());
					HomePageActivity.this.startActivity(webViewIntent);
				}
			});
			advPics.add(networkImageView);
		}

		// 根据需要显示的图片数量来生成需要显示的点
		imageViews = new ImageView[advPics.size()];
		for (int i = 0; i < advPics.size(); i++) {
			imageView = new ImageView(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(10, 10);
			lp.setMargins(5, 0, 5, 0);
			imageView.setLayoutParams(lp);
			imageViews[i] = imageView;
			if (i == 0) {
				// 默认第一个为选中状态
				imageViews[i].setImageResource(R.drawable.banner_dot_focus);
			} else {
				// 未选中状态
				imageViews[i].setImageResource(R.drawable.banner_dot_custom);
			}
			group.addView(imageViews[i]);
		}
		AdViewPager.setAdapter(new AdViewPagerAdapter(advPics));
		AdViewPager.setOnPageChangeListener(new GuidePageChangeListener());
		autoPlayThread.start();
		AdViewPager.setOnTouchListener(this);
	}

	/**
	 * 
	 * @Description:顶部轮播自动播放线程
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-10-30 上午8:31:45
	 */
	private Thread autoPlayThread = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				// 如果目前状态是自动轮播状态时，则执行自动轮播
				if (isContinue) {
					viewHandler.sendEmptyMessage(what.get());
					whatOption();
				}
			}

		}
	});

	/**
	 * 
	 * @className:com.xtwl.jy.client.activity.mainpage.GuidePageChangeListener
	 * @description:ViewPager页面滚动监听，用于显示dot点的状态
	 * @version:v1.0.0
	 * @date:2014-10-30 上午8:33:12
	 * @author:ShenYang
	 */
	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			// 设置当前选中的位置
			what.getAndSet(arg0);
			desc.setText(baseAdModels.get(arg0).getADName());
			for (int i = 0; i < imageViews.length; i++) { // 遍历，如果当前显示和选中的为同一个，就将dot选中为选中状态
				imageViews[arg0].setImageResource(R.drawable.banner_dot_focus);
				if (arg0 != i) {
					imageViews[i].setImageResource(R.drawable.banner_dot_custom);
				}
			}

		}

	}

	/**
	 * 
	 * @Description:设置每间隔5秒滚动一次
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-10-30 上午8:32:50
	 */
	private void whatOption() {
		what.incrementAndGet();
		if (what.get() > imageViews.length - 1) {
			what.getAndAdd(-4);
		}
		try {
			Thread.sleep(5000);// 线程休息4秒
		} catch (InterruptedException e) {

		}
	}

	/**
	 * 
	 * @Description:触摸屏幕事件监听
	 * @version:v1.0
	 * @author:ShenYang
	 * @date:2014-10-30 上午8:31:45
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			// 在手动移动viewpager的时候，不让其进行自动滚动
			isContinue = false;
			break;
		case MotionEvent.ACTION_UP:
			// 当手指离开屏幕时，进行自动轮播
			isContinue = true;
			break;
		default:
			isContinue = true;
			break;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> v, View arg1, int pos, long arg3) {
		if (ContactsUtils.userDetailModel != null) {
			PowerModel pm = (PowerModel) v.getAdapter().getItem(pos);
			switch (pm.getPowerID()) {
			case ContactsUtils.POWER_ALL_ORDER:
				// 订单
				Intent orderIntent = new Intent(HomePageActivity.this, OrderMainNewActivity.class);
				HomePageActivity.this.startActivity(orderIntent);
				break;
			case ContactsUtils.POWER_FLAG:
				// 标签管理操作
				// Intent flagIntent = new Intent(HomePageActivity.this,
				// FlagManager.class);
				// HomePageActivity.this.startActivity(flagIntent);
				Intent flagIntent = new Intent(HomePageActivity.this, FlagSearch.class);
				HomePageActivity.this.startActivity(flagIntent);
				break;
			case ContactsUtils.POWER_KUCUN:
				// showNoticeDialog();
				Intent stockIntent = new Intent(HomePageActivity.this, StockList.class);
				HomePageActivity.this.startActivity(stockIntent);
				break;
			case ContactsUtils.POWER_DRIVE:
				// 发货
				Intent deliveryIntent = new Intent(HomePageActivity.this, MyDaiYunOrderList.class);
				HomePageActivity.this.startActivity(deliveryIntent);
				break;
			case ContactsUtils.POWER_UPLOADING:
				// 卸货
				Intent uploadingIntent = new Intent(HomePageActivity.this, MyUploadingOrderList.class);
				HomePageActivity.this.startActivity(uploadingIntent);
				break;

			case ContactsUtils.POWER_RESOURCE_RECYLE:
				Intent recyleIntent = new Intent(HomePageActivity.this, ChooseCustomerActivity.class);
				HomePageActivity.this.startActivity(recyleIntent);
				// showNoticeDialog();
				break;
			case ContactsUtils.POWER_MY_ORDER:
				// 我的订单
				Intent myOrderIntent = new Intent(HomePageActivity.this, MyOrderListNew.class);
				myOrderIntent.putExtra("orderstate", ContactsUtils.ORDER_ALL);
				HomePageActivity.this.startActivity(myOrderIntent);
				break;
			case ContactsUtils.POWER_MY_MESSAGE:
				// showNoticeDialog();
				Intent messageIntent = new Intent(HomePageActivity.this, DefineCameraActivity.class);
				HomePageActivity.this.startActivity(messageIntent);
				break;
			case ContactsUtils.POWER_MY_TRADE:
				Intent myTradeIntent = new Intent(HomePageActivity.this, BalanceActivity.class);
				HomePageActivity.this.startActivity(myTradeIntent);
				break;
			case ContactsUtils.POWER_LOCATION:
				// 位置查看
				Intent locationIntent = new Intent(HomePageActivity.this, UserLocationMapView.class);
				HomePageActivity.this.startActivity(locationIntent);
				break;

			case ContactsUtils.POWER_DAIXIEHUO:
				// 代卸货
				Intent daixiehuoIntent = new Intent(HomePageActivity.this, DriverList.class);
				HomePageActivity.this.startActivity(daixiehuoIntent);
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);// 必须要调用这句
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		getUnReadMsgNum();
	}

	/**
	 * 
	 * @Title: startOrStopService
	 * @Description: 打开或者关闭服务
	 * @param @param flag  
	 * @return void 返回类型
	 * @throws
	 */
	private void startOrStopService(int flag) {
		Intent serviceIntent = new Intent(this, GetLocationService.class);
		switch (flag) {
		case 0:
			// 如果服务不在运行就打开服务
			if (!Tools.isServiceWork(this, "com.resmanager.client.map.GetLocationService")) {
				// 打开服务
				startService(serviceIntent);
			}
			break;
		case 1:
			// 如果服务在运行就停止服务
			if (Tools.isServiceWork(this, "com.resmanager.client.map.GetLocationService")) {
				// 停止服务
				stopService(serviceIntent);
			}

			break;

		default:
			break;
		}
	}

}
