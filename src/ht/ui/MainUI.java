package ht.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.Vector;

import ht.bean.Account;
import ht.common.Common;
import ht.common.SendMsg;
import ht.common.SendSocket;
import ht.db.BaseDAO;
import ht.db.IBase;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.DefaultListCellRenderer;
import javax.swing.text.BadLocationException;

public class MainUI extends JFrame implements ActionListener{

	JLabel lblFace;
	JTabbedPane tab;
	JButton btnFind;
	Account myAccount;
	JList friendList,familyList,classMateList,blackList;
	Vector<Account> vallInfo,vfriend,vfamily,vclassMate,vblack;
	JMenuItem mnuFriend,mnuFamily,mnuClassmat,mnuBlack,mnuChat,mnuLookInfo,mnuDel;
	JPopupMenu pop;
	IBase base = new BaseDAO();
	Hashtable<Integer, ChatUI> ht_chat = new Hashtable<Integer, ChatUI>();
	Account friendAccount=null;
	JList currList=null;
	public MainUI() {
	
	}
	public MainUI(Account myAccount) {
		//通过构造函数接收登陆成功的个人信息
		this.myAccount = myAccount;
		//显示头像
		setIconImage(new ImageIcon(myAccount.getFaceImage()).getImage());
		//在标题栏显示QQ号码和昵称
		String str = "("+myAccount.getQQNum()+")"+ myAccount.getNickName();
		setTitle(str);
		lblFace = new JLabel(str,new ImageIcon(myAccount.getFaceImage()),JLabel.LEFT);
		add(lblFace,BorderLayout.NORTH);
		friendList = new JList();
		familyList = new JList();
		classMateList = new JList();
		blackList = new JList();
		vfriend = new Vector<Account>();
		vfamily = new Vector<Account>();
		vclassMate = new Vector<Account>();
		vblack = new Vector<Account>();
		refresh();
		//标签控件
		tab = new JTabbedPane();
		tab.add("好友", friendList);
		tab.add("家人", familyList);
		tab.add("同学", classMateList);
		tab.add("黑名单", blackList);
		add(tab);
		btnFind = new JButton("添加好友");
		add(btnFind,BorderLayout.SOUTH);
		
		//创建菜单
		mnuFriend = new JMenuItem("移到好友");
		mnuFamily = new JMenuItem("移到家人");
		mnuClassmat = new JMenuItem("移到同学");
		mnuBlack = new JMenuItem("移到黑名单");
		mnuChat = new JMenuItem("开始聊天");
		mnuLookInfo = new JMenuItem("查看信息");
		mnuDel = new JMenuItem("删除好友");
		mnuFriend.addActionListener(this);
		mnuFamily.addActionListener(this);
		mnuClassmat.addActionListener(this);
		mnuBlack.addActionListener(this);
		mnuChat.addActionListener(this);
		mnuLookInfo.addActionListener(this);
		mnuDel.addActionListener(this);
		
		pop = new JPopupMenu();
		pop.add(mnuFriend);
		pop.add(mnuFamily);
		pop.add(mnuClassmat);
		pop.add(mnuBlack);
		pop.add(mnuChat);
		pop.add(mnuLookInfo);
		pop.add(mnuDel);
		
		
		
		MouseHandle mhandle = new MouseHandle();
		lblFace.addMouseListener(mhandle);
		friendList.addMouseListener(mhandle);
		familyList.addMouseListener(mhandle);
		classMateList.addMouseListener(mhandle);
		blackList.addMouseListener(mhandle);
		//对窗口添加window事件
		addWindowListener(new WindowHandle());
		btnFind.addActionListener(this);
		Toolkit kit = Toolkit.getDefaultToolkit();
		int width =kit.getScreenSize().width;
		int height =kit.getScreenSize().height;
		setResizable(false);
		setSize(300, 700);
		setLocation(width-300, 10);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//启动接收信息的线程
		new ReceiveThread(myAccount).start();//启动接收线程
		//上线通知
		new SendSocket().changeStatus(vallInfo, myAccount,Common.CMD_LOGIN);
	}
	public void refresh(){
		//获取所有用户信息
		vallInfo = base.findAllInfo(myAccount.getQQNum());
		vfamily.clear();
		vfriend.clear();
		vclassMate.clear();
		vblack.clear();
		for(int i=0;i<vallInfo.size();i++){
			Account a = vallInfo.get(i);
			if(a.getFriendType().equals(Common.TYPE_FRIEND)){//好友
				vfriend.add(a);
			}
			if(a.getFriendType().equals(Common.TYPE_FAMILY)){//家人
				vfamily.add(a);
			}
			if(a.getFriendType().equals(Common.TYPE_CLASSMATE)){//同学
				vclassMate.add(a);
			}
			if(a.getFriendType().equals(Common.TYPE_BLACK)){//黑名单
				vblack.add(a);
			}
		}
		friendList.setModel(new listmodel(vfriend));
		friendList.setCellRenderer(new myfind(vfriend));
		familyList.setModel(new listmodel(vfamily));
		familyList.setCellRenderer(new myfind(vfamily));
		classMateList.setModel(new listmodel(vclassMate));
		classMateList.setCellRenderer(new myfind(vclassMate));
		blackList.setModel(new listmodel(vblack));
		blackList.setCellRenderer(new myfind(vblack));

	}

	//设置文本内容
	class listmodel extends AbstractListModel { //继承AbstractListModel类，才能显示图片
		Vector dats;
		public listmodel(Vector dats) {
			this.dats = dats;
		}
		// 获取行数
		public Object getElementAt(int index) {
			Account user = (Account) dats.get(index);
			return user.getNickName().trim() + "【" + user.getQQNum() + "】";
		}
		// 获取长度
		public int getSize() {
			return dats.size();
		}
	}
	// 获取好友头像
	class myfind extends DefaultListCellRenderer {
		Vector datas;
		public myfind(Vector datas) {
			this.datas = datas;
		}
		
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Component c = super.getListCellRendererComponent(list, value,
					index, isSelected, cellHasFocus);
			if (index >= 0 && index < datas.size()) {
				Account user = (Account) datas.get(index);
				// 给列表中好友状态设置头像
				//System.out.println("状态=" + user.状态);
				String status = user.getStatus();
				if (status.equals(Common.STATUS_ONLINE)) {
					setIcon(new ImageIcon(user.getFaceImage()));
				} else if(status.equals(Common.STATUS_LEVEL)){
					String filename = user.getFaceImage(); //face/6.png
					//filename.substring(0, filename.indexOf('.'))得到文件名1.png
					//substring求子串,第一个参数表示开始位置
					//indexOf还回指定字符在字符串中的位置
					String file = filename.substring(0, filename.indexOf('.'))+ "_h.png";
					setIcon(new ImageIcon(file));
				}else if(status.equals(Common.STATUS_BUYS)){
					String filename = user.getFaceImage(); 
					String file = filename.substring(0, filename.indexOf('.'))+ "_w.png";
					setIcon(new ImageIcon(file));
					
				}else if(status.equals(Common.STATUS_HIDDEN)){
					String filename = user.getFaceImage(); 
					String file = filename.substring(0, filename.indexOf('.'))+ "_l.png";
					setIcon(new ImageIcon(file));
				}
				setText(user.getNickName().trim() + "(" + user.getQQNum() + ")");
			}
			// 设置字体颜色
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			return this;
			// if(isSelected==true){
			// setForeground(Color.yellow);
			// setFont(new Font("新宋体",Font.ITALIC+Font.BOLD,20));
			//
			// }
			// return c;
		}
	}
	//弹出聊天窗口
	public void popChat(){
		if(!getFriendInfo())//返回假表示没有选择好友资料
			return;
		//从hashtable中获取聊天窗口,如果不存在则返回null
		ChatUI chat= ht_chat.get(friendAccount.getQQNum());
		if(chat==null){//第一次聊天
			chat=new ChatUI(myAccount,friendAccount);
			//把聊天窗口保存到hashtable中
			ht_chat.put(friendAccount.getQQNum(), chat);
		}
		System.out.println("my port="+myAccount.getPort()+";friend port="+friendAccount.getPort());
		chat.show();//显示窗口
	}
	//点击鼠标右键时获取好友资料
	public boolean getFriendInfo(){
		int index = currList.getSelectedIndex();//获取选中的好友信息
		if(index<0)
			return false;
		
		if(currList==friendList){
			friendAccount = vfriend.get(index);
		}else if(currList==familyList){
			friendAccount = vfamily.get(index);
		}else if(currList==classMateList){
			friendAccount = vclassMate.get(index);
		}else if(currList==blackList){
			friendAccount = vblack.get(index);
		}
		return true;
	}

	//鼠标事件
	class MouseHandle extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			//点击头像标签修改个人信息
			if(e.getSource()==lblFace){
				//MainUI.this代表当前窗口的对象
				new UpdateInfo(myAccount,MainUI.this);
			}else if(e.getSource()==friendList){
				currList = friendList;
				if(e.getClickCount()==2){//双击
					popChat();
				}else if(e.getButton()==3){//右键
					pop.show(friendList, e.getX(), e.getY());
				}
			}else if(e.getSource()==familyList){
				currList = familyList;
				if(e.getClickCount()==2){//双击
					popChat();
				}else if(e.getButton()==3){//右键
					pop.show(familyList, e.getX(), e.getY());
				}
			}else if(e.getSource()==classMateList){
				currList = classMateList;
				if(e.getClickCount()==2){//双击
					popChat();
				}else if(e.getButton()==3){//右键
					pop.show(classMateList, e.getX(), e.getY());
				}
			}else if(e.getSource()==blackList){
				currList = blackList;
				if(e.getClickCount()==2){//双击
					popChat();
				}else if(e.getButton()==3){//右键
					pop.show(blackList, e.getX(), e.getY());
				}
			}
		}
	}
	//windows事件
	class WindowHandle implements WindowListener{

		//激活窗口
		public void windowActivated(WindowEvent e) {
			System.out.println("windowActivated");
		}
		//窗口已关闭
		public void windowClosed(WindowEvent e) {
			System.out.println("windowClosed");
		}
		//窗口正在关闭，但是没没有关闭
		public void windowClosing(WindowEvent e) {
			System.out.println("windowClosing");
			//更改自己的状态为离线状态
			base.changeStatus(myAccount, Common.STATUS_LEVEL);
			//下线通知
			new SendSocket().changeStatus(vallInfo, myAccount,Common.CMD_LEVEL);

		}

		//窗口未激活
		public void windowDeactivated(WindowEvent e) {
			System.out.println("windowDeactivated");
		}

		public void windowDeiconified(WindowEvent e) {
			System.out.println("windowDeiconified");
		}

		//设置状态条图标
		public void windowIconified(WindowEvent e) {
			System.out.println("windowIconified");			
		}

		//打开窗口
		public void windowOpened(WindowEvent e) {
			System.out.println("windowOpened");			
		}
		
	}
	//信息接收线程
	class ReceiveThread extends Thread{
		Account myAccount;
		public ReceiveThread(Account myAccount) {
			this.myAccount = myAccount;
			System.out.println("启动线程....");
		}
		@Override
		public void run() {
			//定义Socket;
			try {
				byte b[] = new byte[1024*64];
				DatagramSocket socket = new DatagramSocket(myAccount.getPort());
				DatagramPacket p = new DatagramPacket(b,b.length);
				
				while(true){
					//接收消息
					System.out.println("准备接收到消息。。。。");
					socket.receive(p);
					System.out.println("接收到消息。。。。");
					ByteArrayInputStream bis = new ByteArrayInputStream(p.getData());
					//得到对象输出流
					ObjectInputStream ois = new ObjectInputStream(bis);
					SendMsg msg = (SendMsg)ois.readObject();
					System.out.println("msg="+msg);
					switch(msg.cmd){
						case Common.CMD_SEND: //接收聊天信息
							//从hashtable中获取聊天窗口,如果不存在则返回null
							ChatUI chat= ht_chat.get(msg.myAccount.getQQNum());
							if(chat==null){//第一次聊天
								chat=new ChatUI(msg.friendAccount,msg.myAccount);
								//把聊天窗口保存到hashtable中
								ht_chat.put(msg.myAccount.getQQNum(), chat);
							}
							chat.show();
							//把接收到的信息显示到聊天窗口的接收框
							chat.appendView(msg.myAccount.getNickName(), msg.msg);
							break;
						case Common.CMD_LOGIN: //登陆信息
						case Common.CMD_BUYS:
						case Common.CMD_LEVEL:
						case Common.CMD_HIDDEN:
							//更改状态，头像会发生变化，端口不一样
							System.out.println("msg.cmd="+msg.cmd);
							refresh();
							break;
						case Common.CMD_SHAKE:
							chat= ht_chat.get(msg.myAccount.getQQNum());
							if(chat==null){//第一次聊天
								chat=new ChatUI(msg.friendAccount,msg.myAccount);
								//把聊天窗口保存到hashtable中
								ht_chat.put(msg.myAccount.getQQNum(), chat);
							}
							chat.show();
							chat.shake();//抖动
							break;
						case Common.CMD_ADDFRIEND:
							String message = msg.myAccount.getNickName()+"("+msg.myAccount.getQQNum()+")添加您为好友，请确定。";
							if(JOptionPane.showConfirmDialog(null, message)==JOptionPane.OK_OPTION){
								//发送同意的信息给对方
								SendMsg sendmsg = new SendMsg();
								sendmsg.cmd = Common.CMD_AGREE;
								sendmsg.myAccount = msg.friendAccount;
								sendmsg.friendAccount = msg.myAccount;
								new SendSocket().send(sendmsg);//发送同意的信息
								//添加好友到朋友表
								base.addFriend(msg.friendAccount.getQQNum(), msg.myAccount.getQQNum());
								refresh();//刷新界面
							}
							break;
						case Common.CMD_AGREE:
							//添加好友到朋友表
							base.addFriend(msg.friendAccount.getQQNum(), msg.myAccount.getQQNum());
							refresh();//刷新界面
							break;
						case Common.CMD_DELFRIEND:
							refresh();//刷新界面
							break;
					}
				}
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			
		}
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnFind){
			new FindUI(myAccount);
		}else if(e.getSource()==mnuFriend){
			if(!getFriendInfo())
				return;
			base.changeType(myAccount.getQQNum(), friendAccount.getQQNum(), Common.TYPE_FRIEND);
			refresh();
		}else if(e.getSource()==mnuFamily){
			if(!getFriendInfo())
				return;
			base.changeType(myAccount.getQQNum(), friendAccount.getQQNum(), Common.TYPE_FAMILY);
			refresh();
		}else if(e.getSource()==mnuClassmat){
			if(!getFriendInfo())
				return;
			base.changeType(myAccount.getQQNum(), friendAccount.getQQNum(), Common.TYPE_CLASSMATE);
			refresh();
		}else if(e.getSource()==mnuBlack){
			if(!getFriendInfo())
				return;
			base.changeType(myAccount.getQQNum(), friendAccount.getQQNum(), Common.TYPE_BLACK);
			refresh();
		}else if(e.getSource()==mnuChat){
			if(!getFriendInfo())
				return;
			popChat();
		}else if(e.getSource()==mnuLookInfo){
			if(!getFriendInfo())
				return;
		}else if(e.getSource()==mnuDel){
			if(!getFriendInfo())
				return;
			base.delFriend(myAccount.getQQNum(), friendAccount.getQQNum());
			refresh();
			SendMsg msg = new SendMsg();
			msg.cmd = Common.CMD_DELFRIEND;
			msg.myAccount = myAccount;
			msg.friendAccount = friendAccount;
			new SendSocket().send(msg);//发送删除好友的信息
		}
	}

}
