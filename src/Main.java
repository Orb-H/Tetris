import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main extends JFrame {

	public Main() {
		AI ai = new AI(AI.AIMode.INFINITY, 100);
		setContentPane(ai.b);
		setVisible(true);
		setTitle("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(700, 300);
		pack();
		ai.b.requestFocus();

		ai.start();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Main();
			}
		});
	}

}
