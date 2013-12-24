package distributed;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

import distributed.tablepart.TablePartInterface;

public class Coordinator {
	private final static Logger LOG = Logger.getLogger(Coordinator.class
			.getName());
	Table table;

	Registry registry;

	public Coordinator(Integer chairAmount, Integer philosopherAmount,
			Integer hungryPhilosopherAmount) {
		table = new Table(chairAmount);

		this.register("127.0.0.1", 1099);
		this.register("127.0.0.1", 55441);

		table.layTable();
		table.createPhilosophers(philosopherAmount);
	}

	private void register(String ip, Integer port) {
		try {
			registry = LocateRegistry.getRegistry(ip, port);
			table.register((TablePartInterface) registry
					.lookup("TablePartInterface"));
		} catch (RemoteException | NotBoundException e) {
			LOG.severe("Couldn't connect");
		}
	}
}
