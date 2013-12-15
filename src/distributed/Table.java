package distributed;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import distributed.tablepart.Chair;
import distributed.tablepart.TablePartInterface;
import distributed.tablepart.TablePart;

public class Table {
	private final static Logger LOG = Logger.getLogger(Table.class.getName());
	private List<TablePartInterface> tableParts = new ArrayList<TablePartInterface>();
	private Semaphore freeChairs;
	private Integer chairAmount;

	public Table(Integer chairAmount) {
		freeChairs = new Semaphore(chairAmount, true);
		this.chairAmount = chairAmount;
	}

	public void standUp(Chair chair) {
		freeChairs.release();
		try {
			tableParts.get(0).standUp(chair);
		} catch (RemoteException e) {
			LOG.severe("Couldn't connect");
		}
	}

	public void addTablePart(TablePartInterface tablePart) {
		tableParts.add(tablePart);
	}

	public void layTable() {
		Integer tablePartAmount = tableParts.size();
		Integer chairsPerTablePart = this.chairAmount / tablePartAmount;
		for (TablePartInterface t : tableParts) {
			try {
				t.addChairsandForks(chairsPerTablePart, tablePartAmount == 1);
			} catch (RemoteException e) {
				LOG.severe("Couldn't connect");
			}
		}
	}

	public void createPhilosophers(Integer philosophersAmount) {
		for (int i = 0; i < philosophersAmount; i++) {
			try {
				tableParts.get(0).createPhilosopher(false);
			} catch (RemoteException e) {
				LOG.severe("Couldn't connect");
			}
		}
	}
}
