package Client;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main extends JFrame {

	Board b;
	Title t;

	public static final String fPrefix = "resource/";

	public Main() {
		// b = new Board(this);
		// setContentPane(b);
		t = new Title(this);
		setContentPane(t);
		setVisible(true);
		setTitle("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();

		Dimension d = getSize();
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((size.width - d.width) / 2, (size.height - d.height) / 2);

		// b.requestFocus();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Main();
			}
		});
	}

}
