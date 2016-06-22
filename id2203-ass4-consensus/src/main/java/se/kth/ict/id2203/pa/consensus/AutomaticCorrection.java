package se.kth.ict.id2203.pa.consensus;

import se.kth.ict.id2203.sim.consensus.ConsensusTester;

public class AutomaticCorrection {
	public static void main(String[] args) {
		String email = "fenlu@kth.se";
		String password = "EEFt8k";
		ConsensusTester.correctAndSubmit(email, password);
	}
}
