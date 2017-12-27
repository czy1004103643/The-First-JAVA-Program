package ht.db;

import ht.bean.Account;
import ht.common.Common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.Vector;

public class BaseDAO implements IBase{
	//判断QQ号码是否存在
	public boolean isExistsqqcode(int qqcode){
		boolean exists=false;
		try{
			//获取数据库连接
			Connection conn = DBConn.opendb();
			Statement stmt = conn.createStatement();
			String sql = "select qqnum from account where qqnum="+qqcode;
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				exists=true;//QQ号码已经存在
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return exists;
	}
	//注册QQ用户
	public int addAccount(Account acc){
		
		int bRet = 0;
		try{
			//获取数据库连接
			Connection conn = DBConn.opendb();
			Statement stmt = conn.createStatement();
			String sql="insert into Account values('";
			sql += acc.getQQNum() +"','";
			sql += acc.getTrueName() +"','";
			sql += acc.getNickName() +"','";
			sql += acc.getPassword() +"','";
			sql += acc.getSex() +"','";
			sql += acc.getAge() +"','";
			sql += acc.getNation() +"','";
			sql += acc.getFaceImage() +"','";
			sql += acc.getAddress() +"','";
			sql += acc.getEmail() +"','";
			sql += acc.getIp() +"','";
			sql += acc.getPort() +"','";
			sql += acc.getStatus() +"')";
			System.out.println(sql);
			bRet=stmt.executeUpdate(sql);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return bRet;
	}
	public Account login(Account acc){
		
		try{
			//获取数据库连接
			Connection conn = DBConn.opendb();
			Statement stmt = conn.createStatement();
			String sql = "select * from account where qqnum="+acc.getQQNum() + " and password='"+acc.getPassword() +"'";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				
				acc.setTrueName(rs.getString("trueName").trim());
				acc.setNickName(rs.getString("nickName").trim());
				acc.setSex(rs.getString("sex").trim());
				acc.setAge(rs.getInt("age"));
				acc.setNation(rs.getString("nation").trim());
				acc.setFaceImage(rs.getString("faceImage").trim());
				acc.setAddress(rs.getString("address").trim());
				acc.setEmail(rs.getString("email").trim());
				acc.setIp(rs.getString("ip").trim());
				int port = getPort(acc.getQQNum());
				System.out.println("port="+port);
				acc.setPort(port); 
				acc.setStatus(Common.STATUS_ONLINE);
			}else{
				acc=null;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return acc;
	}
	//判断端口是否存在
	public boolean isExistsPort(int qqcode,int port){
		boolean exists=false;
		try{
			//获取数据库连接
			Connection conn = DBConn.opendb();
			Statement stmt = conn.createStatement();
			String sql = "select qqnum from account where status='"+Common.STATUS_ONLINE +"' and port="+port;
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				exists=true;//端口已经存在
				return exists;
			}
			//更新端口到数据库,同时更改在线状态
			sql = "update account set port="+port +",status='"+ Common.STATUS_ONLINE+"' where qqnum="+qqcode;
			stmt.executeUpdate(sql);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return exists;
	} 
	//生成端口
	public int getPort(int qqcode){
		Random ran = new Random();
		int port = ran.nextInt(50000)+8000;
		boolean exist = isExistsPort(qqcode,port);
		//循环判断，如果QQ号码已经存在，重新生成新的号码，再查询，直到号码不存在为止
		while(exist){
			port = ran.nextInt(50000)+8000;
			exist = isExistsPort(qqcode,port);
		}
		return port;
	}
	//从数据库查找所有用户信息
	public Vector<Account> findAllInfo(int qqcode){
		Vector<Account> allInfo = new Vector<Account>();
		try{
			//获取数据库连接
			Connection conn = DBConn.opendb();
			Statement stmt = conn.createStatement();
			String sql = "select a.*,type from account a inner join friend f on a.qqnum=f.friendqqnum where myqqnum="+qqcode;
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				Account acc = new Account();
				acc.setQQNum(rs.getInt("qqnum"));
				acc.setTrueName(rs.getString("trueName").trim());
				acc.setNickName(rs.getString("nickName").trim());
				acc.setSex(rs.getString("sex").trim());
				acc.setAge(rs.getInt("age"));
				acc.setNation(rs.getString("nation").trim());
				acc.setFaceImage(rs.getString("faceImage").trim());
				acc.setAddress(rs.getString("address").trim());
				acc.setEmail(rs.getString("email").trim());
				acc.setIp(rs.getString("ip").trim());
				acc.setPort(rs.getInt("port")); 
				acc.setStatus(rs.getString("status"));
				acc.setFriendType(rs.getString("type"));
				allInfo.add(acc);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return allInfo;
	}
	//从数据库查找符合条件的用户信息
	public Vector findAllInfo(Account acc){
		Vector rowsData = new Vector();
		try{
			String sql = "select * from account where 1=1";
			if(acc != null){
				if(!acc.getSex().equals("不选")){
					sql += " and sex = '"+acc.getSex()+"' ";
				}
				if(!acc.getStatus().equals("不选")){
					sql += " and status = '"+acc.getStatus()+"' ";
				}
				if(!acc.getNation().equals("不选")){
					sql += " and nation = '"+acc.getNation()+"' ";
				}
				if(acc.getNickName() !=null && !acc.getNickName().equals("")){
					sql += " and nickname like '%"+acc.getNickName() +"%' ";
				}
				if(acc.getQQNum()!=0){
					sql += " and qqnum="+acc.getQQNum();
				}
			}
			System.out.println(sql);			//获取数据库连接
			Connection conn = DBConn.opendb();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				//产生一条记录
				Vector row = new Vector();
				row.addElement(rs.getInt("qqnum"));
				row.addElement(rs.getString("trueName").trim());
				row.addElement(rs.getString("nickName").trim());
				row.addElement(rs.getString("sex").trim());
				row.addElement(rs.getInt("age"));
				row.addElement(rs.getString("nation").trim());
				row.addElement(rs.getString("faceImage").trim());
				row.addElement(rs.getString("ip").trim());
				row.addElement(rs.getInt("port"));
				row.addElement(rs.getString("status"));
				rowsData.addElement(row);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return rowsData;
	}
	//修改用户资料
	public boolean updateInfo(Account acc){
		
		boolean bRet = false;
		try{
			//获取数据库连接
			Connection conn = DBConn.opendb();
			Statement stmt = conn.createStatement();
			String sql="update Account set truename='";
			sql += acc.getTrueName() +"',nickname='";
			sql += acc.getNickName() +"',sex='";
			sql += acc.getSex() +"',age='";
			sql += acc.getAge() +"',nation='";
			sql += acc.getNation() +"',faceimage='";
			sql += acc.getFaceImage() +"',address='";
			sql += acc.getAddress() +"',email='";
			sql += acc.getEmail() +"',ip='";
			sql += acc.getIp() +"' where qqnum=";
			sql += acc.getQQNum();
			System.out.println(sql);
			if(stmt.executeUpdate(sql)>0)
				bRet=true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return bRet;
	}
	//更改状态
	public boolean changeStatus(Account acc,String status){
		boolean bRet = false;
		try{
			//获取数据库连接
			Connection conn = DBConn.opendb();
			Statement stmt = conn.createStatement();
			String sql="update Account set status='";
			sql += status +"' where qqnum=";
			sql += acc.getQQNum();
			System.out.println(sql);
			if(stmt.executeUpdate(sql)>0)
				bRet=true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return bRet;
	}
	//添加好友
	public boolean addFriend(int myQqcode,int friendQqcode){
		boolean bRet = false;
		try{
			//获取数据库连接
			Connection conn = DBConn.opendb();
			Statement stmt = conn.createStatement();
			String sql="insert friend values(";
			sql += myQqcode +",";
			sql += friendQqcode +",'";
			sql += Common.TYPE_FRIEND+"')";
			
			System.out.println(sql);
			//添加对方为好友
			if(stmt.executeUpdate(sql)>0)
				bRet=true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return bRet;
	}
	
	public boolean isFriend(int myQqcode,int friendQqcode){
		boolean bRet = false;
		try{
			//获取数据库连接
			Connection conn = DBConn.opendb();
			Statement stmt = conn.createStatement();
			String sql="select * from  friend where myqqnum=";
			sql += myQqcode +" and friendqqnum="+friendQqcode;
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				bRet=true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return bRet;
	}
	
	public boolean changeType(int myQqcode,int friendQqcode,String friendType){
		boolean bRet = false;
		try{
			//获取数据库连接
			Connection conn = DBConn.opendb();
			Statement stmt = conn.createStatement();
			String sql="update friend set type='";
			sql += friendType +"' where myqqnum=";
			sql += myQqcode +" and friendqqnum=";
			sql += friendQqcode;
			System.out.println(sql);
			//添加对方为好友
			if(stmt.executeUpdate(sql)>0)
				bRet=true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return bRet;
	}
	
	//删除好友
	public boolean delFriend(int myQqcode,int friendQqcode){
		boolean bRet = false;
		try{
			//获取数据库连接
			Connection conn = DBConn.opendb();
			Statement stmt = conn.createStatement();
			String sql="delete friend where myqqnum=";
			sql += myQqcode +" and friendqqnum=";
			sql += friendQqcode;
			System.out.println(sql);
			stmt.executeUpdate(sql);
			sql="delete friend where myqqnum=";
			sql += friendQqcode +" and friendqqnum=";
			sql += myQqcode;
			System.out.println(sql);
			//删除好友
			if(stmt.executeUpdate(sql)>0)
				bRet=true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return bRet;
	}
}
