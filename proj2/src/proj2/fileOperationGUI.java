package proj2;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class fileOperationGUI extends JFrame {

	/** 一个简洁的View视图界面
	 */
	private static final long serialVersionUID = 1L;

	JPanel jp0 = new JPanel(); //欢迎和帮助部分
	JPanel jp1 = new JPanel(); //文件选中部分
	JPanel jp2 = new JPanel();  //操作部分
	JPanel jp3 = new JPanel();
	JPanel jp4 = new JPanel();
	
	JLabel prompt = new JLabel("文件：");
	JLabel operation = new JLabel("操作：");
	JButton selectFile = new JButton("浏览");  //浏览文件的按钮，会弹出文件选择器
	
	JLabel helper = new JLabel("欢迎使用20185653文件资源管理器！请选择文件，并操作！");
	JTextField SelectfileAddr = new JTextField("           			"); //不填充的话格子太小了
	
	JButton createbutton = new JButton("创建");
	JButton deletebutton = new JButton("删除");
	JButton copybutton = new JButton("复制");
	JButton encodebutton = new JButton("加密");
	JButton decodebutton = new JButton("解密");
	JButton zipbutton = new JButton("压缩");
	JButton unzipbutton = new JButton("解压");
	JButton showbutton = new JButton("展示");

	
	fileOperationGUI(){
		setSize(500, 250);
		setTitle("文件资源管理程序");
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//默认按键事件
		
		jp0.add(helper);
		
		jp1.add(prompt);
		jp1.add(SelectfileAddr);
		jp1.add(selectFile);
		
		jp2.add(operation);
		jp2.add(createbutton);
		jp2.add(deletebutton);
		jp2.add(copybutton);
		jp2.add(encodebutton);
		jp3.add(decodebutton);
		jp3.add(zipbutton);
		jp3.add(unzipbutton);
		jp3.add(showbutton);

		add(jp0);
		add(jp1);
		add(jp2);
		add(jp3);

	}
	
}