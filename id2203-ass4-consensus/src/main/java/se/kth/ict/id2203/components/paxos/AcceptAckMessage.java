package se.kth.ict.id2203.components.paxos;

import se.kth.ict.id2203.ports.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

public class AcceptAckMessage extends Pp2pDeliver {
	int t;
	AcceptMessage am;
	int ts;
	
	protected AcceptAckMessage(Address source,int ts, int t) {	
		super(source);
		this.t=t;
		this.ts=ts;
		this.am=am;
		// TODO Auto-generated constructor stub
	}
	

	public int getT() {
		return t;
	}


	public void setT(int t) {
		this.t = t;
	}


	public AcceptMessage getAm() {
		return am;
	}


	public void setAm(AcceptMessage am) {
		this.am = am;
	}


	public int getTs() {
		return ts;
	}


	public void setTs(int ts) {
		this.ts = ts;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 8524353292980544263L;

}
