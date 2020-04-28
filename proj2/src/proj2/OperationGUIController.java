package proj2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import proj2.FileOperation; // 自己写的
import proj2.fileOperationGUI;


public class OperationGUIController {
	private fileOperationGUI gui = new fileOperationGUI();
	private FileOperation fop = new FileOperation();
	
	class FileTree extends JFrame{
		/** 展示树
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JTree tree;
		private JPanel jp;
		
		public FileTree(File f){
			this.setVisible(true); //显示窗体
			this.setSize(500, 400);
			jp = new JPanel();
			setContentPane(jp);
			JScrollPane jsp = new JScrollPane();

			File[] flist = f.listFiles();
			 DefaultMutableTreeNode rt = new  DefaultMutableTreeNode(); //根节点
			if(f!=null) {
				for(File subf:flist) { 
					rt.add(new DefaultMutableTreeNode(subf)); //全部加入
				}
			}
			tree = new JTree(rt); //加入树
			tree.setSize(500, 400);
			 tree.addTreeSelectionListener(new TreeSelectionListener() {
		            public void valueChanged(TreeSelectionEvent e) {
		                trvalueChanged(e); //获取到了被点击的节点，展开其内容
		            }
		        });
			 jsp.setViewportView(tree);
	       add(jsp);
		}
		
		 void trvalueChanged(TreeSelectionEvent e){
			 DefaultMutableTreeNode selectednode = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
			 File selectedfile = (File)selectednode.getUserObject(); //转成文件
			 if(selectedfile.isDirectory()) {
				 File[] files = selectedfile.listFiles(new FileFilter() {
					 
		                @Override
		                public boolean accept(File pathname) {// 过滤掉隐藏类型文件
		                    if (pathname.isHidden()) {
		                        return false;
		                    } else {
		                        return true;
		                    }
		                }
		            });
				 for(File ff:files)
					 selectednode.add(new DefaultMutableTreeNode(ff));
			 }
			 else return;
		 }
	}
	
	
	OperationGUIController(){
		gui.setVisible(true);
		gui.selectFile.addActionListener(new ActionListener() {
			//点击浏览时选择文件的效果
			@Override
			public void actionPerformed(ActionEvent e) {
				 //打开文件选择
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				JLabel choose = new JLabel();
				chooser.showDialog(choose, "选择");
				File file = chooser.getSelectedFile(); //选中被操作的文件夹/文件位置
				gui.SelectfileAddr.setText(file.getAbsolutePath());
			}
		});
		
		gui.createbutton.addActionListener(new ActionListener() {
			//点击新建时候的效果
			@Override
			public void actionPerformed(ActionEvent e) {
				String src = gui.SelectfileAddr.getText();
				File createResult = fop.createDir(src);
				if(createResult != null)
					JOptionPane.showMessageDialog(null, "成功创建！"+src);
			}
		});
		
		gui.deletebutton.addActionListener(new ActionListener() {
			//点击删除的效果
			@Override
			public void actionPerformed(ActionEvent e) {
				String src = gui.SelectfileAddr.getText();
				File deletetarget = new File(src);
				fop.deleteDir(deletetarget);
				JOptionPane.showMessageDialog(null, "成功删除："+src);
			}
		});
		
		gui.copybutton.addActionListener(new ActionListener() {
			//复制文件、文件夹
			@Override
			public void actionPerformed(ActionEvent e) {
				
				String src = gui.SelectfileAddr.getText();
				String file=JOptionPane.showInputDialog
						 (gui, "位置：","复制结果文件放在：",JOptionPane.PLAIN_MESSAGE);
				File tgtFile = new File(file);
				File srcFile = new File(src);
				if(srcFile.isDirectory()) {
					try {
						fop.dirCopy(tgtFile.getAbsolutePath(),src);
						JOptionPane.showMessageDialog(null, "复制成功！");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "文件夹复制失败！");
					}
				}
				else { //文件复制
					try {
						fop.fileCopy(tgtFile.getAbsolutePath(),src);
						JOptionPane.showMessageDialog(null, "复制成功！");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "文件复制失败！");
					}
					
				}
			}
		
		});
		
		gui.encodebutton.addActionListener(new ActionListener() {
			//点击加密，加密操作
			@Override
			public void actionPerformed(ActionEvent e) {
//				JOptionPane jptest = new JOptionPane();
				JPasswordField pf = new JPasswordField();
				JOptionPane.showMessageDialog(null, pf, "请输入密码：", JOptionPane.PLAIN_MESSAGE);
				String str = pf.getText();
				if(str.equals("123456")) {//人为设置的密码
					 String dstAddr=JOptionPane.showInputDialog
							 (gui,"位置：","加密文件位置",JOptionPane.PLAIN_MESSAGE);
					 File src = new File(gui.SelectfileAddr.getText());
					 try {
						 fop.EncFile(src, dstAddr);
						JOptionPane.showMessageDialog(null, "加密成功！");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "加密失败！文件未找到或选中的不是文件！");
					}
				}
				else 
					JOptionPane.showMessageDialog(null, "密码错误！");
					
			}
			
		});
		
		gui.decodebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPasswordField pf = new JPasswordField();
				JOptionPane.showMessageDialog(null, pf, "请输入密码：", JOptionPane.PLAIN_MESSAGE);
				String str = pf.getText();
				if(str.equals("123456")) {//人为设置的密码
					 String dstAddr=JOptionPane.showInputDialog
							 (gui,"位置：","解密文件位置",JOptionPane.PLAIN_MESSAGE);
					 File src = new File(gui.SelectfileAddr.getText());
					 try {
						 fop.DecFile(src, dstAddr);
						 JOptionPane.showMessageDialog(null, "解密成功！");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "加密失败！文件未找到或选中的不是文件！");
					}
				}
				else 
					JOptionPane.showMessageDialog(null, "密码错误！");
					
			}
		});
		
		
		gui.zipbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 String zipFile=JOptionPane.showInputDialog
						 (gui, "位置：","压缩文件放在：",JOptionPane.PLAIN_MESSAGE);

				 try {
					// (String inputFile, String outputFile)
					 fop.Compress(gui.SelectfileAddr.getText(), zipFile);
					 JOptionPane.showMessageDialog(null, "压缩成功！");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "文件压缩失败！");
				}
			}
		});
		
		gui.unzipbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 String zipFile=JOptionPane.showInputDialog
						 (gui,"位置：","解压文件放在：",JOptionPane.PLAIN_MESSAGE);
				 try {
					 fop.decompress(gui.SelectfileAddr.getText(), zipFile);
					 JOptionPane.showMessageDialog(null, "解压成功！");
					 
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "文件解压失败！");
				}
			}
		});
		
		
		gui.showbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File f = new File(gui.SelectfileAddr.getText());
				new FileTree(f);
				//JOptionPane.showMessageDialog(ft, "test");
				
			}
		});
		
	}
	
	
	
	public static void main(String[] args) {
		 OperationGUIController fogui = new OperationGUIController();
	}
}
