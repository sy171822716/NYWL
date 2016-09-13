package com.resmanager.client.model;

import android.graphics.Bitmap;

public class PhotoModel {

	private String flagContent;

	/**
	 * @return the flagContent
	 */
	public String getFlagContent() {
		if (flagContent == null) {
			return "";
		}
		return flagContent;
	}

	/**
	 * @param flagContent
	 *            the flagContent to set
	 */
	public void setFlagContent(String flagContent) {
		this.flagContent = flagContent;
	}

	/**
	 * @return the bmp
	 */
	public Bitmap getBmp() {
		return bmp;
	}

	/**
	 * @param bmp
	 *            the bmp to set
	 */
	public void setBmp(Bitmap bmp) {
		this.bmp = bmp;
	}

	private Bitmap bmp;
}
