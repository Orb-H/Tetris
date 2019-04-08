import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.BooleanControl.Type;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Board extends JPanel {

	Main m;

	byte[][] board;
	JLabel h;// hold
	JLabel[] n;// next
	JLabel s;// score
	JLabel v;// level
	JLabel b;// board border
	JLabel c;// cover
	JLabel[][] display;
	JLabel full;
	final Dimension size = new Dimension(320, 400);
	final Dimension cell = new Dimension(20, 20);
	final Dimension nextcell = new Dimension(60, 50);
	final Dimension boardcell = new Dimension(202, 400);

	LinkedList<Block> next = new LinkedList<>();
	Block current;
	Block hold = null;
	boolean canHold = true;

	boolean isSoft = false;
	boolean land = false;
	boolean isTspin = false;
	boolean isTspinMini = false;
	boolean btb = false;

	int level = 0;
	int line = 0;
	int denormLine = 0;
	int score = 0;
	int combo = 0;
	int blockcount = 0;
	int linecount = 0;

	int framecount = 0;
	boolean start = false;
	boolean pause = false;
	boolean shadow = true;

	Timer t;
	TimerTask tt;

	public final int msPerFrame = 20;
	public int framePerTick = 25;

	KeyListener l;

	public final ImageIcon blocks[] = new ImageIcon[16];
	public final HashMap<Character, ImageIcon> entire = new HashMap<>();

	public File sMove, sRotate, sDrop, sLine, sTetris, sBack;
	public Clip cBack;

	public final String fPrefix = "resource/";

	public Board(Main m) {
		this.m = m;

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

		h = new JLabel();
		h.setLocation(0, 0);
		h.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		h.setSize(nextcell);
		h.setPreferredSize(nextcell);
		h.setBackground(Color.BLACK);
		add(h);

		s = new JLabel("<html>Score<br>0</html>", SwingConstants.CENTER);
		s.setOpaque(true);
		s.setLocation(260, 350);
		s.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		s.setSize(nextcell);
		s.setPreferredSize(nextcell);
		s.setBackground(Color.BLACK);
		s.setForeground(Color.WHITE);
		s.setFont(new Font("NanumBarunGothic", Font.PLAIN, 15));
		add(s);

		v = new JLabel("<html>Level<br>0</html>", SwingConstants.CENTER);
		v.setOpaque(true);
		v.setLocation(260, 300);
		v.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		v.setSize(nextcell);
		v.setPreferredSize(nextcell);
		v.setBackground(Color.BLACK);
		v.setForeground(Color.WHITE);
		v.setFont(new Font("NanumBarunGothic", Font.PLAIN, 15));
		add(v);

		n = new JLabel[5];
		for (int i = 0; i < 5; i++) {
			n[i] = new JLabel();
			n[i].setOpaque(true);
			n[i].setLocation(260, 50 * i);
			n[i].setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
			n[i].setSize(nextcell);
			n[i].setPreferredSize(nextcell);
			n[i].setBackground(Color.BLACK);
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
				// display[j][i].setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
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
						case KeyEvent.VK_M:
							muteSoundBack();
							break;
						}
					} else {
						if (e.getKeyCode() == KeyEvent.VK_R) {
							restart();
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

		try {
			blocks[0] = null;
			blocks[1] = new ImageIcon(ImageIO.read(new File(fPrefix + "I.png")));
			blocks[2] = new ImageIcon(ImageIO.read(new File(fPrefix + "J.png")));
			blocks[3] = new ImageIcon(ImageIO.read(new File(fPrefix + "L.png")));
			blocks[4] = new ImageIcon(ImageIO.read(new File(fPrefix + "O.png")));
			blocks[5] = new ImageIcon(ImageIO.read(new File(fPrefix + "S.png")));
			blocks[6] = new ImageIcon(ImageIO.read(new File(fPrefix + "T.png")));
			blocks[7] = new ImageIcon(ImageIO.read(new File(fPrefix + "Z.png")));
			blocks[8] = null;
			blocks[9] = new ImageIcon(ImageIO.read(new File(fPrefix + "Ib.png")));
			blocks[10] = new ImageIcon(ImageIO.read(new File(fPrefix + "Jb.png")));
			blocks[11] = new ImageIcon(ImageIO.read(new File(fPrefix + "Lb.png")));
			blocks[12] = new ImageIcon(ImageIO.read(new File(fPrefix + "Ob.png")));
			blocks[13] = new ImageIcon(ImageIO.read(new File(fPrefix + "Sb.png")));
			blocks[14] = new ImageIcon(ImageIO.read(new File(fPrefix + "Tb.png")));
			blocks[15] = new ImageIcon(ImageIO.read(new File(fPrefix + "Zb.png")));

			entire.put('I', new ImageIcon(ImageIO.read(new File(fPrefix + "In.png"))));
			entire.put('J', new ImageIcon(ImageIO.read(new File(fPrefix + "Jn.png"))));
			entire.put('L', new ImageIcon(ImageIO.read(new File(fPrefix + "Ln.png"))));
			entire.put('O', new ImageIcon(ImageIO.read(new File(fPrefix + "On.png"))));
			entire.put('S', new ImageIcon(ImageIO.read(new File(fPrefix + "Sn.png"))));
			entire.put('T', new ImageIcon(ImageIO.read(new File(fPrefix + "Tn.png"))));
			entire.put('Z', new ImageIcon(ImageIO.read(new File(fPrefix + "Zn.png"))));

			sMove = new File(fPrefix + "move.wav");
			sRotate = new File(fPrefix + "rotate.wav");
			sDrop = new File(fPrefix + "drop.wav");
			sLine = new File(fPrefix + "line.wav");
			sTetris = new File(fPrefix + "tetris.wav");
			sBack = new File(fPrefix + "typea.wav");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void start() {
		next.addAll(getShuffled());
		next.addAll(getShuffled());
		nextBlock();
		playSoundBack();

		t = new Timer();
		t.schedule(tt = new TimerTask() {
			public void run() {
				frame();
			}
		}, 0, msPerFrame);
	}

	public void restart() {
		cBack.close();

		m.b = new Board(m);
		m.setContentPane(m.b);
		m.b.requestFocus();
	}

	public synchronized void pause() {
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

	public synchronized void frame() {
		if (land && framecount % (1000 / msPerFrame) == 0) {
			land();
		} else if (framecount % framePerTick == 0)
			tick();
		else if (isSoft && framecount % 5 == 0) {
			if (!(land = checkLand())) {
				current.l.translate(0, 1);
				score += 1;
				playSound(sMove);
				updateScore();
				render();
				framecount = 0;
			}
		}
		framecount = (framecount + 1) % 10000;
	}

	public boolean checkLand() {
		boolean landtemp = false;
		for (int i = 0; i < current.s[0].length; i++) {
			for (int j = 0; j < current.s[0][0].length; j++) {
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

	public boolean checkLand(Block b, int x, int y, int r) {
		boolean landtemp = false;
		for (int i = 0; i < b.s[0].length; i++) {
			for (int j = 0; j < b.s[0][0].length; j++) {
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

	public synchronized void land() {
		for (int i = 0; i < current.s[0].length; i++)
			for (int j = 0; j < current.s[0][0].length; j++)
				if (current.s[current.r][i][j] != 0)
					try {
						board[current.l.y + i][current.l.x + j] += current.s[current.r][i][j];
					} catch (Exception e) {
					}
		playSound(sDrop);
		nextBlock();
	}

	public synchronized void tick() {
		boolean land = checkLand();
		if (!land) {
			current.l.translate(0, 1);
			if (isSoft) {
				score += 1;
				updateScore();
			}
			isTspin = false;
		} else if (this.land != land)
			framecount = 1;
		this.land = land;
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
		blockcount++;

		current = next.poll().clone();
		if (next.size() < 7)
			next.addAll(getShuffled());
		for (int i = 0; i < 5; i++) {
			n[i].setIcon(entire.get((char) next.get(i).n));
		}

		switch (current.n) {
		case 'O':
			current.l = new Point(4, -2);
			break;
		default:
			current.l = new Point(3, -2);
		}
	}

	public void playSoundBack() {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(sBack);
			cBack = AudioSystem.getClip();
			cBack.open(ais);
			cBack.loop(Clip.LOOP_CONTINUOUSLY);
			cBack.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void muteSoundBack() {
		if (cBack.isActive()) {
			BooleanControl c = (BooleanControl) cBack.getControl(Type.MUTE);
			c.setValue(!c.getValue());
		}
	}

	public void playSound(File s) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(s);
			Clip c = AudioSystem.getClip();
			c.open(ais);
			c.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			line += count;
			denormLine += 2 * count - 1;
			checkLevel();
			if (count == 4) {
				playSound(sTetris);
				denormLine += 1;
			} else
				playSound(sLine);
		} else
			combo = 0;
		while (count > 0) {
			board[count] = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			count--;
		}
	}

	public void checkLevel() {
		if (denormLine > (level + 2) * (level + 1) / 2) {
			level++;
			framePerTick = framePerTick > 2 ? framePerTick - 1 : 2;
			updateLevel();
		}
		System.out.println(denormLine + " " + level + " " + framePerTick);// FIXME
	}

	public synchronized void hold() {
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

	public synchronized void moveLeft() {
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
			playSound(sMove);
			render();
		}
	}

	public synchronized void moveRight() {
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
			playSound(sMove);
			render();
		}
	}

	public synchronized void rotateLeft() {
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
			playSound(sRotate);
			render();
		}
	}

	public synchronized void rotateRight() {
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
			playSound(sRotate);
			render();
		}
	}

	public synchronized void harddrop() {
		boolean land = false;
		int y = 0;
		while (!land) {
			for (int i = 0; i < current.s[0].length; i++) {
				for (int j = 0; j < current.s[0][0].length; j++) {
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
		playSound(sDrop);
		render();

		framecount = 1;
	}

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
				display[j][i].setIcon(blocks[board[j][i]]);
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
		h.setIcon(entire.get((char) hold.n));
	}

	public void updateScore() {
		s.setText("<html>Score<br>" + score + "</html>");
	}

	public void updateLevel() {
		v.setText("<html>Level<br>" + level + "</html>");
	}

}
