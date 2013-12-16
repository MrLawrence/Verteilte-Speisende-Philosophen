package distributed.tablepart;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class TablePart implements TablePartInterface {
	private final static Logger LOG = Logger.getLogger(TablePart.class
			.getName());
	private List<Chair> chairs = new ArrayList<Chair>();
	private List<ReentrantLock> forks = new ArrayList<ReentrantLock>();
	private List<Philosopher> philosophers = new ArrayList<Philosopher>();
	private Semaphore freeChairs;
	private Integer id = null;

	public synchronized Chair getFreeChair() {
		try {
			freeChairs.acquire();
		} catch (InterruptedException e) {
			LOG.info(this.toString() + " was interrupted");
		}
		Chair chairFound = null;

		for (int i = 0; i < chairs.size(); i++) {
			Chair someChair = chairs.get(i);
			if (someChair.empty()) {
				chairFound = someChair;
				break;
			}
		}
		return chairFound;
	}

	public void notifyFreeChair() {
		freeChairs.release();
	}

	@Override
	public String toString() {
		return "Table with " + chairs.size() + " Chairs";
	}

	@Override
	public void addChairsandForks(Integer chairAmount, Boolean isOnlyPart)
			throws RemoteException {
		freeChairs = new Semaphore(chairAmount, true);
		Integer gabelAmount = chairAmount;

		for (int i = 0; i < gabelAmount; i++) {
			forks.add(new ReentrantLock());
		}

		for (int i = 0; i < chairAmount; i++) {
			if (i == chairAmount - 1) {
				chairs.add(new Chair(forks.get(i), (isOnlyPart) ? forks.get(0)
						: null));
			} else {
				chairs.add(new Chair(forks.get(i), forks.get(i + 1)));
			}
		}
	}

	@Override
	public void standUp(Chair chair) throws RemoteException {
		freeChairs.release();
		chair.leave();
	}

	@Override
	public void createPhilosophers(Integer philosophersAmount,
			Integer hungryPhilosophersAmount) throws RemoteException {
		for (int i = 0; i < philosophersAmount; i++) {
			Philosopher phil = new Philosopher(this, false);
			philosophers.add(phil);
			new Thread(phil).start();
		}
		for (int i = 0; i < hungryPhilosophersAmount; i++) {
			Philosopher phil = new Philosopher(this, true);
			philosophers.add(phil);
			new Thread(phil).start();
		}
	}
	
	@Override
	public void setID(Integer id) throws RemoteException {
		if(id != null) {
			this.id = id;
		} else {
			LOG.warning("ID already exists!");
		}
	}
	
	@Override
	public Integer getID() throws RemoteException {
		return id;
	}
	
	public void lockFirstFork() throws RemoteException{
		forks.get(0).lock();
	}
	
	public void releaseFirstFork() throws RemoteException {
		forks.get(0).unlock();
	}
}
