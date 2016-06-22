package se.kth.ict.id2203.components.riwcm;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.kth.ict.id2203.ports.ar.ArReadRequest;
import se.kth.ict.id2203.ports.ar.ArReadResponse;
import se.kth.ict.id2203.ports.ar.ArWriteRequest;
import se.kth.ict.id2203.ports.ar.ArWriteResponse;
import se.kth.ict.id2203.ports.ar.AtomicRegister;
import se.kth.ict.id2203.ports.beb.BebBroadcast;
import se.kth.ict.id2203.ports.beb.BestEffortBroadcast;
import se.kth.ict.id2203.ports.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.ports.pp2p.Pp2pSend;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;

public class ReadImposeWriteConsultMajority extends ComponentDefinition {

	private static final Logger logger = LoggerFactory.getLogger(ReadImposeWriteConsultMajority.class);

	private Negative<AtomicRegister> nnar = provides(AtomicRegister.class);
	private Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);
	private Positive<BestEffortBroadcast> beb = requires(BestEffortBroadcast.class);
	
	Address self;
	Triple triple;
	int N;
	int rid;
	int acks;
	int writeval;
	int readval;
	ArrayList<Triple> readlist;
	boolean reading;

	public ReadImposeWriteConsultMajority(ReadImposeWriteConsultMajorityInit event) {
		
		self = event.getSelfAddress();
		triple = new Triple(0,0,0);
		acks = 0;
		rid = 0;
		N = event.getAllAddresses().size();
		readlist = new ArrayList<>();
		reading = false;
		readval=0;
		
		subscribe(ReadRequest, nnar);
		subscribe(ReadBroadcast,beb);
		subscribe(HandleReads,pp2p);
		subscribe(arWriteRequest,nnar);
		subscribe(wr,beb);
		subscribe(ACK,pp2p);
	}
	
	private Handler<ArReadRequest> ReadRequest = new Handler<ArReadRequest>(){
		@Override
		public void handle(ArReadRequest event) {
			rid++;
			acks = 0;
			readlist = new ArrayList<>();
			reading = true;
			trigger(new BebBroadcast(new ReadMessage(self,rid)),beb);
		}
	};
	
	private Handler<ReadMessage> ReadBroadcast = new Handler<ReadMessage>(){
		@Override
		public void handle(ReadMessage event) {
			trigger(new Pp2pSend(event.getSource(),
					new ReadValueMessage(event,triple)),pp2p);
		}
	};
	
	private Handler<ReadValueMessage> HandleReads = new Handler<ReadValueMessage>(){
		@Override
		public void handle(ReadValueMessage event) {
			if(event.getDeliverent().getRid()==rid){
				readlist.add(event.getTp());
				if(readlist.size()>(N/2)){
					Triple highest = new Triple(0,0,0);
					for(Triple r: readlist){
						if(r.getTs()>=highest.getTs()&&r.getWr()>highest.getWr()){
							highest = r;
						} 
						else if(r.getTs()>highest.getTs()) {
							highest = r;
						}
					}
					readval = highest.getVal();
					if(reading){
						trigger(new BebBroadcast(new WriteMessage(self,rid,highest)),beb);
					} 
					else {
						trigger(new BebBroadcast(new WriteMessage(self,rid,
								new Triple(highest.getTs()+1,self.getId(),writeval))),beb);
					}
					readlist = new ArrayList<>();
				}
			}
		}
	};
	
	private Handler<ArWriteRequest> arWriteRequest = new Handler<ArWriteRequest>(){
		@Override
		public void handle(ArWriteRequest event) {
			rid++;
			writeval = event.getValue();
			acks = 0;
			readlist = new ArrayList<>();
			trigger(new BebBroadcast(new ReadMessage(self,rid)),beb);
		}
	};
	
	private Handler<WriteMessage> wr = new Handler<WriteMessage>(){
		@Override
		public void handle(WriteMessage event) {
			if(event.getTp().getTs()>=triple.getTs()&&event.getTp().getWr()>triple.getWr()){
				triple = new Triple(event.getTp());
			} 
			else if(event.getTp().getTs()>triple.getTs()) {
				triple = new Triple(event.getTp());
			}
			trigger(new Pp2pSend(event.getSource(), new ACKMessage(self,rid)),pp2p);
		}
	};
	
	private Handler<ACKMessage> ACK = new Handler<ACKMessage>(){
		@Override
		public void handle(ACKMessage event) {
			acks++;
			if(acks>(N/2)){
				acks=0;
				if(reading){
					reading = false;
					trigger(new ArReadResponse(readval),nnar);
				} 
				else {
					trigger(new ArWriteResponse(),nnar);
				}
			}
		}
	};
}
