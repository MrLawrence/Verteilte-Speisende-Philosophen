package distributed;

import java.rmi.RemoteException;
import java.util.List;

import distributed.tablepart.TablePartInterface;

public interface TableInterface {
	public List<TablePartInterface> getTablePartRegistry() throws RemoteException;
}
