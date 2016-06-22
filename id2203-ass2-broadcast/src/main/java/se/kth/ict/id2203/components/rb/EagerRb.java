package se.kth.ict.id2203.components.rb;

import java.util.HashSet;
import java.util.Set;
 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

import se.kth.ict.id2203.ports.beb.BebBroadcast;
import se.kth.ict.id2203.ports.beb.BestEffortBroadcast;
import se.kth.ict.id2203.ports.rb.RbBroadcast;
import se.kth.ict.id2203.ports.rb.RbDeliver;
import se.kth.ict.id2203.ports.rb.ReliableBroadcast;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
 
public class EagerRb extends ComponentDefinition {
 
        private static final Logger logger = LoggerFactory.getLogger(EagerRb.class);
       
        private Positive<BestEffortBroadcast> beb = requires(BestEffortBroadcast.class);
        private Negative<ReliableBroadcast> rb = provides(ReliableBroadcast.class);
        private Set<RbDataMessage> delivered;
        private int seqnum;
        private EagerRbInit init;
        private Address selfAddress;
        
        public EagerRb(EagerRbInit init) {
                this.init = init;
                selfAddress=init.getSelfAddress();
                delivered = new HashSet<RbDataMessage>();
                seqnum = 0;
               
                subscribe(hRbBroadcast, rb);
                subscribe(hBebDeliver, beb);
        }
 
        private Handler<RbBroadcast> hRbBroadcast = new Handler<RbBroadcast>() {
                public void handle(RbBroadcast msg) {
                        seqnum++;
                        logger.info("RbDataMessage "+init.getSelfAddress().getId()+",");
                        //trigger(new RbDeliver(init.getSelfAddress()), rb);
                        trigger(new BebBroadcast(new RbDataMessage(msg.getDeliverEvent(), seqnum)), beb);
                }
        };
       
        private Handler<RbDataMessage> hBebDeliver = new Handler<RbDataMessage>() {
                public void handle(RbDataMessage msg) {
                        logger.info("Received RbMessage" + init.getSelfAddress().getId()+",");
                      
                        if(!delivered.contains(msg)) {
                                delivered.add(msg);
                                trigger(msg.getDeliverEvent(), rb);
                                trigger(new BebBroadcast(msg), beb);
                        }
                }
        };
}