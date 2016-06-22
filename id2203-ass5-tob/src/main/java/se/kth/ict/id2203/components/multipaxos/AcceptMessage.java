package se.kth.ict.id2203.components.multipaxos;

import se.kth.ict.id2203.ports.fpl.FplDeliver;
import se.sics.kompics.address.Address;

public class AcceptMessage extends FplDeliver {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1256026744436920435L;
	/**
	 * 
	 */

	private final Timestamp ts;
	private final Object[] vsuf;
	private final int offs;
	private final int t;

	protected AcceptMessage(Address source, Timestamp ts, Object[] vsuf, int offs, int t) {
		super(source);
		this.ts = ts;
		this.vsuf = vsuf;
		this.offs = offs;
		this.t = t;
	}

	public Timestamp getTs() {
		return ts;
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
