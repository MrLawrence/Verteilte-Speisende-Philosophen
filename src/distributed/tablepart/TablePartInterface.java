package distributed.tablepart;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TablePartInterface extends Remote {
	void addChairsandForks(Integer chairAmount, Boolean isOnlyPart)
			throws RemoteException;

	void createPhilosopher(Boolean isHungry) throws RemoteException;

	void killPhilosopher() throws RemoteException;

	void setID(Integer id) throws RemoteException;

	void connect(Integer port) throws RemoteException, NotBoundException;

	Integer getID() throws RemoteException;

	void movePhilosopher(Philosopher philosopher) throws RemoteException;
	
	Integer getPhilosopherAmount() throws RemoteException;
	
}
