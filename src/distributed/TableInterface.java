package distributed;

import java.rmi.RemoteException;

import distributed.tablepart.TablePartInterface;

public interface TableInterface {
	public void register(TablePartInterface tablePart) throws RemoteException;
}
