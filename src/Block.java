import java.awt.Point;
import java.util.Arrays;
import java.util.List;

public class Block {

	public static final Point x0y0 = new Point(0, 0);
	public static final Point x1y0 = new Point(1, 0);
	public static final Point x_1y0 = new Point(-1, 0);
	public static final Point x2y0 = new Point(2, 0);
	public static final Point x_2y0 = new Point(-2, 0);
	public static final Point x0y2 = new Point(0, 2);
	public static final Point x0y_2 = new Point(0, -2);
	public static final Point x1y1 = new Point(1, 1);
	public static final Point x_1y1 = new Point(-1, 1);
	public static final Point x1y_1 = new Point(1, -1);
	public static final Point x_1y_1 = new Point(-1, -1);
	public static final Point x1y2 = new Point(1, 2);
	public static final Point x_1y2 = new Point(-1, 2);
	public static final Point x1y_2 = new Point(1, -2);
	public static final Point x_1y_2 = new Point(-1, -2);
	public static final Point x2y1 = new Point(2, 1);
	public static final Point x_2y1 = new Point(-2, 1);
	public static final Point x2y_1 = new Point(2, -1);
	public static final Point x_2y_1 = new Point(-2, -1);

	public static final Point[][] DEFAULT_WALL = new Point[][] { { x0y0, x_1y0, x_1y_1, x0y2, x_1y2 },
			{ x0y0, x1y0, x1y1, x0y_2, x1y_2 }, { x0y0, x1y0, x1y_1, x0y2, x1y2 },
			{ x0y0, x_1y0, x_1y1, x0y_2, x_1y_2 }, { x0y0, x1y0, x1y_1, x0y2, x1y2 },
			{ x0y0, x1y0, x1y1, x0y_2, x1y_2 }, { x0y0, x_1y0, x_1y_1, x0y2, x_1y2 },
			{ x0y0, x_1y0, x_1y1, x0y_2, x_1y_2 } };

	public static final Block I = new Block((byte) 'I', new Point(3, -1),
			new byte[][][] { { { 0, 0, 0, 0 }, { 1, 1, 1, 1 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } },
					{ { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 1, 0 } },
					{ { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 1, 1, 1, 1 }, { 0, 0, 0, 0 } },
					{ { 0, 1, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 0, 0 } } },
			new Point[][] { { x0y0, x_2y0, x1y0, x_2y1, x1y_2 }, { x0y0, x_1y0, x2y0, x_1y_2, x2y1 },
					{ x0y0, x2y0, x_1y0, x2y_1, x_1y2 }, { x0y0, x1y0, x_2y0, x1y2, x_2y_1 },
					{ x0y0, x_1y0, x2y0, x_1y_2, x2y1 }, { x0y0, x2y0, x_1y0, x2y_1, x_1y2 },
					{ x0y0, x1y0, x_2y0, x1y2, x_2y_1 }, { x0y0, x_2y0, x1y0, x_2y1, x1y_2 } });
	public static final Block J = new Block((byte) 'J', new Point(3, -1),
			new byte[][][] { { { 2, 0, 0 }, { 2, 2, 2 }, { 0, 0, 0 } }, { { 0, 2, 2 }, { 0, 2, 0 }, { 0, 2, 0 } },
					{ { 0, 0, 0 }, { 2, 2, 2 }, { 0, 0, 2 } }, { { 0, 2, 0 }, { 0, 2, 0 }, { 2, 2, 0 } } },
			DEFAULT_WALL);
	public static final Block L = new Block((byte) 'L', new Point(3, -1),
			new byte[][][] { { { 0, 0, 3 }, { 3, 3, 3 }, { 0, 0, 0 } }, { { 0, 3, 0 }, { 0, 3, 0 }, { 0, 3, 3 } },
					{ { 0, 0, 0 }, { 3, 3, 3 }, { 3, 0, 0 } }, { { 3, 3, 0 }, { 0, 3, 0 }, { 0, 3, 0 } } },
			DEFAULT_WALL);
	public static final Block O = new Block((byte) 'O', new Point(4, -1), new byte[][][] { { { 4, 4 }, { 4, 4 } },
			{ { 4, 4 }, { 4, 4 } }, { { 4, 4 }, { 4, 4 } }, { { 4, 4 }, { 4, 4 } } }, null);
	public static final Block S = new Block((byte) 'S', new Point(3, -1),
			new byte[][][] { { { 0, 5, 5 }, { 5, 5, 0 }, { 0, 0, 0 } }, { { 0, 5, 0 }, { 0, 5, 5 }, { 0, 0, 5 } },
					{ { 0, 0, 0 }, { 0, 5, 5 }, { 5, 5, 0 } }, { { 5, 0, 0 }, { 5, 5, 0 }, { 0, 5, 0 } } },
			DEFAULT_WALL);
	public static final Block T = new Block((byte) 'T', new Point(3, -1),
			new byte[][][] { { { 0, 6, 0 }, { 6, 6, 6 }, { 0, 0, 0 } }, { { 0, 6, 0 }, { 0, 6, 6 }, { 0, 6, 0 } },
					{ { 0, 0, 0 }, { 6, 6, 6 }, { 0, 6, 0 } }, { { 0, 6, 0 }, { 6, 6, 0 }, { 0, 6, 0 } } },
			DEFAULT_WALL);
	public static final Block Z = new Block((byte) 'Z', new Point(3, -1),
			new byte[][][] { { { 7, 7, 0 }, { 0, 7, 7 }, { 0, 0, 0 } }, { { 0, 0, 7 }, { 0, 7, 7 }, { 0, 7, 0 } },
					{ { 0, 0, 0 }, { 7, 7, 0 }, { 0, 7, 7 } }, { { 0, 7, 0 }, { 7, 7, 0 }, { 7, 0, 0 } } },
			DEFAULT_WALL);

	public static final List<Block> set = Arrays.asList(I, J, L, O, S, T, Z);

	/**
	 * name
	 */
	byte n;
	/**
	 * start location
	 */
	Point l;
	/**
	 * shape
	 */
	byte[][][] s;
	/**
	 * rotation
	 */
	byte r = 0;
	/**
	 * wall kick test
	 */
	Point[][] w;

	Block(byte n, Point l, byte[][][] s, Point[][] w) {
		this.n = n;
		this.l = l;
		this.s = s;
		this.w = w;
	}

	public Block clone() {
		return new Block(n, l, s, w);
	}

}
