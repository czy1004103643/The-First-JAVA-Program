package ht.common;

import ht.bean.Account;

import java.io.Serializable;

import javax.swing.text.StyledDocument;
//��Ϣ������
public class SendMsg implements Serializable{

	public int cmd;//������
	public Account myAccount;
	public Account friendAccount;
	public StyledDocument msg;
}
