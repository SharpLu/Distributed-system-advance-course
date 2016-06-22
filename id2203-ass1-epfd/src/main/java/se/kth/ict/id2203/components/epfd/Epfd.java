package se.kth.ict.id2203.components.epfd;

import java.util.Collections;
import java.util.HashSet;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.kth.ict.id2203.ports.epfd.EventuallyPerfectFailureDetector;
import se.kth.ict.id2203.ports.epfd.Restore;
import se.kth.ict.id2203.ports.epfd.Suspect;
import se.kth.ict.id2203.ports.pp2p.PerfectPointToPointLink;
import se.kth.ict.id2203.ports.pp2p.Pp2pSend;
import se.sics.kompics.ComponentDefinition;
import se.sics.kompics.Handler;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.Start;
import se.sics.kompics.address.Address;

import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

public class Epfd extends ComponentDefinition {

	private static final Logger logger = LoggerFactory.getLogger(Epfd.class);

	private Positive<Timer> timer = requires(Timer.class);
	private Negative<EventuallyPerfectFailureDetector> epfd = provides(EventuallyPerfectFailureDetector.class);
	private Positive<PerfectPointToPointLink> pp2p = requires(PerfectPointToPointLink.class);

	private Address self;
	private Set<Address> p;
	private Set<Address> alive;
	private Set<Address> suspected;

	private int seqnum;
	private long delay;
	private long delta;

	public Epfd(EpfdInit init) {
		logger.info("[Epfd] Constructor");
		subscribe(handleRequest, pp2p);
		subscribe(handleReply, pp2p);
		subscribe(handleTimeout, timer);
		subscribe(start, control);

		self = init.getSelfAddress();
		p = new HashSet<Address>(init.getAllAddresses());
		alive = new HashSet<Address>(init.getAllAddresses());
		delay = init.getInitialDelay();
		delta = init.getDeltaDelay();
		seqnum = 0;
		suspected = new HashSet<Address>();

	}

	private Handler<HeartbeatRequestMessage> handleRequest = new Handler<HeartbeatRequestMessage>() {
		@Override
		public void handle(HeartbeatRequestMessage event) {
			logger.info("Received hbRequestMessage {}", event.getSequnce());
			trigger(new Pp2pSend(event.getSource(), new HeartbeatReplyMessage(
					self, event.getSequnce())), pp2p);
		}
	};
	private Handler<HeartbeatReplyMessage> handleReply = new Handler<HeartbeatReplyMessage>() {
		@Override
		public void handle(HeartbeatReplyMessage event) {
			logger.info("Received hbReplyMessage {}", event.getSequence());
			if (event.getSequence() == seqnum | suspected.contains(event.getSource())) {
				alive.add(event.getSource());
			}
		}
	};
	private Handler<CheckTimeout> handleTimeout = new Handler<CheckTimeout>() {
		@Override
		public void handle(CheckTimeout event) {
			if (!Collections.disjoint(alive, suspected)) {
				logger.info("Someones probably dead" + "  " + delay);
				delay = delay + delta;
			}
			seqnum++;

			for (Address a : p) {

				if (!alive.contains(a) && !suspected.contains(a)) {
					suspected.add(a);
					trigger(new Suspect(a), epfd);

				} else if (alive.contains(a) && suspected.contains(a)) {
					suspected.remove(a);
					trigger(new Restore(a), epfd);

				}
				logger.info("sending heartbeat");
				trigger(new Pp2pSend(a, new HeartbeatRequestMessage(self,
						seqnum)), pp2p);

			}
			alive.clear();
			ScheduleTimeout st = new ScheduleTimeout(delay);
			st.setTimeoutEvent(new CheckTimeout(st));
			trigger(st, timer);

		}
	};

	private Handler<Start> start = new Handler<Start>() {
		@Override
		public void handle(Start event) {
			startCheck(delay);
		}

	};

	private void startCheck(long check) {
		logger.info("Sleeping {} milliseconds...", check);
		ScheduleTimeout st = new ScheduleTimeout(check);
		st.setTimeoutEvent(new CheckTimeout(st));
		trigger(st, timer);

	}

}