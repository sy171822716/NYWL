/**   
 * @Title: ScanBimpModel.java 
 * @Package com.resmanager.client.model 
 * @Description:标签桶扫描发货回收实体类
 * @author ShenYang   
 * @date 2015-12-22 下午3:03:30 
 * @version V1.0   
 */
package com.resmanager.client.model;

import java.io.Serializable;

import android.graphics.Bitmap;

/**
 * @ClassName: ScanBimpModel
 * @Description: 标签桶扫描发货回收实体类
 * @author ShenYang
 * @date 2015-12-22 下午3:03:30
 * 
 */
public class ScanBimpModel implements Serializable {
	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private String labelCode = "";// 二维码标签内容
	private Bitmap bmp;// 桶图片
	private String bmpPath = "";// 图片地址
	private String thumbPath = "";// 缩略图地址
	private String packageType = "";
	private int isRecyle;// 是否回收 0:不回收，1：回收
	private String resourceTypeId = "";// 产品类型ID
	private String resourceTypeName = "";// 桶类型名
	private String remark = "";// 备注，在回收的时候需要
	private String recPID = "";// 图片编号，回收使用

	public String getLabelCode() {
		return labelCode;
	}

	public void setLabelCode(String labelCode) {
		this.labelCode = labelCode;
	}

	public Bitmap getBmp() {
		return bmp;
	}

	public void setBmp(Bitmap bmp) {
		this.bmp = bmp;
	}

	public int isRecyle() {
		return isRecyle;
	}

	public void setRecyle(int isRecyle) {
		this.isRecyle = isRecyle;
	}

	public String getResourceTypeId() {
		return resourceTypeId;
	}

	public void setResourceTypeId(String resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public String getResourceTypeName() {
		return resourceTypeName;
	}

	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBmpPath() {
		return bmpPath;
	}

	public void setBmpPath(String bmpPath) {
		this.bmpPath = bmpPath;
	}

	public String getPackageType() {
		return packageType;
	}

	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}

	public String getRecPID() {
		return recPID;
	}

	public void setRecPID(String recPID) {
		this.recPID = recPID;
	}

	public String getThumbPath() {
		return thumbPath;
	}

	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}
}
