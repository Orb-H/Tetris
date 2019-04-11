package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {
	private Server server;
	private Socket s;
	private BufferedReader br;
	private PrintWriter pw;

	private String ip;

	private Room r;

	public Client(Socket s) throws IOException {
		this.s = s;
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
		s.setSoTimeout(1000);
	}

	public void run() {
		try {
			String str = "";
			while (true) {
				try {
					str = br.readLine();
					if (str.startsWith("(join")) {
						setIP(str.substring(6));
						if (server.joinAvailable(this)) {
							r = server.getRoom();
						} else {
							close();
						}
					} else if (str.equals("(ready)")) {
						r.ready(this);
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
		}
	}

	public void sendMessage(String s) {
		pw.println(s);
		pw.flush();
	}

	public void close() {
		try {
			if (r != null)
				r.exit(this);
			br.close();
			pw.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getIP() {
		return ip;
	}

	void setIP(String ip) {
		this.ip = ip;
	}

}