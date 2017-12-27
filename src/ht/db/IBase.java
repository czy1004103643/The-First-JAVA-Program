package ht.db;

import java.util.Vector;

import ht.bean.Account;
import ht.common.SendMsg;

public interface IBase {

	public boolean isExistsqqcode(int qqcode);
	public boolean isExistsPort(int qqcode,int port);
	public int addAccount(Account acc);
	public Account login(Account acc);
	public Vector<Account> findAllInfo(int qqcode);
	public Vector findAllInfo(Account acc);
	public boolean updateInfo(Account acc);
	public boolean changeStatus(Account acc,String status);
	public boolean addFriend(int myQqcode,int friendQqcode);
	public boolean isFriend(int myQqcode,int friendQqcode);
	
	public boolean changeType(int myQqcode,int friendQqcode,String friendType);
	public boolean delFriend(int myQqcode,int friendQqcode);
	
	
}
