package se.kth.ict.id2203.components.riwcm;

import se.kth.ict.id2203.ports.ar.ArReadRequest;
import se.kth.ict.id2203.ports.beb.BebDeliver;
import se.kth.ict.id2203.ports.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;


public class ReadMessage extends BebDeliver{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3240638319168912917L;
	private int rid;

	public ReadMessage(Address source, int rid) {
		super(source);
		this.rid=rid;
		
	}
	public int getRid() {
		return rid;
	}


	
	
	

}
