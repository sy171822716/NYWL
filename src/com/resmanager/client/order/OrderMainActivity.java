///**   
// * @Title: OrderMainActivity.java 
// * @Package com.resmanager.client.order 
// * @Description: 订单主界面
// * @author ShenYang  
// * @date 2015-11-27 上午10:09:29 
// * @version V1.0   
// */
//package com.resmanager.client.order;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.resmanager.client.R;
//import com.resmanager.client.common.TopContainActivity;
//import com.resmanager.client.model.OrderStatusNumberModel;
//import com.resmanager.client.model.OrderStatusNumberModel2;
//import com.resmanager.client.order.GetTjEachOrderStatusAsyncTask.GetOrderStatusNumberListener;
//import com.resmanager.client.utils.ContactsUtils;
//import com.resmanager.client.utils.Tools;
//
///**
// * @ClassName: OrderMainActivity
// * @Description: 订单主界面
// * @author ShenYang
// * @date 2015-11-27 上午10:09:29
// * 
// */
//public class OrderMainActivity extends TopContainActivity implements OnClickListener {
//	private TextView all_order_number, order_daichuli_number, order_daiyunsong_number, order_yunsongzhong_number, order_daishenhe_number,
//			order_daijiesuan_number;
//
//	/*
//	 * (非 Javadoc) <p>Title: onClick</p> <p>Description: </p>
//	 * 
//	 * @param arg0
//	 * 
//	 * @see android.view.View.OnClickListener#onClick(android.view.View)
//	 */
//	@Override
//	public void onClick(View v) {
//		Intent allOrder = new Intent(OrderMainActivity.this, AllOrderList.class);
//		switch (v.getId()) {
//		case R.id.all_order_txt:
//			allOrder.putExtra("orderState", ContactsUtils.ORDER_ALL);
//			break;
//		case R.id.order_daijiesuan_txt:
//			allOrder.putExtra("orderState", ContactsUtils.ORDER_DAIJIESUAN);
//			break;
//		case R.id.order_daishenhe_txt:
//			allOrder.putExtra("orderState", ContactsUtils.ORDER_DAISHENHE);
//			break;
//		case R.id.order_daichuli_txt:
//			allOrder.putExtra("orderState", ContactsUtils.ORDER_DAICHULI);
//			break;
//		case R.id.order_daiyunsong_txt:
//			allOrder.putExtra("orderState", ContactsUtils.ORDER_DAIYUNSONG);
//			break;
//		case R.id.order_yunsongzhong_txt:
//			allOrder.putExtra("orderState", ContactsUtils.ORDER_YUNSONGZHONG);
//			break;
//		default:
//			break;
//		}
//		startActivity(allOrder);
//	}
//
//	/*
//	 * (非 Javadoc) <p>Title: getTopView</p> <p>Description: </p>
//	 * 
//	 * @return
//	 * 
//	 * @see com.resmanager.client.common.TopContainActivity#getTopView()
//	 */
//	@SuppressLint("InflateParams")
//	@Override
//	protected View getTopView() {
//		View topView = inflater.inflate(R.layout.custom_title_bar, null);
//		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
//		leftImg.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				OrderMainActivity.this.finish();
//			}
//		});
//		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
//		titleContent.setText("订单");
//		return topView;
//	}
//
//	/*
//	 * (非 Javadoc) <p>Title: getCenterView</p> <p>Description: </p>
//	 * 
//	 * @return
//	 * 
//	 * @see com.resmanager.client.common.TopContainActivity#getCenterView()
//	 */
//	@SuppressLint("InflateParams")
//	@Override
//	protected View getCenterView() {
//		View contentView = inflater.inflate(R.layout.order_main, null);
//		contentView.findViewById(R.id.all_order_txt).setOnClickListener(this);
//		contentView.findViewById(R.id.order_daijiesuan_txt).setOnClickListener(this);
//		contentView.findViewById(R.id.order_daishenhe_txt).setOnClickListener(this);
//		contentView.findViewById(R.id.order_daichuli_txt).setOnClickListener(this);
//		contentView.findViewById(R.id.order_daiyunsong_txt).setOnClickListener(this);
//		contentView.findViewById(R.id.order_yunsongzhong_txt).setOnClickListener(this);
//		all_order_number = (TextView) contentView.findViewById(R.id.all_order_number);
//		order_daichuli_number = (TextView) contentView.findViewById(R.id.order_daichuli_number);
//		order_daiyunsong_number = (TextView) contentView.findViewById(R.id.order_daiyunsong_number);
//		order_yunsongzhong_number = (TextView) contentView.findViewById(R.id.order_yunsongzhong_number);
//		order_daishenhe_number = (TextView) contentView.findViewById(R.id.order_daishenhe_number);
//		order_daijiesuan_number = (TextView) contentView.findViewById(R.id.order_daijiesuan_number);
//		getEachOrderStatusNumber();
//		return contentView;
//	}
//
//	/**
//	 * 
//	 * @Title: getEachOrderStatusNumber
//	 * @Description:获取各种状态订单数
//	 * @param 设定文件
//	 * @return void 返回类型
//	 * @throws
//	 */
//	private void getEachOrderStatusNumber() {
//		GetTjEachOrderStatusAsyncTask getTjEachOrderStatusAsyncTask = new GetTjEachOrderStatusAsyncTask(this);
//		getTjEachOrderStatusAsyncTask.setGetOrderStatusNumberListener(new GetOrderStatusNumberListener() {
//
//			@Override
//			public void getOrderStatusNumberResult(OrderStatusNumberModel2 orderStatusNumberModel) {
//				if (orderStatusNumberModel != null) {
//					if (orderStatusNumberModel.getResult().equals("true")) {
//						setNumber(orderStatusNumberModel.getData());
//					} else {
//						Tools.showToast(OrderMainActivity.this, orderStatusNumberModel.getDescription());
//					}
//				} else {
//					Tools.showToast(OrderMainActivity.this, "订单数量获取失败，请检查网络");
//				}
//			}
//		});
//		getTjEachOrderStatusAsyncTask.execute();
//	}
//
//	/**
//	 * 
//	 * @Title: setNumber
//	 * @Description: 设置数字
//	 * @param 设定文件
//	 * @return void 返回类型
//	 * @throws
//	 */
//	private void setNumber(OrderStatusNumberModel orderStatusNumberModel) {
//		all_order_number.setText("(" + orderStatusNumberModel.getOrderall() + ")");
//		order_daichuli_number.setText("(" + orderStatusNumberModel.getOrder0() + ")");
//		order_daiyunsong_number.setText("(" + orderStatusNumberModel.getOrder1() + ")");
//		order_yunsongzhong_number.setText("(" + orderStatusNumberModel.getOrder2() + ")");
//		order_daishenhe_number.setText("(" + orderStatusNumberModel.getOrder3() + ")");
//		order_daijiesuan_number.setText("(" + orderStatusNumberModel.getOrder4() + ")");
//	}
//
//	@Override
//	protected void onRestart() {
//		super.onRestart();
//		getEachOrderStatusNumber();
//	}
//}
