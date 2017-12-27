package ht.ui;

import ht.bean.Account;
import ht.common.Common;
import ht.common.SendMsg;
import ht.common.SendSocket;
import ht.db.BaseDAO;
import ht.db.IBase;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

public class FindUI extends JFrame implements ActionListener,MouseListener{
	
	JLabel lblSex,lblNation,lblQqnum,lblNickName,lblStatus;
	JComboBox cbSex,cbNation,cbStatus;
	JTextField txtQqnum,txtNickName;
	JButton btnFind,btnAdd,btnClose;
	JTable dataTable;
	Vector rowsData;//数据
	Vector<String> headData; //表头
	IBase base;
	Account myAccount;
	public FindUI(Account myAccount) {
		super("查找好友");
		this.myAccount = myAccount;
		JPanel nPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblSex = new JLabel("性别",JLabel.RIGHT);
		lblNation = new JLabel("民族",JLabel.RIGHT);
		lblQqnum = new JLabel("QQ号码",JLabel.RIGHT);
		lblNickName = new JLabel("昵称",JLabel.RIGHT);
		lblStatus = new JLabel("状态",JLabel.RIGHT);
		
		String sex[] = {"不选","男","女"};
		cbSex = new JComboBox(sex);
		String[] snation={"不选","汉族","苗族","壮族","回族","藏族","侗族"};
		cbNation = new JComboBox(snation);
		String status[] = {"不选","在线","离线","隐身","忙碌"};
		cbStatus = new JComboBox(status);
		txtQqnum = new JTextField(8);
		txtNickName = new JTextField(8);
		btnFind = new JButton("查找");
		nPanel.add(lblSex);
		nPanel.add(cbSex);
		nPanel.add(lblNation);
		nPanel.add(cbNation);
		nPanel.add(lblStatus);
		nPanel.add(cbStatus);
		nPanel.add(lblNickName);
		nPanel.add(txtNickName);
		nPanel.add(lblQqnum);
		nPanel.add(txtQqnum);
		nPanel.add(btnFind);
		add(nPanel,BorderLayout.NORTH);
		
		//表头
		headData = new Vector<String>();
		headData.addElement("QQ号码");
		headData.addElement("姓名");
		headData.addElement("昵称");
		headData.addElement("性别");
		headData.addElement("年龄");
		headData.addElement("民族");
		headData.addElement("头像");
		headData.addElement("IP地址");
		headData.addElement("端口");
		headData.addElement("状态");
		base = new BaseDAO();
		DataModel model = new DataModel();
		dataTable = new JTable(model);
		dataTable.setRowHeight(60);
		find(null);//没有条件
		add(new JScrollPane(dataTable));
		JPanel sPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnAdd = new JButton("添加好友");
		btnClose = new JButton("关闭");
		sPanel.add(btnAdd);
		sPanel.add(btnClose);
		add(sPanel,BorderLayout.SOUTH);
 
		btnFind.addActionListener(this);
		btnAdd.addActionListener(this);
		btnClose.addActionListener(this);
		dataTable.addMouseListener(this);
		
		setSize(700, 550);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//关闭当前窗口
		setLocationRelativeTo(null);
	}
	public void find(Account acc){
		rowsData = base.findAllInfo(acc);//获取数据
		dataTable.updateUI();//刷新界面;
	}
	public static void main(String[] args) {
//		new FindUI();
	}

	class DataModel extends AbstractTableModel{

		public DataModel() {
		
		}
		public int getColumnCount() {
			
			return headData.size();
		}

		public int getRowCount() {
			
			return rowsData.size();
		}
		//获取表头标题
		@Override
		public String getColumnName(int column) {
			return headData.get(column);
		}

		//显示每一列时系统会自动调用该函数
		public Object getValueAt(int rowIndex, int columnIndex) {

			Vector row = (Vector)rowsData.get(rowIndex);//获取一行
			if(columnIndex==6){//表示头像
				return new ImageIcon(row.get(columnIndex).toString());
			}else{
				return row.get(columnIndex);
			}
		}
		//设置每一列的数据类型
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if(columnIndex==6){//表示头像
				return ImageIcon.class;
			}else{
				return super.getColumnClass(columnIndex);
			}
		}
		
	}

	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == btnFind){
			Account acc = new Account();
			acc.setSex(cbSex.getSelectedItem().toString());
			acc.setNation(cbNation.getSelectedItem().toString());
			acc.setStatus(cbStatus.getSelectedItem().toString());
			if(!txtNickName.getText().trim().equals("")){
				acc.setNickName(txtNickName.getText().trim());
			}
			int qqcode = 0;
			if(!txtQqnum.getText().trim().equals("")){
				qqcode=Integer.parseInt(txtQqnum.getText().trim());
				acc.setQQNum(qqcode);
			}
			find(acc);//查找
		}else if(e.getSource()==btnAdd){
			add();
		}else if(e.getSource()==btnClose){
			dispose();
		}
		
	}
	//添加好友
	public void add(){
		if(dataTable.getSelectedRow()<0){
			JOptionPane.showMessageDialog(null, "请选择你要添加的好友");
			return;
		}
		int index = dataTable.getSelectedRows()[0];//获取选中的行
		Vector vfriend = (Vector)rowsData.get(index);
		//获取好友信息
		Account friendAccount = new Account();
		friendAccount.setQQNum(Integer.parseInt(vfriend.get(0).toString()));
		friendAccount.setTrueName(vfriend.get(1).toString());
		friendAccount.setNickName(vfriend.get(2).toString());
		friendAccount.setSex(vfriend.get(3).toString());
		//friendAccount.setFaceImage(vfriend.get(6).toString());
		friendAccount.setIp(vfriend.get(7).toString());
		friendAccount.setPort(Integer.parseInt(vfriend.get(8).toString()));
		friendAccount.setStatus(vfriend.get(9).toString());
		
		if(myAccount.getQQNum() == friendAccount.getQQNum()){
			JOptionPane.showMessageDialog(null, "不能添加自己为好友。");
			return;
		}
		//判断对方是否已经是我的好友了
		String message=friendAccount.getNickName() +"("+friendAccount.getQQNum()+")已经是好友。";
		if(base.isFriend(myAccount.getQQNum(), friendAccount.getQQNum())){
			JOptionPane.showMessageDialog(null, message);
			return;
		}
		message="您确定添加"+friendAccount.getNickName() +"("+friendAccount.getQQNum()+")为好友吗？";
		if(JOptionPane.showConfirmDialog(null, message)==JOptionPane.OK_OPTION){
			//1.发送消息给好友;
			SendMsg msg = new SendMsg();
			msg.cmd = Common.CMD_ADDFRIEND;
			msg.friendAccount = friendAccount;
			msg.myAccount = myAccount;
			//发送消息
			new SendSocket().send(msg);
		}
	}
	public void mouseClicked(MouseEvent e) {
		//双击表格添加好友
		if(e.getSource()==dataTable){
			if(e.getClickCount()==2){
				add();
			}
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
}
