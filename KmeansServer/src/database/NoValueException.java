package database;

/**
 * La classe estende Exception e modella un'eccezione che si verifica nel caso di
 * assenza di un valore all'interno di un resultset
 * 
 * @author Veronico, Mazzone, Sgaramella
 */
@SuppressWarnings("serial")
public class NoValueException extends Exception{

	/**
	 * Richiama il costruttore della classe madre
	 */
	public NoValueException() 
	{
		super("ATTENZIONE!");
	}
	
	public String toString()
	{
		return "Assenza di un valore all'interno del resultset!";
	}

}
