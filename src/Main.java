import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame {

	public Main() {
		String s = new java.io.File("img/img.png").getAbsolutePath();
		setTitle(s);
		setSize(600, 600);
		setIconImage(new ImageIcon(s).getImage());
		add(new JPanel() {
			@Override
			protected void paintComponent(java.awt.Graphics g) {
				g.drawImage(new ImageIcon(s).getImage(), 0, 0, 600, 600, null);
				setOpaque(false);
			}
		});
		setVisible(true);
	}

	public static void main(String[] args) {
		new Main();
	}
}