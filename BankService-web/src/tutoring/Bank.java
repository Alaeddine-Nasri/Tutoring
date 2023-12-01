package tutoring;

import java.io.File;

import java.util.Scanner;
import java.util.*;
import java.rmi.server.*;
import java.rmi.RemoteException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.FileWriter;
import java.io.IOException;
public class Bank extends UnicastRemoteObject implements IBank {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Bank(String mdp) throws RemoteException, IOException {
        super(1099);
        //je dois savoir ce port pour l'ouvrir dans le NAT (pour rendre el serveur accessible par internet)
        this.mdp = mdp;
        this.lastId = 0;
        myWriter = new FileWriter("server.logs");
        helperWrite(dtf.format(now) + " server created");
        System.out.println(dtf.format(now) + " server created");


    }
    public double getExchangeRate(String currencyFrom, String currencyTo) throws IOException {
        // Implémentez la logique pour récupérer le taux de change
        // Par exemple, vous pouvez avoir des taux prédéfinis ou les récupérer à partir d'une source externe
        double exchangeRate = 1.0; // Par défaut, taux de change 1:1

        // Ajoutez votre logique pour déterminer le taux de change réel ici
        // Vous pouvez utiliser des API externes, des taux prédéfinis, etc.

        helperWrite(dtf.format(now) + " Taux de change demandé: " + currencyFrom + " vers " + currencyTo + " : " + exchangeRate);

        return exchangeRate;
    }
   
    
    public String  StatusAccount(int id, String mdp) throws IOException {
        helperWrite(dtf.format(now) + "invocation de etatCompte avec ID " + id);
        if (id < 0 || id >= lastId) {
            helperWrite("ID incorrecte");
            return "erreur: ID incorrecte";
        }
        if (!liste.get(id).mdp.equals(mdp)) {
            helperWrite("mdp faux");
            return "Compte: " + erreur + "\n";
        }
        helperWrite("Succes");
        return "compte de " + liste.get(id).name + " " + liste.get(id).surname+ " ID: " + id + "\nvotre solde est " + liste.get(id).sold;
    }
    public String AddAccount(int id, String mdp, int montant)  throws IOException {
        helperWrite(dtf.format(now) + "invocation de ajouterSurCompte avec ID " + id + " et montant: " + montant);

        if (id < 0 || id >= lastId) {
            helperWrite("ID incorrect");
            return "eror: ID incorrect";
        }
        if (!liste.get(id).mdp.equals(mdp)) {
            helperWrite("mdp faux");
            return "compte: " + erreur;
        }
        liste.get(id).sold += montant;
        helperWrite(" montant: " + montant + " amount added");
        return "succès";
    }
    public String  RemoveAccount(int id, String mdpCompte, int montant)  throws IOException {
        helperWrite(dtf.format(now) + "invocation de enleverSurCompte avec ID " + id + " et montant: " + montant);
        if (id < 0 || id >= lastId) {
            helperWrite("ID incorrect");
            return "error: ID incorrect";
        }
        if (!liste.get(id).mdp.equals(mdpCompte)) {
            helperWrite("mdp faux");
            return erreur;
        }
        if (montant <= 0) {
            helperWrite("Negative amount");
            return "Negative amount";
        }
        if (liste.get(id).sold <= montant) {
            helperWrite("Insufficient funds");
            return "Insufficient funds";
        }
        helperWrite(" montant: " + montant + "amount removed");

        helperWrite("success amount removed " + montant);
        liste.get(id).sold -= montant;
        return "Done";
    }
    
    public String payWithCurrency(int id, String mdp, double amount, String currency) throws RemoteException, IOException {
        helperWrite(dtf.format(now) + "invocation de payer avec devise pour ID " + id + " et montant: " + amount + " devise: " + currency);

        if (id < 0 || id >= lastId) {
            helperWrite("ID incorrect");
            return "error: ID incorrect";
        }
        if (!liste.get(id).mdp.equals(mdp)) {
            helperWrite("mdp faux");
            return erreur;
        }

        // Utilisez la logique de taux de change pour effectuer le paiement dans la devise spécifiée
        double exchangeRate = getExchangeRate("DEVISE_ACTUELLE", currency);
        double amountInCurrentCurrency = amount * exchangeRate;

        if (liste.get(id).sold <= amountInCurrentCurrency) {
            helperWrite("Solde insuffisant");
            return "Solde insuffisant";
        }

        liste.get(id).sold -= amountInCurrentCurrency;
        helperWrite("Montant: " + amount + " devise: " + currency + " payé avec succès");

        return "Montant: " + amount + " devise: " + currency + " payé avec succès";
    }
    
    public String CreateAccount(String mdp, String mdpCompte, String nom, String prenom)  throws IOException {
        helperWrite(dtf.format(now) + " invocation de creerCompte: nom: " + nom + " prenom: " + prenom);

        if (!this.mdp.equals(mdp)) {
            helperWrite("mdp incorrect");
            return "Admin: " + erreur;
        } else {

            Account tmpCompte = new Account(mdpCompte);
            tmpCompte.id = lastId;
            tmpCompte.mdp = mdpCompte;
            tmpCompte.name = nom;
            tmpCompte.surname = prenom;
            lastId++;
            liste.add(tmpCompte);
            helperWrite(dtf.format(now) + "Account created with ID: " + (lastId - 1));
            return "succès l'ID de votre compte est " + (lastId - 1) + " compte de " + nom + " " + prenom;
        }
    }


    public String TransferbewtweenAccount(int id, String mdpCompte, int montant, int idVers)  throws IOException {
        helperWrite(dtf.format(now) + "invocation de transfert Entre Compte: \n" +
                    "ID: " + id + " montant: " + montant + " destination " + idVers);
        if (id < 0 || id >= lastId) {
            helperWrite("ID incorrect");
            return "error: ID incorrect";
        }
        if (!liste.get(id).mdp.equals(mdpCompte)) {
            helperWrite("mdp incorrect");
            return erreur;
        }
        if (montant <= 0) {
            helperWrite("Negative amount");
            return "Negative amount";
        }

        if (liste.get(id).sold <= montant) {
            helperWrite("Insufficient funds");
            return "Insufficient funds";
        }
        liste.get(id).sold -= montant;
        liste.get(idVers).sold += montant;
        helperWrite("Success");
        return "Success";
    }

    public String GetValue(int id, String mdp) throws IOException  {
        helperWrite(dtf.format(now) + " invocation de get valeur: ID: " + id);
        if (id < 0 || id >= lastId) {
            helperWrite("ID incorrect");
            return "error: ID incorrect";
        }
        if (!this.mdp.equals(mdp)) {
            helperWrite("mdp incorrect");
            return "Admin: " + erreur;
        }
        helperWrite("Success");

        return "solde: " + liste.get(id).sold ;
    }
    public String Setvalue(int id, String mdp, int solde)  throws IOException {
        helperWrite(dtf.format(now) + " invocation de setValeur: ID: " + id + " avec solde " + solde);
        if (id < 0 || id >= lastId) {
            helperWrite("ID in correct");

            return "error: ID incorrect";
        }
        if (!this.mdp.equals(mdp)) {
            helperWrite("mdp incorrect");

            return "Admin: " + erreur;
        }
        liste.get(id).sold = solde;
        helperWrite("Success");

        return "Success";
    }
    public String Showlogs(String adminMdp) throws IOException {
        helperWrite(" invocation de affiche Log");
        if (!this.mdp.equals(adminMdp)) {
            helperWrite("mdp incorrect");
            return "Admin: " + erreur;
        }

        File file = new File("server.logs");
        try {
            Scanner sc = new Scanner(file);
            String s = "";

            while (sc.hasNextLine())
                s += sc.nextLine() + "\n";
            sc.close();
            return s;
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
            return "server error";
        }



    }


    private final String erreur = "wrong password";
    transient private String mdp;
    transient private int lastId;
    transient private FileWriter myWriter;
    transient DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    transient LocalDateTime now = LocalDateTime.now();
    private ArrayList<Account> liste = new ArrayList<Account>();
    private void helperWrite(String s)throws IOException {
        myWriter.write(s + "\n");
        myWriter.flush();
        return ;
    }

	@Override
	public String SetValue(int id, String mdp, int sold) throws RemoteException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String ShowLogs(String mdp) throws RemoteException, IOException {
		// TODO Auto-generated method stub
		return null;
	}


}