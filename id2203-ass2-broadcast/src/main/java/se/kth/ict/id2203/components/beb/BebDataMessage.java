package se.kth.ict.id2203.components.beb;

import se.kth.ict.id2203.ports.beb.BebDeliver;
import se.kth.ict.id2203.ports.pp2p.Pp2pDeliver;
import se.sics.kompics.address.Address;

public class BebDataMessage extends Pp2pDeliver {
	private BebDeliver deliverEvent;
	
	protected BebDataMessage(BebDeliver deliverEvent) {
		super(deliverEvent.getSource());
		this.deliverEvent= deliverEvent;
		// TODO Auto-generated constructor stub
	}

	public BebDeliver getDeliverEvent() {
		return deliverEvent;
	}

	public void setDeliverEvent(BebDeliver deliverEvent) {
		this.deliverEvent = deliverEvent;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 9183185042302932366L;
								//7151991466156096079L
}
