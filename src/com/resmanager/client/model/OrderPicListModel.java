package com.resmanager.client.model;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderPicListModel implements Serializable {

	/**
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<OrderPicModel> data;
	private String result;
	private String description;
	private String Original_Path_xhd;
	private String Thumb_Path_xhd;

	public ArrayList<OrderPicModel> getData() {
		return data;
	}

	public void setData(ArrayList<OrderPicModel> data) {
		this.data = data;
	}

	/**
	 * @return result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param result
	 *            要设置的 result
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @param description
	 *            要设置的 description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return original_Path_xhd
	 */
	public String getOriginal_Path_xhd() {
		return Original_Path_xhd;
	}

	/**
	 * @param original_Path_xhd
	 *            要设置的 original_Path_xhd
	 */
	public void setOriginal_Path_xhd(String original_Path_xhd) {
		Original_Path_xhd = original_Path_xhd;
	}

	public String getThumb_Path_xhd() {
		return Thumb_Path_xhd;
	}

	public void setThumb_Path_xhd(String thumb_Path_xhd) {
		Thumb_Path_xhd = thumb_Path_xhd;
	}
}
