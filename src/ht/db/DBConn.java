package ht.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Connection;

public class DBConn {

	public static String driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//ʵ�����ƣ��˿�,���ݿ�����
	public static String url="jdbc:sqlserver://localhost\\SQL2005:1433;databasename=QQ2017";
//	public static String url="jdbc:sqlserver://localhost:1433;databasename=QQ2017";
	public static String username="sa";
	public static String password="sa";
	public static Connection conn = null;
	
	//��̬���飬����������������
	static{
		try{
			//1.��������
			Class.forName(driver);//��������
			//2.�������ݿ�
			if(conn==null || conn.isClosed()){
				conn=DriverManager.getConnection(url, username, password);
			}
			if(!conn.isClosed())
				System.out.println("���ݿ����ӳɹ�...");
			else
				System.out.println("���ݿ�����ʧ�ܣ���ȷ���û����������Ƿ�׼ȷ...");
		}catch (Exception e) {
			e.printStackTrace();//��ӡ������Ϣ
		}	
	}
	//�������ݿ�
	public static Connection opendb(){
		try{
			//1.��������
			Class.forName(driver);//��������
			//2.�������ݿ�
			if(conn==null || conn.isClosed()){
				conn = DriverManager.getConnection(url, username, password);
			}
			if(!conn.isClosed())
				System.out.println("���ݿ����ӳɹ�...");
			else
				System.out.println("���ݿ�����ʧ�ܣ���ȷ���û����������Ƿ�׼ȷ...");
		}catch (Exception e) {
			e.printStackTrace();//��ӡ������Ϣ
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
