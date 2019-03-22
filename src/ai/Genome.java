package ai;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Genome {

	double maxHeight;// �ְ� ����
	double sumHeight;// ���� ��
	double diffHeight;// �ְ� ����-���� ����
	double hole;// ������ ����
	double standDeriv;// ������ ǥ������
	double scoreInc;// ���� ������(���� Ŭ����)

	public Genome() {
		maxHeight = Math.random() * 2 - 1;
		sumHeight = Math.random() * 2 - 1;
		diffHeight = Math.random() * 2 - 1;
		hole = Math.random() * 2 - 1;
		standDeriv = Math.random() * 2 - 1;
		scoreInc = Math.random() * 2 - 1;
	}

	public Genome(double mh, double sh, double dh, double h, double sd, double si) {
		maxHeight = mh;
		sumHeight = sh;
		diffHeight = dh;
		hole = h;
		standDeriv = sd;
		scoreInc = si;
	}

	public static Genome readFromFile(File f) {
		try {
			Scanner s = new Scanner(f);
			Genome g = new Genome(s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(), s.nextDouble(),
					s.nextDouble());
			s.close();
			return g;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void saveFile(String s) {
		File f = new File(s);
		if (!f.exists()) {
			try {
				f.createNewFile();
				PrintWriter pw = new PrintWriter(new FileWriter(f));
				pw.write(maxHeight + " " + sumHeight + " " + diffHeight + " " + hole + " " + standDeriv + " "
						+ scoreInc);
				pw.flush();
				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
