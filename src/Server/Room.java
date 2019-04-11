package Server;

public class Room {

	Server s;
	Client p1;
	Client p2;

	boolean ready1;
	boolean ready2;

	public boolean join(Client c) {
		if ((p1 != null && c.getIP().equals(p1.getIP())) || (p2 != null && c.getIP().equals(p2.getIP()))) {
			c.sendMessage("(duplicate)");
			return false;
		}
		if (p1 != null && p2 != null) {
			c.sendMessage("(full)");
			return false;
		}

		if (p1 == null) {
			p1 = c;
			if (p2 != null) {
				p2.sendMessage("(rivaljoin)");
				p1.sendMessage("(rivaljoin)");
				p1.sendMessage("(rivalready/" + (ready2 ? 1 : 0) + ")");
			}
		} else {
			p2 = c;
			if (p1 != null) {
				p1.sendMessage("(rivaljoin)");
				p2.sendMessage("(rivaljoin)");
				p2.sendMessage("(rivalready/" + (ready1 ? 1 : 0) + ")");
			}
		}
		return true;
	}

	public void ready(Client c) {
		if (c == p1) {
			ready1 = !ready1;
			if (p2 != null)
				p2.sendMessage("(rivalready/" + (ready1 ? 1 : 0) + ")");
		} else if (c == p2) {
			ready2 = !ready2;
			if (p1 != null)
				p1.sendMessage("(rivalready/" + (ready2 ? 1 : 0) + ")");
		}
	}

	public void exit(Client c) {
		if (c == p1) {
			p1 = null;
			ready1 = false;
		} else if (c == p2) {
			p2 = null;
			ready2 = false;
		}
	}

}
