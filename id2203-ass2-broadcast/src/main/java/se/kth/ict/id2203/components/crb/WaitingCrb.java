package se.kth.ict.id2203.components.crb;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import se.kth.ict.id2203.ports.crb.CausalOrderReliableBroadcast;
import se.kth.ict.id2203.ports.crb.CrbBroadcast;
import se.kth.ict.id2203.ports.rb.RbBroadcast;
import se.kth.ict.id2203.ports.rb.ReliableBroadcast;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;

public class WaitingCrb extends ComponentDefinition {
	private Negative<CausalOrderReliableBroadcast> crb = provides(CausalOrderReliableBroadcast.class);
	private Positive<ReliableBroadcast> rb = requires(ReliableBroadcast.class);
	
	int self;
	HashMap<Integer, Integer> V = new HashMap<Integer, Integer>();
	private int lsn;
	private Set<CrbDataMessage> pending;
	private boolean exists;
	private HashMap<Integer,Integer> W;
	
	public WaitingCrb(WaitingCrbInit init) {
		this.self = init.getSelfAddress().getId();
		for(Address e:init.getAllAddresses()){
			V.put(e.getId(), 0);
		}
		lsn = 0;
		pending = new HashSet<CrbDataMessage>();
		
		subscribe(broadcast,crb);
		subscribe(deliver,rb);
	}
	
	private Handler<CrbBroadcast> broadcast = new Handler<CrbBroadcast>(){
		@Override
		public void handle(CrbBroadcast event) {
			W = new HashMap<Integer,Integer>(V);
			W.put(self, new Integer(lsn));
			lsn++;
			trigger(new RbBroadcast(new CrbDataMessage(event.getDeliverEvent(),W)),rb);
		}
	};
	
	private Handler<CrbDataMessage> deliver = new Handler<CrbDataMessage>(){
		@Override
		public void handle(CrbDataMessage event) {
			pending.add(event);
			exists=true;
			while(exists){
				exists=false;
		
				for (Iterator<CrbDataMessage> i = pending.iterator(); i.hasNext();){
					System.out.println(pending.size());
					CrbDataMessage C = i.next();
					if(V.get(C.getSource().getId())>=C.getW().get(C.getSource().getId())){
						V.put(C.getSource().getId(), V.get(C.getSource().getId())+1);
						trigger(C.getDeliverEvent(),crb);
						i.remove();
					}
				}
				for(CrbDataMessage C : pending){
					if(V.get(C.getSource().getId())>=C.getW().get(C.getSource().getId())){
						exists=true;
					}
				}
			}
		}
	};
}
