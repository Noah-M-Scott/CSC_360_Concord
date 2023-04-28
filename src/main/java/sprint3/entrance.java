package sprint3;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import concordSprint2.ClientObject;
import concordSprint2.ServerObject;

public class entrance {
	
	/*
	public static void main(String[] args) throws RemoteException {
		
		ServerObject s = new ServerObject();
		Registry registry = LocateRegistry.createRegistry(2700);
		registry.rebind("concord-s", s);
		sprint3.Client = new ClientObject(registry);
		sprint3.main(args);
	}
	*/
	
	public static void main(String[] args, ClientObject client) {
		sprint3.c = client;
		sprint3.main(args);
	}
}

