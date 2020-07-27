package database;

/**
 * La classe estende Exception per modellare il fallimento nella connessione al database.
 * 
 * @author Veronico, Mazzone, Sgaramella
 *
 */
@SuppressWarnings("serial")
public class DatabaseConnectionException extends Exception{

	/**
	 * Richiama il costruttore della classe base
	 */
	public DatabaseConnectionException() 
	{
		super("ATTENZIONE!");
	}
	
	public String toString()
	{
		return "Impossibile connettersi al database!";
	}

}
