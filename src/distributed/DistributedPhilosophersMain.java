package distributed;

import java.util.logging.Logger;

public class DistributedPhilosophersMain {
	private final static Logger LOG = Logger
			.getLogger(DistributedPhilosophersMain.class.getName());

	public static void main(String args[]) {
		Integer philosophersAmount = 5;
		Integer hungryPhilosophersAmount = 0;
		Integer chairAmount = 5;
		Integer runtime = 30; // in s

		Coordinator coordinator = new Coordinator(chairAmount,
				philosophersAmount, hungryPhilosophersAmount);
	}
}
