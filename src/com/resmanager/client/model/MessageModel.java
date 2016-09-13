package com.resmanager.client.model;

import java.io.Serializable;

public class MessageModel implements Serializable {

	/** 
	*/
	private static final long serialVersionUID = 1L;
	private String InstationTitle;
	private String Contents;
	private String SenderTime;
	private String Unread;
	private String ReceiverId;

	/**
	 * @return instationTitle
	 */
	public String getInstationTitle() {
		return InstationTitle;
	}

	/**
	 * @param instationTitle
	 *            要设置的 instationTitle
	 */
	public void setInstationTitle(String instationTitle) {
		InstationTitle = instationTitle;
	}

	/**
	 * @return contents
	 */
	public String getContents() {
		return Contents;
	}

	/**
	 * @param contents
	 *            要设置的 contents
	 */
	public void setContents(String contents) {
		Contents = contents;
	}

	/**
	 * @return senderTime
	 */
	public String getSenderTime() {
		return SenderTime;
	}

	/**
	 * @param senderTime
	 *            要设置的 senderTime
	 */
	public void setSenderTime(String senderTime) {
		SenderTime = senderTime;
	}

	/**
	 * @return unread
	 */
	public String getUnread() {
		return Unread;
	}

	/**
	 * @param unread
	 *            要设置的 unread
	 */
	public void setUnread(String unread) {
		Unread = unread;
	}

	/**
	 * @return receiverId
	 */
	public String getReceiverId() {
		return ReceiverId;
	}

	/**
	 * @param receiverId
	 *            要设置的 receiverId
	 */
	public void setReceiverId(String receiverId) {
		ReceiverId = receiverId;
	}

}
