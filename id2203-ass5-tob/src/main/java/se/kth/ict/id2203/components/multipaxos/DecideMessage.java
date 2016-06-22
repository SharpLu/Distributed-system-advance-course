package se.kth.ict.id2203.components.multipaxos;

import se.kth.ict.id2203.ports.fpl.FplDeliver;
import se.sics.kompics.address.Address;

public class DecideMessage extends FplDeliver {



	/**
	 * 
	 */
	private static final long serialVersionUID = -5834251287114480358L;
	private final Object[] vsuf;
	private final int offs;
	private final int t;

	protected DecideMessage(Address source, Object[] vsuf, int offs, int t) {
		super(source);
		this.vsuf = vsuf;
		this.offs = offs;
		this.t = t;
	}

	public Object[] getVsuf() {
		return vsuf;
	}

	public int getOffs() {
		return offs;
	}

	public int getT() {
		return t;
	}
}
