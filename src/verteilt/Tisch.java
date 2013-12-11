package verteilt;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import verteilt.tischteilserver.TischteilMain;
import verteilt.tischteilserver.Stuhl;
import verteilt.tischteilserver.TischteilInterface;

public class Tisch {
	private final static Logger LOG = Logger.getLogger(Tisch.class.getName());
	private List<TischteilInterface> tischteile = new ArrayList<TischteilInterface>();
	

	public Tisch(Integer stuhlAmount) {
		try {
			Registry registry = LocateRegistry.getRegistry("127.0.0.1");
			tischteile.add((TischteilInterface) registry.lookup("TischteilInterface"));
			tischteile.get(0).deckeTisch(stuhlAmount, true);

		} catch (RemoteException | NotBoundException e) {
			LOG.severe(e.getMessage());
		}
	}

	public Stuhl findeStuhl(Philosoph philosoph) {
		try {
			return tischteile.get(0).getStuhl();
		} catch (RemoteException e) {
			LOG.severe("Konnte findeStuhl nicht ausführen für " + philosoph.toString());
			e.printStackTrace();
			return null;
		}
	}

	public void aufstehen(Stuhl stuhl) {

	}
}
