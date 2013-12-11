package verteilt.tischteilserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

import verteilt.Philosoph;

public interface TischteilInterface extends Remote {
	void deckeTisch(Integer stuhlAmount, Boolean istEinzigerTeil)
			throws RemoteException;

	Stuhl getFreienStuhl() throws RemoteException;
	
	void aufstehen(Stuhl stuhl) throws RemoteException;
}
