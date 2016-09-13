/**
 * 
 */
package com.resmanager.client.order;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.Order;
import com.resmanager.client.model.OrderDetailInfo;
import com.resmanager.client.model.OrderPicListModel;
import com.resmanager.client.model.OrderPicModel;
import com.resmanager.client.model.ResultModel;
import com.resmanager.client.model.SourceModel;
import com.resmanager.client.order.GetOrderDetailAsyncTask.GetOrderDetailListener;
import com.resmanager.client.order.GetOrderPicListByOrderIdAsyncTask.GetOrderPicListListener;
import com.resmanager.client.order.OrderDeliveryCancelAsyncTask.OrderDeliveryCancelListener;
import com.resmanager.client.order.OrderUploadingCancelAsyncTask.OrdeDischargeCancelListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.CustomDialog;
import com.resmanager.client.view.DefineListView;
import com.resmanager.client.view.CustomDialog.ToDoListener;

/**
 * @author ShenYang
 * 
 */
public class OrderDetailActivity extends TopContainActivity implements OnClickListener {
	private TextView order_no_txt, order_customer_txt, order_address_txt, town_txt, saler_txt, weight_txt, quantity_txt, order_state_name,
			delivery_standards_txt, area_txt, area_price_txt, order_date_txt, request_date_txt, order_remark_txt, qianshou_man_txt, qianshou_date_txt,
			reply_info_txt, delivery_info_txt, delivery_chepai_txt, delivery_phone_txt, delivery_man_txt, qianshou_phone_txt, delivery_date_txt;
	private String orderId;
	private ScrollView detail_scoll;
	private DefineListView source_list;
	private OrderDetailInfo baseOrderDetailInfo;
	private LinearLayout driver_layout, uploading_layout;
	private Button reset_btn;
	private int btn_flag = 0;// 0:取消发货，1：取消卸货
	private CustomDialog customDialog;

	/*
	 * (非 Javadoc) <p>Title: onClick</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_img:
			this.finish();
			break;
		case R.id.delivery_phone_txt:
			if (baseOrderDetailInfo != null) {
				String number = baseOrderDetailInfo.getHead().getDriverTel().toString().trim();
				if (!number.equals("")) {
					Tools.jumpToTelPhone(this, number);
				}
			}

			break;
		case R.id.qianshou_phone_txt:
			if (baseOrderDetailInfo != null) {
				String number = baseOrderDetailInfo.getHead().getDeliveryTel().toString().trim();
				if (!number.equals("")) {
					Tools.jumpToTelPhone(this, number);
				}
			}

			break;
		case R.id.delivery_info_txt:
			getOrderPicsByOrderId();
			break;

		case R.id.reset_btn:
			showNoticeDialog();
			break;

		default:
			break;
		}
	}

	// private void getViewLabels(int picState){
	// if (baseOrderDetailInfo != null) {
	// GetViewLabelsAsyncTask getViewLabelsAsyncTask = new
	// GetViewLabelsAsyncTask(this, orderId,picState);
	// getViewLabelsAsyncTask.setGetLabelListener(new GetViewLabelListener() {
	//
	// @Override
	// public void getViewWLabelResult(ViewLabelListModel viewLabelListModel) {
	// if (viewLabelListModel != null) {
	// if (viewLabelListModel.getResult().equals("true")) {
	// if (viewLabelListModel.getData() != null &&
	// viewLabelListModel.getData().size() > 0) {
	// Intent intent = new Intent(OrderDetailActivity.this,
	// ViewLabelActivity.class);
	// intent.putExtra("viewLabelModels", viewLabelListModel.getData());
	// startActivity(intent);
	// } else {
	// Tools.showToast(OrderDetailActivity.this, "暂时还没有货物图片");
	// }
	// } else {
	// Tools.showToast(OrderDetailActivity.this,
	// viewLabelListModel.getDescription());
	// }
	// } else {
	// Tools.showToast(OrderDetailActivity.this, "暂时没有货物图片");
	// }
	// }
	// });
	// getViewLabelsAsyncTask.execute();
	// }
	// }

	/**
	 * 
	 * @Title: getOrderPicsByOrderId
	 * @Description: 根据订单ID获取订单图片
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getOrderPicsByOrderId() {
		GetOrderPicListByOrderIdAsyncTask getOrderPicListByOrderIdAsyncTask = new GetOrderPicListByOrderIdAsyncTask(this, orderId);
		getOrderPicListByOrderIdAsyncTask.setGetOrderPicListListener(new GetOrderPicListListener() {

			@Override
			public void getOrderPicListResult(OrderPicListModel orderPicListModel) {
				if (orderPicListModel != null) {
					if (orderPicListModel.getResult().equals("true")) {
						ArrayList<OrderPicModel> orderPicModels = orderPicListModel.getData();
						if (orderPicModels != null && orderPicModels.size() > 0) {
							Intent orderPicIntent = new Intent(OrderDetailActivity.this, ViewLabelActivity.class);
							orderPicIntent.putExtra("orderPicListModel", orderPicListModel);
							startActivity(orderPicIntent);
						} else {
							Tools.showToast(OrderDetailActivity.this, "该订单未卸货，暂无货物列表");
						}
					} else {
						Tools.showToast(OrderDetailActivity.this, orderPicListModel.getDescription());
					}
				} else {
					Tools.showToast(OrderDetailActivity.this, "暂无货物，请检查网络");
				}
			}
		});
		getOrderPicListByOrderIdAsyncTask.execute();
	}

	/*
	 * (非 Javadoc) <p>Title: getTopView</p> <p>Description: </p>
	 * 
	 * @return
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getTopView()
	 */
	@SuppressLint("InflateParams")
	@Override
	protected View getTopView() {
		orderId = getIntent().getExtras().getString("orderId");
		View topView = inflater.inflate(R.layout.custom_title_bar, null);
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("订单明细");
		return topView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.resmanager.client.common.TopContainActivity#getCenterView()
	 */
	@SuppressLint("InflateParams")
	@Override
	protected View getCenterView() {
		View contentView = inflater.inflate(R.layout.order_detail, null);
		initLayoutView(contentView);
		getOrderDetail();
		return contentView;
	}

	/**
	 * 
	 * @Title: initView
	 * @Description: 初始化布局中的View
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void initLayoutView(View v) {
		reset_btn = (Button) v.findViewById(R.id.reset_btn);
		reset_btn.setOnClickListener(this);
		delivery_date_txt = (TextView) v.findViewById(R.id.delivery_date_txt);
		order_no_txt = (TextView) v.findViewById(R.id.order_no_txt);
		detail_scoll = (ScrollView) v.findViewById(R.id.detail_scoll);
		driver_layout = (LinearLayout) v.findViewById(R.id.driver_layout);
		uploading_layout = (LinearLayout) v.findViewById(R.id.uploading_layout);
		order_customer_txt = (TextView) v.findViewById(R.id.order_customer_txt);
		order_address_txt = (TextView) v.findViewById(R.id.order_address_txt);
		town_txt = (TextView) v.findViewById(R.id.town_txt);
		saler_txt = (TextView) v.findViewById(R.id.saler_txt);
		weight_txt = (TextView) v.findViewById(R.id.weight_txt);
		quantity_txt = (TextView) v.findViewById(R.id.quantity_txt);
		order_state_name = (TextView) v.findViewById(R.id.order_state_name);
		delivery_standards_txt = (TextView) v.findViewById(R.id.delivery_standards_txt);
		area_txt = (TextView) v.findViewById(R.id.area_txt);
		area_price_txt = (TextView) v.findViewById(R.id.area_price_txt);
		order_date_txt = (TextView) v.findViewById(R.id.order_date_txt);
		source_list = (DefineListView) v.findViewById(R.id.source_list);
		request_date_txt = (TextView) v.findViewById(R.id.request_date_txt);
		order_remark_txt = (TextView) v.findViewById(R.id.order_remark_txt);
		qianshou_man_txt = (TextView) v.findViewById(R.id.qianshou_man_txt);
		qianshou_date_txt = (TextView) v.findViewById(R.id.qianshou_date_txt);
		reply_info_txt = (TextView) v.findViewById(R.id.reply_info_txt);
		reply_info_txt.setOnClickListener(this);
		delivery_info_txt = (TextView) v.findViewById(R.id.delivery_info_txt);
		delivery_info_txt.setOnClickListener(this);
		delivery_chepai_txt = (TextView) v.findViewById(R.id.delivery_chepai_txt);
		delivery_man_txt = (TextView) v.findViewById(R.id.delivery_man_txt);
		delivery_phone_txt = (TextView) v.findViewById(R.id.delivery_phone_txt);
		delivery_phone_txt.setOnClickListener(this);
		qianshou_phone_txt = (TextView) v.findViewById(R.id.qianshou_phone_txt);
		qianshou_phone_txt.setOnClickListener(this);
	}

	/**
	 * 
	 * @Title: getOrderDetail
	 * @Description: 获取订单明细
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getOrderDetail() {
		GetOrderDetailAsyncTask getOrderDetailAsyncTask = new GetOrderDetailAsyncTask(this, orderId, ContactsUtils.USER_KEY);
		getOrderDetailAsyncTask.setGetOrderDetailListener(new GetOrderDetailListener() {

			@Override
			public void getOrderDetailResult(OrderDetailInfo orderDetailInfo) {
				if (orderDetailInfo != null) {
					baseOrderDetailInfo = orderDetailInfo;
					if (orderDetailInfo.getResult().equals("true")) {
						detail_scoll.setVisibility(View.VISIBLE);
						setOrderDetail(orderDetailInfo);
					} else {
						detail_scoll.setVisibility(View.GONE);
						Tools.showToast(OrderDetailActivity.this, orderDetailInfo.getDescription());
					}
				} else {
					detail_scoll.setVisibility(View.GONE);
					Tools.showToast(OrderDetailActivity.this, "订单获取失败，请检查网络");
				}
			}
		});
		getOrderDetailAsyncTask.execute();
	}

	/**
	 * 
	 * @Title: setOrderDetail
	 * @Description: 设置订单详细
	 * @param @param orderDetailInfo 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void setOrderDetail(OrderDetailInfo orderDetailInfo) {

		Order order = orderDetailInfo.getHead();
		int orderState = Integer.parseInt(order.getOrderStateCode());
		if (orderState == ContactsUtils.ORDER_DAICHULI) {
			uploading_layout.setVisibility(View.GONE);
			driver_layout.setVisibility(View.GONE);
		} else if (orderState == ContactsUtils.ORDER_YUNSONGZHONG || orderState == ContactsUtils.ORDER_DAIYUNSONG) {
			uploading_layout.setVisibility(View.GONE);
			driver_layout.setVisibility(View.VISIBLE);
		} else {
			uploading_layout.setVisibility(View.VISIBLE);
			driver_layout.setVisibility(View.VISIBLE);
		}
		if (order.getDriverID().equals(ContactsUtils.userDetailModel.getUserId() + "")) {
			if (orderState == ContactsUtils.ORDER_YUNSONGZHONG) {
				reset_btn.setVisibility(View.VISIBLE);
				reset_btn.setText("撤销发货");
				btn_flag = 0;
			} else if (orderState == ContactsUtils.ORDER_DAISHENHE) {
				reset_btn.setVisibility(View.VISIBLE);
				reset_btn.setText("撤销卸货");
				btn_flag = 1;
			} else {
				reset_btn.setVisibility(View.GONE);
			}
		}
		ArrayList<SourceModel> sourceModels = orderDetailInfo.getDetail();
		order_no_txt.setText("订单编号: " + order.getSaleoid());
		order_customer_txt.setText("客户: " + order.getOrdercustomer());
		order_address_txt.setText("地址: " + order.getShippingaddress());
		town_txt.setText("区域: " + order.getTown());
		saler_txt.setText("业务员: " + order.getSaler());
		weight_txt.setText("重量: " + order.getShippingweight());
		quantity_txt.setText("数量: " + order.getQuantity());
		order_state_name.setText("状态: " + order.getOrderStateName());
		delivery_standards_txt.setText("配送标准: " + order.getDeliverystandards());
		area_txt.setText("区域: " + order.getTown());
		area_price_txt.setText("物流费用: ¥" + order.getOrdermoney());
		order_date_txt.setText("订单确认日期: " + order.getOrderdate());
		request_date_txt.setText("要求到货日期: " + order.getRequestarrivedate());
		if (order.getReferenceinfo() != null && !order.getReferenceinfo().equals("")) {
			order_remark_txt.setText(order.getReferenceinfo());
		} else {
			order_remark_txt.setText("无");
		}
		if (sourceModels != null && sourceModels.size() > 0) {
			ResourceListAdapter goodsListAdapter = new ResourceListAdapter(this, sourceModels);
			source_list.setAdapter(goodsListAdapter);
		}
		qianshou_man_txt.setText("收  货  人: " + order.getDeliveryMan());
		qianshou_phone_txt.setText("联系电话:" + order.getDeliveryTel());
		qianshou_date_txt.setText("收货日期: " + order.getDeliveryDate());
		delivery_man_txt.setText("承  运  人:" + order.getDriverName());
		delivery_chepai_txt.setText("车牌号码:" + order.getCarNumber());
		delivery_phone_txt.setText("联系电话:" + order.getDriverTel());
		delivery_date_txt.setText("发货日期:" + order.getDelivery_Date());
	}

	/**
	 * 
	 * @Title: showDialog
	 * @Description: 弹出提示对话框
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void showNoticeDialog() {
		if (customDialog == null) {
			customDialog = new CustomDialog(this, R.style.myDialogTheme);
		}
		if (btn_flag == 0) {
			customDialog.setContentText("注意:撤销发货后，同批次发货订单同时取消，请确认！");
		} else {
			customDialog.setContentText("是否确认撤销卸货？");
		}

		customDialog.setToDoListener(new ToDoListener() {

			@Override
			public void doSomething() {
				switch (btn_flag) {
				case 0:
					// 撤销发货
					OrderDeliveryCancelAsyncTask orderDeliveryCancelAsyncTask = new OrderDeliveryCancelAsyncTask(OrderDetailActivity.this, baseOrderDetailInfo
							.getHead().getWorkID());
					orderDeliveryCancelAsyncTask.setOrderDeliveryCancelListener(new OrderDeliveryCancelListener() {

						@Override
						public void orderDeliveryCancelResult(ResultModel resultModel) {
							if (resultModel != null) {
								if (resultModel.getResult().equals("true")) {
									Tools.showToast(OrderDetailActivity.this, "撤销发货成功");
									getOrderDetail();
								} else {
									Tools.showToast(OrderDetailActivity.this, resultModel.getDescription());
								}
							} else {
								Tools.showToast(OrderDetailActivity.this, "撤销发货失败,请检查网络");
							}

						}
					});
					orderDeliveryCancelAsyncTask.execute();
					break;
				case 1:
					// 撤销卸货

					OrderUploadingCancelAsyncTask orderUploadingCancelAsyncTask = new OrderUploadingCancelAsyncTask(OrderDetailActivity.this,
							baseOrderDetailInfo.getHead().getWorkID(), baseOrderDetailInfo.getHead().getOrderID());
					orderUploadingCancelAsyncTask.setOrderDischargeCancelListener(new OrdeDischargeCancelListener() {

						@Override
						public void orderDischargeCancelResult(ResultModel resultModel) {
							if (resultModel != null) {
								if (resultModel.getResult().equals("true")) {
									Tools.showToast(OrderDetailActivity.this, "卸货撤销成功");
									getOrderDetail();
								} else {
									Tools.showToast(OrderDetailActivity.this, resultModel.getDescription());
								}
							} else {
								Tools.showToast(OrderDetailActivity.this, "卸货撤销失败,请检查网络");
							}
						}
					});
					orderUploadingCancelAsyncTask.execute();
					break;

				default:
					break;
				}
			}
		});
		customDialog.show();
	}
}
