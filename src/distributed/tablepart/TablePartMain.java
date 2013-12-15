package distributed.tablepart;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

public class TablePartMain {
	private final static Logger LOG = Logger.getLogger(TablePartMain.class
			.getName());

	public static void main(String args[]) {

		TablePart tablePart = new TablePart();
		TablePartInterface stub;
		try {
			stub = (TablePartInterface) UnicastRemoteObject.exportObject(
					tablePart, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("TablePartInterface", stub);
			LOG.info("Successfully registered");
		} catch (RemoteException | AlreadyBoundException e) {
			LOG.severe("Registration didn't work: " + e.getMessage());
		}
	}
}
