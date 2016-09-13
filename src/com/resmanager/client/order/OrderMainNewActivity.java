package com.resmanager.client.order;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.OrderOrpModel;
import com.resmanager.client.model.OrderStatusNumberModel;
import com.resmanager.client.model.OrderStatusNumberModel2;
import com.resmanager.client.order.GetTjEachOrderStatusAsyncTask.GetOrderStatusNumberListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.PullToRefreshLayout;
import com.resmanager.client.view.PullToRefreshLayout.OnRefreshListener;

@SuppressLint("InflateParams")
public class OrderMainNewActivity extends TopContainActivity implements
		OnRefreshListener {
	String[] orpName = { "全部订单", "待过滤", "待分配", "待运送", "运送中", "待审核", "待结算",
			"已完成" };
	int[] orpId = { ContactsUtils.ORDER_ALL, ContactsUtils.ORDER_ALL,
			ContactsUtils.ORDER_DAICHULI, ContactsUtils.ORDER_DAIYUNSONG,
			ContactsUtils.ORDER_YUNSONGZHONG, ContactsUtils.ORDER_DAISHENHE,
			ContactsUtils.ORDER_DAIJIESUAN, ContactsUtils.ORDER_YIWANCHENG };
	int[] isUsed = { ContactsUtils.ORDER_NOGUOLU, ContactsUtils.ORDER_DAIGUOLU,
			ContactsUtils.ORDER_YOUXIAO, ContactsUtils.ORDER_YOUXIAO,
			ContactsUtils.ORDER_YOUXIAO, ContactsUtils.ORDER_YOUXIAO,
			ContactsUtils.ORDER_YOUXIAO, ContactsUtils.ORDER_YOUXIAO };
	int[] drawableIds = { R.drawable.order_all, R.drawable.order_guolv,
			R.drawable.order_fenpei, R.drawable.order_daiyunsong,
			R.drawable.order_yunsongzhong, R.drawable.order_daishenhe,
			R.drawable.order_daijiesuan, R.drawable.order_yiwancheng };
	private ArrayList<OrderOrpModel> orderOrpModels = new ArrayList<OrderOrpModel>();
	private GridView order_main_grid;
	private OrderGirdAdapter orderGirdAdapter;

	// private String UserType;
	// private String DayType;
	// private String Days;
	// private String Town;
	// private String Saleoid;
	// private String ordercustomer;
	// private String userId;

	@Override
	protected View getTopView() {
		View topView = inflater.inflate(R.layout.custom_title_bar, null);
		return topView;
	}

	@Override
	protected View getCenterView() {
		View contentView = inflater.inflate(R.layout.order_main_new, null);
		return contentView;
	}

	// 0普通用户,1驾驶员,2管理员如果当前登录用户是驾驶员,把待分配,待过滤,全部订单隐藏掉待运送可以查看,但是顶部的操作按钮要隐藏掉不能让驾驶员操作

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// UserType = getIntent().getExtras().getString("UserType");
		// DayType = getIntent().getExtras().getString("DayType");
		// Days = getIntent().getExtras().getString("Days");
		// Town = getIntent().getExtras().getString("Town");
		// Saleoid = getIntent().getExtras().getString("Saleoid");
		// ordercustomer = getIntent().getExtras().getString("ordercustomer");
		// userId = getIntent().getExtras().getString("userId");
		ImageView leftImg = (ImageView) topView
				.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				OrderMainNewActivity.this.finish();
			}
		});
		TextView titleContent = (TextView) topView
				.findViewById(R.id.title_content);
		titleContent.setText("订单");
		order_main_grid = (GridView) centerView
				.findViewById(R.id.order_main_grid);
		getEachOrderStatusNumber();

	}

	/**
	 * 
	 * @Title: initOrpGrid
	 * @Description: 初始化操作表格
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void initOrpGrid(OrderStatusNumberModel orderStatusNumberModel) {
		// //当用户类型为驾驶员或者管理员搜索指定用户的订单时，不显示全部订单，订单过滤，订单分配等
		// if (UserType.equals("1") || !userId.equals("")) {
		// for (int i = 3; i < orpName.length; i++) {
		// OrderOrpModel orderOrpModel = new OrderOrpModel();
		// orderOrpModel.setIsused(isUsed[i]);
		// orderOrpModel.setNum(0);
		// orderOrpModel.setOrpImg(drawableIds[i]);
		// orderOrpModel.setOrpName(orpName[i]);
		// orderOrpModel.setOrpId(orpId[i]);
		// if (orderStatusNumberModel != null) {
		// if (orpId[i] == ContactsUtils.ORDER_ALL && isUsed[i] ==
		// ContactsUtils.ORDER_NOGUOLU) {
		// orderOrpModel.setNum(orderStatusNumberModel.getOrderall());
		// } else if (orpId[i] == ContactsUtils.ORDER_ALL && isUsed[i] ==
		// ContactsUtils.ORDER_DAIGUOLU) {
		// orderOrpModel.setNum(orderStatusNumberModel.getOrder_dcl());
		// } else if (orpId[i] == ContactsUtils.ORDER_DAICHULI && isUsed[i] ==
		// ContactsUtils.ORDER_YOUXIAO) {
		// orderOrpModel.setNum(orderStatusNumberModel.getOrder0());
		// } else if (orpId[i] == ContactsUtils.ORDER_DAIYUNSONG && isUsed[i] ==
		// ContactsUtils.ORDER_YOUXIAO) {
		// orderOrpModel.setNum(orderStatusNumberModel.getOrder1());
		// } else if (orpId[i] == ContactsUtils.ORDER_YUNSONGZHONG && isUsed[i]
		// == ContactsUtils.ORDER_YOUXIAO) {
		// orderOrpModel.setNum(orderStatusNumberModel.getOrder2());
		// } else if (orpId[i] == ContactsUtils.ORDER_DAISHENHE && isUsed[i] ==
		// ContactsUtils.ORDER_YOUXIAO) {
		// orderOrpModel.setNum(orderStatusNumberModel.getOrder3());
		// } else if (orpId[i] == ContactsUtils.ORDER_DAIJIESUAN && isUsed[i] ==
		// ContactsUtils.ORDER_YOUXIAO) {
		// orderOrpModel.setNum(orderStatusNumberModel.getOrder4());
		// } else if (orpId[i] == ContactsUtils.ORDER_YIWANCHENG && isUsed[i] ==
		// ContactsUtils.ORDER_YOUXIAO) {
		// orderOrpModel.setNum(orderStatusNumberModel.getOrder5());
		// }
		// }
		// orderOrpModels.add(orderOrpModel);
		// }
		// } else {

		for (int i = 0; i < orpName.length; i++) {
			OrderOrpModel orderOrpModel = new OrderOrpModel();
			orderOrpModel.setIsused(isUsed[i]);
			orderOrpModel.setNum(0);
			orderOrpModel.setOrpImg(drawableIds[i]);
			orderOrpModel.setOrpName(orpName[i]);
			orderOrpModel.setOrpId(orpId[i]);
			if (orderStatusNumberModel != null) {
				if (orpId[i] == ContactsUtils.ORDER_ALL
						&& isUsed[i] == ContactsUtils.ORDER_NOGUOLU) {
					orderOrpModel.setNum(orderStatusNumberModel.getOrderall());
				} else if (orpId[i] == ContactsUtils.ORDER_ALL
						&& isUsed[i] == ContactsUtils.ORDER_DAIGUOLU) {
					orderOrpModel.setNum(orderStatusNumberModel.getOrder_dcl());
				} else if (orpId[i] == ContactsUtils.ORDER_DAICHULI
						&& isUsed[i] == ContactsUtils.ORDER_YOUXIAO) {
					orderOrpModel.setNum(orderStatusNumberModel.getOrder0());
				} else if (orpId[i] == ContactsUtils.ORDER_DAIYUNSONG
						&& isUsed[i] == ContactsUtils.ORDER_YOUXIAO) {
					orderOrpModel.setNum(orderStatusNumberModel.getOrder1());
				} else if (orpId[i] == ContactsUtils.ORDER_YUNSONGZHONG
						&& isUsed[i] == ContactsUtils.ORDER_YOUXIAO) {
					orderOrpModel.setNum(orderStatusNumberModel.getOrder2());
				} else if (orpId[i] == ContactsUtils.ORDER_DAISHENHE
						&& isUsed[i] == ContactsUtils.ORDER_YOUXIAO) {
					orderOrpModel.setNum(orderStatusNumberModel.getOrder3());
				} else if (orpId[i] == ContactsUtils.ORDER_DAIJIESUAN
						&& isUsed[i] == ContactsUtils.ORDER_YOUXIAO) {
					orderOrpModel.setNum(orderStatusNumberModel.getOrder4());
				} else if (orpId[i] == ContactsUtils.ORDER_YIWANCHENG
						&& isUsed[i] == ContactsUtils.ORDER_YOUXIAO) {
					orderOrpModel.setNum(orderStatusNumberModel.getOrder5());
				}
			}
			orderOrpModels.add(orderOrpModel);
		}
		// }
		orderGirdAdapter = new OrderGirdAdapter(this, orderOrpModels);
		order_main_grid.setAdapter(orderGirdAdapter);
		order_main_grid.setOnItemClickListener(onItemClickListener);
	}

	/**
	 * 
	 * @Title: getEachOrderStatusNumber
	 * @Description:获取各种状态订单数
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getEachOrderStatusNumber() {
		GetTjEachOrderStatusAsyncTask getTjEachOrderStatusAsyncTask = new GetTjEachOrderStatusAsyncTask(
				this);
		getTjEachOrderStatusAsyncTask
				.setGetOrderStatusNumberListener(new GetOrderStatusNumberListener() {

					@Override
					public void getOrderStatusNumberResult(
							OrderStatusNumberModel2 orderStatusNumberModel) {
						if (orderStatusNumberModel != null) {
							if (orderStatusNumberModel.getResult().equals(
									"true")) {
								initOrpGrid(orderStatusNumberModel.getData());
							} else {
								Tools.showToast(OrderMainNewActivity.this,
										orderStatusNumberModel.getDescription());
								initOrpGrid(null);
							}
						} else {
							Tools.showToast(OrderMainNewActivity.this,
									"订单数量获取失败，请检查网络");
							initOrpGrid(null);
						}
					}
				});
		getTjEachOrderStatusAsyncTask.execute();
	}

	/**
	 * gridView点击事件
	 */
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			OrderOrpModel orderOrpModel = (OrderOrpModel) orderGirdAdapter
					.getItem(pos);
			int orpId = orderOrpModel.getOrpId();
			int isUsedId = orderOrpModel.getIsused();
			Intent allOrder = new Intent();
			if (orpId == ContactsUtils.ORDER_ALL
					&& isUsedId == ContactsUtils.ORDER_DAIGUOLU) {
				allOrder.setClass(OrderMainNewActivity.this,
						OrderListFilter.class);
			} else if (orpId == ContactsUtils.ORDER_DAICHULI
					&& isUsedId == ContactsUtils.ORDER_YOUXIAO) {
				allOrder.setClass(OrderMainNewActivity.this,
						OrderListSend.class);
			} else if (orpId == ContactsUtils.ORDER_DAIYUNSONG
					&& isUsedId == ContactsUtils.ORDER_YOUXIAO) {
				allOrder.setClass(OrderMainNewActivity.this,
						OrderListRevoke.class);
			} else {
				allOrder.setClass(OrderMainNewActivity.this, AllOrderList.class);
			}
			allOrder.putExtra("orderState", orderOrpModel.getOrpId());
			allOrder.putExtra("isUsed", orderOrpModel.getIsused());
			if (ContactsUtils.userDetailModel.getUserType().equals("4")) {
				allOrder.putExtra("saler",
						ContactsUtils.userDetailModel.getNickName() + "");
			} else {
				allOrder.putExtra("saler", "");
			}
			// allOrder.putExtra("UserType", UserType);
			// allOrder.putExtra("DayType", DayType);
			// allOrder.putExtra("Days", Days);
			// allOrder.putExtra("Town", Town);
			// allOrder.putExtra("Saleoid", Saleoid);
			// allOrder.putExtra("userId", userId);
			// allOrder.putExtra("ordercustomer", ordercustomer);
			startActivity(allOrder);
		}
	};

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		getEachOrderStatusNumber();
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		getEachOrderStatusNumber();
	}

}
