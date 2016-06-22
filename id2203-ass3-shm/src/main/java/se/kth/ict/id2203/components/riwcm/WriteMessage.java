package se.kth.ict.id2203.components.riwcm;

import se.kth.ict.id2203.ports.beb.BebDeliver;
import se.sics.kompics.address.Address;

public class WriteMessage extends BebDeliver{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3320878266630667284L;
	private int rid;
	private Triple tp;
	
	protected WriteMessage(Address source,int rid, Triple tp) {
		super(source);
		this.rid=rid;
		this.tp=tp;
		
	}
	public int getRid() {
		return rid;
	}
	

	public Triple getTp() {
		return tp;
	}




}
