package se.kth.ict.id2203.components.riwcm;

import java.io.Serializable;

public class Triple implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3470792880954589404L;
	private int ts;
	private int wr;
	private int val;
	
	public Triple (int ts, int wr, int val) {
		
		this.ts = ts;
		this.wr = wr;
		this.val = val;
	}
	public Triple(Triple tp) {
		this.ts= new Integer(tp.getTs());
		this.wr=new Integer(tp.getWr());
		this.val=new Integer(tp.getVal());
	}
	public int getTs() {
		return ts;
	}
	
	public int getWr() {
		return wr;
	}
	
	public int getVal() {
		return val;
	}
	
	

}
