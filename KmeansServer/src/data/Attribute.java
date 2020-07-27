package data;

import java.io.Serializable;

/**
 * Classe astratta che modella un attributo.
 * 
 * @author Veronico, Mazzone, Sgaramella
 *
 */
@SuppressWarnings("serial")
public abstract class Attribute implements Serializable{
	
	
	/**
	 * Nome simbolico dell'attributo.
	 */
	protected String name;
	
	/**
	 * Identificativo numerico dell'attributo
	 */
	protected int index;

	/**
	 * Inizializza l'attributo assegnandoli un nome e un indice.
	 * 
	 * @param name Nome dell'attributo
	 * @param index Identificativo numerico dell'attributo
	 */
	Attribute(String name, int index)
	{
		this.name=name;
		this.index=index;
	}
	
	/**
	 * Ritorna un oggetto istanza della classe String corrispondente
	 * al nome dell'attributo
	 *  
	 * @return {@link #name}
	 */
	String getName()
	{
		return this.name;
	}
	
	/**
	 * Ritorna un intero corrispondente all'identificativo dell'attributo
	 * 
	 * @return {@link #index}
	 */
	int getIndex()
	{
		return this.index;
	}
	
	/**
	 * Metodo toString. Il metodo ritorna semplicemente il nome dell'attributo.
	 * 
	 * @return {@link #name}
	 */
	public String toString()
	{
		return this.name;
	}

}
