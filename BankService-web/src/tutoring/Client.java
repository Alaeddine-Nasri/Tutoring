package tutoring;

import java.util.*;
import java.rmi.Naming;
public class Client {
  
    private static boolean ouvrirInterface() {
        scan = new Scanner(System.in);
        System.out.println("welcome to our bank system");
        System.out.println("Client interface:");
        System.out.println("1:View account status");
        System.out.println("2:Add to account ");
        System.out.println("3:Remove from account");
        System.out.println("4:Transfer between account\n");
        System.out.println("Admin Interface");
        System.out.println("5:Get account value");
        System.out.println("6:Set account value");
        System.out.println("7:Creation a account");
        System.out.println("8:Show logs\n");
        System.out.println("9:getExchangeRate\n");
        System.out.println("0:Close the programm");
        System.out.println( "Entrez le numéro de l'opération: ");
        int choix=0;
        choix = scan.nextInt();
        

        

        switch (choix) {
        case 0:
      
            return false;
        case 1:
            StatusAccount();
            break;

        case 2:
            AddAccount();
            break;
        case 3:
        	RemoveAccount();
            break;

        case 4:
        	TransferbewtweenAccount();
            break;

        case 5:
            GetValue();
            break;

        case 6:
            SetValue();
            break;

        case 7:
            CreateAccount();
            break;
        case 8:
            ShowLogs();
            break;
        case 9:
            getExchangeRate();
            break;
        default:
            System.out.println("error enter a number between 1 and 8 \n");
            break;


        }
        
        return true;
    }
    public static void main(String[] args) {
        try {
            stub = (IBank)Naming.lookup("rmi://localhost:1900" + "/Bank");
            boolean t = true;
            while (t) {
                t = (ouvrirInterface());
            }

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
    public Client() {
    }
    private static Scanner scan;
    private static IBank stub;
    private static void sep() {
        System.out.println("\n------------------------\n");
    }
    private static int EnterID() {

        System.out.println("enter ID of your account: ");
 
        return scan.nextInt();
        
    }

    private static String EnterMdp() {
        
        System.out.println("enter mdp of your account: ");
      
        return scan.next();

    }

    private static int EnterMontant() {

        System.out.println("enter amount: ");
     
        return scan.nextInt();
    }
    private static void getExchangeRate() {
        System.out.println("Enter the source currency: ");
        String currencyFrom = scan.next();
        System.out.println("Enter the destination currency: ");
        String currencyTo = scan.next();
        try {
            double exchangeRate = stub.getExchangeRate(currencyFrom, currencyTo);
            sep();
            System.out.println("Exchange rate from " + currencyFrom + " to " + currencyTo + " : " + exchangeRate);
            sep();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void CreateAccount() {

        System.out.println("enter  mdp of admin: ");
  
        String mdpAdmin = scan.next();
        System.out.println("enter mdp of your account: ");
        String mdpCompte = scan.next();
        System.out.println("enter your name: ");
        String name = scan.next();
        System.out.println("enter your surname: ");
        String surname = scan.next();
        try {
            String ret = stub.CreateAccount(mdpAdmin, mdpCompte, name, surname);
            sep();
            System.out.println(ret);
            sep();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void StatusAccount() {


        System.out.println("enter ID of your account: ");
        int id = scan.nextInt();


        System.out.println("enter  the password of your account:");
        String mdp = scan.next();
        try {
            String ret = stub.StatusAccount(id, mdp);
            sep();
            System.out.println(ret);
            sep();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

    }
    private static void AddAccount() {
        int id =  EnterID();
        String mdp = EnterMdp();

        int montant = EnterMontant();
        try {
            sep();
            System.out.println(stub.AddAccount(id, mdp, montant));
            sep();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }


    }
    private static void RemoveAccount() {
        int id = EnterID();
        String mdp = EnterMdp();
        int montant = EnterMontant();
        try {
            sep();
            System.out.println(stub.RemoveAccount(id, mdp, montant));
            sep();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }


    }
    private static void TransferbewtweenAccount() {
  

        int id = EnterID();
        String mdpCompte = EnterMdp();
        int montant = EnterMontant();
        System.out.println("Enter the recipient ID: ");
        int idVers = scan.nextInt();
        try {
            sep();
            System.out.println(stub.TransferbewtweenAccount(id, mdpCompte, montant, idVers));
            sep();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

    }

    private static void GetValue() {
        System.out.println("enter the password of admin: ");

        String mdpAdmin = scan.next();
        int id = EnterID();
        try {
            sep();
            System.out.println(stub.GetValue(id, mdpAdmin));
            sep();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
   

    }
    private static void SetValue() {
        System.out.println("enter the password of admin: ");
    
        String mdpAdmin = scan.next();
        int id = EnterID();
        int montant = EnterMontant();
        try {
            sep();
            System.out.println(stub.SetValue(id, mdpAdmin, montant));
            sep();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
     
    }
    private static void ShowLogs() {
        System.out.println("EnterMontant: ");

        String mdpAdmin = scan.next();
        sep();
        try {
            System.out.println(stub.ShowLogs(mdpAdmin));
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        sep();

    }


}
