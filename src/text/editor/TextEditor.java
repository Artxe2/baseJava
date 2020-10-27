package text.editor;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class TextEditor extends JFrame implements ActionListener, KeyListener, UndoableEditListener {

	private static final String title = "TextEditor v0.3";
	private static final JTextArea jta = new JTextArea();
	private static final JScrollPane jsp = new JScrollPane(jta);
	private static final JMenuBar jmb = new JMenuBar();
	private static final JMenu jm_file = new JMenu("File");
	private static final JMenuItem jmi_new = new JMenuItem("new");
	private static final JMenuItem jmi_open = new JMenuItem("open");
	private static final JMenuItem jmi_save = new JMenuItem("save");
	private static final JMenuItem jmi_saveAs = new JMenuItem("saveAs");
	private static final JLabel jlb = new JLabel(" 0 characters");
	private static final JFileChooser jfc = new JFileChooser(new File("").getAbsolutePath());
	private static File currentFile;
	private static final UndoManager udm = new UndoManager();
	private static final Font font = new Font("맑은 고딕", 1, 20);

	public TextEditor() {
		initDisplay();
	}

	public void initDisplay() {
		setTitle(title);
		setSize(640, 720);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setJMenuBar(jmb);
		jmb.add(jm_file);
		jm_file.add(jmi_new).addActionListener(this);
		jm_file.add(jmi_open).addActionListener(this);
		jm_file.add(jmi_save).addActionListener(this);
		jm_file.add(jmi_saveAs).addActionListener(this);
		add("North", jlb).setFont(font);
		add("Center", jsp);
		jta.setFont(font);
		jta.setLineWrap(true);
		jta.addKeyListener(this);
		jta.getDocument().addUndoableEditListener(this);
		setVisible(true);
	}

	public void newFile() {
		jfc.showSaveDialog(null);
		if (jfc.getSelectedFile() != null) {
			currentFile = jfc.getSelectedFile();
			if (currentFile.getName().indexOf('.') < 0) {
				currentFile = new File(currentFile.getAbsolutePath() + ".txt");
			}
			jta.setText("");
			try {
				currentFile.createNewFile();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "File create failure", "Error", 0);
			}
			setTitle(title + " - " + currentFile.getName());
		}
	}

	public void openFile() {
		jfc.showOpenDialog(null);
		if (jfc.getSelectedFile() != null) {
			setTitle(title + " - " + currentFile.getName());
			try {
				FileInputStream fis = new FileInputStream(currentFile);
				StringWriter sw = new StringWriter();
				int i;
				while ((i = fis.read()) > -1) sw.write(i);
				jta.setText(sw.toString());
				fis.close();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "File open failure", "Error", 0);
			}
			currentFile = jfc.getSelectedFile();
		}
	}

	public void saveFile() {
		if (currentFile != null) {
			try {
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(currentFile));
				bos.write(jta.getText().getBytes());
				bos.close();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "File save failure", "Error", 0);
			}
		} else {
			saveAsFile();
		}
	}

	public void saveAsFile() {
		jfc.showSaveDialog(null);
		if (jfc.getSelectedFile() != null) {
			currentFile = jfc.getSelectedFile();
			if (currentFile.getName().indexOf('.') < 0) {
				currentFile = new File(currentFile.getAbsolutePath() + ".txt");
			}
			try {
				currentFile.createNewFile();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "File create failure", "Error", 0);
			}
			setTitle(title + " - " + currentFile.getName());
			saveFile();
		}
	}

	public static void main(String[] args) {
		setDefaultLookAndFeelDecorated(true);
		new TextEditor();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object act = e.getSource();
		if (act.equals(jmi_new)) {
			newFile();
		} else if (act.equals(jmi_open)) {
			openFile();
		} else if (act.equals(jmi_save)) {
			saveFile();
		} else if (act.equals(jmi_saveAs)) {
			saveAsFile();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()) {
			try {
				udm.undo();
			} catch (CannotUndoException c) {}
		} else if (e.getKeyCode() == KeyEvent.VK_Y && e.isControlDown()) {
			try {
				udm.redo();
			} catch (CannotRedoException c) {}
		} else if (e.getKeyCode() == KeyEvent.VK_S && e.isControlDown()) {
			saveFile();
		} else if (e.getKeyCode() == KeyEvent.VK_N && e.isControlDown()) {
			newFile();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		jlb.setText(" " + jta.getText().length() + " characters");
	}

	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		udm.addEdit(e.getEdit());
	}
}
