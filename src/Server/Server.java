package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private Server s;
	private ServerSocket ss;
	private Room r = new Room();

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

	public boolean joinAvailable(Client c) {
		return r.join(c);
	}

	public Room getRoom() {
		return r;
	}

	public static void main(String[] args) {
		new Server().startServer();
	}

}
