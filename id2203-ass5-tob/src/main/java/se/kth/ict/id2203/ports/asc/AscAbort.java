package se.kth.ict.id2203.ports.asc;

import se.sics.kompics.Event;

public class AscAbort extends Event {

	public enum Reason {
		ABORTED_BY_HIGHER_ROUND,
		NOT_IN_CONFIGURATION,
	}

	private final Reason reason;
	
	public AscAbort(Reason reason) {
		this.reason = reason;
	}

	public Reason getReason() {
		return reason;
	}
}
