package se.kth.ict.id2203.components.multipaxos;

import se.kth.ict.id2203.ports.fpl.FplDeliver;
import se.sics.kompics.address.Address;

public class StoppedConfigurationMessage extends FplDeliver {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1528410547025765487L;
	private final int cfg;
	private final int nextCfg;
	private final int t;

	protected StoppedConfigurationMessage(Address source, int cfg, int nextCfg, int t) {
		super(source);
		this.cfg = cfg;
		this.nextCfg = nextCfg;
		this.t = t;
	}

	public int getCfg() {
		return cfg;
	}

	public int getNextCfg() {
		return nextCfg;
	}

	public int getT() {
		return t;
	}
}
