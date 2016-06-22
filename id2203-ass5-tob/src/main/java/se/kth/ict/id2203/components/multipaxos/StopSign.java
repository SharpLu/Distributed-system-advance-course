package se.kth.ict.id2203.components.multipaxos;

import java.util.Set;

import se.sics.kompics.address.Address;

public interface StopSign {

	public Set<Address> getSuccedingConfiguration();

}
