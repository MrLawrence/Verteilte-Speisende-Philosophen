package verteilt.tischteilserver;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

public class TischteilMain {
	private final static Logger LOG = Logger.getLogger(TischteilMain.class
			.getName());

	public static void main(String args[]) {

		Tischteil tischteil = new Tischteil();
		TischteilInterface stub;
		try {
			stub = (TischteilInterface) UnicastRemoteObject.exportObject(
					tischteil, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("TischteilInterface", stub);
			LOG.info("Successfully registered");
		} catch (RemoteException | AlreadyBoundException e) {
			LOG.severe("Registrieren ging nicht: " + e.getMessage());
		}
	}
}
