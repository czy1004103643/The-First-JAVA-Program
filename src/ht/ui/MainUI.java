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
		//ͨ�����캯�����յ�½�ɹ��ĸ�����Ϣ
		this.myAccount = myAccount;
		//��ʾͷ��
		setIconImage(new ImageIcon(myAccount.getFaceImage()).getImage());
		//�ڱ�������ʾQQ������ǳ�
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
		//��ǩ�ؼ�
		tab = new JTabbedPane();
		tab.add("����", friendList);
		tab.add("����", familyList);
		tab.add("ͬѧ", classMateList);
		tab.add("������", blackList);
		add(tab);
		btnFind = new JButton("��Ӻ���");
		add(btnFind,BorderLayout.SOUTH);
		
		//�����˵�
		mnuFriend = new JMenuItem("�Ƶ�����");
		mnuFamily = new JMenuItem("�Ƶ�����");
		mnuClassmat = new JMenuItem("�Ƶ�ͬѧ");
		mnuBlack = new JMenuItem("�Ƶ�������");
		mnuChat = new JMenuItem("��ʼ����");
		mnuLookInfo = new JMenuItem("�鿴��Ϣ");
		mnuDel = new JMenuItem("ɾ������");
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
		//�Դ������window�¼�
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
		//����������Ϣ���߳�
		new ReceiveThread(myAccount).start();//���������߳�
		//����֪ͨ
		new SendSocket().changeStatus(vallInfo, myAccount,Common.CMD_LOGIN);
	}
	public void refresh(){
		//��ȡ�����û���Ϣ
		vallInfo = base.findAllInfo(myAccount.getQQNum());
		vfamily.clear();
		vfriend.clear();
		vclassMate.clear();
		vblack.clear();
		for(int i=0;i<vallInfo.size();i++){
			Account a = vallInfo.get(i);
			if(a.getFriendType().equals(Common.TYPE_FRIEND)){//����
				vfriend.add(a);
			}
			if(a.getFriendType().equals(Common.TYPE_FAMILY)){//����
				vfamily.add(a);
			}
			if(a.getFriendType().equals(Common.TYPE_CLASSMATE)){//ͬѧ
				vclassMate.add(a);
			}
			if(a.getFriendType().equals(Common.TYPE_BLACK)){//������
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

	//�����ı�����
	class listmodel extends AbstractListModel { //�̳�AbstractListModel�࣬������ʾͼƬ
		Vector dats;
		public listmodel(Vector dats) {
			this.dats = dats;
		}
		// ��ȡ����
		public Object getElementAt(int index) {
			Account user = (Account) dats.get(index);
			return user.getNickName().trim() + "��" + user.getQQNum() + "��";
		}
		// ��ȡ����
		public int getSize() {
			return dats.size();
		}
	}
	// ��ȡ����ͷ��
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
				// ���б��к���״̬����ͷ��
				//System.out.println("״̬=" + user.״̬);
				String status = user.getStatus();
				if (status.equals(Common.STATUS_ONLINE)) {
					setIcon(new ImageIcon(user.getFaceImage()));
				} else if(status.equals(Common.STATUS_LEVEL)){
					String filename = user.getFaceImage(); //face/6.png
					//filename.substring(0, filename.indexOf('.'))�õ��ļ���1.png
					//substring���Ӵ�,��һ��������ʾ��ʼλ��
					//indexOf����ָ���ַ����ַ����е�λ��
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
			// ����������ɫ
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
			// setFont(new Font("������",Font.ITALIC+Font.BOLD,20));
			//
			// }
			// return c;
		}
	}
	//�������촰��
	public void popChat(){
		if(!getFriendInfo())//���ؼٱ�ʾû��ѡ���������
			return;
		//��hashtable�л�ȡ���촰��,����������򷵻�null
		ChatUI chat= ht_chat.get(friendAccount.getQQNum());
		if(chat==null){//��һ������
			chat=new ChatUI(myAccount,friendAccount);
			//�����촰�ڱ��浽hashtable��
			ht_chat.put(friendAccount.getQQNum(), chat);
		}
		System.out.println("my port="+myAccount.getPort()+";friend port="+friendAccount.getPort());
		chat.show();//��ʾ����
	}
	//�������Ҽ�ʱ��ȡ��������
	public boolean getFriendInfo(){
		int index = currList.getSelectedIndex();//��ȡѡ�еĺ�����Ϣ
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

	//����¼�
	class MouseHandle extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			//���ͷ���ǩ�޸ĸ�����Ϣ
			if(e.getSource()==lblFace){
				//MainUI.this����ǰ���ڵĶ���
				new UpdateInfo(myAccount,MainUI.this);
			}else if(e.getSource()==friendList){
				currList = friendList;
				if(e.getClickCount()==2){//˫��
					popChat();
				}else if(e.getButton()==3){//�Ҽ�
					pop.show(friendList, e.getX(), e.getY());
				}
			}else if(e.getSource()==familyList){
				currList = familyList;
				if(e.getClickCount()==2){//˫��
					popChat();
				}else if(e.getButton()==3){//�Ҽ�
					pop.show(familyList, e.getX(), e.getY());
				}
			}else if(e.getSource()==classMateList){
				currList = classMateList;
				if(e.getClickCount()==2){//˫��
					popChat();
				}else if(e.getButton()==3){//�Ҽ�
					pop.show(classMateList, e.getX(), e.getY());
				}
			}else if(e.getSource()==blackList){
				currList = blackList;
				if(e.getClickCount()==2){//˫��
					popChat();
				}else if(e.getButton()==3){//�Ҽ�
					pop.show(blackList, e.getX(), e.getY());
				}
			}
		}
	}
	//windows�¼�
	class WindowHandle implements WindowListener{

		//�����
		public void windowActivated(WindowEvent e) {
			System.out.println("windowActivated");
		}
		//�����ѹر�
		public void windowClosed(WindowEvent e) {
			System.out.println("windowClosed");
		}
		//�������ڹرգ�����ûû�йر�
		public void windowClosing(WindowEvent e) {
			System.out.println("windowClosing");
			//�����Լ���״̬Ϊ����״̬
			base.changeStatus(myAccount, Common.STATUS_LEVEL);
			//����֪ͨ
			new SendSocket().changeStatus(vallInfo, myAccount,Common.CMD_LEVEL);

		}

		//����δ����
		public void windowDeactivated(WindowEvent e) {
			System.out.println("windowDeactivated");
		}

		public void windowDeiconified(WindowEvent e) {
			System.out.println("windowDeiconified");
		}

		//����״̬��ͼ��
		public void windowIconified(WindowEvent e) {
			System.out.println("windowIconified");			
		}

		//�򿪴���
		public void windowOpened(WindowEvent e) {
			System.out.println("windowOpened");			
		}
		
	}
	//��Ϣ�����߳�
	class ReceiveThread extends Thread{
		Account myAccount;
		public ReceiveThread(Account myAccount) {
			this.myAccount = myAccount;
			System.out.println("�����߳�....");
		}
		@Override
		public void run() {
			//����Socket;
			try {
				byte b[] = new byte[1024*64];
				DatagramSocket socket = new DatagramSocket(myAccount.getPort());
				DatagramPacket p = new DatagramPacket(b,b.length);
				
				while(true){
					//������Ϣ
					System.out.println("׼�����յ���Ϣ��������");
					socket.receive(p);
					System.out.println("���յ���Ϣ��������");
					ByteArrayInputStream bis = new ByteArrayInputStream(p.getData());
					//�õ����������
					ObjectInputStream ois = new ObjectInputStream(bis);
					SendMsg msg = (SendMsg)ois.readObject();
					System.out.println("msg="+msg);
					switch(msg.cmd){
						case Common.CMD_SEND: //����������Ϣ
							//��hashtable�л�ȡ���촰��,����������򷵻�null
							ChatUI chat= ht_chat.get(msg.myAccount.getQQNum());
							if(chat==null){//��һ������
								chat=new ChatUI(msg.friendAccount,msg.myAccount);
								//�����촰�ڱ��浽hashtable��
								ht_chat.put(msg.myAccount.getQQNum(), chat);
							}
							chat.show();
							//�ѽ��յ�����Ϣ��ʾ�����촰�ڵĽ��տ�
							chat.appendView(msg.myAccount.getNickName(), msg.msg);
							break;
						case Common.CMD_LOGIN: //��½��Ϣ
						case Common.CMD_BUYS:
						case Common.CMD_LEVEL:
						case Common.CMD_HIDDEN:
							//����״̬��ͷ��ᷢ���仯���˿ڲ�һ��
							System.out.println("msg.cmd="+msg.cmd);
							refresh();
							break;
						case Common.CMD_SHAKE:
							chat= ht_chat.get(msg.myAccount.getQQNum());
							if(chat==null){//��һ������
								chat=new ChatUI(msg.friendAccount,msg.myAccount);
								//�����촰�ڱ��浽hashtable��
								ht_chat.put(msg.myAccount.getQQNum(), chat);
							}
							chat.show();
							chat.shake();//����
							break;
						case Common.CMD_ADDFRIEND:
							String message = msg.myAccount.getNickName()+"("+msg.myAccount.getQQNum()+")�����Ϊ���ѣ���ȷ����";
							if(JOptionPane.showConfirmDialog(null, message)==JOptionPane.OK_OPTION){
								//����ͬ�����Ϣ���Է�
								SendMsg sendmsg = new SendMsg();
								sendmsg.cmd = Common.CMD_AGREE;
								sendmsg.myAccount = msg.friendAccount;
								sendmsg.friendAccount = msg.myAccount;
								new SendSocket().send(sendmsg);//����ͬ�����Ϣ
								//��Ӻ��ѵ����ѱ�
								base.addFriend(msg.friendAccount.getQQNum(), msg.myAccount.getQQNum());
								refresh();//ˢ�½���
							}
							break;
						case Common.CMD_AGREE:
							//��Ӻ��ѵ����ѱ�
							base.addFriend(msg.friendAccount.getQQNum(), msg.myAccount.getQQNum());
							refresh();//ˢ�½���
							break;
						case Common.CMD_DELFRIEND:
							refresh();//ˢ�½���
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
			new SendSocket().send(msg);//����ɾ�����ѵ���Ϣ
		}
	}

}
