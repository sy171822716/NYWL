/**   
 * @Title: ChooseResourceActivity.java 
 * @Package com.resmanager.client.user.recyle 
 * @Description: 选择回收货物
 * @author ShenYang  
 * @date 2016-1-7 下午1:32:28 
 * @version V1.0   
 */
package com.resmanager.client.user.recyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.resmanager.client.R;
import com.resmanager.client.common.TopContainActivity;
import com.resmanager.client.model.RecyleLabelListModel;
import com.resmanager.client.model.RecyleLabelModel;
import com.resmanager.client.user.recyle.GetLabelByCustomerListAsyncTask.GetLabelByCustomerListener;
import com.resmanager.client.utils.ContactsUtils;
import com.resmanager.client.utils.Tools;

/**
 * @ClassName: ChooseResourceActivity
 * @Description: 选择回收货物
 * @author ShenYang
 * @date 2016-1-7 下午1:32:28
 * 
 */
@SuppressLint("InflateParams")
public class ChooseResourceActivity extends TopContainActivity implements
		OnClickListener {
	private ArrayList<RecyleLabelModel> recyleLabelModels;
	private ListView label_list;
	private EditText searchLabelEdit;
	// private CustomDialog noticeDialog;
	private ChooseResourceListAdapter chooseResourceListAdapter;
	private ArrayList<RecyleLabelModel> selectedLabelModels;
	private ArrayList<RecyleLabelModel> filterLabelModels = new ArrayList<RecyleLabelModel>();
	private String workId;
	private String customerId;

	// private Map<String, RecyleLabelModel> selectMap = new HashMap<String,
	// RecyleLabelModel>();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_img:
			this.finish();
			break;
		case R.id.ok_btn:
			// if (selectMap.size() > 0) {
			// ArrayList<RecyleLabelModel> selectedModels = new
			// ArrayList<RecyleLabelModel>();
			// for (Map.Entry<String, RecyleLabelModel> entry :
			// selectMap.entrySet()) {
			// selectedModels.add(entry.getValue());
			// }
			// Intent intent = new Intent(ChooseResourceActivity.this,
			// AddRecyleSource.class);
			// intent.putExtra("recyleLabelModels", selectedModels);
			// setResult(ContactsUtils.CHOOSE_LABEL_RESULT, intent);
			// this.finish();
			// } else {
			// Tools.showToast(this, "请选择回收货物的标签");
			// }
			// if (filterLabelModels != null && filterLabelModels.size() != 0) {
			// for (int i = 0; i < filterLabelModels.size(); i++) {
			// if (filterLabelModels.get(i).isSelect()) {
			// selectedModels.add(filterLabelModels.get(i));
			// }
			// }
			// if (selectedModels.size() > 0) {
			// Intent intent = new Intent(ChooseResourceActivity.this,
			// AddRecyleSource.class);
			// intent.putExtra("recyleLabelModels", selectedModels);
			// setResult(ContactsUtils.CHOOSE_LABEL_RESULT, intent);
			// this.finish();
			// } else {
			// Tools.showToast(this, "请选择回收货物的标签");
			// }
			// } else {
			// if (recyleLabelModels != null) {
			// for (int i = 0; i < recyleLabelModels.size(); i++) {
			// if (recyleLabelModels.get(i).isSelect()) {
			// selectedModels.add(recyleLabelModels.get(i));
			// }
			// }
			//
			// if (selectedModels.size() > 0) {
			// Intent intent = new Intent(ChooseResourceActivity.this,
			// AddRecyleSource.class);
			// intent.putExtra("recyleLabelModels", selectedModels);
			// setResult(ContactsUtils.CHOOSE_LABEL_RESULT, intent);
			// this.finish();
			// } else {
			// Tools.showToast(this, "请选择回收货物的标签");
			// }
			// }
			// }

			break;
		default:
			break;
		}
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
		return inflater.inflate(R.layout.choose_resource_label_list, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		workId = getIntent().getExtras().getString("workId");
		customerId = getIntent().getExtras().getString("customerId");
		selectedLabelModels = (ArrayList<RecyleLabelModel>) getIntent()
				.getExtras().getSerializable("selectedLabelModels");
		recyleLabelModels = (ArrayList<RecyleLabelModel>) getIntent()
				.getExtras().getSerializable("recyleLabelModels");
		ImageView leftImg = (ImageView) topView
				.findViewById(R.id.title_left_img);
		searchLabelEdit = (EditText) centerView
				.findViewById(R.id.search_input_edit);
		leftImg.setOnClickListener(this);
		TextView titleContent = (TextView) topView
				.findViewById(R.id.title_content);
		titleContent.setText("选择货物标签");
		label_list = (ListView) centerView.findViewById(R.id.label_list);
		for (int i = 0; i < selectedLabelModels.size(); i++) {
			RecyleLabelModel selectModel = selectedLabelModels.get(i);
			// if (!selectMap.containsKey(selectModel.getLabelCode())) {
			// selectMap.put(selectModel.getLabelCode(), selectModel);//
			// 选中的加入到map中
			// }
			for (int j = 0; j < recyleLabelModels.size(); j++) {
				RecyleLabelModel recyleLabelModel = recyleLabelModels.get(j);
				if (recyleLabelModel.getLabelCode().equals(
						selectModel.getLabelCode())) {
					recyleLabelModel.setSelect(true);
				}
			}
		}
		if (recyleLabelModels != null) {
			chooseResourceListAdapter = new ChooseResourceListAdapter(this,
					recyleLabelModels, true);
			label_list.setAdapter(chooseResourceListAdapter);
		}

		label_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> v, View arg1, int pos,
					long arg3) {
				RecyleLabelModel recyleLabelModel = null;
				if (filterLabelModels != null && filterLabelModels.size() != 0) {
					recyleLabelModel = filterLabelModels.get(pos);
				} else {
					recyleLabelModel = recyleLabelModels.get(pos);
				}
				ArrayList<RecyleLabelModel> selectedModels = new ArrayList<RecyleLabelModel>();
				// for (Map.Entry<String, RecyleLabelModel> entry : selectMap
				// .entrySet()) {
				//
				// }
				selectedModels.add(recyleLabelModel);
				Intent intent = new Intent(ChooseResourceActivity.this,
						AddRecyleSource.class);
				intent.putExtra("recyleLabelModels", selectedModels);
				setResult(ContactsUtils.CHOOSE_LABEL_RESULT, intent);
				ChooseResourceActivity.this.finish();
				// if (recyleLabelModel.isSelect() == false) {
				// recyleLabelModel.setSelect(true);
				// if (!selectMap.containsKey(recyleLabelModel.getLabelCode()))
				// {
				// selectMap.put(recyleLabelModel.getLabelCode(),
				// recyleLabelModel);// 选中的加入到map中
				// }
				// } else {
				// recyleLabelModel.setSelect(false);
				// if (selectMap.containsKey(recyleLabelModel.getLabelCode())) {
				// selectMap.remove(recyleLabelModel.getLabelCode());
				// }
				// }
				// if (chooseResourceListAdapter != null) {
				// chooseResourceListAdapter.notifyDataSetChanged();
				// }

			}
		});
		centerView.findViewById(R.id.ok_btn).setOnClickListener(this);

		searchLabelEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				searchData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});
	}

	// /**
	// *
	// * @Title: showNoticeDialog
	// * @Description: 提示框
	// * @param @param recyleLabelModel 设定文件
	// * @return void 返回类型
	// * @throws
	// */
	// private void showNoticeDialog(RecyleLabelModel recyleLabelModel) {
	// final RecyleLabelModel tempModel = recyleLabelModel;
	// if (noticeDialog == null) {
	// noticeDialog = new CustomDialog(this, R.style.myDialogTheme);
	// noticeDialog.setContentText("该标签已选择，是否取消选择");
	// noticeDialog.setToDoListener(new ToDoListener() {
	//
	// @Override
	// public void doSomething() {
	// tempModel.setSelect(false);
	// if (chooseResourceListAdapter != null) {
	// chooseResourceListAdapter.notifyDataSetChanged();
	// }
	// noticeDialog.dismiss();
	// }
	// });
	// }
	// noticeDialog.show();
	//
	// }

	/**
	 * @Description:过滤数据
	 * @param string
	 * @version:v1.0
	 * @author:FuHuiHui
	 * @date:2016-3-28 上午10:30:11
	 */
	protected void searchData(String filterStr) {
		GetLabelByCustomerListAsyncTask getLabelByCustomerListAsyncTask = new GetLabelByCustomerListAsyncTask(
				this, customerId, workId, filterStr, false);
		getLabelByCustomerListAsyncTask
				.setGetLabelByCustomerListener(new GetLabelByCustomerListener() {

					@Override
					public void getLabelByCustomerResult(
							RecyleLabelListModel recyleLabelListModel) {
						if (recyleLabelListModel != null) {
							if (recyleLabelListModel.getResult().equals("true")) {
								if (recyleLabelListModel.getData() != null) {
									filterLabelModels = recyleLabelListModel
											.getData();
									for (int i = 0; i < filterLabelModels
											.size(); i++) {
										RecyleLabelModel selectModel = filterLabelModels
												.get(i);
										if (selectedLabelModels.size() > 0
												&& selectModel
														.getLabelCode()
														.equals(selectedLabelModels
																.get(0)
																.getLabelCode())) {
											selectModel.setSelect(true);
										}
									}
									chooseResourceListAdapter = new ChooseResourceListAdapter(
											ChooseResourceActivity.this,
											filterLabelModels, true);
									label_list
											.setAdapter(chooseResourceListAdapter);
								}
							} else {
								Tools.showToast(ChooseResourceActivity.this,
										recyleLabelListModel.getDescription());
							}
						}

					}
				});
		getLabelByCustomerListAsyncTask.execute();

	}
}
