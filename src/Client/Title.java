package Client;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Title extends JPanel {

	FadeLabel back;
	final Dimension size = new Dimension(400, 480);

	Main m;

	KeyListener l;

	public BufferedImage back1, back2;

	long startTime;// Loading time

	public Title(Main m) {
		this.m = m;

		setLayout(new BorderLayout());
		setOpaque(true);

		addKeyListener(l = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});

		try {
			back1 = ImageIO.read(new File(Main.fPrefix + "back1.png"));
			back2 = ImageIO.read(new File(Main.fPrefix + "back2.png"));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		back = new FadeLabel(back1, back2);
		back.setOpaque(true);
		back.setLocation(0, 0);
		back.setSize(size);
		back.setPreferredSize(size);
		add(back);
	}

}

class FadeLabel extends JLabel {
	private BufferedImage back;
	private BufferedImage cover;

	private long startTime;

	private Timer t;

	private double alpha;

	public FadeLabel(BufferedImage b, BufferedImage c) {
		back = b;
		cover = c;
		startTime = System.currentTimeMillis();

		t = new Timer(20, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				alpha = (Math.sin((System.currentTimeMillis() - startTime) / 4000.0 * Math.PI) + 1) / 2;

				repaint();
			}
		});
		t.start();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setComposite(AlphaComposite.SrcOver.derive((float) alpha));
		g2.drawImage(back, 0, 0, this);

		g2.setComposite(AlphaComposite.SrcOver.derive(1 - (float) alpha));
		g2.drawImage(cover, 0, 0, this);
		g2.dispose();
	}
}
