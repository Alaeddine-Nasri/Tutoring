package tutoring;

public class Account implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Account(String mdp)
    {
        this.mdp=mdp;
        sold=0;
    }
    public String mdp;
    public int id;
    public int sold;
    public String name;
    public String surname;
    
}
