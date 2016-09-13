package com.resmanager.client.model;

import java.io.Serializable;

public class ResultModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String result;
	private String description;

	/**
	 * @return result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 *            要设置的 result
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            要设置的 description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
