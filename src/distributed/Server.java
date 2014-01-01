package distributed;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

public class Server {
	private final static Logger LOG = Logger.getLogger(Server.class
			.getName());

	public static void main(String args[]) throws RemoteException {
		Integer philosophersAmount = 5;
		Integer hungryPhilosophersAmount = 0;
		Integer chairAmount = 5;
		Integer runtime = 30; // in s

		LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

		TableInterface table = new Table(chairAmount);
		TableInterface stub = (TableInterface) UnicastRemoteObject
				.exportObject(table, 0);

		Registry registry = LocateRegistry.getRegistry();
		registry.rebind("table", stub);
	}
}
