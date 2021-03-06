package distributed.tablepart;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import distributed.Table;
import distributed.TableInterface;

public class TablePart implements TablePartInterface, Serializable {
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(TablePart.class
			.getName());
	private List<Chair> chairs = new ArrayList<Chair>();
	private List<ReentrantLock> forks = new ArrayList<ReentrantLock>();
	private List<Thread> philosophers = new ArrayList<Thread>();
	private List<TablePart> otherParts = new ArrayList<TablePart>();

	private Random random = new Random();
	private TableInterface table;
	private Integer id = null;

	public void connect(Integer port) {
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(port);
			table = (TableInterface) registry.lookup("table");
			table.register(this);
			LOG.info("Connected to table");
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}

	public Chair getChair() {
		Integer randomPosition = random.nextInt(chairs.size() - 1);
		
		//Only every 2nd chair, never the last one
		if (randomPosition % 2 != 0 || randomPosition == chairs.size() - 1) {
			randomPosition -= 1;
		}
		return chairs.get(randomPosition);

	}

	@Override
	public void addChairsandForks(Integer chairAmount, Boolean isOnlyPart)
			throws RemoteException {
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
	public void createPhilosopher(Boolean isHungry) throws RemoteException {
		Thread phil = new Thread(new Philosopher(this, isHungry));
		philosophers.add(phil);
		phil.start();
		LOG.info("TablePart #" + id + " received a philosopher");
	}

	@Override
	public void recreatePhilosopher(Boolean isHungry, Integer id, Integer meals)
			throws RemoteException {
		Thread phil = new Thread(new Philosopher(this, isHungry, id, meals));
		philosophers.add(phil);
		phil.start();
		LOG.info("TablePart #" + id + " received an existing philosopher");
	}

	@Override
	public void setID(Integer id) throws RemoteException {
		if (this.id == null) {
			this.id = id;
		} else {
			LOG.warning("ID already exists!");
		}
	}

	@Override
	public Integer getID() throws RemoteException {
		return id;
	}

	@Override
	public String toString() {
		return "TablePart #" + this.id;
	}

	@Override
	public void movePhilosopher(Philosopher philosopher) throws RemoteException {
		LOG.info("Moving philosopher...");
		TablePart randomTablePart = otherParts.get(random.nextInt(otherParts
				.size()));
		randomTablePart.recreatePhilosopher(false, philosopher.getID(),
				philosopher.getMeals());
		philosophers.remove(philosopher.getThread());
		philosopher.kill();
	}

	public Boolean isCrowded() {
		return chairs.size() < philosophers.size();
	}

	@Override
	public void killPhilosopher() throws RemoteException {
		if (philosophers.size() > 0) {
			Thread philosopher = philosophers.get(philosophers.size() - 1);
			philosophers.remove(philosopher);
			philosopher.stop();
			LOG.info("Killed a Philosopher successfully");
		} else {
			LOG.warning("TablePart" + id + "has no Philosophers to remove!");
		}
	}

	@Override
	public Integer getPhilosopherAmount() throws RemoteException {
		return philosophers.size();
	}

	public void notifyNewPart(TablePart part) throws RemoteException {
		otherParts.add(part);
	}

	@Override
	public void createChair() throws RemoteException {
		ReentrantLock newFork = new ReentrantLock();
		forks.add(newFork);
		Chair lastChair = chairs.get(chairs.size() - 1);
		lastChair.sitDown();
		lastChair.changeRightFork(newFork);
		lastChair.leave();

		Chair newChair = new Chair(newFork, null);
		chairs.add(newChair);
	}

	@Override
	public void killChair() throws RemoteException {
		Chair lastChair = chairs.get(chairs.size() - 1);
		ReentrantLock lastFork = forks.get(forks.size() - 1);
		lastFork.lock();
		forks.remove(lastFork);
		lastFork.unlock();
		lastChair.sitDown();
		chairs.remove(lastChair);
		lastChair.leave();
		LOG.info("Removed " + lastChair.toString());
	}

	@Override
	public Integer getChairAmount() throws RemoteException {
		return chairs.size();
	}
}
