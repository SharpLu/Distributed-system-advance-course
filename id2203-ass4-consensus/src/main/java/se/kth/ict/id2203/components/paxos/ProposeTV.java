package se.kth.ict.id2203.components.paxos;

import java.io.Serializable;

public class ProposeTV implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4120051578635222254L;
	private int pts;
	private int pv;
	public int getPts() {
		return pts;
	}
	public int getPv() {
		return pv;
	}
	public ProposeTV(int pts, int pv) {
		super();
		this.pts = pts;
		this.pv = pv;
	}
	public ProposeTV(ProposeTV ptv) {
		this.pts= new Integer(ptv.getPts());
		this.pv= new Integer(ptv.getPv());
	}
	public void setPts(int pts) {
		this.pts = pts;
	}
	public void setPv(int pv) {
		this.pv = pv;
	}
}
