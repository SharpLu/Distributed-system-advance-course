package se.kth.ict.id2203.components.multipaxos;

import se.kth.ict.id2203.ports.fpl.FplDeliver;
import se.sics.kompics.address.Address;

public class DecideAcceptedMessage extends FplDeliver {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7911983656369810093L;
	/**
	 * 
	 */

	private final Timestamp ts;
	private final int l;
	private final int t;

	protected DecideAcceptedMessage(Address source, Timestamp ts, int l, int t) {
		super(source);
		this.ts = ts;
		this.l = l;
		this.t = t;
	}

	public Timestamp getTs() {
		return ts;
	}

	public int getL() {
		return l;
	}

	public int getT() {
		return t;
	}
}
