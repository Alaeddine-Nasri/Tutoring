package tutoring;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.EnumSet;
import java.util.UUID;

public class Professor extends UnicastRemoteObject implements IProfessor {
	UUID uuid;
	String name;
	EnumSet<Modules> modules;
	
	protected Professor(String name, EnumSet<Modules> modules) throws RemoteException {
		this.uuid = UUID.randomUUID();
		this.name = name;
		this.modules = modules;
	}
	public UUID getUUID() throws RemoteException {
		return this.uuid;
	}
	
	public String getName() throws RemoteException {
		return this.name;
	}
	public EnumSet<Modules> getModules() throws RemoteException {
		return this.modules;
	}
}