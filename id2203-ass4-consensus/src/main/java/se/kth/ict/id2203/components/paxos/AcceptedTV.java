package se.kth.ict.id2203.components.paxos;

import java.io.Serializable;

public class AcceptedTV implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8131276480310435623L;
	private int ats;
	private int av;

	public AcceptedTV(int ats, int av) {
		super();
		this.ats = ats;
		this.av = av;
	}

	public int getAts() {
		return ats;
	}

	public int getAv() {
		return av;
	}

	public AcceptedTV(AcceptedTV Atv) {
		this.ats = new Integer(Atv.getAts());
		this.av = new Integer(Atv.getAv());

	}

	public void setAts(int ats) {
		this.ats = ats;
	}

	public void setAv(int av) {
		this.av = av;
	}
}
