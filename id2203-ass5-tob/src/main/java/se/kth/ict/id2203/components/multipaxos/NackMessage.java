package se.kth.ict.id2203.components.multipaxos;

import se.kth.ict.id2203.ports.fpl.FplDeliver;
import se.sics.kompics.address.Address;

public class NackMessage extends FplDeliver {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1716388153701614390L;
	private final Timestamp pts;
	private final int t;

	protected NackMessage(Address source, Timestamp pts, int t) {
		super(source);
		this.pts = pts;
		this.t = t;
	}

	public Timestamp getPts() {
		return pts;
	}

	public int getT() {
		return t;
	}
}
