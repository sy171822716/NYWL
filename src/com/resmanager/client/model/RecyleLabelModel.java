/**   
 * @Title: RecyleLabelModel.java 
 * @Package com.resmanager.client.model 
 * @Description:  需回收标签实体 
 * @author ShenYang  
 * @date 2016-1-7 下午12:40:43 
 * @version V1.0   
 */
package com.resmanager.client.model;

import java.io.Serializable;

/**
 * @ClassName: RecyleLabelModel
 * @Description: 需回收标签实体
 * @author ShenYang
 * @date 2016-1-7 下午12:40:43
 * 
 */
public class RecyleLabelModel implements Serializable {

	/** 
	*/
	private static final long serialVersionUID = 1L;
	private String LabelCode;
	private String Packagetype;
	private boolean isSelect = false;

	/**
	 * @return labelCode
	 */
	public String getLabelCode() {
		return LabelCode;
	}

	/**
	 * @param labelCode
	 *            要设置的 labelCode
	 */
	public void setLabelCode(String labelCode) {
		LabelCode = labelCode;
	}

	/**
	 * @return packagetype
	 */
	public String getPackagetype() {
		return Packagetype;
	}

	/**
	 * @param packagetype
	 *            要设置的 packagetype
	 */
	public void setPackagetype(String packagetype) {
		Packagetype = packagetype;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

}
