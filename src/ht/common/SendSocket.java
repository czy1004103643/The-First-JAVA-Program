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

	//������Ϣ����
	public void send(SendMsg msg){
		try {
			//����Socket
			DatagramSocket socket = new DatagramSocket();
			//����һ���ֽ����������
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			//�Ѷ���������ֽ�������
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(msg);
			//��Ҫ���͵�����ת��Ϊ�ֽ�����
			byte b[]=bos.toByteArray();
			//��ȡ���ѵ�IP��ַ
			InetAddress addr = InetAddress.getByName(msg.friendAccount.getIp());
			//��ȡ���ѵĽ��ն˿�
			int port = msg.friendAccount.getPort();
			//���ɷ��ͱ�
			DatagramPacket pack = new DatagramPacket(b,0,b.length,addr,port);
			socket.send(pack);//����
			System.out.println(msg.cmd +"==������Ϣ");
			socket.close();
			oos.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	//�����û�״̬,����֪ͨ������֪ͨ������֪ͨ��æµ֪ͨ
	public void changeStatus(Vector<Account> allInfo,Account myInfo,int cmd){
		int size = allInfo.size();
		for(int i=0;i<size;i++){
			Account acc = allInfo.get(i);
			//��������������Լ���������
			if(acc.getQQNum() !=myInfo.getQQNum()){
				SendMsg msg = new SendMsg();
				msg.cmd = cmd;
				msg.myAccount = myInfo;
				msg.friendAccount = acc;
				//����
				send(msg);
			}
		}
	}

}
