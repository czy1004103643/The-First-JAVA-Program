package ht.common;

import ht.bean.Account;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Vector;

public class SendSocket {

	//发送消息函数
	public void send(SendMsg msg){
		try {
			//定义Socket
			DatagramSocket socket = new DatagramSocket();
			//创建一个字节数组输出流
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			//把对象输出到字节数组中
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(msg);
			//把要发送的数据转换为字节数组
			byte b[]=bos.toByteArray();
			//获取好友的IP地址
			InetAddress addr = InetAddress.getByName(msg.friendAccount.getIp());
			//获取好友的接收端口
			int port = msg.friendAccount.getPort();
			//生成发送报
			DatagramPacket pack = new DatagramPacket(b,0,b.length,addr,port);
			socket.send(pack);//发送
			System.out.println(msg.cmd +"==发送消息");
			socket.close();
			oos.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	//更改用户状态,上线通知，离线通知，隐身通知，忙碌通知
	public void changeStatus(Vector<Account> allInfo,Account myInfo,int cmd){
		int size = allInfo.size();
		for(int i=0;i<size;i++){
			Account acc = allInfo.get(i);
			//如果好友资料是自己，不发送
			if(acc.getQQNum() !=myInfo.getQQNum()){
				SendMsg msg = new SendMsg();
				msg.cmd = cmd;
				msg.myAccount = myInfo;
				msg.friendAccount = acc;
				//发送
				send(msg);
			}
		}
	}

}
