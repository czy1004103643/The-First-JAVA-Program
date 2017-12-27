package ht.ui;

import ht.bean.Account;
import ht.common.Common;
import ht.db.BaseDAO;
import ht.db.IBase;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class LoginUI extends JFrame implements MouseListener,ActionListener{

	JLabel lblbg,lblQQnum,lblpwd,lblFace;//���� �˺� ���� ͷ��ͼƬ
	JComboBox cbQQcode,cbState;//�˺������   �˺�״̬
	JPasswordField txtpwd;//���������
	JButton btnLogin,btnCancel;//��½���ر�
	JLabel lblReg,lblForgetpwd;//ע�� �һ�����
	Account myInfo;
	IBase base;
	Hashtable<Integer, Account> myAccount = new Hashtable<Integer, Account>();
	public LoginUI() {
		
		super("QQ2017");
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image image =kit.createImage("face/tubiao.png");
		setIconImage(image);

		lblbg = new JLabel(new ImageIcon("face/timg.jpg"));
		add(lblbg);
		
		lblQQnum = new JLabel("�˺�:",JLabel.RIGHT);
		lblpwd = new JLabel("����:",JLabel.RIGHT);
		cbQQcode = new JComboBox();
		cbQQcode.setEditable(true);//���������
		txtpwd = new JPasswordField("1234");
		lblFace = new JLabel(new ImageIcon("face/tubiao.png"));
		btnLogin = new JButton("��½(L)");
		btnLogin.setMnemonic('L');
		btnCancel = new JButton("�ر�(X)");
		btnCancel.setMnemonic('X');
		lblReg = new JLabel("ע        ��",JLabel.RIGHT);
		lblForgetpwd = new JLabel("�һ�����",JLabel.RIGHT);
		cbState=new JComboBox(Common.QQSTATUS);
		
		lblQQnum.setBounds(130, 80, 100,30 );
		lblpwd.setBounds(130,160 ,100 , 30);
		cbQQcode.setBounds(250, 80, 180, 30);
		txtpwd.setBounds(250, 160, 180,30 );
		btnLogin.setBounds(220,250 ,100 ,30 );
		btnCancel.setBounds(400,250 ,100 ,30 );
		lblReg.setBounds(450, 80,60 ,30 );
		lblForgetpwd.setBounds(450, 160,60 ,30);
		lblFace.setBounds(100,100 , 60,60 );
		cbState.setBounds(200,200 ,60 ,20 );
		
		
		lblbg.add(lblQQnum);
		lblbg.add(cbQQcode);
		lblbg.add(lblpwd);
		lblbg.add(txtpwd);
		lblbg.add(btnLogin);
		lblbg.add(btnCancel);
		lblbg.add(lblReg);
		lblbg.add(lblForgetpwd);
		lblbg.add(lblFace);
		lblbg.add(cbState);
		
		lblReg.addMouseListener(this);
		lblForgetpwd.addMouseListener(this);
		
		btnLogin.addActionListener(this);
		btnCancel.addActionListener(this);
		
		
		setResizable(false);
		setSize(640, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		readAccount();//��ȡ�ļ�
		base = new BaseDAO();
	}
	public static void main(String[] args) {
		new LoginUI();
	}
	public void mouseClicked(MouseEvent e) {
		
		if(e.getSource()==lblReg){
			new Reg();
		}
		if(e.getSource()==lblForgetpwd){
			JOptionPane.showMessageDialog(null, "����������");
		}
		
	}
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void actionPerformed(ActionEvent e) {

		if(e.getSource()==btnLogin){
			String qqcode = cbQQcode.getSelectedItem().toString().trim();
			String pwd = txtpwd.getText().trim();
			if(qqcode==null || qqcode.equals("")){
				JOptionPane.showMessageDialog(null, "������QQ����");
				return;
			}
			try{
				Integer.parseInt(qqcode);
			}catch(Exception ex){
				JOptionPane.showMessageDialog(null, "QQ���뺬�зǷ��ַ���");
				return;
			}
			if(pwd==null || pwd.equals("")){
				JOptionPane.showMessageDialog(null, "������QQ����");
				return;
			}
			
			myInfo=new Account();
			myInfo.setQQNum(Integer.parseInt(qqcode));
			myInfo.setPassword(pwd);
			myInfo=base.login(myInfo);//��½
			if(myInfo==null){
				JOptionPane.showMessageDialog(null, "��½ʧ�ܣ�����QQ������������Ƿ�׼ȷ.");
				return;
			}
			saveAccount(myInfo);//����QQ���뵽�ļ�
//			JOptionPane.showMessageDialog(null, "��½�ɹ�");
			dispose();//�رյ�½����
			//��������
			new MainUI(myInfo);
		}else if(e.getSource()==btnCancel){
			System.exit(0);//�˳�����
		}
	}
	//����QQ���뵽�ļ�
	public void saveAccount(Account acc){
		myAccount.put(acc.getQQNum(), acc);
		File file = new File("accounted.dat");
		try{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(myAccount);
			oos.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	//���ļ���ȡ������½����QQ����
	public void readAccount(){
		File file = new File("accounted.dat");
		//��һ��ʹ��QQʱ�ļ������ڣ�ֱ���˳�
		if(!file.exists())
			return;
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			myAccount=(Hashtable<Integer, Account>)ois.readObject();
			ois.close();
			//��ȡ���м�
			Set<Integer> set = myAccount.keySet();
			Iterator<Integer> it = set.iterator();
			while(it.hasNext()){
				cbQQcode.addItem(it.next());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
}
