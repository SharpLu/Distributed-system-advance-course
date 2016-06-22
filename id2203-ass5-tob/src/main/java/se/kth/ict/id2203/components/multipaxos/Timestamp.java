package se.kth.ict.id2203.components.multipaxos;

import java.io.Serializable;

public class Timestamp implements Serializable, Comparable<Timestamp> {




	/**
	 * 
	 */
	private static final long serialVersionUID = -6155544848727204104L;

	public final static Timestamp ZERO = new Timestamp(0, 0, 0);

	private final int cfg;
	private final int t;
	private final int pid;

	public Timestamp(int cfg, int t, int pid) {
		this.cfg = cfg;
		this.t = t;
		this.pid = pid;
	}

	public int getCfg() {
		return cfg;
	}

	public int getT() {
		return t;
	}

	public int getPid() {
		return pid;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Timestamp) {
			Timestamp ts = (Timestamp) obj;
			return cfg == ts.cfg && t == ts.t && pid == ts.pid;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("Timestamp(%d, %d, %d)", cfg, t, pid);
	}

	@Override
	public int compareTo(Timestamp o) {
		int c = Integer.compare(cfg, o.cfg);
		if (c == 0) {
			c = Integer.compare(t, o.t);
			if (c == 0) {
				c = Integer.compare(pid, o.pid);
			}
		}
		return c;
	}
}
