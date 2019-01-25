import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main extends JFrame {

	public Main() {
		Board b = new Board();
		setContentPane(b);
		setVisible(true);
		setTitle("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(700, 300);
		pack();
		b.requestFocus();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Main();
			}
		});
	}

}
