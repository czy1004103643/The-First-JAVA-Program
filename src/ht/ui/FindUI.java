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
	Vector rowsData;//����
	Vector<String> headData; //��ͷ
	IBase base;
	Account myAccount;
	public FindUI(Account myAccount) {
		super("���Һ���");
		this.myAccount = myAccount;
		JPanel nPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		lblSex = new JLabel("�Ա�",JLabel.RIGHT);
		lblNation = new JLabel("����",JLabel.RIGHT);
		lblQqnum = new JLabel("QQ����",JLabel.RIGHT);
		lblNickName = new JLabel("�ǳ�",JLabel.RIGHT);
		lblStatus = new JLabel("״̬",JLabel.RIGHT);
		
		String sex[] = {"��ѡ","��","Ů"};
		cbSex = new JComboBox(sex);
		String[] snation={"��ѡ","����","����","׳��","����","����","����"};
		cbNation = new JComboBox(snation);
		String status[] = {"��ѡ","����","����","����","æµ"};
		cbStatus = new JComboBox(status);
		txtQqnum = new JTextField(8);
		txtNickName = new JTextField(8);
		btnFind = new JButton("����");
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
		
		//��ͷ
		headData = new Vector<String>();
		headData.addElement("QQ����");
		headData.addElement("����");
		headData.addElement("�ǳ�");
		headData.addElement("�Ա�");
		headData.addElement("����");
		headData.addElement("����");
		headData.addElement("ͷ��");
		headData.addElement("IP��ַ");
		headData.addElement("�˿�");
		headData.addElement("״̬");
		base = new BaseDAO();
		DataModel model = new DataModel();
		dataTable = new JTable(model);
		dataTable.setRowHeight(60);
		find(null);//û������
		add(new JScrollPane(dataTable));
		JPanel sPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnAdd = new JButton("��Ӻ���");
		btnClose = new JButton("�ر�");
		sPanel.add(btnAdd);
		sPanel.add(btnClose);
		add(sPanel,BorderLayout.SOUTH);
 
		btnFind.addActionListener(this);
		btnAdd.addActionListener(this);
		btnClose.addActionListener(this);
		dataTable.addMouseListener(this);
		
		setSize(700, 550);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//�رյ�ǰ����
		setLocationRelativeTo(null);
	}
	public void find(Account acc){
		rowsData = base.findAllInfo(acc);//��ȡ����
		dataTable.updateUI();//ˢ�½���;
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
		//��ȡ��ͷ����
		@Override
		public String getColumnName(int column) {
			return headData.get(column);
		}

		//��ʾÿһ��ʱϵͳ���Զ����øú���
		public Object getValueAt(int rowIndex, int columnIndex) {

			Vector row = (Vector)rowsData.get(rowIndex);//��ȡһ��
			if(columnIndex==6){//��ʾͷ��
				return new ImageIcon(row.get(columnIndex).toString());
			}else{
				return row.get(columnIndex);
			}
		}
		//����ÿһ�е���������
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if(columnIndex==6){//��ʾͷ��
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
			find(acc);//����
		}else if(e.getSource()==btnAdd){
			add();
		}else if(e.getSource()==btnClose){
			dispose();
		}
		
	}
	//��Ӻ���
	public void add(){
		if(dataTable.getSelectedRow()<0){
			JOptionPane.showMessageDialog(null, "��ѡ����Ҫ��ӵĺ���");
			return;
		}
		int index = dataTable.getSelectedRows()[0];//��ȡѡ�е���
		Vector vfriend = (Vector)rowsData.get(index);
		//��ȡ������Ϣ
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
			JOptionPane.showMessageDialog(null, "��������Լ�Ϊ���ѡ�");
			return;
		}
		//�ж϶Է��Ƿ��Ѿ����ҵĺ�����
		String message=friendAccount.getNickName() +"("+friendAccount.getQQNum()+")�Ѿ��Ǻ��ѡ�";
		if(base.isFriend(myAccount.getQQNum(), friendAccount.getQQNum())){
			JOptionPane.showMessageDialog(null, message);
			return;
		}
		message="��ȷ�����"+friendAccount.getNickName() +"("+friendAccount.getQQNum()+")Ϊ������";
		if(JOptionPane.showConfirmDialog(null, message)==JOptionPane.OK_OPTION){
			//1.������Ϣ������;
			SendMsg msg = new SendMsg();
			msg.cmd = Common.CMD_ADDFRIEND;
			msg.friendAccount = friendAccount;
			msg.myAccount = myAccount;
			//������Ϣ
			new SendSocket().send(msg);
		}
	}
	public void mouseClicked(MouseEvent e) {
		//˫�������Ӻ���
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
