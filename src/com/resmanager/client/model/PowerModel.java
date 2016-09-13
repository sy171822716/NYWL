package com.resmanager.client.model;

public class PowerModel {
	private int powerID;
	private int powerName;
	private int powerImg;
	private boolean isShowNum;
	private int num;
	public int getPowerID() {
		return powerID;
	}

	public void setPowerID(int powerID) {
		this.powerID = powerID;
	}

	public int getPowerName() {
		return powerName;
	}

	public void setPowerName(int powerName) {
		this.powerName = powerName;
	}

	public int getPowerImg() {
		return powerImg;
	}

	public void setPowerImg(int powerImg) {
		this.powerImg = powerImg;
	}

	public boolean isShowNum() {
		return isShowNum;
	}

	public void setShowNum(boolean isShowNum) {
		this.isShowNum = isShowNum;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
