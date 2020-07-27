@SuppressWarnings("serial")
/**
 * Classe che gestisce un'eccezione di tipo ServerException che indica una eccezione sollevata dal
 * Server e trasmessa al client dallo stream di connessione
 * @author Veronico, Mazzone, Sgaramella
 */
public class ServerException extends Exception {

	/**
	 * Costruttore della classe che richiama il gestore dell'eccezione una volta che essa viene sollevata
	 */
	ServerException(){
		super("ATTENZIONE!");
	}

	/**
	 * Metodo che genera una stringa contenente il messaggio di errore.
	 * @return Messaggio di errore.
	 */
	public String toString() {
		return "Errore nella comunicazione con il server!";
	}

}


