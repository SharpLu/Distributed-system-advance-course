package se.kth.ict.id2203.pa.shm;

import se.kth.ict.id2203.sim.shm.SharedMemoryTester;

public class AutomaticCorrection {
	public static void main(String[] args) {
		String email = "fenlu@kth.se";
		String password = "EEFt8k";
		SharedMemoryTester.correctAndSubmit(email, password);
	}
}
