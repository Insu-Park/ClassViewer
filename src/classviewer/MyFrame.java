package classviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

@SuppressWarnings("serial")
public class MyFrame extends JFrame{
	
	private String filename = "";
	private String colname1[] = {"Name", "Type", "Access"};		// ���̺� �÷���
	private String colname2[] = {"Name", "Methods"};
	private String var;
	private JPanel p = new JPanel();
	private JPanel p1 = new JPanel();
	private JPanel p2 = new JPanel();
	private JPanel p3 = new JPanel();
	private JMenuBar mb = new JMenuBar();
	private JMenu mfile = new JMenu("File");
	private JMenuItem mopen = new JMenuItem("Open");
	private JMenuItem msave = new JMenuItem("Save");
	private JMenuItem mexit = new JMenuItem("Exit");
	private JScrollPane jp1, jp2;
	private DefaultTableModel model_1 = new DefaultTableModel(colname1, 0);
	private DefaultTableModel model_2 = new DefaultTableModel(colname2, 0);
	private JTable table_1 = new JTable(model_1);
	private JTable table_2 = new JTable(model_2);
	private JTextArea jt = new JTextArea();
	private JLabel ja = new JLabel();
	private ReadFileData rd = null;
	private boolean modify = false;		// ������ �������� Ȯ���ϴ� ����
	
	public MyFrame(){

		Toolkit kit = Toolkit.getDefaultToolkit();		
		Dimension screenSize = kit.getScreenSize();
		Dimension frameSize = this.getSize();
		setPreferredSize(new Dimension(800, 400));
		setLocation((screenSize.width-frameSize.width)/2, (screenSize.height-frameSize.height)/2);	// ȭ�� ��� â�� �߰� ����
		setResizable(false);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){			// ������ ���� �̺�Ʈ
				if(modify)										// ������ ������ ���� �� True
					fileExit();									// True �϶� fileExit()�޼��� ȣ��
				else
					System.exit(0);										
			}
		});
		pack();
		
		setTitle("Class Viewer");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(new TAdapter());
		
		p1.setBackground(Color.WHITE);
		p1.setLocation(10, 10);
		p1.setSize(250, 230);
		p1.setLayout(new BorderLayout());
		
		p2.setBackground(Color.WHITE);
		p2.setLocation(10, 250);
		p2.setSize(250, 85);
		p2.setLayout(new BorderLayout());
		
		p3.setBackground(Color.WHITE);
		p3.setLocation(270, 10);
		p3.setSize(515, 325);
		p3.setLayout(new BorderLayout());
		
		add(p1);
		add(p2);
		add(p3);
		add(p);
		
		mopen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));	// ����Ű ����
		mopen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {open();}});							// �׼Ǹ�����
		msave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));	
		msave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {save();}});
		mexit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mexit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {System.exit(0);}});
		
		mb.add(mfile);
		mfile.add(mopen);
		mfile.add(msave);
		mfile.add(mexit);
		this.setJMenuBar(mb);
		this.setFocusable(true);
		this.setVisible(true);
	}
	
	public void open(){
		
		JFileChooser fc = new JFileChooser();
		File nametemp = null;
		
		fc.setFileFilter(new MyFilter("cpp", "c++ ����(*.cpp)"));				// c++ ��������
		
		int yn = fc.showOpenDialog(mopen.getParent());						// Open ���̾�α� ���
		if(yn == JFileChooser.CANCEL_OPTION) return;						// ��� ��ư �������� �ٽ� �������� ���ư�
		filename = fc.getSelectedFile().toString();							// Open�� ������ ��θ� String������ fileName�� ����
		nametemp = new File(fc.getSelectedFile().toString());				// ���õ� ���ϸ��� �񱳺����� ����
		
		if(nametemp.exists())												// ������ ���ϸ��� �����ϸ� True
		{
			setTitle(filename);												// Ÿ��Ʋ�� ���ϸ����� ����

			rd = new ReadFileData(filename);								// ReadFileData ȣ��
			StringToken st = new StringToken();								// StringToken Ŭ���� ȣ��
			st.makeList(rd.getString());									// ��ü �ȿ� ���̺� ������ �ʿ��� ������ ȣ��
			
			DefaultMutableTreeNode root = new DefaultMutableTreeNode(st.cpplist.getName());		// root ��� �̸� �޾ƿͼ� ����
			for(int i=0; i<100; i++){
				if(st.cpplist.cpp[i].getName().compareTo("")!=0){
					DefaultMutableTreeNode child = new DefaultMutableTreeNode(st.cpplist.cpp[i].getName());	// Ʈ�� ��� ����
					root.add(child);
				}
			}
			
			JTree myTree = new JTree(root);													// Ʈ�� ����
			myTree.addMouseListener(new MouseAdapter(){										// Ʈ���� Ŭ�������� ���� ����
				public void mouseClicked(MouseEvent evt){
					TreePath path = myTree.getPathForLocation(evt.getX(),evt.getY());
					if(path == null) return;
				    Object obj = path.getLastPathComponent();
				    
				    if(obj instanceof DefaultMutableTreeNode){
				    	DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)obj;
				    	if(modify){															// Ŭ�������� �޼ҵ尡 ��������� �޽��� ����
				    		int r = JOptionPane.showConfirmDialog(mexit.getParent(), "�޼ҵ带 �������� �ʾҽ��ϴ�. �����Ͻðڽ��ϱ�?","Message",JOptionPane.YES_NO_OPTION); 
							if(r == JOptionPane.YES_OPTION){
								rd.setString(st.saveText(rd.getString(), var, jt.getText()));	// �ؽ�Ʈ ����
								save();															// ����
						    }
							modify = false;
							setTitle(filename);
					    }
				    	
				    	if(dmtn.getUserObject().toString().compareTo(st.cpplist.getName())==0){		// ��Ʈ ��� Ŭ���� 
				    		jp1.setVisible(true);
				    		jt.setVisible(false);
				    		ja.setVisible(false);
				    		jp2.setVisible(false);
				    	}
				    	else if(dmtn.getUserObject().toString().indexOf(':')==-1){					// �޼ҵ� Ŭ����
				    		jp1.setVisible(false);
				    		jp2.setVisible(false);
				    		jt.setVisible(true);
				    		ja.setVisible(true);
				    		jt.addKeyListener(new KeyAdapter(){
				    			public void keyTyped(KeyEvent e){
									if(modify == false)					
										modify = true;
									else
										setTitle("File Name : " + filename + " * ");
				    			}
				    		});
				    		jt.setLineWrap(true);
					    	jt.setText(st.getMethod(rd.getString(), dmtn.getUserObject().toString()));
					    	var = jt.getText();
					    	ja.setText(st.getmem());
					    	p2.add(ja);
					    	p3.add(jt);
				    	}
				    	else{								// ���� Ŭ����	
				    		jt.setVisible(false);
				    		jp1.setVisible(false);
				    		ja.setVisible(false);
				    		jp2.setVisible(true);
				    		String[] row = {"", ""};
				    		row[0] = dmtn.getUserObject().toString().substring(0, dmtn.getUserObject().toString().indexOf(':'));	// ':' ���ڿ����� ����
				    		row[1] = st.getMember(rd.getString(), row[0]);															// �Ľ�
				    		if(model_2.getRowCount()==1)						// �̹� row�� ������ 
				    			model_2.removeRow(0);							// ����
				    		model_2.addRow(row);								// �߰�
				    		p3.add(jp2);
				    	}
				    	p3.revalidate();			// ������Ʈ
				    	
				    	p3.setVisible(true);
				    }
				 }
			});
			p1.add(myTree);
			
			for(int i=0; i<100; i++){									// ���̺� ����
				if(st.cpplist.cpp[i].getName().compareTo("")!=0){
					String[] row = {"","",""};
					row[0]=st.cpplist.cpp[i].getName();
					row[1]=st.cpplist.cpp[i].getType();
					row[2]=st.cpplist.cpp[i].getAccess();
					System.out.println(row[0] + row[1] + row[2]);
					model_1.addRow(row);
					
				}
			}
			System.out.println(model_1.getRowCount());
			
			table_1.setPreferredScrollableViewportSize(new Dimension(497,300));
			table_2.setPreferredScrollableViewportSize(new Dimension(497,300));
			table_1.setRowHeight(40);
			table_2.setRowHeight(40);
			table_1.setEnabled(false);
			table_2.setEnabled(false);
			table_2.getColumnModel().getColumn(0).setPreferredWidth(30);
			
			jp1 = new JScrollPane(table_1);	// ������ Ʈ��
			jp2 = new JScrollPane(table_2);
			
			jt.setVisible(false);
    		ja.setVisible(false);
    		jp2.setVisible(false);
    		jp1.setVisible(true);
    		
    		p3.add(jp1);
    		
			this.revalidate();
		}
		else
			JOptionPane.showMessageDialog(mopen.getParent(), "�����Ͻ� ������ �������� �ʽ��ϴ�.");
	}

	public void save(){
		
		if(rd != null){
			try{
				FileWriter fw = null;
				BufferedWriter bw = null;
	
				try{
						setTitle(filename);							// Ÿ��Ʋ�� ���ϸ����� ����
						fw = new FileWriter(filename);			
						bw = new BufferedWriter(fw);
						bw.write(rd.getString());					// ��ü �ؽ�Ʈ���� ����	
						modify = false;							
				}
				catch(IOException ie){
					ie.printStackTrace();
				}
				finally{
					try{
						if(bw!=null)
							bw.close();
					}
					catch(IOException e) {}
				}
			}
			catch(Exception se){
				se.printStackTrace();
			}
		}
		else
			JOptionPane.showMessageDialog(msave.getParent(), "������ �ҷ����� �ʾҽ��ϴ�.");
	}
	
	public void fileExit(){
		if(modify){				// ������ ������ ���� ���
			int r = JOptionPane.showConfirmDialog(mexit.getParent(), "������ �������� �ʾҽ��ϴ�. �����Ͻðڽ��ϱ�?","Message",JOptionPane.YES_NO_OPTION);	// ���̾�α� ���
			if(r == JOptionPane.YES_OPTION)	
				save();
			else
				System.exit(0);
		}
		else										
			System.exit(0);
	}
	
	private class TAdapter extends KeyAdapter{
		
		@Override
		public void keyPressed(KeyEvent e){
			
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
			}
		}
	}
}
