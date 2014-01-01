package distributed;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import distributed.tablepart.TablePart;
import distributed.tablepart.TablePartInterface;

public interface TableInterface extends Remote {
	void register(TablePart tablePart) throws RemoteException;

	TablePart getNextTablePart(TablePart tablePart) throws RemoteException;
}
