package se.kth.ict.id2203.components.paxos;

import se.kth.ict.id2203.ports.beb.BebDeliver;
import se.sics.kompics.address.Address;

public class AcceptMessage extends BebDeliver{
	ProposeTV tv;
	int t;
	int pts;
	int pv;
	public AcceptMessage(Address source, int pts, int pv, int t) {
		super(source);
		this.tv=tv;
		this.t=t;
		this.pts=pts;
		this.pv=pv;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4077684345730752971L;
	public ProposeTV getTv() {
		return tv;
	}

	public void setTv(ProposeTV tv) {
		this.tv = tv;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public int getPts() {
		return pts;
	}

	public void setPts(int pts) {
		this.pts = pts;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

}
