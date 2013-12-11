package verteilt.tischteilserver;

import java.io.Serializable;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import verteilt.Philosoph;

public class Gabel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -880841900585741305L;
	private final static Logger LOG = Logger.getLogger(Gabel.class.getName());
	private static AtomicInteger nextId = new AtomicInteger();
	private Semaphore mutex = new Semaphore(1, true);

	private Integer id;

	private Philosoph besitzenderPhilosoph = null;

	public Gabel() {
		this.id = nextId.incrementAndGet();
		LOG.fine("Gabel #" + id + " erzeugt");
	}

	public Gabel nimm(Philosoph philosoph) {
		try {
			mutex.acquire();
			besitzenderPhilosoph = philosoph;
			return this;
			
		} catch (InterruptedException e) {
			LOG.info(this.toString() + " wurde abgebrochen");
			return null;
		}
	}

	public void legAb(Philosoph philosoph) {
		if (besitzenderPhilosoph == philosoph) {
			mutex.release();
			besitzenderPhilosoph = null;
		} else {
			LOG.severe(this.toString() + " gehört " + philosoph.toString()
					+ " nicht");
		}
	}

	public AtomicBoolean istBesitzer(Philosoph philosoph) {
		return new AtomicBoolean(besitzenderPhilosoph == philosoph);
	}

	
	public Boolean hasBiggerIdThan(Gabel andereGabel) {
		return andereGabel.isIdBiggerThan(this.id);
	}
	
	public Boolean isIdBiggerThan(Integer otherId) {
		return this.id > otherId;
	}

	@Override
	public String toString() {
		return "Gabel #" + id;
	}
}
