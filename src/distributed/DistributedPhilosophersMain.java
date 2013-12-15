package distributed;

import java.util.ArrayList;
import java.util.logging.Logger;

import distributed.tablepart.Philosopher;


public class DistributedPhilosophersMain {
	private final static Logger LOG = Logger
			.getLogger(DistributedPhilosophersMain.class.getName());

	public static void main(String args[]) {
		Integer philosopherAmount = 5;
		Integer hungryPhilosopherAmount = 0;
		Integer chairAmount = 5;
		Integer runtime = 30; // in s

		Coordinator coordinator = new Coordinator(chairAmount, philosopherAmount, hungryPhilosopherAmount);
	}
}
