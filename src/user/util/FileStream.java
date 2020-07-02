package user.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;

public class FileStream {

	FileStream() {

	}

	public static void main(String[] args) {
		JFileChooser jfc = new JFileChooser();
		jfc.showOpenDialog(null);
		File f = jfc.getSelectedFile();
		System.out.println(f);
		String[] sa = f.toString().split("\\.");
		File f2 = new File(sa[0] + "2." + sa[1]);
		System.out.println(f2);

		FileOutputStream fos = null;
		FileInputStream fis = null;
		BufferedOutputStream bos = null;
		BufferedReader br = null;
		BufferedInputStream bis = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;

		try {
			fos = new FileOutputStream(f2);
			f2 = new File(sa[0] + "3." + sa[1]);
			fis = new FileInputStream(f);
			// bos = new BufferedOutputStream(fos);
			bis = new BufferedInputStream(fis);
			// br = new BufferedReader(new InputStreamReader(fis));
			bos = new BufferedOutputStream(fos);
//			oos = new ObjectOutputStream(fos);
			// ois = new ObjectInputStream(fis);
			int i = -1;
			Object o = null;
			while ((i = bis.read()) > -1) {
				fos.write(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null)
					bos.close();
				if (br != null)
					br.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
			}
		}
	}
}