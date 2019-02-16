
public class AI {
	public enum AIMode {
		BLOCK, SCORE, LINE, INFINITY
	}

	Board b;
	double maxHeight;// 최고 높이
	double sumHeight;// 높이 합
	double diffHeight;// 최고 높이-최저 높이
	double hole;// 구멍의 개수
	double standDeriv;// 높이의 표준편차
	double scoreInc;// 점수 증가분(라인 클리어)

	AIMode mode;
	int limit;
	int current;

	AI(AIMode a, int val) {
		maxHeight = getRandom();
		sumHeight = getRandom();
		diffHeight = getRandom();
		hole = getRandom();
		standDeriv = getRandom();
		scoreInc = getRandom();

		mode = a;
		limit = val;

		b = new Board();
	}

	void start() {
		b.start();
		b.start = true;
		b.shadow = true;
	}

	double getProbrability(int t, int rot) {
		int y = 0;
		for (; y < 19; y++) {
			b.checkLand(b.current, t, y, rot);
		}
		return 0;
	}

	void addBlock() {

	}

	void addLine(int n) {

	}

	void addScore(int n) {

	}

	private static double getRandom() {
		return 2 * Math.random() - 1;
	}

}
