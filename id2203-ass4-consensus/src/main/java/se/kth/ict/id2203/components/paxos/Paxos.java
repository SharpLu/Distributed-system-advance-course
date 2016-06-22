package se.kth.ict.id2203.components.paxos;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.kth.ict.id2203.ports.ac.AbortableConsensus;
import se.kth.ict.id2203.ports.ac.AcAbort;
import se.kth.ict.id2203.ports.ac.AcDecide;
import se.kth.ict.id2203.ports.ac.AcPropose;
import se.kth.ict.id2203.ports.beb.BebBroadcast;
import se.kth.ict.id2203.ports.beb.BestEffortBroadcast;
import se.kth.ict.id2203.ports.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.ports.pp2p.Pp2pSend;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;

public class Paxos extends ComponentDefinition {

	private static final Logger logger = LoggerFactory.getLogger(Paxos.class);

	private Negative<AbortableConsensus> ac = provides(AbortableConsensus.class);
	private Positive<BestEffortBroadcast> beb = requires(BestEffortBroadcast.class);
	private Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);

	private Address self;
	private int N;
	private int t;
	private int prepts;
	private AcceptedTV atv;
	private ProposeTV ptv;
	private ArrayList<AcceptedTV> readlist;


	private int acks;

	public Paxos(PaxosInit init) {
		t = 0;
		prepts = 0;
		atv = new AcceptedTV(0, 0);
		ptv = new ProposeTV(0, 0);

		readlist = new ArrayList<AcceptedTV>();
		acks = 0;
		self = init.getSelfAddress();
		N = init.getAllAddresses().size();

		subscribe(propose, ac);
		subscribe(acceptmessage, beb);
		subscribe(nackmessage, pp2p);
		subscribe(ppamessage, pp2p);
		subscribe(amessage, beb);
		subscribe(ackmessage, pp2p);

	}

	private Handler<AcPropose> propose = new Handler<AcPropose>() {
		@Override
		public void handle(AcPropose event) {
			t = t + 1;

			ptv.setPts(t * N + self.getId());
			ptv.setPv(event.getValue());
			// ptv = new ProposeTV(t*N+self.getId(), event.getValue());
			readlist = new ArrayList<AcceptedTV>();
			acks = 0;

			trigger(new BebBroadcast(new PrepareMessage(self, ptv.getPts(), t)),
					beb);
			// trigger(new BebBroadcast(new PrepareMessage(self,ptv,t)),beb);

		}
	};

	private Handler<PrepareMessage> acceptmessage = new Handler<PrepareMessage>() {
		@Override
		public void handle(PrepareMessage event) {
			// logger.info(String.format("Prepare message(source = %s, ts = %d)",
			// event.getSource(), event.getPts()));
			t = Math.max(t, event.getT()) + 1;
			if (event.getPts() < prepts) {
				trigger(new Pp2pSend(event.getSource(), new NackMessage(self,
						event.getPts(), t)), pp2p);
			} else {
				prepts = event.getPts();
				// trigger(new Pp2pSend(event.getSource(),new
				// PrepareAckMessage(self,atv,event,t)),pp2p);
				trigger(new Pp2pSend(event.getSource(), new PrepareAckMessage(
						self, atv, event.getPts(), t)), pp2p);
			}

		}
	};

	private Handler<NackMessage> nackmessage = new Handler<NackMessage>() {
		@Override
		public void handle(NackMessage event) {
			t = Math.max(t, event.getT()) + 1;
			if (event.getTs() == ptv.getPts()) {
				// ptv = new ProposeTV(0,0);
				ptv.setPts(0);
				trigger(new AcAbort(), ac);

			}

		}
	};

	private Handler<PrepareAckMessage> ppamessage = new Handler<PrepareAckMessage>() {
		@Override
		public void handle(PrepareAckMessage event) {

			t = Math.max(t, event.getT()) + 1;
			if (event.getPts() == ptv.getPts()) {
				readlist.add(event.getAtv());

				if (readlist.size() > (N / 2)) {

					AcceptedTV highest = new AcceptedTV(0, 0);

					for (AcceptedTV r : readlist) {
						if (r.getAts() > highest.getAts()) {

							highest = r;
						}
					}

					if (!(highest.getAts() == 0)) {

						// ptv = new
						// ProposeTV(event.getPmDeliverEvent().getPts(),highest.getAv());
						ptv.setPv(highest.getAv());

					}
					readlist = new ArrayList<AcceptedTV>();
					trigger(new BebBroadcast(new AcceptMessage(self,
							ptv.getPts(), ptv.getPv(), t)), beb);
				}

			}

		}
	};

	private Handler<AcceptMessage> amessage = new Handler<AcceptMessage>() {
		@Override
		public void handle(AcceptMessage event) {
			t = Math.max(t, event.getT()) + 1;

			if (event.getPts() < prepts) {
				trigger(new Pp2pSend(event.getSource(), new NackMessage(self,
						event.getPts(), t)), pp2p);
			} else {
				prepts = event.getPts();
				atv.setAts(event.getPts());
				atv.setAv(event.getPv());
				// atv=new AcceptedTV(
				// event.getTv().getPts(),event.getTv().getPv());

				trigger(new Pp2pSend(event.getSource(), new AcceptAckMessage(
						self, event.getPts(), t)), pp2p);
			}

		}
	};

	private Handler<AcceptAckMessage> ackmessage = new Handler<AcceptAckMessage>() {
		@Override
		public void handle(AcceptAckMessage event) {
			t = Math.max(t, event.getT()) + 1;
			if (event.getTs() == ptv.getPts()) {
				acks = acks + 1;
				if (acks > (N / 2)) {
					ptv.setPts(0);
					trigger(new AcDecide(ptv.getPv()), ac);

				}
			}
		}
	};

}
