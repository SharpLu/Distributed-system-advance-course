package se.kth.ict.id2203.components.multipaxos;

import se.kth.ict.id2203.ports.fpl.FplDeliver;
import se.sics.kompics.address.Address;

public class PrepareAckMessage extends FplDeliver {


	/**
	 * 
	 */
	private static final long serialVersionUID = 769690617367940438L;
	private final Timestamp pts;
	private final Timestamp ts;
	private final Object[] vsuf;
	private final int dl;
	private final int t;

	protected PrepareAckMessage(Address source, Timestamp pts, Timestamp ts, Object[] vsuf, int dl, int t) {
		super(source);
		this.pts = pts;
		this.ts = ts;
		this.vsuf = vsuf;
		this.dl = dl;
		this.t = t;
	}

	public Timestamp getPts() {
		return pts;
	}

	public Timestamp getTs() {
		return ts;
	}

	public Object[] getVsuf() {
		return vsuf;
	}

	public int getDl() {
		return dl;
	}

	public int getT() {
		return t;
	}
}
