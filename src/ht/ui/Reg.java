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


public class Reg extends JFrame implements ActionListener,KeyListener{

	private JLabel lblTitle,lblUserName,lblNickName,lblPwd,lblCfgPwd,lblAge,lblSex,lblNation;
	private JLabel lblFace,lblIp,lblPort,lblEmal,lblAddr;
	private JLabel lblbg;
	private JTextField txtUserName,txtNickName,txtAge,txtIp,txtEmail,txtAddr;
	private JPasswordField txtPwd,txtCfgPwd;
	private JRadioButton rbmale,rbremale;
	private JComboBox cbNation,cbFace;
	private JButton btnReg,btnCancel,btnopenFile;
	private IBase base;
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
	public Reg() {
		super("ע��");
		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.MotifLookAndFeel");
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//			com.sun.java.swing.plaf.windows.WindowsLookAndFeel
		} catch (Exception e) {
			e.printStackTrace();
		}
		setIconImage( new ImageIcon("face/tubiao.png").getImage());
		lblbg = new JLabel(new ImageIcon("face/bg.png"));
		add(lblbg);
		//���ܸ��Ĵ��ڵĴ�С
		setResizable(false);
		lblTitle = new JLabel("�û�ע��",JLabel.CENTER);
		lblTitle.setFont(new Font("����",Font.BOLD,28));
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setSize(500, 30);
		lblTitle.setLocation(0, 10);
		lblbg.add(lblTitle);
		
		lblUserName = new JLabel("��ʵ����:",JLabel.RIGHT);
		lblUserName.setLocation(100, 50);
		lblUserName.setSize(80, 20);
		txtUserName = new JTextField();
		txtUserName.setLocation(180, 50);
		txtUserName.setSize(180, 20);

		lblNickName = new JLabel("�ǳ�:",JLabel.RIGHT);
		lblNickName.setLocation(100, 80);
		lblNickName.setSize(80, 20);
		txtNickName = new JTextField();
		txtNickName.setLocation(180, 80);
		txtNickName.setSize(180, 20);

		lblbg.add(lblUserName);
		lblbg.add(txtUserName);
		lblbg.add(lblNickName);
		lblbg.add(txtNickName);
		lblPwd = new JLabel("��½����:",JLabel.RIGHT);
		lblPwd.setLocation(100, 110);
		lblPwd.setSize(80, 20);
		txtPwd = new JPasswordField();
		txtPwd.setLocation(180, 110);
		txtPwd.setSize(180, 20);
		lblCfgPwd = new JLabel("ȷ������:",JLabel.RIGHT);
		lblCfgPwd.setLocation(100, 140);
		lblCfgPwd.setSize(80, 20);
		txtCfgPwd = new JPasswordField();
		txtCfgPwd.setLocation(180, 140);
		txtCfgPwd.setSize(180, 20);
		lblbg.add(lblPwd);
		lblbg.add(txtPwd);
		lblbg.add(lblCfgPwd);
		lblbg.add(txtCfgPwd);
		
		lblAge = new JLabel("����:",JLabel.RIGHT);
		lblAge.setLocation(100, 170);
		lblAge.setSize(80, 20);
		txtAge = new JTextField();
		txtAge.setLocation(180, 170);
		txtAge.setSize(180, 20);

		lblbg.add(lblAge);
		lblbg.add(txtAge);

		lblSex = new JLabel("�Ա�:",JLabel.RIGHT);
		lblSex.setLocation(100, 200);
		lblSex.setSize(80, 20);
		rbmale = new JRadioButton("��");
		rbmale.setLocation(180, 200);
		rbmale.setSize(50, 20);
		rbmale.setSelected(true);//ѡ��
		rbremale = new JRadioButton("Ů");
		rbremale.setLocation(230, 200);
		rbremale.setSize(90, 20);
		ButtonGroup bg = new ButtonGroup();
		bg.add(rbmale);
		bg.add(rbremale);
		lblbg.add(lblSex);
		lblbg.add(rbmale);
		lblbg.add(rbremale);
		
		lblNation = new JLabel("����:",JLabel.RIGHT);
		lblNation.setLocation(100, 230);
		lblNation.setSize(80, 20);
		String[] snation={"����","����","׳��","����","����","����"};
		cbNation = new JComboBox(snation);
		cbNation.setLocation(180, 230);
		cbNation.setSize(180, 20);
		lblbg.add(lblNation);
		lblbg.add(cbNation);
		
		lblFace = new JLabel("ͷ��:",JLabel.RIGHT);
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
		
		lblIp = new JLabel("IP��ַ:",JLabel.RIGHT);
		lblIp.setLocation(100, 340);
		lblIp.setSize(80, 20);
		
		//InetAddress.getLocalHost().getHostAddress()
		txtIp = new JTextField("127.0.0.1");
		txtIp.setLocation(180, 340);
		txtIp.setSize(180, 20);

		lblbg.add(lblIp);
		lblbg.add(txtIp);
		
		lblEmal = new JLabel("�����ַ:",JLabel.RIGHT);
		lblEmal.setLocation(100, 370);
		lblEmal.setSize(80, 20);
		txtEmail = new JTextField();
		txtEmail.setLocation(180, 370);
		txtEmail.setSize(180, 20);
		lblbg.add(lblEmal);
		lblbg.add(txtEmail);

		lblAddr = new JLabel("��ͥ��ַ:",JLabel.RIGHT);
		lblAddr.setLocation(100, 400);
		lblAddr.setSize(80, 20);
		txtAddr = new JTextField();
		txtAddr.setLocation(180, 400);
		txtAddr.setSize(180, 20);
		lblbg.add(lblAddr);
		lblbg.add(txtAddr);

		
		btnReg = new JButton("ע��");
		btnCancel = new JButton("�ر�");
		btnReg.addActionListener(this);
		btnCancel.addActionListener(this);
		btnReg.setSize(80, 30);
		btnCancel.setSize(80, 30);
		btnReg.setLocation(140, 460);
		btnCancel.setLocation(280, 460);
		
		lblbg.add(btnReg);
		lblbg.add(btnCancel);
		setSize(500, 550);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//�رյ�ǰ����
		setLocationRelativeTo(null);
		base = new BaseDAO();//�������ݿ��������

	}
	public static void main(String[] args) {
		new Reg();
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnReg){
			//��ȡ��Ա����
			//trim()������ɾ����ͷ�Ŀո�
			String sname = txtUserName.getText().trim();
			if(sname.equals("")){
				JOptionPane.showMessageDialog(null,"�������Ա����");
				txtUserName.requestFocus();//��ȡ����
				return;
			}
			if(txtPwd.getText().equals("")){
				JOptionPane.showMessageDialog(null,"����������");
				txtPwd.requestFocus();//��ȡ����
				return;
			}
			if(!txtPwd.getText().equals(txtCfgPwd.getText())){
				JOptionPane.showMessageDialog(null,"��½������ȷ�����벻��");
				txtCfgPwd.requestFocus();//��ȡ����
				return;
			}
			String sage = txtAge.getText().trim();
			int age=0;
			try{
				age = Integer.parseInt(sage);
				if(age<0 || age>150){
					JOptionPane.showMessageDialog(null,"�������Ϊ>0����<150������");
					return;
				}
			}catch (Exception e1) {
				JOptionPane.showMessageDialog(null,"�������Ϊ>0����<150������");
				txtAge.requestFocus();//��ȡ����
				return;
			}
			Account acc = new Account();
			acc.setTrueName(txtUserName.getText().trim());
			acc.setNickName(txtNickName.getText().trim());
			acc.setPassword(txtPwd.getText().trim());
			acc.setAge(age);
			if(rbmale.isSelected())
				acc.setSex("��");
			else
				acc.setSex("Ů");
			acc.setNation(cbNation.getSelectedItem().toString());
			acc.setIp(txtIp.getText().trim());
			acc.setEmail(txtEmail.getText().trim());
			acc.setAddress(txtAddr.getText().trim());
			acc.setFaceImage(imgFiles[cbFace.getSelectedIndex()]);
			acc.setStatus(Common.STATUS_LEVEL);
			acc.setPort(0);
			//��ȡQQ����
			
			acc.setQQNum(getQQcode());
			//���浽���ݿ�addAccount
			if(base.addAccount(acc)<=0){
				JOptionPane.showMessageDialog(null, "ע��ʧ��");
			}else{
				JOptionPane.showMessageDialog(null, "ע��ɹ�,����QQ�����ǣ�"+acc.getQQNum()+";�����Ʊ��ܡ�");
			}
			
			
		}else if(e.getSource()==btnCancel){
			dispose();//�رյ�ǰ����
		}
		
	}
	public int getQQcode(){
		Random ran = new Random();
		int qqcode = ran.nextInt(90000)+10000;
		boolean exist = base.isExistsqqcode(qqcode);
		//ѭ���жϣ����QQ�����Ѿ����ڣ����������µĺ��룬�ٲ�ѯ��ֱ�����벻����Ϊֹ
		while(exist){
			qqcode = ran.nextInt(90000)+10000;
			exist = base.isExistsqqcode(qqcode);
		}
		return qqcode;
	}
	public void keyPressed(KeyEvent e) {
		if(e.getSource()==txtAge){
			if(e.getKeyCode()<48 || e.getKeyCode()>57){
				JOptionPane.showMessageDialog(null, "�㰴���ǷǷ��ַ�");
				txtAge.setText("");
			}
			int age = Integer.parseInt(txtAge.getText());
			if(age<0 || age>150){
				JOptionPane.showMessageDialog(null,"�������Ϊ>0����<150������");
				txtAge.setText("");
			}
		}
	}
	public void keyReleased(KeyEvent e) {
		
	}
	public void keyTyped(KeyEvent e) {
		
	}
	
}
