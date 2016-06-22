package se.kth.ict.id2203.components.multipaxos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.ports.asc.AbortableSequenceConsensus;
import se.kth.ict.id2203.ports.asc.AscAbort;
import se.kth.ict.id2203.ports.asc.AscDecide;
import se.kth.ict.id2203.ports.asc.AscPropose;
import se.kth.ict.id2203.ports.fpl.FIFOPerfectPointToPointLink;
import se.kth.ict.id2203.ports.fpl.FplSend;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;

public class MultiPaxos extends ComponentDefinition {

	private static final Logger logger = LoggerFactory.getLogger(MultiPaxos.class);
	private Negative<AbortableSequenceConsensus> asc = provides(AbortableSequenceConsensus.class);
	private Positive<FIFOPerfectPointToPointLink> fpl = requires(FIFOPerfectPointToPointLink.class);
	private final Address self;
	private int t = 0;
	private final TreeSet<Integer> cfgs = new TreeSet<Integer>();
	private int cfg = 0;
	private Set<Address> pi;
	private final ArrayList<Object> seq = new ArrayList<Object>();
	private int decidedLen = 0;
	private Timestamp prepts = Timestamp.ZERO;
	private Timestamp ats = Timestamp.ZERO;
	private int acceptedLen = 0;
	private Timestamp pts = Timestamp.ZERO;
	private final ArrayList<Object> pv = new ArrayList<Object>();
	private int pl = 0;

	private final ArrayList<Object> proposedValues = new ArrayList<Object>();
	private final HashMap<Address, PrepareAckMessage> readlist = new HashMap<Address, PrepareAckMessage>();
	private final HashMap<Address, Integer> accepted = new HashMap<Address, Integer>();

	public MultiPaxos(MultiPaxosInit event) {
		subscribe(handlePropose, asc);
		subscribe(handlePrepare, fpl);
		subscribe(handleNackUnknownCfg, fpl);
		subscribe(handleNack, fpl);
		subscribe(handlePrepareAck, fpl);
		subscribe(handleAccept, fpl);
		subscribe(handleAcceptAck, fpl);
		subscribe(handleDecideAccepted, fpl);
		subscribe(handleDecide, fpl);
		subscribe(handleStoppedConfiguration, fpl);
		subscribe(handlePleaseUpdate, fpl);
		self = event.getSelfAddress();
		pi = event.getAllAddresses();
		cfgs.add(0);
		cfg = 0;
		logger.info("Constructing MultiPaxos component.");

	}

	private Handler<AscPropose> handlePropose = new Handler<AscPropose>() {
		@Override
		public void handle(AscPropose event) {
			logger.info(String.format("ArscPropose %s", event.getValue()));
			t = t + 1;
			Object v = event.getValue();
			if (pts.equals(Timestamp.ZERO)) {
				if (!pi.contains(self)) {
					trigger(new AscAbort(AscAbort.Reason.NOT_IN_CONFIGURATION),asc);
				} else {
					pts = new Timestamp(cfg, t, self.getId());
					pv.clear();
					pv.addAll(seq.subList(0, decidedLen));  // pv is prefex
					pl = 0;
					proposedValues.clear();
					proposedValues.add(v);
					readlist.clear();
					accepted.clear();
					for (Address p : pi) {// loop for all address
						trigger(new FplSend(p, new PrepareMessage(self, pts,
								pv.size(), t)), fpl);
					}
				}
			} else {
				if (readlist.size() <= pi.size() / 2) { // 
					proposedValues.add(v);
				} else {
					int pvIndex = pv.indexOf(v);
					if (pvIndex != -1) {
						if (pvIndex < decidedLen) {
							trigger(new AscDecide(v), asc);
						}
					} else {
						if (!(cfg < pv.size() && pv.get(pv.size() - 1) instanceof StopSign)) {
							pv.add(v);
							Object[] vsuf = new Object[] { v };
							for (Address p : readlist.keySet()) {
								trigger(new FplSend(p, new AcceptMessage(self,
										pts, vsuf, pv.size() - 1, t)), fpl);
							}
						}
					}
				}
			}
		}
	};

	private Handler<PrepareMessage> handlePrepare = new Handler<PrepareMessage>() {
		@Override
		public void handle(PrepareMessage event) {
			logger.info(String.format(
					"PrepareMessage source = %s, ts = %s, dl = %d",
					event.getSource(), event.getTs(), event.getDl()));
			t = Math.max(t, event.getT()) + 1;
			Timestamp ts = event.getTs();
			if (ts.getCfg() > cfg) {
				trigger(new FplSend(event.getSource(),
						new NackUnknownCfgMessage(self, ts, decidedLen, t)),
						fpl);
			} else if (ts.getCfg() < cfg) {
				int from = event.getDl();
				Object[] vsuf = seq.subList(from, cfgs.higher(from)).toArray();
				trigger(new FplSend(event.getSource(), new DecideMessage(self,
						vsuf, from, t)), fpl);
			} else if (ts.compareTo(prepts) < 0) {
				trigger(new FplSend(event.getSource(), new NackMessage(self,
						ts, t)), fpl);
			} else {
				if (!event.getSource().equals(self)
						&& !pts.equals(Timestamp.ZERO)) {
					pts = Timestamp.ZERO;
					trigger(new AscAbort(
							AscAbort.Reason.ABORTED_BY_HIGHER_ROUND), asc);
				}
				prepts = ts;
				Object[] vsuf = event.getDl() < acceptedLen ? seq.subList(
						event.getDl(), acceptedLen).toArray() : new Object[0];
				trigger(new FplSend(event.getSource(), new PrepareAckMessage(
						self, ts, ats, vsuf, decidedLen, t)), fpl);
			}
		}
	};

	private Handler<NackUnknownCfgMessage> handleNackUnknownCfg = new Handler<NackUnknownCfgMessage>() {
		@Override
		public void handle(NackUnknownCfgMessage event) {
			logger.info(String.format(
					"NackUnkonwnCfgMessage source = %s, ts = %s, dl = %d",
					event.getSource(), event.getTs(), event.getDl()));
			t = Math.max(t, event.getT()) + 1;
			if (pts.equals(event.getTs())) {
				int offs = event.getDl();
				Object[] vsuf = seq.subList(offs, decidedLen).toArray();
				trigger(new FplSend(event.getSource(), new DecideMessage(self,
						vsuf, offs, t)), fpl);
				trigger(new FplSend(event.getSource(), new PrepareMessage(self,
						pts, pv.size(), t)), fpl);
			}
		}
	};

	private Handler<NackMessage> handleNack = new Handler<NackMessage>() {
		@Override
		public void handle(NackMessage event) {
			logger.info(String.format("NackMessage source = %s, pts = %s",
					event.getSource(), event.getPts()));
			t = Math.max(t, event.getT()) + 1;
			if (pts.equals(event.getPts())) {
				pts = Timestamp.ZERO;
				trigger(new AscAbort(AscAbort.Reason.ABORTED_BY_HIGHER_ROUND),
						asc);
			}
		}
	};

	private Handler<PrepareAckMessage> handlePrepareAck = new Handler<PrepareAckMessage>() {
		@Override
		public void handle(PrepareAckMessage event) {
			logger.info(String
					.format("PrepareAckMessage source = %s, pts = %s, |vsuf| = %d, dl = %d",
							event.getSource(), event.getPts(),
							event.getVsuf().length, event.getDl()));
			t = Math.max(t, event.getT()) + 1;
			if (pts.equals(event.getPts())) {
				readlist.put(event.getSource(), event);
				if (readlist.size() == pi.size() / 2 + 1) {
					PrepareAckMessage max = null;
					for (PrepareAckMessage m : readlist.values()) {
						if (max == null || max.getTs().compareTo(m.getTs()) < 0
								|| max.getTs().compareTo(m.getTs()) == 0
								&& max.getVsuf().length < m.getVsuf().length) {
							max = m;
						}
					}
					pv.addAll(Arrays.asList(max.getVsuf()));
					for (Object o : proposedValues) {
						int pvIndex = pv.indexOf(o);
						if (pvIndex != -1) {
							if (pvIndex < decidedLen) {
								trigger(new AscDecide(o), asc);
							}
						} else if (!(cfg < pv.size() && pv.get(pv.size() - 1) instanceof StopSign)) {
							pv.add(o);
						}
					}
					for (Address p : readlist.keySet()) {
						int l = readlist.get(p).getDl();
						Object[] vsuf = pv.subList(l, pv.size()).toArray();
						trigger(new FplSend(p, new AcceptMessage(self, pts,
								vsuf, l, t)), fpl);
					}
				} else if (readlist.size() > pi.size() / 2 + 1) {
					Address p = event.getSource();
					int l = readlist.get(p).getDl();
					Object[] vsuf = pv.subList(l, pv.size()).toArray();
					trigger(new FplSend(p, new AcceptMessage(self, pts, vsuf,
							l, t)), fpl);
					if (pl != 0) {
						trigger(new FplSend(p, new DecideAcceptedMessage(self,
								pts, pl, t)), fpl);
					}
				}
			}
		}
	};

	private Handler<AcceptMessage> handleAccept = new Handler<AcceptMessage>() {
		@Override
		public void handle(AcceptMessage event) {
			logger.info(String
					.format("AcceptMessage source = %s, ts = %s, |vsuf| = %d, offs = %d",
							event.getSource(), event.getTs(),
							event.getVsuf().length, event.getOffs()));
			t = Math.max(t, event.getT()) + 1;
			Timestamp ts = event.getTs();
			if (!ts.equals(prepts)) {
				trigger(new FplSend(event.getSource(), new NackMessage(self,
						ts, t)), fpl);
			} else {
				ats = ts;
				if (event.getOffs() < seq.size()) {
					seq.subList(event.getOffs(), seq.size()).clear();
				}
				seq.addAll(Arrays.asList(event.getVsuf()));
				acceptedLen = event.getOffs() + event.getVsuf().length;
				trigger(new FplSend(event.getSource(), new AcceptAckMessage(
						self, ts, acceptedLen, t)), fpl);
			}
		}
	};

	private Handler<AcceptAckMessage> handleAcceptAck = new Handler<AcceptAckMessage>() {
		@Override
		public void handle(AcceptAckMessage event) {
			logger.info(String.format(
					"AcceptAckMessage source = %s, pts = %s, al = %d",
					event.getSource(), event.getPts(), event.getAl()));
			t = Math.max(t, event.getT()) + 1;
			if (pts.equals(event.getPts())) {
				int l = event.getAl();
				accepted.put(event.getSource(), l);
				if (pl < l && isSupported(l)) {
					pl = l;
					for (Address p : readlist.keySet()) {
						trigger(new FplSend(p, new DecideAcceptedMessage(self,
								pts, pl, t)), fpl);
					}
				}
			}
		}
	};

	private boolean isSupported(int l) {
		int cnt = 0;
		for (int l2 : accepted.values()) {
			if (l2 >= l) {
				cnt += 1;
			}
		}
		return cnt > pi.size() / 2;
	}

	private void decideValue(int index) {
		Object o = seq.get(index);
		if (o instanceof StopSign) {
			StopSign ss = (StopSign) o;
			if (pi.contains(self)) {
				TreeSet<Address> sendTo = new TreeSet<Address>(pi);
				sendTo.addAll(ss.getSuccedingConfiguration());
				sendTo.remove(self);
				for (Address p : sendTo) {
					trigger(new FplSend(p, new StoppedConfigurationMessage(
							self, cfg, index + 1, t)), fpl);
				}
			}
			cfg = index + 1;
			cfgs.add(cfg);
			pi = ss.getSuccedingConfiguration();
			ats = new Timestamp(cfg, 0, 0);
			acceptedLen = cfg;
			prepts = ats;
			pts = Timestamp.ZERO;
		}
		trigger(new AscDecide(o), asc);
	}

	private Handler<DecideAcceptedMessage> handleDecideAccepted = new Handler<DecideAcceptedMessage>() {
		@Override
		public void handle(DecideAcceptedMessage event) {
			logger.info(String.format(
					"DecideAcceptMessage source = %s, ts = %s, l = %d",
					event.getSource(), event.getTs(), event.getL()));
			t = Math.max(t, event.getT()) + 1;
			if (prepts.equals(event.getTs())) {
				while (decidedLen < event.getL()) {
					decideValue(decidedLen);
					decidedLen += 1;
				}
			}
		}
	};

	private Handler<DecideMessage> handleDecide = new Handler<DecideMessage>() {
		@Override
		public void handle(DecideMessage event) {
			logger.info(String.format(
					"DecideMessage source = %s, |vsuf| = %d, offs = %d",
					event.getSource(), event.getVsuf().length, event.getOffs()));
			t = Math.max(t, event.getT()) + 1;
			Object[] vsuf = event.getVsuf();
			int offs = event.getOffs();
			while (decidedLen < offs + vsuf.length) {
				Object o = vsuf[decidedLen - offs];
				if (seq.size() == decidedLen) {
					seq.add(o);
				} else {
					seq.set(decidedLen, o);
				}
				decideValue(decidedLen);
				decidedLen += 1;
			}
		}
	};

	private Handler<StoppedConfigurationMessage> handleStoppedConfiguration = new Handler<StoppedConfigurationMessage>() {
		@Override
		public void handle(StoppedConfigurationMessage event) {
			logger.info(String
					.format("StoppedConfigurationMessage source = %s, cfg = %d, nextCfg = %d",
							event.getSource(), event.getCfg(),
							event.getNextCfg()));
			t = Math.max(t, event.getT()) + 1;
			if (decidedLen < event.getNextCfg()) {
				trigger(new FplSend(event.getSource(), new PleaseUpdateMessage(
						self, decidedLen, event.getNextCfg(), t)), fpl);
			}
		}
	};

	private Handler<PleaseUpdateMessage> handlePleaseUpdate = new Handler<PleaseUpdateMessage>() {
		@Override
		public void handle(PleaseUpdateMessage event) {
			logger.info(String.format(
					"PleaseUpdateMessage source = %s, from = %d, to = %d",
					event.getSource(), event.getFrom(), event.getTo()));
			t = Math.max(t, event.getT()) + 1;
			Object[] vsuf = seq.subList(event.getFrom(), event.getTo())
					.toArray();
			trigger(new FplSend(event.getSource(), new DecideMessage(self,
					vsuf, event.getFrom(), t)), fpl);
		}
	};
}
