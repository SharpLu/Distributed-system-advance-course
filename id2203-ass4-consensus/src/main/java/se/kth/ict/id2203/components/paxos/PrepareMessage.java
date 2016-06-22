package se.kth.ict.id2203.components.paxos;

import se.kth.ict.id2203.ports.beb.BebDeliver;
import se.sics.kompics.address.Address;

public class PrepareMessage extends BebDeliver{
	   int pts;
		private int t;
	public PrepareMessage(Address source,int pts,int t) {
		super(source);
		this.t=t;
		this.pts=pts;
		// TODO Auto-generated constructor stub
	}



	public int getPts() {
		return pts;
	}



	public void setPts(int pts) {
		this.pts = pts;
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
	private static final long serialVersionUID = -1551419783218735716L;

}
