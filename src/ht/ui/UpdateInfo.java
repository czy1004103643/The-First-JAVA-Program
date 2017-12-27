package ht.ui;

import ht.bean.Account;
import ht.common.Common;
import ht.db.BaseDAO;
import ht.db.IBase;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;


public class UpdateInfo extends JFrame implements ActionListener,KeyListener{

	private JLabel lblTitle,lblUserName,lblNickName,lblPwd,lblCfgPwd,lblAge,lblSex,lblNation;
	private JLabel lblFace,lblIp,lblPort,lblEmal,lblAddr,lblqqnum;
	private JLabel lblbg;
	private JTextField txtUserName,txtNickName,txtAge,txtIp,txtEmail,txtAddr,txtqqnum;
	private JPasswordField txtPwd,txtCfgPwd;
	private JRadioButton rbmale,rbremale;
	private JComboBox cbNation,cbFace;
	private JButton btnReg,btnCancel,btnopenFile;
	private IBase base;
	private Account myAccount;
	private MainUI mainUI;
	String imgFiles[]={
			"face/0.png",
			"face/1.png",
			"face/2.png",
			"face/3.png",
			"face/4.png",
			"face/5.png",
			"face/6.png",
			"face/7.png",
			"face/8.png",
			"face/9.png",
			"face/10.png"
	};
	public UpdateInfo(Account myAccount,MainUI mainUI) {
		super("修改"+myAccount.getNickName()+"的个人资料");
		this.myAccount = myAccount;
		this.mainUI = mainUI;
		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.MotifLookAndFeel");
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//			com.sun.java.swing.plaf.windows.WindowsLookAndFeel
		} catch (Exception e) {
			e.printStackTrace();
		}
		setIconImage( new ImageIcon(myAccount.getFaceImage()).getImage());
		lblbg = new JLabel(new ImageIcon("face/beijin.jpg"));
		add(lblbg);
		//不能更改窗口的大小
		setResizable(false);
		lblTitle = new JLabel("修改用户信息",JLabel.CENTER);
		lblTitle.setFont(new Font("隶书",Font.BOLD,28));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setSize(500, 30);
		lblTitle.setLocation(0, 10);
		lblbg.add(lblTitle);
		
		lblUserName = new JLabel("真实名字:",JLabel.RIGHT);
		lblUserName.setLocation(100, 50);
		lblUserName.setSize(80, 20);
		txtUserName = new JTextField(myAccount.getTrueName());
		txtUserName.setLocation(180, 50);
		txtUserName.setSize(180, 20);

		lblNickName = new JLabel("昵称:",JLabel.RIGHT);
		lblNickName.setLocation(100, 80);
		lblNickName.setSize(80, 20);
		txtNickName = new JTextField(myAccount.getNickName());
		txtNickName.setLocation(180, 80);
		txtNickName.setSize(180, 20);

		lblbg.add(lblUserName);
		lblbg.add(txtUserName);
		lblbg.add(lblNickName);
		lblbg.add(txtNickName);
		lblPwd = new JLabel("登陆密码:",JLabel.RIGHT);
		lblPwd.setLocation(100, 110);
		lblPwd.setSize(80, 20);
		txtPwd = new JPasswordField();
		txtPwd.setLocation(180, 110);
		txtPwd.setSize(180, 20);
		txtPwd.setEnabled(false);//无效
		lblCfgPwd = new JLabel("确认密码:",JLabel.RIGHT);
		lblCfgPwd.setLocation(100, 140);
		lblCfgPwd.setSize(80, 20);
		txtCfgPwd = new JPasswordField();
		txtCfgPwd.setLocation(180, 140);
		txtCfgPwd.setSize(180, 20);
		txtCfgPwd.setEnabled(false);//无效
		lblbg.add(lblPwd);
		lblbg.add(txtPwd);
		lblbg.add(lblCfgPwd);
		lblbg.add(txtCfgPwd);
		
		lblAge = new JLabel("年龄:",JLabel.RIGHT);
		lblAge.setLocation(100, 170);
		lblAge.setSize(80, 20);
		txtAge = new JTextField(myAccount.getAge()+"");
		txtAge.setLocation(180, 170);
		txtAge.setSize(180, 20);

		lblbg.add(lblAge);
		lblbg.add(txtAge);

		lblSex = new JLabel("性别:",JLabel.RIGHT);
		lblSex.setLocation(100, 200);
		lblSex.setSize(80, 20);
		rbmale = new JRadioButton("男");
		rbmale.setLocation(180, 200);
		rbmale.setSize(50, 20);
		rbremale = new JRadioButton("女");
		rbremale.setLocation(230, 200);
		rbremale.setSize(90, 20);
		ButtonGroup bg = new ButtonGroup();
		if(myAccount.getSex().equals("男")){
			rbmale.setSelected(true);//选中
		}else{
			rbremale.setSelected(true);//选中
		}
		bg.add(rbmale);
		bg.add(rbremale);
		lblbg.add(lblSex);
		lblbg.add(rbmale);
		lblbg.add(rbremale);
		
		lblNation = new JLabel("民族:",JLabel.RIGHT);
		lblNation.setLocation(100, 230);
		lblNation.setSize(80, 20);
		String[] snation={"汉族","苗族","壮族","回族","藏族","侗族"};
		cbNation = new JComboBox(snation);
		cbNation.setLocation(180, 230);
		cbNation.setSize(180, 20);
		lblbg.add(lblNation);
		lblbg.add(cbNation);
		for(int i=0;i<snation.length;i++){
			if(myAccount.getNation().equals(snation[i])){
				cbNation.setSelectedIndex(i);
				break;
			}
		}
		lblFace = new JLabel("头像:",JLabel.RIGHT);
		lblFace.setLocation(100, 260);
		lblFace.setSize(80, 20);
		ImageIcon img[]={
				new ImageIcon("face/0.png"),
				new ImageIcon("face/1.png"),
				new ImageIcon("face/2.png"),
				new ImageIcon("face/3.png"),
				new ImageIcon("face/4.png"),
				new ImageIcon("face/5.png"),
				new ImageIcon("face/6.png"),
				new ImageIcon("face/7.png"),
				new ImageIcon("face/8.png"),
				new ImageIcon("face/9.png"),
				new ImageIcon("face/10.png")
		};
		cbFace = new JComboBox(img);
		cbFace.setLocation(180, 260);
		cbFace.setSize(180, 65);
		
		lblbg.add(lblFace);
		lblbg.add(cbFace);
		
		for(int i=0;i<imgFiles.length;i++){
			if(myAccount.getFaceImage().equals(imgFiles[i])){
				cbFace.setSelectedIndex(i);//选中
				break;
			}
		}
		
		
		lblIp = new JLabel("IP地址:",JLabel.RIGHT);
		lblIp.setLocation(100, 340);
		lblIp.setSize(80, 20);
		
		//InetAddress.getLocalHost().getHostAddress()
		txtIp = new JTextField(myAccount.getIp());
		txtIp.setLocation(180, 340);
		txtIp.setSize(180, 20);

		lblbg.add(lblIp);
		lblbg.add(txtIp);
		
		lblEmal = new JLabel("邮箱地址:",JLabel.RIGHT);
		lblEmal.setLocation(100, 370);
		lblEmal.setSize(80, 20);
		txtEmail = new JTextField(myAccount.getEmail());
		txtEmail.setLocation(180, 370);
		txtEmail.setSize(180, 20);
		lblbg.add(lblEmal);
		lblbg.add(txtEmail);

		lblAddr = new JLabel("家庭地址:",JLabel.RIGHT);
		lblAddr.setLocation(100, 400);
		lblAddr.setSize(80, 20);
		txtAddr = new JTextField(myAccount.getAddress());
		txtAddr.setLocation(180, 400);
		txtAddr.setSize(180, 20);
		lblbg.add(lblAddr);
		lblbg.add(txtAddr);

		lblqqnum = new JLabel("QQ号码:",JLabel.RIGHT);
		lblqqnum.setLocation(100, 430);
		lblqqnum.setSize(80, 20);
		txtqqnum = new JTextField(myAccount.getQQNum()+"");
		txtqqnum.setLocation(180, 430);
		txtqqnum.setSize(180, 20);
		txtqqnum.setEditable(false);//只读
		lblbg.add(lblqqnum);
		lblbg.add(txtqqnum);
		
		btnReg = new JButton("修改");
		btnCancel = new JButton("关闭");
		btnReg.addActionListener(this);
		btnCancel.addActionListener(this);
		btnReg.setSize(80, 30);
		btnCancel.setSize(80, 30);
		btnReg.setLocation(140, 480);
		btnCancel.setLocation(280, 480);
		
		lblbg.add(btnReg);
		lblbg.add(btnCancel);
		setSize(500, 550);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//关闭当前窗口
		setLocationRelativeTo(null);
		base = new BaseDAO();//创建数据库操作对象

	}
	public static void main(String[] args) {
		new Reg();
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnReg){
			//获取会员名称
			//trim()函数，删除两头的空格
			String sname = txtUserName.getText().trim();
			if(sname.equals("")){
				JOptionPane.showMessageDialog(null,"请输入真实姓名");
				txtUserName.requestFocus();//获取焦点
				return;
			}
			String sage = txtAge.getText().trim();
			int age=0;
			try{
				age = Integer.parseInt(sage);
				if(age<0 || age>150){
					JOptionPane.showMessageDialog(null,"年龄必须为>0并且<150的数字");
					return;
				}
			}catch (Exception e1) {
				JOptionPane.showMessageDialog(null,"年龄必须为>0并且<150的数字");
				txtAge.requestFocus();//获取焦点
				return;
			}
			Account acc = new Account();
			acc.setQQNum(Integer.parseInt(txtqqnum.getText().trim()));
			acc.setTrueName(txtUserName.getText().trim());
			acc.setNickName(txtNickName.getText().trim());
			acc.setPassword(txtPwd.getText().trim());
			acc.setAge(age);
			if(rbmale.isSelected())
				acc.setSex("男");
			else
				acc.setSex("女");
			acc.setNation(cbNation.getSelectedItem().toString());
			acc.setIp(txtIp.getText().trim());
			acc.setEmail(txtEmail.getText().trim());
			acc.setAddress(txtAddr.getText().trim());
			acc.setFaceImage(imgFiles[cbFace.getSelectedIndex()]);
			//获取QQ号码
			
			//保存到数据库addAccount
			if(base.updateInfo(acc)){
				JOptionPane.showMessageDialog(null, "修改成功");
			}
			//改变主窗口的显示
			mainUI.myAccount = acc;
			String str = "("+acc.getQQNum()+")"+ acc.getNickName();
			mainUI.lblFace.setIcon(new ImageIcon(acc.getFaceImage()));
			mainUI.setIconImage(new ImageIcon(acc.getFaceImage()).getImage());
			mainUI.lblFace.setText(str);
			mainUI.refresh();
			
		}else if(e.getSource()==btnCancel){
			dispose();//关闭当前窗口
		}
		
	}
	public int getQQcode(){
		Random ran = new Random();
		int qqcode = ran.nextInt(90000)+10000;
		boolean exist = base.isExistsqqcode(qqcode);
		//循环判断，如果QQ号码已经存在，重新生成新的号码，再查询，直到号码不存在为止
		while(exist){
			qqcode = ran.nextInt(90000)+10000;
			exist = base.isExistsqqcode(qqcode);
		}
		return qqcode;
	}
	public void keyPressed(KeyEvent e) {
		if(e.getSource()==txtAge){
			if(e.getKeyCode()<48 || e.getKeyCode()>57){
				JOptionPane.showMessageDialog(null, "你按的是非法字符");
				txtAge.setText("");
			}
			int age = Integer.parseInt(txtAge.getText());
			if(age<0 || age>150){
				JOptionPane.showMessageDialog(null,"年龄必须为>0并且<150的数字");
				txtAge.setText("");
			}
		}
	}
	public void keyReleased(KeyEvent e) {
		
	}
	public void keyTyped(KeyEvent e) {
		
	}
	
}
