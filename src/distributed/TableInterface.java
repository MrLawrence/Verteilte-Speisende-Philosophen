package distributed;

import java.rmi.Remote;
import java.rmi.RemoteException;
import distributed.tablepart.TablePart;

public interface TableInterface extends Remote {
	void register(TablePart tablePart) throws RemoteException;

	void layTable() throws RemoteException;

	void createPhilosophers(Integer philosophersAmount, Boolean isHungry) throws RemoteException;

	void killPhilosophers(Integer philosophersAmount) throws RemoteException;
}
