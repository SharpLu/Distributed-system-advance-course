package se.kth.ict.id2203.components.multipaxos;

import se.kth.ict.id2203.ports.fpl.FplDeliver;
import se.sics.kompics.address.Address;

public class PleaseUpdateMessage extends FplDeliver {




	/**
	 * 
	 */
	private static final long serialVersionUID = -1299629842383849027L;
	private final int from;
	private final int to;
	private final int t;

	protected PleaseUpdateMessage(Address source, int from, int to, int t) {
		super(source);
		this.from = from;
		this.to = to;
		this.t = t;
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	public int getT() {
		return t;
	}
}
