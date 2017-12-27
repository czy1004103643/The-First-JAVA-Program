package ht.common;

import ht.bean.Account;

import java.io.Serializable;

import javax.swing.text.StyledDocument;
//消息发送类
public class SendMsg implements Serializable{

	public int cmd;//命令字
	public Account myAccount;
	public Account friendAccount;
	public StyledDocument msg;
}
