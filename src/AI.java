
public class AI {

	int size = 50;
	double[] genomes;
	int index = -1;
	int generation = 0;

	double mutationRate = 0.05;
	double mutationStep = 0.2;

	Board b;

	public AI() {
		b = new Board();
		init();
	}

	public void init() {
	}

}
