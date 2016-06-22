package se.kth.ict.id2203.components.riwcm;

import se.kth.ict.id2203.ports.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

public class ACKMessage extends Pp2pDeliver{
	private int r;

	protected ACKMessage(Address source,int r) {
		super(source);
		this.r=r;
		
	}

	public int getR() {
		return r;
	}
	private static final long serialVersionUID = 8919297128166512603L;

	

}
