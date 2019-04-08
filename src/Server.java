import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	Server s;
	ServerSocket ss;

	Client p1, p2;

	public void startServer() {
		s = this;
		try {
			ss = new ServerSocket(2245);
			while (true) {
				Socket s = ss.accept();
				Client c = new Client(s);
				c.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Server().startServer();
	}

	class Client extends Thread {
		private Socket s;
		private BufferedReader br;
		private PrintWriter pw;

		public Client(Socket s) throws IOException {
			this.s = s;
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
			s.setSoTimeout(1000);
		}

		public void run() {

		}

	}

}
