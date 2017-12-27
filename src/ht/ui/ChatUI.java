package ht.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import ht.bean.Account;
import ht.common.Common;
import ht.common.SendMsg;
import ht.common.SendSocket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatUI extends JFrame implements ActionListener,ItemListener{
	
	JLabel lblChatInfo,lblboy,lblgril;//
	JTextPane txtReceive,txtSend;
	JButton btnSend,btnClose;
	JComboBox cbFont,cbSize,cbStyle;
	
	Account myAccount,friendAccount;
	String style[] ={"����","����","б��","��б��"}; 
	String sfont[] = {"����","����","����","����"};
	Integer nsize[] = {10,11,12,14,16,18,20,24,28,36,48,72};

	public ChatUI(Account myAccount,Account friendAccount) {
		String title = "��������";
		setTitle(title);
		setIconImage(new ImageIcon(friendAccount.getFaceImage()).getImage());
		this.myAccount=myAccount;
		this.friendAccount=friendAccount;
		//�ұߵģ���2��QQ��
	//	JPanel epanel = new JPanel(new GridLayout(2,1,5,5));
		//�м�ģ��ֳ�2���֣����沿���ǽ��տ����沿���Ƿ�bpanel
		JPanel cpanel = new JPanel(new GridLayout(2,1,5,5));
		
		
		//�ֳ�3���֣�����Ű�ť,�м�ŷ��Ϳ�����ŷ��Ͱ�ť��
		JPanel bpanel = new JPanel(new BorderLayout());
		//����Ű�ť
		JPanel npanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));//����
		//����ŷ��Ͱ�ť��
		JPanel npanel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));//����
		
		lblChatInfo = new JLabel(title);
		add(lblChatInfo,BorderLayout.NORTH);
		add(cpanel);
		//add(epanel,BorderLayout.EAST);
		txtReceive = new JTextPane();
		cpanel.add(new JScrollPane(txtReceive));//���沿���ǽ��տ�
		cpanel.add(bpanel);//���沿���Ƿ�bpanel
		bpanel.add(npanel1,BorderLayout.NORTH);
		bpanel.add(npanel2,BorderLayout.SOUTH);
		//btnFace = new JButton(new ImageIcon("face/sendFace.png"));
		//btnFace.setMargin(new Insets(0, 0, 0, 0)); // ʹ��ť��С��ͼƬ��Сһ��
		//btnShake = new JButton(new ImageIcon("face/zd.png"));
	//	btnShake.setMargin(new Insets(0, 0, 0, 0)); // ʹ��ť��С��ͼƬ��Сһ��
		cbFont = new JComboBox(sfont);
		cbSize = new JComboBox(nsize);
		cbStyle = new JComboBox(style);
		//btnColor = new JButton("������ɫ");
		//btnSendFile = new JButton("�����ļ�");
		//npanel1.add(btnFace);
		//npanel1.add(btnShake);
		npanel1.add(cbFont);
		npanel1.add(cbSize);
		npanel1.add(cbStyle);
		//npanel1.add(btnColor);
		//npanel1.add(btnSendFile);
		txtSend = new JTextPane();
		bpanel.add(new JScrollPane(txtSend));
		btnSend = new JButton("����(S)");
		btnSend.setMnemonic('S');
		btnClose = new JButton("�ر�(C)");
		btnClose.setMnemonic('C');
		npanel2.add(btnSend);
		npanel2.add(btnClose);
		btnSend.addActionListener(this);
		btnClose.addActionListener(this);
		//btnColor.addActionListener(this);
		//btnFace.addActionListener(this);
		//btnShake.addActionListener(this);
		
		cbFont.addItemListener(this);
		cbSize.addItemListener(this);
		cbStyle.addItemListener(this);
		
		//lblboy = new JLabel(new ImageIcon("face/boy.gif"));
		//lblgril = new JLabel(new ImageIcon("face/girl.gif"));
		//epanel.add(lblboy);
		//epanel.add(lblgril);
		//setResizable(false);
		setSize(700, 500);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//���ش���
		
	}
	//�ѷ��Ϳ�������ύ�����տ�ͬʱ������Ϳ������
	public  void appendView(String name, StyledDocument xx)throws BadLocationException {
		//��ȡ���տ���ĵ������ݣ�
		StyledDocument vdoc = txtReceive.getStyledDocument();//��ȡ���տ������
		
		// ��ʽ��ʱ��
		Date date = new Date();//��ȡϵͳ��ǰʱ��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); //������ʾʱ����ĸ�ʽ
		String time = sdf.format(date);//��ȡʱ����
		//����һ�����Լ���
		SimpleAttributeSet as = new SimpleAttributeSet();
		// ��ʾ˭˵
		//vdoc.getLength()��ȡ���տ��е�ԭ�����ݵĳ���
		String s =name + "    " + time + "\n";
//		saveRecord(name,s);//���������¼
		vdoc.insertString(vdoc.getLength(), s, as);
		int end = 0;
		//������ʾ������
		while (end < xx.getLength()) {
			// ���һ��Ԫ��
			Element e0 = xx.getCharacterElement(end);
			//��ȡ��Ӧ�ķ��
			SimpleAttributeSet as1 = new SimpleAttributeSet();
			StyleConstants.setForeground(as1, StyleConstants.getForeground(e0.getAttributes()));
			StyleConstants.setFontSize(as1, StyleConstants.getFontSize(e0.getAttributes()));
			StyleConstants.setFontFamily(as1, StyleConstants.getFontFamily(e0.getAttributes()));
			//��ȡ��Ԫ�ص�����
			s = e0.getDocument().getText(end, e0.getEndOffset() - end);
			// ��Ԫ�ؼӵ��������
			if("icon".equals(e0.getName())){
				vdoc.insertString(vdoc.getLength(), s, e0.getAttributes());
			}
			else{
				vdoc.insertString(vdoc.getLength(), s, as1);
//				saveRecord(name,s+"\n");//���������¼
			}
			//getEndOffset�����������ǻ�ȡ��ǰԪ�صĳ���
			end = e0.getEndOffset(); 
		}
		// ����һ������
		vdoc.insertString(vdoc.getLength(), "\n", as);
		
		// ������ʾ��ͼ���ַ���λ�����ĵ���β���Ա���ͼ����
		txtReceive.setCaretPosition(vdoc.getLength());
	}
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==btnSend){
			SendMsg msg = new SendMsg();
			msg.cmd = Common.CMD_SEND;
			msg.myAccount = myAccount;
			msg.friendAccount = friendAccount;
			msg.msg = txtSend.getStyledDocument();//��������
			//������Ϣ
			new SendSocket().send(msg);
			//�ѷ��Ϳ��������ʾ�����տ�
			try {
				appendView(myAccount.getNickName(), txtSend.getStyledDocument());
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			//��շ��Ϳ������
			txtSend.removeAll();
			txtSend.setText("");
			
		}else if(e.getSource()==btnClose){
			dispose();//�رյ�ǰ����
		}/*else if(e.getSource()==btnColor){
			JColorChooser colordlg = new JColorChooser();
			Color c =colordlg.showDialog(null, "��ѡ����ɫ", Color.BLACK);
			txtSend.setForeground(c);
		}else if(e.getSource()==btnShake){
			SendMsg msg = new SendMsg();
			msg.cmd = Common.CMD_SHAKE;
			msg.myAccount = myAccount;
			msg.friendAccount = friendAccount;
			new SendSocket().send(msg);
//			shake();
		}else if(e.getSource()==btnFace){
			BqUI bqui = new BqUI(this);
		}*/
		
	}
	//����
	public void shake(){
		int left = this.getX();
		int top = this.getY();
		for(int i=0;i<20;i++){
			try {
				if(i%2==0){
					this.setLocation(left-5, top-5);
				}else{
					this.setLocation(left+5, top+5);
				}
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource()== cbFont || e.getSource()==cbSize || e.getSource()==cbStyle){
			String font = sfont[cbFont.getSelectedIndex()]; 
			int size = nsize[cbSize.getSelectedIndex()];
			String st = style[cbStyle.getSelectedIndex()];
			int nstyle = Font.PLAIN;
			//{"����","����","б��","��б��"}; 
			if(st.equals(style[1])){
				nstyle=Font.BOLD;
			}else if(st.equals(style[2])){
				nstyle=Font.ITALIC;
			}else if(st.equals(style[3])){
				nstyle=Font.ITALIC+Font.BOLD;
			}
			Font f = new Font(font,nstyle,size);
			txtSend.setFont(f);
		}
	}


}
