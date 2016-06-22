package se.kth.ict.id2203.components.multipaxos;

import se.kth.ict.id2203.ports.fpl.FplDeliver;
import se.sics.kompics.address.Address;

public class AcceptAckMessage extends FplDeliver {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2368179411367470413L;
	private final Timestamp pts;
	private final int al;
	private final int t;

	protected AcceptAckMessage(Address source, Timestamp pts, int al, int t) {
		super(source);
		this.pts = pts;
		this.al = al;
		this.t = t;
	}

	public Timestamp getPts() {
		return pts;
	}

	public int getAl() {
		return al;
	}

	public int getT() {
		return t;
	}
}
