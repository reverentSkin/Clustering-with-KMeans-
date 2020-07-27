package data;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptySetException;
import database.Example;
import database.NoValueException;
import database.QUERY_TYPE;
import database.TableData;
import database.TableSchema;

/**
 * La classe Data modella l'insieme di transazioni (o tuple) ottenute dalla base di dati.
 * La classe fa ampio uso delle classi presenti all'interno del package database, per leggere le
 * singole tuple appartenenti alla base di dati rendendole utilizzabili all'interno dell'intero 
 * sistema.
 * La gestione delle tuple avviene tramite i metodi della classe {@link database.TableData} in
 * particolare: {@link database.TableData#getDistinctTransazioni(String)}, 
 * {@link database.TableData#getAggregateColumnValue(String, database.TableSchema.Column, QUERY_TYPE)},
 * {@link database.TableData#getDistinctColumnValues(String, database.TableSchema.Column)},
 * sfruttando il risultato della classe {@link database.TableSchema} (tale classe infatti
 * si occupa di ottenere una versione - utilizzabile all'interno del sistema - della tabella
 * nella base di dati).
 * L'accesso alla base di dati avviene attraverso la classe {@link database.DBAccess}
 * Le tuple che caratterizzeranno la tabella data sono univoche, quindi duplicati presenti
 * all'interno della base di dati verranno considerati una sola volta (migliorando cosi' le 
 * operazioni di scoperta dei cluster e rendendole piu' efficienti).
 * Quindi la tabella Data rappresenta tutti i dati che verranno utilizzati all'interno del sistema,
 * non a caso prima di effettuare qualsiasi operazione da parte del server, viene innanzitutto 
 * istanziato un oggetto di questa classe e i dati prodotti vengono resi disponibili per tutti 
 * le attivita' del sistema (a meno che non sia un'attivita' di
 * scoperta di cluster da file, in questo caso la tabbella data non e' istanziata e i cluster
 * sono ottenuto dal file).
 * Per ottenere correttamente i dati e' necessario che all'atto dell'istanziazione della classe
 * sia specificato il nome della tabella, che coincide esattamente con quello della tabella contenuta
 * nella base di dati.
 * La classe rende disponibili attraverso i metodi getter:
 * Il numero dell tuple
 * Il numero degli attributi
 * Il valore che appartiene ad un attributo
 * Un determinato attributo
 * 
 * @author Veronico, Mazzone, Sgaramella
 *
 */
@SuppressWarnings("serial")
public class Data implements Serializable {
	
    /**
     * Indica una matrice nXm di tipo Object dove ogni riga modella una transazione
     */
	private List<Example> data;
	
	/**
	 * Indica la cardinalita' dell'insieme di transazioni (numero di righe in data)
	 */
	private int numberOfExamples;
	
	/**
	 * Indica un vettore degli attributi in ciascuna tupla (schema della tabella di dati)
	 */
	private List<Attribute> explanatorySet = new LinkedList<Attribute>();// 

	public Data(String tableName) throws ClassNotFoundException, DatabaseConnectionException, SQLException, NoValueException, InstantiationException, EmptySetException 
	{
		DbAccess db = new DbAccess();
		DbAccess.initConnection();
		TableData table = new TableData(db);
		data = table.getDistinctTransazioni(tableName);

		numberOfExamples = data.size();

		TableSchema schema = new TableSchema(db, tableName);
		explanatorySet = new LinkedList<Attribute>();
		for(int i = 0; i < schema.getNumberOfAttributes(); i++) {
			if(schema.getColumn(i).isNumber()) {
				double min = Double.parseDouble(table.getAggregateColumnValue(tableName, schema.getColumn(i), QUERY_TYPE.MIN).toString());
				double max = Double.parseDouble(table.getAggregateColumnValue(tableName, schema.getColumn(i), QUERY_TYPE.MAX).toString());
				explanatorySet.add(new ContinuosAttribute(schema.getColumn(i).getColumnName(),i, min, max));
			}else {
				String[] value= (String[])table.getDistinctColumnValues(tableName, schema.getColumn(i)).toArray(new String[table.getDistinctColumnValues(tableName, schema.getColumn(i)).size()]);
				explanatorySet.add(new DiscreteAttribute(schema.getColumn(i).getColumnName(),i, value));
			}
		}
		DbAccess.closeConnection();
	}


	/**
	 * Restituisce il numero di tuple, quindi il numero di esempi salvati in memoria
	 * @return restituisce numberOfExamples
	 */
	public int getNumberOfExamples(){
		return this.numberOfExamples;
	}
	
	/**
	 * Restituisce il numero di attributi
	 * @return restituisce explanatorySet.size()
	 */
	public int getNumberOfAttributes(){
		return this.explanatorySet.size();
	}
	
	/**
	 * Restituisce lo schema dei dati
	 * @return restituisce explanatorySet
	 */
	List<Attribute> getAttributeSchema()
	{
		return explanatorySet;
	}
	
	/**
	 * Restituisce data[exampleIndex][attributeIndex]
	 * 
	 * @param exampleIndex
	 *            indice di riga
	 * @param attributeIndex
	 *            indice di colonna
	 * @return valore assunto in data dall'attributo in posizione attributeIndex,
	 *         nella riga in posizione exampleIndex
	 */
	public Object getAttributeValue(int exampleIndex, int attributeIndex)
	{
		return data.get(exampleIndex).get(attributeIndex);
	}

	public int[] sampling(int k) throws OutOfRangeSampleSize
	{

		if(k > data.size() || k == 0) {
			throw new OutOfRangeSampleSize();
		}	
		int centroidIndexes[] = new int[k];
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		for(int i = 0; i < k; i++) {
			boolean found = false;
			int c;
			do {
				found = false;
				c = rand.nextInt(getNumberOfExamples());
				for(int j = 0; j < i; j++) {
					if(compare(centroidIndexes[j],c)) {
						found = true;
						break;
					}
				}
			}while(found);
			centroidIndexes[i]=c;
		}
		return centroidIndexes;
	}
	
	/**
	 * Restituisce vero se le due righe di data contengono gli stessi valori, falso
	 * altrimenti
	 * 
	 * @param i
	 *            indice della prima riga
	 * @param j
	 *            indice della seconda riga
	 * @return booleano, esito del confronto
	 */
	private boolean compare(int i, int j)
	{
		boolean equals = true;
		for(int k = 0; k < getNumberOfAttributes(); k++ ) {
			if(!getAttributeValue(i,k).equals(getAttributeValue(j,k))) {
				equals = false;
				break;
			}
		}
		return equals;
	}

	/**
	 * Restituisce computePrototype(idList, (DiscreteAttribute)attribute)
	 * 
	 * @param clusteredData
	 *            insieme di indici di riga
	 * @param attribute
	 *            attributo rispetto al quale calcolare il prototipo (centroide)
	 * @return valore centroide rispetto ad attribute
	 */
	Object computePrototype(Set<Integer> idList, Attribute attribute)
	{
		if(attribute instanceof DiscreteAttribute)
			return computePrototype(idList,(DiscreteAttribute) attribute);
		else if(attribute instanceof ContinuosAttribute)
		    return computePrototype(idList, (ContinuosAttribute) attribute);
		
		return null;
	}

	/**
	 * Determina il valore che occorre piu' frequentemente per attribute nel
	 * sottoinsieme di dati individuato da idList facendo uso del metodo
	 * frequency di DiscretAttribute
	 * 
	 * @param idList
	 *            insieme degli indici delle righe di data appartenenti ad un
	 *            cluster
	 * @param attribute
	 *            attributo discreto rispetto al quale calcolare il
	 *            prototipo(centroide)
	 * @return centroide rispetto ad attribute
	 */
	private String computePrototype(Set<Integer> idList, DiscreteAttribute attribute)
	{
		Iterator<String> iter = attribute.iterator();
		String elem = iter.next();
		int currentFrequecy = attribute.frequency(this, idList, elem);
		String nomeAttributo=elem;
		while(iter.hasNext())//for(int i=1;i<attribute.getNumberOfDistinctValues();i++)
		{
			elem= iter.next();
			if(attribute.frequency(this, idList, elem)>currentFrequecy)
			{
				currentFrequecy=attribute.frequency(this, idList, elem);
				nomeAttributo=elem;
			}
		}
		return nomeAttributo;
	}
	
	Double computePrototype(Set<Integer> idList, ContinuosAttribute attribute)
	{
		double total=0.0;
		int numberOfValue=0;
		
		for(int i=0;i<getNumberOfExamples();i++)
			if(idList.contains(i))
			{
				total+=(Double) getAttributeValue(i, attribute.getIndex());
				numberOfValue++;
			}
		return total/numberOfValue;
	}

	public String toString()
	{
		String str="";
		for(int i=0;i<getNumberOfAttributes();i++)
		{
			str+=getAttributeSchema().get(i);
			str+="  ";
		}
		for(int i=0;i<getNumberOfExamples();i++)
		{
			str+="\n";
			for(int j=0;j<getNumberOfAttributes();j++)
			{
				str+=getAttributeValue(i,j);
				str+="  ";
			}
		}

		return str;
	}
	
	public Tuple getItemSet(int index)
	{
		Tuple tuple=new Tuple(explanatorySet.size());
		for(int i=0;i<explanatorySet.size();i++)
			if(explanatorySet.get(i) instanceof DiscreteAttribute)
				tuple.add(new DiscreteItem(explanatorySet.get(i), (String)data.get(index).get(i)), i);
			else if (explanatorySet.get(i) instanceof ContinuosAttribute)
				tuple.add(new ContinuosItem(explanatorySet.get(i), (Double)data.get(index).get(i)), i);

		return tuple;
	}

}
