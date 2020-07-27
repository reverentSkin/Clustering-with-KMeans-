package data;

/**
 * La classe OutOfRangeSampleSize modella una eccezione controllata 
 * sollevata nel momento in cui il numero k di cluster da scoprire passato al server e'
 * maggiore rispetto al numero di centroidi generabili dall'insieme di transazioni.
 *
 * @author Veronico, Mazzone, Sgaramella
 *
 */
@SuppressWarnings("serial")
public class OutOfRangeSampleSize extends Exception
{
	/**
	 * Richiama il costruttore della classe madre per creare l'oggetto eccezione
	 * 
	 */
	public OutOfRangeSampleSize()
	{
		super("ATTENZIONE!");
	}
	
	public String toString()
	{
		return "Il valore inserito non ammette l'esecuzione del programma.\nRiprova con un altro valore";
	}
}
