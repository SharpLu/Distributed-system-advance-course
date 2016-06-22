package se.kth.ict.id2203.components.multipaxos;

import se.kth.ict.id2203.ports.fpl.FplDeliver;
import se.sics.kompics.address.Address;

public class NackUnknownCfgMessage extends FplDeliver {



	/**
	 * 
	 */
	private static final long serialVersionUID = -4587404364616131200L;
	private final Timestamp ts;
	private final int dl;
	private final int t;

	protected NackUnknownCfgMessage(Address source, Timestamp ts, int dl, int t) {
		super(source);
		this.ts = ts;
		this.dl = dl;
		this.t = t;
	}

	public Timestamp getTs() {
		return ts;
	}

	public int getDl() {
		return dl;
	}

	public int getT() {
		return t;
	}
}
