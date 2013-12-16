package distributed.tablepart;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TablePartInterface extends Remote {
	void addChairsandForks(Integer chairAmount, Boolean isOnlyPart)
			throws RemoteException;
	
	void standUp(Chair chair) throws RemoteException;
	
	void createPhilosophers(Integer philosophersAmount,
			Integer hungryPhilosophersAmount) throws RemoteException;
}
