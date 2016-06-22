package se.kth.ict.id2203.components.crb;

import java.util.HashMap;

import se.kth.ict.id2203.ports.crb.CrbDeliver;
import se.kth.ict.id2203.ports.rb.RbDeliver;

public class CrbDataMessage extends RbDeliver{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -323441342301339725L;
	CrbDeliver deliverEvent;
	HashMap<Integer, Integer> w;

	public CrbDataMessage(CrbDeliver deliverEvent, HashMap<Integer, Integer> w) {
		super(deliverEvent.getSource());
		this.deliverEvent = deliverEvent;
		this.w = w;
	}
	
	public CrbDeliver getDeliverEvent(){
		return this.deliverEvent;
	}
	
	public HashMap<Integer, Integer> getW(){
		return this.w;
	}
	
	@Override
	public boolean equals(Object o) {
		CrbDataMessage e = (CrbDataMessage) o;
	    return (this.deliverEvent.getSource().getId() == e.deliverEvent.getSource().getId()
	    		&&this.getW().get(this.deliverEvent.getSource().getId())==e.getW().get(e.deliverEvent.getSource().getId()));
	}
	
	@Override
	public int hashCode() {
		final int PRIME = 31;
	    int result = 1;
	    result = PRIME * result + this.deliverEvent.getSource().getId() + this.getW().get(this.deliverEvent.getSource().getId());
	    return result;
	}
}
