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

	JLabel lblbg,lblQQnum,lblpwd,lblFace;//背景 账号 密码 头像图片
	JComboBox cbQQcode,cbState;//账号输入框   账号状态
	JPasswordField txtpwd;//密码输入框
	JButton btnLogin,btnCancel;//登陆，关闭
	JLabel lblReg,lblForgetpwd;//注册 找回密码
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
		
		lblQQnum = new JLabel("账号:",JLabel.RIGHT);
		lblpwd = new JLabel("密码:",JLabel.RIGHT);
		cbQQcode = new JComboBox();
		cbQQcode.setEditable(true);//下拉输入框
		txtpwd = new JPasswordField("1234");
		lblFace = new JLabel(new ImageIcon("face/tubiao.png"));
		btnLogin = new JButton("登陆(L)");
		btnLogin.setMnemonic('L');
		btnCancel = new JButton("关闭(X)");
		btnCancel.setMnemonic('X');
		lblReg = new JLabel("注        册",JLabel.RIGHT);
		lblForgetpwd = new JLabel("找回密码",JLabel.RIGHT);
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
		readAccount();//读取文件
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
			JOptionPane.showMessageDialog(null, "功能完善中");
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
				JOptionPane.showMessageDialog(null, "请输入QQ号码");
				return;
			}
			try{
				Integer.parseInt(qqcode);
			}catch(Exception ex){
				JOptionPane.showMessageDialog(null, "QQ号码含有非法字符。");
				return;
			}
			if(pwd==null || pwd.equals("")){
				JOptionPane.showMessageDialog(null, "请输入QQ密码");
				return;
			}
			
			myInfo=new Account();
			myInfo.setQQNum(Integer.parseInt(qqcode));
			myInfo.setPassword(pwd);
			myInfo=base.login(myInfo);//登陆
			if(myInfo==null){
				JOptionPane.showMessageDialog(null, "登陆失败，请检测QQ号码或者密码是否准确.");
				return;
			}
			saveAccount(myInfo);//保存QQ号码到文件
//			JOptionPane.showMessageDialog(null, "登陆成功");
			dispose();//关闭登陆窗口
			//打开主窗口
			new MainUI(myInfo);
		}else if(e.getSource()==btnCancel){
			System.exit(0);//退出程序
		}
	}
	//保存QQ号码到文件
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
	//从文件读取曾经登陆过的QQ号码
	public void readAccount(){
		File file = new File("accounted.dat");
		//第一次使用QQ时文件不存在，直接退出
		if(!file.exists())
			return;
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			myAccount=(Hashtable<Integer, Account>)ois.readObject();
			ois.close();
			//获取所有键
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
