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

import distributed.TableInterface;

public class TablePart implements TablePartInterface, Serializable {
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(TablePart.class
			.getName());
	private List<Chair> chairs = new ArrayList<Chair>();
	private List<ReentrantLock> forks = new ArrayList<ReentrantLock>();
	private List<Thread> philosophers = new ArrayList<Thread>();

	private Random random = new Random();
	private TableInterface table;
	private Integer id = null;

	public void connect(Integer port) {
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(port);
			table = (TableInterface) registry.lookup("table");
			table.register(this);
			LOG.info("Connected");
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}

	public Chair getChair() {
		return chairs.get(random.nextInt(chairs.size()));
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
	public void createPhilosophers(Integer philosophersAmount, Boolean isHungry)
			throws RemoteException {
		for (int i = 0; i < philosophersAmount; i++) {
			Thread phil = new Thread(new Philosopher(this, isHungry));
			philosophers.add(phil);
			phil.start();
		}
	}

	@Override
	public void setID(Integer id) throws RemoteException {
		if (id != null) {
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
		return "TablePart(id=" + this.id + ")";
	}

	@Override
	public void movePhilosopher(Philosopher philosopher) throws RemoteException {
		table.getNextTablePart(this).createPhilosophers(1, false);
		philosophers.remove(philosopher.getThread());
		philosopher.stop();	
	}
	
	
}
