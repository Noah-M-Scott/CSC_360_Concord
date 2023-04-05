package concordSprint2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import ConcordData.*;

public interface ClientInterface extends Remote {
	public void processUpdate(GroupData Update) throws RemoteException;
}
