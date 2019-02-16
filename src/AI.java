
public class AI {
	public enum AIMode {
		BLOCK, SCORE, LINE, INFINITY
	}

	Board b;
	double maxHeight;// �ְ� ����
	double sumHeight;// ���� ��
	double diffHeight;// �ְ� ����-���� ����
	double hole;// ������ ����
	double standDeriv;// ������ ǥ������
	double scoreInc;// ���� ������(���� Ŭ����)

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
