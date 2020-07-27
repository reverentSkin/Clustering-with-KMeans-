package data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * La classe estende la classe Attribute e rappresenta un attributo discreto (categorico).
 * L'attributo e' caratterizzato da un insieme di valori che lo rappresentano, oltre che da
 * un indice e da un nome, ereditati dalla classe Attribute.
 * 
 * @author Veronico, Mazzone, Sgaramella
 */
@SuppressWarnings("serial")
public class DiscreteAttribute extends Attribute implements Iterable<String>,Serializable{

	/**
	 * TreeSet di oggetti String, uno per ciascun valore del dominio discreto.
	 * I valori del dominio sono univoci e sono ordinati seguendo un ordine lessicografico.
	 */
	private TreeSet<String> values;

	/**
	 * Il costruttore invoca anzitutto il costruttore della classe madre e inizializza 
	 * un attributo discreto specificando il nome dell'attributo, l'identificativo numerico 
	 * dell'attributo e il TreeSet di stringhe rappresentanti il dominio dell'attributo
	 * 
	 * @param name Nome dell'attributo
	 * @param index Identificativo dell'attributo
	 * @param values TreeSet rappresentante il dominio di valori per l'attributo
	 * 
	 */
	public DiscreteAttribute(String name, int index,String values[]) 
	{
		super(name, index);
		this.values = new TreeSet<String>();
		for(int i = 0; i < values.length; i++) 
		{
			this.values.add(values[i]);
		}
	}


	/**
	 * Restituisce la dimensione di values
	 * @return numero di valori discreti nel dominio dell'attributo
	 */
	int getNumberOfDistinctValues()
	{
		return values.size();
	}

	/**
	 * Determina il numero di volte che il valore v compare in corrispondenza dell'attributo corrente (indice di colonna) negli esempi memorizzati in data e indicizzate (per riga) da idList
	 * @param data oggetto
	 * @param idList oggetto che mantiene l'insieme degli indici di riga di alcune tuple memorizzate in data
	 * @param v valore discreto
	 * @return Numero di occorrenze del valore discreto
	 */
	public int frequency(Data data, Set<Integer> idList, String v)
	{
		int occ=0;
		for(int i=0;i<data.getNumberOfExamples();i++)
			if(idList.contains(i))
				for(int j=0; j<data.getNumberOfAttributes();j++)
					if(data.getAttributeValue(i, j).equals(v))
						occ++;
		return occ;
	}
	@Override
	public Iterator<String> iterator() {
		return values.iterator();
	}
}
