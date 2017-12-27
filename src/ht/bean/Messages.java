package ht.bean;

import java.io.Serializable;

public class Messages implements Serializable{

	private int id;
	private int myQQNum;
	private int friendQQNum;
	private String msg;
	private int isRead;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMyQQNum() {
		return myQQNum;
	}
	public void setMyQQNum(int myQQNum) {
		this.myQQNum = myQQNum;
	}
	public int getFriendQQNum() {
		return friendQQNum;
	}
	public void setFriendQQNum(int friendQQNum) {
		this.friendQQNum = friendQQNum;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getIsRead() {
		return isRead;
	}
	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
}
