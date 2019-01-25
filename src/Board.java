import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Board extends JPanel {

	char[][] board;
	JLabel h;
	JLabel[] n;
	JLabel s;
	JLabel b;
	JLabel[][] display;
	JLabel full;
	final Dimension size = new Dimension(320, 400);
	final Dimension cell = new Dimension(20, 20);
	final Dimension nextcell = new Dimension(60, 50);
	final Dimension boardcell = new Dimension(202, 402);

	LinkedList<Block> next = new LinkedList<>();
	Block current;
	Block hold = null;
	boolean canHold = true;

	boolean isSoft = false;
	boolean land = false;
	boolean isTspin = false;
	boolean isTspinMini = false;
	boolean btb = false;

	int score = 0;
	int combo = 0;

	int framecount = 0;

	Timer t;
	TimerTask tt;

	public final int msPerFrame = 20;
	public final int framePerTick = 25;

	KeyListener l;

	public Board() {
		setLayout(new BorderLayout());
		setBackground(Color.BLACK);

		h = new JLabel();
		h.setOpaque(true);
		h.setLocation(0, 0);
		h.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		h.setSize(nextcell);
		h.setPreferredSize(nextcell);
		h.setBackground(Color.BLACK);
		h.setForeground(Color.WHITE);
		h.setFont(new Font("NanumBarunGothic", Font.PLAIN, 20));
		h.setText("<html>Hold<br>_</html>");
		add(h);

		s = new JLabel();
		s.setOpaque(true);
		s.setLocation(260, 350);
		s.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		s.setSize(nextcell);
		s.setPreferredSize(nextcell);
		s.setBackground(Color.BLACK);
		s.setForeground(Color.WHITE);
		s.setFont(new Font("NanumBarunGothic", Font.PLAIN, 15));
		add(s);

		n = new JLabel[5];
		for (int i = 0; i < 5; i++) {
			n[i] = new JLabel();
			n[i].setOpaque(true);
			n[i].setLocation(260, 50 * i);
			n[i].setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
			n[i].setSize(nextcell);
			n[i].setPreferredSize(nextcell);
			n[i].setEnabled(false);
			n[i].setBackground(Color.BLACK);
			n[i].setForeground(Color.WHITE);
			n[i].setFont(new Font("NanumBarunGothic", Font.PLAIN, 20));
			add(n[i]);
		}

		board = new char[20][10];
		display = new JLabel[20][10];

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 20; j++) {
				display[j][i] = new JLabel();
				add(display[j][i]);
				display[j][i].setOpaque(true);
				display[j][i].setBackground(Color.BLACK);
				display[j][i].setLocation(60 + 20 * i, 20 * j);
				display[j][i].setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
				display[j][i].setSize(cell);
				display[j][i].setPreferredSize(cell);
			}
		}

		b = new JLabel();
		b.setOpaque(true);
		b.setLocation(59, -1);
		b.setSize(boardcell);
		b.setPreferredSize(boardcell);
		b.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		add(b);

		full = new JLabel();
		full.setOpaque(true);
		full.setLocation(0, 0);
		full.setSize(size);
		full.setPreferredSize(size);
		full.setBackground(Color.BLACK);
		add(full);

		setSize(size);
		setPreferredSize(size);
		setOpaque(true);

		addKeyListener(l = new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					moveLeft();
					break;
				case KeyEvent.VK_RIGHT:
					moveRight();
					break;
				case KeyEvent.VK_Z:
					rotateLeft();
					break;
				case KeyEvent.VK_X:
					rotateRight();
					break;
				case KeyEvent.VK_C:
					hold();
					break;
				case KeyEvent.VK_SPACE:
					harddrop();
					break;
				case KeyEvent.VK_DOWN:
					isSoft = true;
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_DOWN:
					isSoft = false;
					break;
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

		});

		start();
	}

	public void start() {
		next.addAll(getShuffled());
		next.addAll(getShuffled());
		nextBlock();

		t = new Timer();
		t.schedule(tt = new TimerTask() {
			public void run() {
				frame();
			}
		}, 0, msPerFrame);
	}

	public void frame() {
		if (framecount == 0)
			tick();
		else if (isSoft && framecount % 5 == 0) {
			if (!checkLand()) {
				current.l.translate(0, 1);
				score += 1;
				updateScore();
				render();
				framecount = 0;
			}
		}
		framecount = (framecount + 1) % framePerTick;
	}

	public boolean checkLand() {
		boolean land = false;
		for (int i = 0; i < current.s[0].length; i++) {
			for (int j = 0; j < current.s[0][0].length; j++) {
				if (land)
					break;
				if (current.s[current.r][i][j] != 0) {
					if (current.l.y + i == 19) {
						land = true;
						break;
					}
					try {
						if (board[current.l.y + i + 1][current.l.x + j] != 0) {
							land = true;
							break;
						}
					} catch (Exception e) {
					}
				}
			}
			if (land)
				break;
		}
		return land;
	}

	public void tick() {
		boolean land = checkLand();
		if (!land) {
			current.l.translate(0, 1);
			if (isSoft) {
				score += 1;
				updateScore();
			}
			isTspin = false;
		} else {
			for (int i = 0; i < current.s[0].length; i++)
				for (int j = 0; j < current.s[0][0].length; j++)
					if (current.s[current.r][i][j] != 0)
						board[current.l.y + i][current.l.x + j] += current.s[current.r][i][j];
			nextBlock();
		}
		render();
	}

	public void nextBlock() {
		checkLine();
		canHold = true;
		if (board[0][3] != 0 || board[0][4] != 0 || board[0][5] != 0 || board[0][6] != 0) {
			t.cancel();
			System.out.println("Game Over");// FIXME
			removeKeyListener(l);
		}

		current = next.poll().clone();
		if (next.size() < 7)
			next.addAll(getShuffled());
		for (int i = 0; i < 5; i++) {
			n[i].setText((char) next.get(i).n + "");
		}

		switch (current.n) {
		case 'O':
			current.l = new Point(4, -2);
			break;
		default:
			current.l = new Point(3, -2);
		}
	}

	public void checkLine() {
		int count = 0;
		for (int i = 19; i >= 0; i--) {
			int check = 1;
			for (int j = 0; j < 10; j++) {
				check *= board[i][j];
			}
			if (check > 0) {
				count++;
			} else if (count > 0) {
				board[i + count] = Arrays.copyOf(board[i], 10);
			}
		}
		switch (count) {
		case 0:
			if (isTspin) {
				if (isTspinMini)
					score += 100;
				else
					score += 400;
				isTspin = false;
			}
			break;
		case 1:
			if (isTspin) {
				if (isTspinMini)
					score += 200 * (btb ? 1.5 : 1) + combo * 50;
				else
					score += 800 * (btb ? 1.5 : 1) + combo * 50;
				isTspin = false;
				btb = true;
			} else
				score += 100 + combo * 50;
			break;
		case 2:
			if (isTspin) {
				if (isTspinMini)
					score += 400 * (btb ? 1.5 : 1) + combo * 50;
				else
					score += 1200 * (btb ? 1.5 : 1) + combo * 50;
				btb = true;
				isTspin = false;
			} else
				score += 300 + combo * 50;
			break;
		case 3:
			if (isTspin) {
				score += 1600 * (btb ? 1.5 : 1) + combo * 50;
				isTspin = false;
				btb = true;
			} else
				score += 500 + combo * 50;
			break;
		case 4:
			score += 800 * (btb ? 1.5 : 1) + combo * 50;
			btb = true;
			break;
		}

		int sum = 0;
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 10; j++) {
				sum += board[i][j];
				if (sum != 0)
					break;
			}
			if (sum != 0)
				break;
		}
		if (sum == 0) {
			switch (count) {
			case 1:
				score += 900;
				break;
			case 2:
				score += 1200;
				break;
			case 3:
				score += 1800;
				break;
			case 4:
				score += 2700;
				break;
			}
		}
		updateScore();
		if (count > 0)
			combo++;
		else
			combo = 0;
		while (count > 0) {
			board[count] = new char[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			count--;
		}
	}

	public void hold() {
		if (!canHold)
			return;
		if (hold == null) {
			hold = current;
			nextBlock();
		} else {
			Block temp = current;
			current = hold;
			hold = temp;
			if (current.n == 'O')
				current.l = new Point(4, -1);
			else
				current.l = new Point(3, -1);
			current.r = 0;
		}
		updateHold();
		render();
		t.cancel();
		t = new Timer();
		framecount = 0;
		t.schedule(new TimerTask() {
			public void run() {
				frame();
			}
		}, msPerFrame, msPerFrame);
		canHold = false;
	}

	public void moveLeft() {
		boolean available = true;
		for (int i = 0; i < current.s[0].length; i++) {
			for (int j = 0; j < current.s[0][0].length; j++) {
				if (current.s[current.r][i][j] != 0) {
					if (current.l.x + j - 1 < 0) {
						available = false;
						break;
					}
					try {
						if (board[current.l.y + i][current.l.x + j - 1] != 0) {
							available = false;
							break;
						}
					} catch (Exception e) {
					}
				}
			}
			if (!available)
				break;
		}
		if (available) {
			current.l.translate(-1, 0);
			render();
		}
	}

	public void moveRight() {
		boolean available = true;
		for (int i = 0; i < current.s[0].length; i++) {
			for (int j = 0; j < current.s[0][0].length; j++) {
				if (current.s[current.r][i][j] != 0) {
					if (current.l.x + j + 1 > 9) {
						available = false;
						break;
					}
					try {
						if (board[current.l.y + i][current.l.x + j + 1] != 0) {
							available = false;
							break;
						}
					} catch (Exception e) {
					}
				}
			}
			if (!available)
				break;
		}
		if (available) {
			current.l.translate(1, 0);
			render();
		}
	}

	public void rotateLeft() {
		if (current.n == 'O')
			return;
		boolean available = true;
		byte newr = (byte) ((current.r + 3) % 4);
		for (int i = 0; i < 5; i++) {
			available = true;
			Point p = current.w[current.r + 4][i];
			for (int j = 0; j < current.s[0].length; j++) {
				for (int k = 0; k < current.s[0][0].length; k++) {
					if (current.s[newr][j][k] != 0) {
						if (current.l.y + p.y + j > 19 || current.l.x + p.x + k < 0 || current.l.x + p.x + k > 9) {
							available = false;
							break;
						}
						try {
							if (board[current.l.y + p.y + j][current.l.x + p.x + k] != 0) {
								available = false;
								break;
							}
						} catch (Exception e) {
						}
					}
				}
				if (!available)
					break;
			}
			if (available) {
				current.l.translate(p.x, p.y);
				break;
			}
		}
		if (available) {
			if (current.n == 'T') {
				int sum = 0;
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < 2; j++) {
						try {
							sum += (board[current.l.y + 2 * j][current.l.x + 2 * i] != 0 ? 1 : 0);
						} catch (Exception e) {
						}
					}
				}
				if (sum >= 3) {
					isTspin = true;
					isTspinMini = current.r == 0;
				}
			}
			current.r = newr;
			render();
		}
	}

	public void rotateRight() {
		if (current.n == 'O')
			return;
		boolean available = true;
		byte newr = (byte) ((current.r + 1) % 4);
		for (int i = 0; i < 5; i++) {
			available = true;
			Point p = current.w[current.r][i];
			for (int j = 0; j < current.s[0].length; j++) {
				for (int k = 0; k < current.s[0][0].length; k++) {
					if (current.s[newr][j][k] != 0) {
						if (current.l.y + p.y + j > 19 || current.l.x + p.x + k < 0 || current.l.x + p.x + k > 9) {
							available = false;
							break;
						}
						try {
							if (board[current.l.y + p.y + j][current.l.x + p.x + k] != 0) {
								available = false;
								break;
							}
						} catch (Exception e) {
						}
					}
				}
				if (!available)
					break;
			}
			if (available) {
				current.l.translate(p.x, p.y);
				break;
			}
		}
		if (available) {
			if (current.n == 'T') {
				int sum = 0;
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < 2; j++) {
						try {
							sum += (board[current.l.y + 2 * j][current.l.x + 2 * i] != 0 ? 1 : 0);
						} catch (Exception e) {
						}
					}
				}
				if (sum >= 3) {
					isTspin = true;
					isTspinMini = current.r == 0;
				}
			}
			current.r = newr;
			render();
		}
	}

	public void harddrop() {
		boolean land = false;
		int y = 0;
		while (!land) {
			for (int i = 0; i < current.s[0].length; i++) {
				for (int j = 0; j < current.s[0][0].length; j++) {
					if (land)
						break;
					if (current.s[current.r][i][j] != 0) {
						if (current.l.y + i + y == 19) {
							land = true;
							break;
						}
						try {
							if (board[current.l.y + i + y + 1][current.l.x + j] != 0) {
								land = true;
								break;
							}
						} catch (Exception e) {
						}
					}
				}
				if (land)
					break;
			}
			y++;
		}
		y--;
		for (int i = 0; i < current.s[0].length; i++)
			for (int j = 0; j < current.s[0][0].length; j++)
				if (current.s[current.r][i][j] != 0)
					board[current.l.y + i + y][current.l.x + j] += current.s[current.r][i][j];
		nextBlock();
		score += 2 * y;
		render();

		t.cancel();
		t = new Timer();
		framecount = 0;
		t.schedule(new TimerTask() {
			public void run() {
				frame();
			}
		}, msPerFrame, msPerFrame);
	}

	public List<Block> getShuffled() {
		Collections.shuffle(Block.set);
		return Block.set;
	}

	public synchronized void render() {
		int x = current.l.x;
		int y = current.l.y;
		for (int i = 0; i < current.s[0].length; i++) {
			for (int j = 0; j < current.s[0][0].length; j++) {
				try {
					board[y + i][x + j] += current.s[current.r][i][j];
				} catch (Exception e) {
				}
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 20; j++) {
				switch (board[j][i]) {
				case 0:
					display[j][i].setBackground(Color.BLACK);
					break;
				case 1:
					display[j][i].setBackground(Color.CYAN);
					break;
				case 2:
					display[j][i].setBackground(Color.BLUE);
					break;
				case 3:
					display[j][i].setBackground(Color.ORANGE);
					break;
				case 4:
					display[j][i].setBackground(Color.YELLOW);
					break;
				case 5:
					display[j][i].setBackground(Color.GREEN);
					break;
				case 6:
					display[j][i].setBackground(Color.MAGENTA.darker());
					break;
				case 7:
					display[j][i].setBackground(Color.RED);
					break;
				}
			}
		}
		setBackground(Color.BLACK);
		repaint();
		for (int i = 0; i < current.s[0].length; i++) {
			for (int j = 0; j < current.s[0][0].length; j++) {
				try {
					board[y + i][x + j] -= current.s[current.r][i][j];
				} catch (Exception e) {
				}
			}
		}
	}

	public void updateHold() {
		h.setText("<html><body><div align=center>Hold<br>" + (char) hold.n + "</div></body></html>");
	}

	public void updateScore() {
		s.setText("<html>Score<br>" + score + "</html>");
	}

}
