package tutoring;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			LocateRegistry.createRegistry(1101);
			ProfessorList list = new ProfessorList();
			Naming.rebind("rmi://127.0.0.1:1101/professor_list", list);
			StudentList list2 = new StudentList();
			Naming.rebind("rmi://127.0.0.1:1101/student_list", list2);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
