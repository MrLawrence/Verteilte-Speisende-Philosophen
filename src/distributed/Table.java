package distributed;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

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
		for (TablePart t : tableParts) {
			t.notifyNewPart(tablePart);
			tablePart.notifyNewPart(t);
		}

		tableParts.add(tablePart);
		LOG.info("TablePart #" + tablePart.getID() + " registered");
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

	public void createPhilosophers(Integer philosophersAmount, Boolean isHungry) {
		try {
			for (Integer i = 0; i < philosophersAmount; i++) {
				tableParts.get(random.nextInt(tableParts.size()))
						.createPhilosopher(isHungry);
			}
		} catch (RemoteException e) {
			LOG.severe("Couldn't connect");
		}
	}

	public void killPhilosophers(Integer philosopherAmount)
			throws RemoteException {
		for (Integer i = 0; i < philosopherAmount; i++) {
			Boolean removed = false;
			while (!removed) {
				TablePart randomPart = tableParts.get(random.nextInt(tableParts
						.size()));
				if (randomPart.getPhilosopherAmount() > 0) {
					randomPart.killPhilosopher();
					removed = true;
				}
			}
		}
	}

	@Override
	public void createChair(Integer chairAmount) throws RemoteException {
		TablePart smallestTablePart = null;
		Integer smallestChairAmount = 99999;
		for (TablePart t : tableParts) {
			if (t.getChairAmount() < smallestChairAmount) {
				smallestChairAmount = t.getChairAmount();
				smallestTablePart = t;
			}
		}

		smallestTablePart.createChair();
	}

	@Override
	public void killChair(Integer chairAmount) throws RemoteException {
		TablePart largestTablePart = null;
		Integer largestChairAmount = 0;
		for (TablePart t : tableParts) {
			if (t.getChairAmount() > largestChairAmount) {
				largestChairAmount = t.getChairAmount();
				largestTablePart = t;
			}
		}
		largestTablePart.killChair();
	}
}
