package ht.bean;

import java.io.Serializable;

public class Account implements Serializable{
	private int 	qQNum;//QQ��
	private String 	trueName;//��ʵ����
	private String 	nickName;//�ǳ�
	private String 	password;//����
	private String 	sex;//�Ա�
	private int 	age;//����
	private String 	nation;//--����
	private String 	faceImage;//--ͷ��
	private String 	address;//--��ַ
	private String 	email;//����
	private String 	ip;//��ַ
	private int 	port;//�˿ں�
	private String 	status;//״̬(����,����,����,æµ)
	private String friendType;
	public String getFriendType() {
		return friendType;
	}
	public void setFriendType(String friendType) {
		this.friendType = friendType;
	}
	public int getQQNum() {
		return qQNum;
	}
	public void setQQNum(int num) {
		qQNum=num;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getFaceImage() {
		return faceImage;
	}
	public void setFaceImage(String faceImage) {
		this.faceImage = faceImage;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
}
