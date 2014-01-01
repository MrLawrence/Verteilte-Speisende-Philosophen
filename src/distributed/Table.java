package distributed;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.rmi.server.UnicastRemoteObject;

import distributed.tablepart.TablePartInterface;
import distributed.tablepart.TablePart;

public class Table implements TableInterface {
	private final static Logger LOG = Logger.getLogger(Table.class.getName());
	private List<TablePart> tableParts = new ArrayList<TablePart>();
	private Integer chairAmount;
	private Integer ids = 1;
	private Random random = new Random();

	public Table(Integer chairAmount) throws RemoteException {
		this.chairAmount = chairAmount;
	}

	public void register(TablePart tablePart) throws RemoteException {
		try {
			tablePart.setID(ids++);
		} catch (RemoteException e) {
			LOG.severe("Couldn't connect");
		}
		tableParts.add(tablePart);
		LOG.info("New TablePart registered");
	}

	public void layTable() throws RemoteException {
		Integer tablePartAmount = tableParts.size();
		Integer chairsPerTablePart = this.chairAmount / tablePartAmount;
		LOG.info(tablePartAmount + " table parts with " + chairsPerTablePart
				+ " chairs each");
		for (TablePartInterface t : tableParts) {
			try {
				t.addChairsandForks(chairsPerTablePart, tablePartAmount == 1);
			} catch (RemoteException e) {
				LOG.severe("Couldn't reach");
			}
		}
	}

	public void createPhilosophers(Integer philosophersAmount) {
		try {
			for (Integer i = 0; i < philosophersAmount; i++) {
				tableParts.get(random.nextInt(tableParts.size()))
						.createPhilosopher(false);
			}
		} catch (RemoteException e) {
			LOG.severe("Couldn't connect");
		}
	}

	@Override
	public TablePart getNextTablePart(TablePart tablePart)
			throws RemoteException {
		return tableParts.get((tableParts.indexOf(tablePart) + 1)
				% tableParts.size());
	}
}
