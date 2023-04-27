package concordSprint2;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import ConcordData.GroupData;

public class ClientSpy extends ClientObject {

	
	public ClientSpy(Registry in) {
		super(in);
	}

	public int callCount = 0;
	
	@Override
	public void processUpdate(GroupData Update) throws RemoteException {
		callCount++;
		CurrentGroup = Update;
	}
	
}
