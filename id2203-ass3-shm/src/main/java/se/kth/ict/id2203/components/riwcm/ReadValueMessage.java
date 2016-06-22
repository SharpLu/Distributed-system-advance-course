package se.kth.ict.id2203.components.riwcm;

import se.kth.ict.id2203.ports.ar.ArReadResponse;
import se.kth.ict.id2203.ports.beb.BebDeliver;
import se.kth.ict.id2203.ports.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;


public class ReadValueMessage extends Pp2pDeliver{
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 6757045959302766776L;
	private Triple tp;
	private ReadMessage deliverent;
	
	protected ReadValueMessage(ReadMessage deliverent, Triple tp) {
		super(deliverent.getSource());
		
		this.tp=tp;
		this.deliverent=deliverent;
	}
	public Triple getTp() {
		return tp;
	}


	public ReadMessage getDeliverent() {
		return deliverent;
	}
	


}
