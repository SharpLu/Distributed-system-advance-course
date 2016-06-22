package se.kth.ict.id2203.components.paxos;

import se.kth.ict.id2203.ports.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

public class NackMessage extends Pp2pDeliver{
	int ts;
	int t;
	protected NackMessage(Address source,int ts, int t) {
		super(source);
		this.t=t;
		this.ts=ts;
		// TODO Auto-generated constructor stub
	}

	public int getTs() {
		return ts;
	}

	public void setTs(int ts) {
		this.ts = ts;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6508249656116571461L;

}
