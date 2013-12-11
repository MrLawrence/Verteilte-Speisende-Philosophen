package verteilt.tischteilserver;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import verteilt.Philosoph;
import verteilt.Tisch;

public class Stuhl implements Serializable {
	private static final long serialVersionUID = 48981734896023809L;
	private final static Logger LOG = Logger.getLogger(Stuhl.class.getName());
	private static AtomicInteger nextId = new AtomicInteger();
	private Integer id;

	private Gabel linkeGabel;
	private Gabel rechteGabel;
	private Philosoph sitzenderPhilosoph;

	public Stuhl(Tischteil tischteil, Gabel linkeGabel, Gabel rechteGabel) {
		this.id = nextId.incrementAndGet();
		this.linkeGabel = linkeGabel;
		this.rechteGabel = rechteGabel;
		LOG.info("Stuhl #" + this.id + " erzeugt");
	}

	public Stuhl hinsetzen(Philosoph philosoph) {
		if (this.istFrei()) {
			this.sitzenderPhilosoph = philosoph;
		} else {
			LOG.severe(philosoph.toString() + "versuchte sich auf "
					+ this.toString() + "zu setzen, aber "
					+ sitzenderPhilosoph.toString() + "sitzt dort schon!");
		}
		return this;
	}

	public void aufstehen() {
		sitzenderPhilosoph = null;
	}

	public Boolean istLinkeGabelGroesser() {
		return linkeGabel.hasBiggerIdThan(rechteGabel);
	}

	public Gabel nimmLinkeGabel() {
		return nimmGabel(linkeGabel);
	}

	public Gabel nimmRechteGabel() {
		return nimmGabel(rechteGabel);
	}

	private Gabel nimmGabel(Gabel gabel) {
		gabel.nimm(sitzenderPhilosoph);
		return gabel;
	}

	public boolean istFrei() {
		return sitzenderPhilosoph == null;
	}

	@Override
	public String toString() {
		return "Stuhl #" + this.id;
	}
}
