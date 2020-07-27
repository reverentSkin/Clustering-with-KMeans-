package data;

import java.io.Serializable;
import java.util.Set;

/**
 * La classe astratta Item modella un generico item (coppia attributo-valore)
 * 
 * @author Veronico, Mazzone, Sgaramella
 *
 */
@SuppressWarnings("serial")
public abstract class Item implements Serializable
{
	/**
	 * Attributo coinvolto nell'item
	 */
	protected Attribute attribute;
	
	/**
	 * Valore assegnato all'attributo
	 */
	protected Object value;
	
	/**
	 * Il costruttore si occupa di inizializzare i valori dei membri attributi.
	 * 
	 * @param attribute {@link #attribute}
	 * @param value {@link #value}
	 */
	public Item(Attribute attribute, Object value) 
	{
		this.attribute=attribute;
		this.value=value;
	}
	
	/**
	 * Restituisce il membro attribute
	 * 
	 * @return {@link #attribute}
	 */
	public Attribute getAttribute()
	{
		return this.attribute;
	}
	
	/**
	 * Restituisce il membro value
	 * 
	 * @return {@link #value}
	 */
	public Object getValue()
	{
		return this.value;
	}
	
	/**
	 * Sovrascrive il metodo toString della classe Object e si occupa di
	 * ritornare il valore per l'attributo in formato stringa.
	 * 
	 */
	public String toString()
	{
		return value.toString();
	}
	
	/**
	 * L’implementazione sara' diversa per item discreto e item continuo
	 * 
	 * @param a Oggetto che corrispondera' o a un attributo di tipo discreto o
	 * 			a uno di tipo continuo.
	 * 
	 * @return Un valore di tipo double contenente il risultato del metodo
	 */
	public abstract double distance(Object a);
	
	/**
	 * Il metodo modifica il membro value, assegnandogli il valore
	 * restituito da {@link Data#computePrototype(Set, Attribute)}
	 * 
	 * @param data Riferimento ad un oggetto della classe Data
	 * @param clusteredData Insieme di indici delle righe della matrice in data che formano il cluster
	 */
	public void update(Data data, Set<Integer> clusteredData)
	{
		value=data.computePrototype(clusteredData,attribute);
	}
}
