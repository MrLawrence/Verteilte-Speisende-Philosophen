package distributed.tablepart;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TablePartInterface extends Remote {
	void addChairsandForks(Integer chairAmount, Boolean isOnlyPart)
			throws RemoteException;

	void createPhilosophers(Integer philosophersAmount,
			Integer hungryPhilosophersAmount) throws RemoteException;

	void setID(Integer id) throws RemoteException;

	void connect(Integer port) throws RemoteException, NotBoundException;

	Integer getID() throws RemoteException;
}
