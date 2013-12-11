package verteilt.tischteilserver;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import verteilt.Philosoph;

public class Tischteil implements TischteilInterface {
	private final static Logger LOG = Logger.getLogger(Tischteil.class
			.getName());
	private static AtomicInteger nextId = new AtomicInteger();
	private Integer id;
	private List<Stuhl> stuehle = new ArrayList<Stuhl>();
	private List<Gabel> gabeln = new ArrayList<Gabel>();

	public Tischteil() {
		this.id = nextId.incrementAndGet();
		LOG.info("Tischteil #" + id + " erstellt");
	}

	public void deckeTisch(Integer stuhlAmount, Boolean istEinzigerTeil)
			throws RemoteException {
		if (istEinzigerTeil) {
			Integer gabelAmount = stuhlAmount;
			for (int i = 0; i < gabelAmount; i++) {
				gabeln.add(new Gabel());
			}

			for (int i = 0; i < stuhlAmount; i++) {
				if (i == stuhlAmount - 1) {
					stuehle.add(new Stuhl(this, gabeln.get(i), gabeln.get(0)));
				} else {
					stuehle.add(new Stuhl(this, gabeln.get(i), gabeln
							.get(i + 1)));
				}
			}
		} else {
			// TODO mehrere Tischteile
		}
	}

	@Override
	public Stuhl getStuhl() throws RemoteException {
		Stuhl gefundenerStuhl = null;

		for (int i = 0; i < stuehle.size(); i++) {
			Stuhl dieserStuhl = stuehle.get(i);
			if (dieserStuhl.istFrei()) {
				gefundenerStuhl = dieserStuhl;
				break;
			}
		}
		return gefundenerStuhl;
	}
}
