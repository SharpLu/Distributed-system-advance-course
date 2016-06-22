/**
 * This file is part of the ID2203 course assignments kit.
 * 
 * Copyright (C) 2009-2013 KTH Royal Institute of Technology
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package se.kth.ict.id2203.components.beb;
 
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.kth.ict.id2203.ports.beb.BebBroadcast;
import se.kth.ict.id2203.ports.beb.BestEffortBroadcast;
import se.kth.ict.id2203.ports.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.ports.pp2p.Pp2pSend;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
 
public class BasicBroadcast extends ComponentDefinition {
 
        private static final Logger logger = LoggerFactory.getLogger(BasicBroadcast.class);
 
        private Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
        private Negative<BestEffortBroadcast> beb = provides(BestEffortBroadcast.class);
        private BasicBroadcastInit init;
        private Set<Address> processes;
    	private Address selfAddress;
       
        public BasicBroadcast(BasicBroadcastInit init) {
                this.init = init;
                //processes = new HashSet<Address>(init.getAllAddresses());
                processes=init.getAllAddresses();
                selfAddress=init.getSelfAddress();
                
                subscribe(hBebBroadcast, beb);
                subscribe(hBebDeliver, pp2p);
               
                //logger.info("Started");
        }
 
        private Handler<BebBroadcast> hBebBroadcast = new Handler<BebBroadcast>() {
                public void handle(BebBroadcast msg) {
                        for(Address p : processes) {
                                logger.info("Broadcast message" + p.getId());
                                trigger(new Pp2pSend(p, new BebDataMessage(msg.getDeliverEvent())), pp2p);
                        }
                }
        };
       
        private Handler<BebDataMessage> hBebDeliver = new Handler<BebDataMessage>() {
                public void handle(BebDataMessage msg) {
                        logger.info("Delivered  message " + msg.getSource().getId());
                        trigger(msg.getDeliverEvent(), beb);
                }
        };
}