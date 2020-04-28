package proj2;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileOperation {
	private static final int EnDecode = 0x99;
	
	public File createDir(String dir) {
		/** 创建文件夹
		 * @param d 是新创建的文件夹对象
		 * */
		File d=new File(dir);
		if(!d.exists()) {
			d.mkdirs();//mkdir只能创建一层目录，而mkdirs可以创建多层
			System.out.println("文件夹创建成功！");
			}
		return d;
	}
	
	public void gotoFile(File f) {
		/** 打印绝对路径
		 * */
		System.out.println(f.getAbsolutePath());
	}
	
	public void display(File folder) {
		/** 实现当前文件夹下的内容罗列
		 * 递归调用，遍历所有的内容
		 * */
		File[] f = folder.listFiles();
		if(f!=null) {
			for(File i:f) {
				if(i.isDirectory())
					display(i);
				System.out.println(i.getPath());
			}
		}
	}
	
	public void deleteDir(File folder) {
		/** 删除文件夹
		 * @param f 罗列出文件夹的内容，方便后续删除
		 */
		File[] f=folder.listFiles();
		if(f!=null) {
			for(File i:f) {
				if(i.isDirectory())
					deleteDir(i); //递归删除子文件
				else i.delete();
			}
		}
		folder.delete();
		System.out.println("文件夹删除成功！");
	}
	
	public void fileCopy(String dstP, String srcP) throws IOException {
		/** 文件的复制
		 * @param dstP 复制文件目标位置
		 * @param srcP 被复制文件位置
		 * */
		File dst=new File(dstP);
		FileInputStream fin=new FileInputStream(srcP);
		FileOutputStream fout=new FileOutputStream(dstP);
		//字节流，对文件进行复制，并写入到对应的地址内
		int size=fin.available();
		for(int i=0; i<size; i++) 
			fout.write(fin.read());
		System.out.println("文件复制完成！位置："+dst.getAbsolutePath());
		fin.close();
		fout.close();
	}
	
	public void dirCopy(String dstP, String srcP) throws IOException {
		/** 文件夹的复制
		 * @param dstP 复制文件夹目标位置
		 * @param srcP 被复制文件夹位置
		 * 由于复制文件时会打开文件，因此考虑IO异常
		 * */
		File src = new File(srcP);
		File dst = new File(dstP);
		String[] fpath = src.list();
		if(!dst.exists()) 
			dst.mkdirs();//如果目标地址为空，新建
		for(int i=0; i<fpath.length; i++) {
			if(new File(srcP+File.separator+fpath[i]).isDirectory())
				dirCopy(dstP+File.separator+fpath[i],srcP+File.separator+fpath[i]);
			if(new File(srcP+File.separator+fpath[i]).isFile())
				fileCopy(dstP+File.separator+fpath[i],srcP+File.separator+fpath[i]);
		}
	}
	
	public void EncFile(File src, String enc) throws IOException {
		/** 文件加密
		 * 加密方式选用  EnDecode = 0x99，即：
		 * 给定的加密秘钥（异或数据，可以在合法范围内随便定义）为十六进制数0x99
		 * */
		if(!src.exists()){
			System.out.println("加密失败！该文件不存在！");
			return;
		}
		File encodeFile = new File(enc);
		FileInputStream fis = new FileInputStream(src);
		FileOutputStream fos = new FileOutputStream(encodeFile);
		int data = -1;
		while((data = fis.read()) > -1) //input还有内容
			fos.write(data ^ EnDecode);
		System.out.println("加密成功！");

		fis.close(); 
		fos.flush();
		fos.close(); //关闭
	}
	
	public void DecFile(File src, String deAddr) throws IOException {
		/** 文件解密
		 * 和加密是相逆的过程，都是异或算法
		 * */
		File dec = new File(deAddr);
		
		if(!src.exists()) {
			System.out.println("解密失败！该文件不存在！");
			return;
		}
		if(!dec.exists()) {
			dec.createNewFile();
			System.out.println("生成解密文件中...");
		}
		InputStream fis = new FileInputStream(src);
		OutputStream fos = new FileOutputStream(dec);
		
		int data = 0;
		while((data = fis.read()) > -1)
			fos.write(data ^ EnDecode);
		System.out.print("解密成功！");
		gotoFile(dec);
		fis.close();
		fos.flush();
		fos.close();
	}
	
	public void Compress(String inputFile, String outputFile) throws IOException {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile));
		BufferedOutputStream bos = new BufferedOutputStream(out);
		File iptfile = new File(inputFile);
		fileZip(out, bos, iptfile, null);
		bos.close();//输出流先关上
		out.close();
	}
	
	private void fileZip(ZipOutputStream out, BufferedOutputStream bos, File iptfile, String name) throws IOException {
		/** @param name 是压缩文件的名字，可以写null用下面的getName获取
		 * 
		 * */
		if(name == null)
			name = iptfile.getName();
		if(iptfile.isDirectory()) {
			//若是文件夹
			File[] f = iptfile.listFiles(); 
			if(f.length == 0)  //如果文件夹中没有内容，在目的zip文件中只有一个入口
				out.putNextEntry(new ZipEntry(name + File.separator));
			else {
				for(File subfile:f) 
					fileZip(out, bos, subfile, name + File.separator + subfile.getName());
			}
		}
		else { //如果是文件
			out.putNextEntry(new ZipEntry(name));
			FileInputStream fis = new FileInputStream(iptfile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			int len = -1;
			byte[] buffer = new byte[1024];
			while((len = bis.read(buffer)) != -1) {
				//将读取到的给len，如果len不为-1说明还有数据
				bos.write(buffer, 0, len); //控制为len可以保证不输出多了
			}
			bis.close();
			fis.close();
		}
	}
	
	@SuppressWarnings("resource")
	public void decompress(String tgtFile,String dstFile) throws IOException {
		/** 对文件做解压
		 * @param tgtFile 是需要解压的文件的路径
		 * @param dstFile 是解压目的地址
		 * */
		File source = new File(tgtFile);
		
		ZipInputStream zis = new ZipInputStream(new FileInputStream(source));
		ZipEntry entry = null;
		
		File file = null;
		
		while((entry = zis.getNextEntry()) != null) { //如果还能获取
			if(!entry.isDirectory()) {
				file = new File(dstFile, entry.getName());
				if(!file.exists()) { //文件不存在，创建上级目录
					new File(file.getParent()).mkdirs(); 
				}
				OutputStream ops = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(ops);
				int len = -1;
				byte[] buf = new byte[1024];
				while((len = zis.read(buf)) != -1) 
					bos.write(buf, 0, len); //避免多写！！
				
				bos.close();
				ops.close();
			}
		}
	}
	
	public static void main(String[] args) throws IOException { 
		FileOperation fo = new FileOperation();
		
	}
//	public static void main(String[] args) throws IOException {
//		/** 测试部分
//		 * fo是测试类
//		 * */
//		FileOperation fo = new FileOperation();
//		File target = new File("src/t1");
//		String zipFile = new String("src/rst.zip");
//		fo.compress(zipFile, target);
//		fo.decompress(zipFile, "src/rtt");
////		fo.decompress("src/result.zip", "src/");
////		fo.gotoFile(f);
////		fo.deleteDir(f);
////		String srcP="src/t1/t2/t3/timg.jpeg";
////		String src="src/t1/t2";
////		String dstP="src/Morty.png";
////		String dst="src/sss";
////		fo.fileCopy(dstP, srcP);
////		fo.dirCopy(dst, src);
////		File fe = new File("src/f.txt");
////		FileWriter fr = new FileWriter(fe);
////		fr.write("我爱Java！");//写文件
////		fr.close();
////		
////		File encodeResult = new File("src/encodeResult.txt");
////		File decodeResult = new File("src/decodeResult.txt");
////		fo.EncFile(fe, encodeResult);
////		fo.DecFile(encodeResult, decodeResult);
//	}
}
