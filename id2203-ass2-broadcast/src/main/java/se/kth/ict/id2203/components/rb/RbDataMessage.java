package se.kth.ict.id2203.components.rb;

import se.kth.ict.id2203.ports.beb.BebDeliver;
import se.kth.ict.id2203.ports.rb.RbDeliver;
import se.sics.kompics.address.Address;
 
public class RbDataMessage extends BebDeliver {
 
        private static final long serialVersionUID = 2021283271786402377L;
 
        private final RbDeliver DeliverEvent;
       
        private final Integer sequenceNumber;
 
        public RbDataMessage(RbDeliver DeliverEvent, Integer sequenceNumber) {
                super(DeliverEvent.getSource());
               this.DeliverEvent=DeliverEvent;
             //   this.data = data;
                this.sequenceNumber = sequenceNumber;
        }
 
        public RbDeliver getDeliverEvent() {
                return DeliverEvent;
        }
 
        public Integer getSequenceNumber() {
                return sequenceNumber;
        }
 
        @Override
        public boolean equals(Object other) {
                boolean equal = false;
 
                if (other instanceof RbDataMessage) {
                        equal = (this.sequenceNumber.equals(((RbDataMessage) other)
                                        .getSequenceNumber()) && super.getSource().equals(
                                        ((RbDataMessage) other).getSource()));
                }
               
                return equal;
        }
       
        @Override
        public int hashCode() {
                return (this.sequenceNumber * super.getSource().hashCode());
        }
}