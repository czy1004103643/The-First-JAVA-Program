package ht.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Connection;

public class DBConn {

	public static String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//实例名称：端口,数据库名称
	public static String url="jdbc:sqlserver://localhost\\SQL2005:1433;databasename=QQ2017";
//	public static String url="jdbc:sqlserver://localhost:1433;databasename=QQ2017";
	public static String username="sa";
	public static String password="sa";
	public static Connection conn = null;
	
	//静态语句块，程序启动立即运行
	static{
		try{
			//1.调入驱动
			Class.forName(driver);//调入驱动
			//2.连接数据库
			if(conn==null || conn.isClosed()){
				conn=DriverManager.getConnection(url, username, password);
			}
			if(!conn.isClosed())
				System.out.println("数据库连接成功...");
			else
				System.out.println("数据库连接失败，请确认用户名或密码是否准确...");
		}catch (Exception e) {
			e.printStackTrace();//打印错误信息
		}	
	}
	//连接数据库
	public static Connection opendb(){
		try{
			//1.调入驱动
			Class.forName(driver);//调入驱动
			//2.连接数据库
			if(conn==null || conn.isClosed()){
				conn = DriverManager.getConnection(url, username, password);
			}
			if(!conn.isClosed())
				System.out.println("数据库连接成功...");
			else
				System.out.println("数据库连接失败，请确认用户名或密码是否准确...");
		}catch (Exception e) {
			e.printStackTrace();//打印错误信息
		}	
		return conn;
	}
	public void closedb(){
		try {
			if(!conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		
	}
}
