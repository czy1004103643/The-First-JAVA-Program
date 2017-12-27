package ht.bean;

import java.io.Serializable;

public class Friend implements Serializable{

	private int id;
	private int myQQNum;
	private int friendQQNum;
	private String type;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
