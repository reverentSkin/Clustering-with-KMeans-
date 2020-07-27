package database;

/**
 * La classe EmptySetException estende Exception e modella
 * un'eccezione che si verifica nel caso in cui viene restituito un resultset vuoto.
 * 
 * @author Veronico, Mazzone, Sgaramella
 */
@SuppressWarnings("serial")
public class EmptySetException extends Exception {
	
	/**
	 * Richiama il costruttore della classe madre
	 */
	EmptySetException(){
		super("Eccezione sollevata!");
	}
	
	public String toString() {
		return "ResultSet VUOTO!";
	}
}
