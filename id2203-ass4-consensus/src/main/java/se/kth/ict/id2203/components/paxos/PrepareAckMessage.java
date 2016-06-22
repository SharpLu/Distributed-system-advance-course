package se.kth.ict.id2203.components.paxos;

import se.kth.ict.id2203.ports.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

public class PrepareAckMessage extends Pp2pDeliver{
	AcceptedTV atv;
	int t;
	PrepareMessage pm;
	int ats;
	int av;
	int pts;
	
	protected PrepareAckMessage(Address source,AcceptedTV atv,int pts, int t) {
		super(source);
		this.atv=atv;
		this.t=t;
		this.pm=pm;
		this.atv=atv;
		this.ats=ats;
		this.av=av;
		this.pts=pts;
		// TODO Auto-generated constructor stub
	}

	public AcceptedTV getAtv() {
		return atv;
	}

	public void setAtv(AcceptedTV atv) {
		this.atv = atv;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public PrepareMessage getPm() {
		return pm;
	}

	public void setPm(PrepareMessage pm) {
		this.pm = pm;
	}

	public int getAts() {
		return ats;
	}

	public void setAts(int ats) {
		this.ats = ats;
	}

	public int getAv() {
		return av;
	}

	public void setAv(int av) {
		this.av = av;
	}

	public int getPts() {
		return pts;
	}

	public void setPts(int pts) {
		this.pts = pts;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3101569120524809550L;

}
