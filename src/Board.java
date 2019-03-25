import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
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
import javax.swing.SwingConstants;

public class Board extends JPanel {

	byte[][] board;// board array
	JLabel h;// hold
	JLabel[] n;// next
	JLabel s;// score
	JLabel b;// board border
	JLabel c;// cover
	JLabel[][] display;// Display cell
	JLabel full;
	final Dimension size = new Dimension(320, 400);// size of window
	final Dimension cell = new Dimension(20, 20);// size of cell
	final Dimension nextcell = new Dimension(60, 50);// size of next block holder
	final Dimension boardcell = new Dimension(202, 400);// size of board

	LinkedList<Block> next = new LinkedList<>();// next block list
	Block current;// current dropping block
	Block hold = null;// hold block
	boolean canHold = true;// can player hold

	String[] toStart = { "Press", "Enter", "to", "start", "game" };

	boolean isSoft = false;// is player doing soft-drop
	boolean land = false;// is block landed
	boolean isTspin = false;// is T-spin
	boolean isTspinMini = false;// is T-Spin mini
	boolean btb = false;// is back-to-back

	int score = 0;// score
	int combo = 0;// combo
	int blockcount = 0;// blocks placed
	int linecount = 0;// lines cleared

	int framecount = 0;// counter for frame
	boolean start = false;// is playing
	boolean pause = false;// is paused
	boolean shadow = false;// is shadow displaying

	Timer t;
	TimerTask tt;

	public final int msPerFrame = 20;// milliseconds per frame
	public final int framePerTick = 25;// frames per tick

	KeyListener l;

	public Board() {
		setLayout(new BorderLayout());
		setBackground(Color.BLACK);

		c = new JLabel("Paused", SwingConstants.CENTER);
		c.setOpaque(true);
		c.setLocation(59, 0);
		c.setSize(boardcell);
		c.setPreferredSize(boardcell);
		c.setBackground(Color.DARK_GRAY);
		c.setFont(new Font("NanumBarunGothic", Font.PLAIN, 25));
		c.setForeground(Color.WHITE);
		c.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		c.setVisible(false);
		add(c);

		h = new JLabel("<html>Hold<br></html>", SwingConstants.CENTER);
		h.setOpaque(true);
		h.setLocation(0, 0);
		h.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		h.setSize(nextcell);
		h.setPreferredSize(nextcell);
		h.setBackground(Color.BLACK);
		h.setForeground(Color.WHITE);
		h.setFont(new Font("NanumBarunGothic", Font.PLAIN, 20));
		add(h);

		s = new JLabel("", SwingConstants.CENTER);
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
			n[i] = new JLabel(toStart[i], SwingConstants.CENTER);
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

		board = new byte[20][10];
		display = new JLabel[20][10];

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 20; j++) {
				display[j][i] = new JLabel();
				add(display[j][i]);
				display[j][i].setOpaque(true);
				display[j][i].setBackground(Color.BLACK);
				display[j][i].setLocation(60 + 20 * i, 20 * j);
				display[j][i].setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
				display[j][i].setSize(cell);
				display[j][i].setPreferredSize(cell);
			}
		}

		b = new JLabel();
		b.setOpaque(true);
		b.setLocation(59, 0);
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
				if (e.getKeyCode() == KeyEvent.VK_ENTER && !start) {
					start();
					start = true;
				} else if (start) {
					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						pause();
					}
					if (!pause) {
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
						case KeyEvent.VK_UP:
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
						case KeyEvent.VK_S:
							shadow = !shadow;
							break;
						}
					}
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
	}

	// game start
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

	// game pause
	public void pause() {
		if (pause) {
			t = new Timer();
			t.schedule(tt = new TimerTask() {
				public void run() {
					frame();
				}
			}, 0, msPerFrame);
			c.setVisible(false);
		} else {
			t.cancel();
			c.setVisible(true);
		}
		pause = !pause;
	}

	// invoked every frame
	public void frame() {
		if (land && framecount % (1000 / msPerFrame) == 0) {
			land();
		} else if (framecount % framePerTick == 0)
			tick();
		else if (isSoft && framecount % 5 == 0) {
			if (!(land = checkLand())) {
				current.l.translate(0, 1);
				score += 1;
				updateScore();
				render();
				framecount = 0;
			}
		}
		framecount = (framecount + 1) % 10000;
	}

	// check if block is landed
	public boolean checkLand() {
		boolean landtemp = false;
		for (int i = 0; i < current.s[0].length; i++) {
			for (int j = 0; j < current.s[0][0].length; j++) {
				if (landtemp)
					break;
				if (current.s[current.r][i][j] != 0) {
					if (current.l.y + i == 19) {
						landtemp = true;
						break;
					}
					try {
						if (board[current.l.y + i + 1][current.l.x + j] != 0
								&& board[current.l.y + i + 1][current.l.x + j] < 8) {
							landtemp = true;
							break;
						}
					} catch (Exception e) {
					}
				}
			}
			if (landtemp)
				break;
		}
		return landtemp;
	}

	// check if block b is at location (x, y) and rotation r
	public boolean checkLand(Block b, int x, int y, int r) {
		boolean landtemp = false;
		for (int i = 0; i < b.s[0].length; i++) {
			for (int j = 0; j < b.s[0][0].length; j++) {
				if (landtemp)
					break;
				if (b.s[r][i][j] != 0) {
					if (y + i == 19) {
						landtemp = true;
						break;
					}
					try {
						if (board[y + i + 1][x + j] != 0 && board[y + i + 1][x + j] < 8) {
							landtemp = true;
							break;
						}
					} catch (Exception e) {
					}
				}
			}
			if (landtemp)
				break;
		}
		return landtemp;
	}

	// set block as landed and call next block
	public void land() {
		for (int i = 0; i < current.s[0].length; i++)
			for (int j = 0; j < current.s[0][0].length; j++)
				if (current.s[current.r][i][j] != 0)
					board[current.l.y + i][current.l.x + j] += current.s[current.r][i][j];
		nextBlock();
	}

	// invoked every tick
	public void tick() {
		land = checkLand();
		if (!land) {
			current.l.translate(0, 1);
			if (isSoft) {
				score += 1;
				updateScore();
			}
			isTspin = false;
		}
		render();
	}

	// grab next block and fall
	public void nextBlock() {
		checkLine();
		canHold = true;
		if (board[0][3] != 0 || board[0][4] != 0 || board[0][5] != 0 || board[0][6] != 0) {
			t.cancel();
			System.out.println("Game Over");// FIXME
			removeKeyListener(l);
		}
		blockcount++;

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

	// check if line clear exists
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
				board[i] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
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
		linecount += count;

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
		if (count > 0) {
			combo++;
			Toolkit.getDefaultToolkit().beep();
		} else
			combo = 0;
		while (count > 0) {
			board[count] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
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
		framecount = 1;
		canHold = false;
	}

	// <-
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

	// ->
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
			if (land) {
				land = checkLand();
				if (!land)
					framecount = 1;
			}
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
			if (land) {
				land = checkLand();
				if (!land)
					framecount = 1;
			}
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
		current.l.translate(0, y);
		land();
		score += 2 * y;
		render();

		framecount = 1;
	}

	// get one bag and shuffle
	public List<Block> getShuffled() {
		Collections.shuffle(Block.set);
		return Block.set;
	}

	public synchronized void render() {
		int x = current.l.x;
		int y = current.l.y;
		int sy = y;
		if (shadow) {
			for (; sy < 20; sy++) {
				if (checkLand(current, x, sy, current.r)) {
					for (int i = 0; i < current.s[0].length; i++) {
						for (int j = 0; j < current.s[0][0].length; j++) {
							try {
								if (current.s[current.r][i][j] != 0)
									board[sy + i][x + j] += current.s[current.r][i][j] + 8;
							} catch (Exception e) {
							}
						}
					}
					break;
				}
			}
		}
		for (int i = 0; i < current.s[0].length; i++) {
			for (int j = 0; j < current.s[0][0].length; j++) {
				try {
					if (current.s[current.r][i][j] != 0)
						board[y + i][x + j] = current.s[current.r][i][j];
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
				case 9:
					display[j][i].setBackground(Color.CYAN.darker());
					break;
				case 10:
					display[j][i].setBackground(Color.BLUE.darker());
					break;
				case 11:
					display[j][i].setBackground(Color.ORANGE.darker());
					break;
				case 12:
					display[j][i].setBackground(Color.YELLOW.darker());
					break;
				case 13:
					display[j][i].setBackground(Color.GREEN.darker());
					break;
				case 14:
					display[j][i].setBackground(Color.MAGENTA.darker().darker());
					break;
				case 15:
					display[j][i].setBackground(Color.RED.darker());
					break;
				}
			}
		}
		setBackground(Color.BLACK);
		repaint();
		for (int i = 0; i < current.s[0].length; i++) {
			for (int j = 0; j < current.s[0][0].length; j++) {
				try {
					if (current.s[current.r][i][j] != 0)
						board[y + i][x + j] = 0;
				} catch (Exception e) {
				}
			}
		}
		if (shadow) {
			for (int i = 0; i < current.s[0].length; i++) {
				for (int j = 0; j < current.s[0][0].length; j++) {
					try {
						if (current.s[current.r][i][j] != 0)
							board[sy + i][x + j] = 0;
					} catch (Exception e) {
					}
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
