package tutoring;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.IOException;
public interface IBank extends Remote {
    public String StatusAccount(int id, String mdp) throws RemoteException, IOException ;
    public String AddAccount(int id, String mdp, int montant) throws RemoteException, IOException ;
    public String RemoveAccount(int id, String mdp, int montant) throws RemoteException, IOException ;
    public String CreateAccount(String mdpAdmin, String mdpCompte, String nom, String prenom)throws RemoteException, IOException;
    public String TransferbewtweenAccount(int id, String mdpCompte, int montant, int id2) throws  RemoteException, IOException ;
    public String GetValue(int id, String mdp) throws RemoteException, IOException ;
    public String SetValue(int id, String mdp, int sold) throws RemoteException, IOException ;
    public String ShowLogs(String mdp) throws RemoteException, IOException;
    public double getExchangeRate(String currencyFrom, String currencyTo) throws RemoteException, IOException;
    String payWithCurrency(int id, String mdp, double amount, String currency) throws RemoteException, IOException;
    }

    // public String afficheLesComptes(String mdp) ;



