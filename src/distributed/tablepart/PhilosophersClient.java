package distributed.tablepart;

import java.util.logging.Logger;

public class PhilosophersClient {
	private final static Logger LOG = Logger.getLogger(PhilosophersClient.class
			.getName());

	public static void main(String args[]) {
		TablePart tablePart = new TablePart();
		tablePart.connect(1099);
		while(true){
		
		}
	}
}
