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
package se.kth.ict.id2203.pa.epfd;

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.kth.ict.id2203.components.epfd.Epfd;
import se.kth.ict.id2203.components.epfd.EpfdInit;
import se.kth.ict.id2203.ports.epfd.EventuallyPerfectFailureDetector;
import se.kth.ict.id2203.ports.pp2p.PerfectPointToPointLink;
import se.sics.kompics.Negative;
import se.sics.kompics.Positive;
import se.sics.kompics.address.Address;
import se.sics.kompics.launch.Topology;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timeout;
import se.sics.kompics.timer.Timer;


public final class ApplicationContinue extends Timeout {
	

	
	

	public ApplicationContinue(ScheduleTimeout request) {
		
		super(request);
	}

}
