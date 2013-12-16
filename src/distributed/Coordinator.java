package distributed;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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

		try {
			registry = LocateRegistry.getRegistry("127.0.0.1");
			table.addTablePart((TablePartInterface) registry
					.lookup("TablePartInterface"));

			registry = LocateRegistry.getRegistry("127.0.0.1", 55441);
			table.addTablePart((TablePartInterface) registry
					.lookup("TablePartInterface"));
		} catch (RemoteException | NotBoundException e) {
			LOG.severe("Couldn't connect");
		}

		table.layTable();
		table.createPhilosophers(philosopherAmount);
	}
}
