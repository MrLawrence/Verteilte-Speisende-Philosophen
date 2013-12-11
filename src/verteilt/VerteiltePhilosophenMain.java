package verteilt;

import java.util.ArrayList;
import java.util.logging.Logger;


public class VerteiltePhilosophenMain {
	private final static Logger LOG = Logger
			.getLogger(VerteiltePhilosophenMain.class.getName());

	public static void main(String args[]) {
		Integer anzahlPhilosophen = 5;
		Integer anzahlHungrigePhilosophen = 2;
		Integer anzahlStuehle = 5;
		Integer gesamtlaufzeit = 30; // in s

		LOG.info("Zu generierende Philosophen: " + anzahlPhilosophen);
		LOG.info("Zu generierende Stuehle: " + anzahlStuehle);
		Tisch tisch = new Tisch(anzahlStuehle);
		
		ArrayList<Philosoph> philosophen = new ArrayList<Philosoph>();
		ArrayList<Thread> philosophenThreads = new ArrayList<Thread>();
		
		for (int i = 0; i < anzahlPhilosophen; i++) {
			Philosoph philosoph = new Philosoph(tisch, false);
			philosophenThreads.add(new Thread(philosoph));
			philosophen.add(philosoph);
		}

		for (int i = 0; i < anzahlHungrigePhilosophen; i++) {
			Philosoph philosoph = new Philosoph(tisch, true);
			philosophenThreads.add(new Thread(philosoph));
			philosophen.add(philosoph);
		}
		
		for (Thread t : philosophenThreads) {
			t.start();
		}
	}
}
