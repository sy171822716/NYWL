/**   
 * @Title: ChooseCustomerActivity.java 
 * @Package com.resmanager.client.user.recyle 
 * @Description: 选择客户 
 * @author ShenYang  
 * @date 2016-1-5 上午9:20:28 
 * @version V1.0   
 */
package com.resmanager.client.user.recyle;

import java.util.ArrayList;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.CustomerListModel;
import com.resmanager.client.model.CustomerModel;
import com.resmanager.client.model.RecyleLabelListModel;
import com.resmanager.client.user.recyle.GetCustomerListAsyncTask.GetCustomerListListener;
import com.resmanager.client.user.recyle.GetLabelByCustomerListAsyncTask.GetLabelByCustomerListener;
import com.resmanager.client.user.recyle.SideBar.OnTouchingLetterChangedListener;
import com.resmanager.client.utils.Tools;
import com.resmanager.client.view.CustomDialog;
import com.resmanager.client.view.CustomDialog.ToDoListener;

/**
 * @ClassName: ChooseCustomerActivity
 * @Description: 选择客户
 * @author ShenYang
 * @date 2016-1-5 上午9:20:28
 * 
 */
@SuppressLint({ "InflateParams", "DefaultLocale" })
public class ChooseCustomerActivity extends TopContainActivity implements OnClickListener {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortGroupMemberAdapter adapter;
	private ClearEditText mClearEditText;

	private LinearLayout titleLayout;
	private TextView title;
	private CharacterParser characterParser;
	private TextView tvNofriends;
	/**
	 * 上次第一个可见元素，用于滚动时记录标识。
	 */
	private int lastFirstVisibleItem = -1;
	/**
	 * 汉字转换成拼音的类
	 */

	private ArrayList<CustomerModel> SourceDateList;
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private CustomDialog noticeDialog;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_img:
			this.finish();
			break;
		case R.id.title_right_img:
			showNoticeDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Title: showNoticeDialog
	 * @Description: 弹出提示框
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void showNoticeDialog() {
		if (noticeDialog == null) {
			noticeDialog = new CustomDialog(this, R.style.myDialogTheme);
			noticeDialog.setContentText("列表中没有想要的客户？刷新下试试！");
			noticeDialog.setToDoListener(new ToDoListener() {

				@Override
				public void doSomething() {
					if (adapter != null) {
						adapter = null;
					}
					getCustomerByNet(true);
					noticeDialog.dismiss();
				}
			});
		}
		noticeDialog.show();
	}

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
		return topView;
	}

	@Override
	protected View getCenterView() {
		return inflater.inflate(R.layout.activity_add_customer, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		ImageView leftImg = (ImageView) topView.findViewById(R.id.title_left_img);
		leftImg.setOnClickListener(this);
		ImageView rightImg = (ImageView) topView.findViewById(R.id.title_right_img);
		rightImg.setVisibility(View.VISIBLE);
		rightImg.setImageResource(R.drawable.question);
		rightImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView.findViewById(R.id.title_content);
		titleContent.setText("客户列表");
		inits();
		getCustomerByNet(false);

	}

	private void inits() {
		titleLayout = (LinearLayout) centerView.findViewById(R.id.title_layout);
		title = (TextView) centerView.findViewById(R.id.title_layout_catalog);
		tvNofriends = (TextView) centerView.findViewById(R.id.title_layout_no_friends);

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) centerView.findViewById(R.id.sidrbar);
		dialog = (TextView) centerView.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) centerView.findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				final CustomerModel customerModel = (CustomerModel) adapter.getItem(position);
				GetLabelByCustomerListAsyncTask getLabelByCustomerListAsyncTask = new GetLabelByCustomerListAsyncTask(ChooseCustomerActivity.this,
						customerModel.getCustomerID(), "", "", true);
				getLabelByCustomerListAsyncTask.setGetLabelByCustomerListener(new GetLabelByCustomerListener() {

					@Override
					public void getLabelByCustomerResult(RecyleLabelListModel recyleLabelListModel) {
						if (recyleLabelListModel != null) {
							if (recyleLabelListModel.getResult().equals("true")) {
								if (recyleLabelListModel.getData().size() > 0) {
									Intent recyleIntent = new Intent(ChooseCustomerActivity.this, RecyleActivity.class);
									recyleIntent.putExtra("customerModel", customerModel);
									startActivity(recyleIntent);
								} else {
									Tools.showToast(ChooseCustomerActivity.this, "该客户暂时没有桶需要回收");
								}
							} else {
								Tools.showToast(ChooseCustomerActivity.this, "该客户暂时没有桶需要回收");
							}
						} else {
							Tools.showToast(ChooseCustomerActivity.this, "获取客户信息获取失败，请检查网络");
						}
					}
				});
				getLabelByCustomerListAsyncTask.execute();

			}
		});

	}

	/**
	 * 
	 * @Title: getCustomerByNet
	 * @Description: 从服务器获取客户
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void getCustomerByNet(boolean isGetFromNet) {
		GetCustomerListAsyncTask getCustomerListAsyncTask = new GetCustomerListAsyncTask(this, "", 1, isGetFromNet);
		getCustomerListAsyncTask.setGetCustomerListListener(new GetCustomerListListener() {

			@Override
			public void getCustomerListResult(CustomerListModel customerListModel) {
				if (customerListModel != null) {
					if (customerListModel.getResult().equals("true")) {
						SourceDateList = customerListModel.getData();
						// 根据a-z进行排序源数据
						Collections.sort(SourceDateList, pinyinComparator);
						adapter = new SortGroupMemberAdapter(ChooseCustomerActivity.this, SourceDateList);
						sortListView.setAdapter(adapter);
						sortListView.setOnScrollListener(new OnScrollListener() {
							@Override
							public void onScrollStateChanged(AbsListView view, int scrollState) {
							}

							@Override
							public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
								int section = getSectionForPosition(firstVisibleItem);
								int nextSection = getSectionForPosition(firstVisibleItem + 1);
								int nextSecPosition = getPositionForSection(+nextSection);
								if (firstVisibleItem != lastFirstVisibleItem) {
									MarginLayoutParams params = (MarginLayoutParams) titleLayout.getLayoutParams();
									params.topMargin = 0;
									titleLayout.setLayoutParams(params);
									title.setText(SourceDateList.get(getPositionForSection(section)).getSortLetters());
								}
								if (nextSecPosition == firstVisibleItem + 1) {
									View childView = view.getChildAt(0);
									if (childView != null) {
										int titleHeight = titleLayout.getHeight();
										int bottom = childView.getBottom();
										MarginLayoutParams params = (MarginLayoutParams) titleLayout.getLayoutParams();
										if (bottom < titleHeight) {
											float pushedDistance = bottom - titleHeight;
											params.topMargin = (int) pushedDistance;
											titleLayout.setLayoutParams(params);
										} else {
											if (params.topMargin != 0) {
												params.topMargin = 0;
												titleLayout.setLayoutParams(params);
											}
										}
									}
								}
								lastFirstVisibleItem = firstVisibleItem;
							}
						});
						mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

						// 根据输入框输入值的改变来过滤搜索
						mClearEditText.addTextChangedListener(new TextWatcher() {

							@Override
							public void onTextChanged(CharSequence s, int start, int before, int count) {
								// 这个时候不需要挤压效果 就把他隐藏掉
								titleLayout.setVisibility(View.GONE);
								// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
								filterData(s.toString());
							}

							@Override
							public void beforeTextChanged(CharSequence s, int start, int count, int after) {

							}

							@Override
							public void afterTextChanged(Editable s) {
							}
						});
					} else {
						Tools.showToast(ChooseCustomerActivity.this, customerListModel.getDescription());
					}
				} else {
					Tools.showToast(ChooseCustomerActivity.this, "客户列表获取失败，请检查网络");
				}
			}
		});
		getCustomerListAsyncTask.execute();
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		if (SourceDateList != null && SourceDateList.size() > 0 && position < (SourceDateList.size()-1)) {
			try {
				return SourceDateList.get(position).getSortLetters().charAt(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < SourceDateList.size(); i++) {
			String sortStr = SourceDateList.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		ArrayList<CustomerModel> filterDateList = new ArrayList<CustomerModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
			tvNofriends.setVisibility(View.GONE);
		} else {
			filterDateList.clear();
			for (CustomerModel sortModel : SourceDateList) {
				String name = sortModel.getCustomerName();
				if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
		if (filterDateList.size() == 0) {
			tvNofriends.setVisibility(View.VISIBLE);
		}
	}
}
