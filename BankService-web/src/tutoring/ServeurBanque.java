package tutoring;

import java.rmi.registry.*;

import java.rmi.*;

public class ServeurBanque {
    public ServeurBanque() {
        //
    }
    public static void main(String args[]) {
        try {
            Bank objDistant = new Bank("admin");
            //IBank stub = (IBank) UnicastRemoteObject.exportObject(objDistant, 0);
            //Registry registry = LocateRegistry.getRegistry();
            LocateRegistry.createRegistry(1900); 
            //registry.bind("Bank", stub);
            Naming.rebind("rmi://localhost:1900"+"/Bank",objDistant); 
            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}