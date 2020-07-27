package user.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class Object_File_Stream implements Serializable {

	class Data implements Serializable {
		private String name = "object";

		private void mtd() {
			System.out.println("im " + name);
		}
	}

	private static final File f = new File("D:\\a");
	private static String s;

	Object_File_Stream() throws Exception {
		oosToFile();
		oisFromFile();
		oosToString();
		oisFromString();
	}

	public static void main(String[] args) throws Exception {
		new Object_File_Stream();
	}

	public void oosToFile() throws Exception {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
		Data o = new Data();
		oos.writeObject(o);
		oos.close();
	}

	public void oisFromFile() throws Exception {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
		Data o = (Data) ois.readObject();
		o.mtd();
		ois.close();
	}

	public void oosToString() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
		Data o = new Data();
		oos.writeObject(o);
		oos.close();
		s = new String(baos.toByteArray(), "ISO-8859-1");
	}

	public void oisFromString() throws Exception {
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(s.getBytes("ISO-8859-1"))));
		Data o = (Data) ois.readObject();
		o.mtd();
		ois.close();
	}
}