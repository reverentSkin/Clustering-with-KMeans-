package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import database.TableSchema.Column;

/**
 * La classe si occupa di ottenere in maniera appropriata la tuple dalla base di dati e renderle
 * disponibili al sistema.
 * In particolare le tuple vengono estratte singolarmente, quindi ci saranno tuple univoche nel
 * sistema, nonostante la base di dati puo' possedere duplicati.
 * Lo schema originale della tabella (che quindi equivale a quello della base di dati)
 * e' ottenuto grazie alla classe {@link database.TableSchema}.
 * Il risultato fornito da questa classe viene trattato dai metodi qui presenti. In particolare 
 * grazie ai metodi
 * {@link TableData#getDistinctTransazioni(String)}, 
 * {@link TableData#getDistinctColumnValues(String, TableSchema.Column)} e 
 * {@link TableData#getAggregateColumnValue(String, TableSchema.Column, QUERY_TYPE)} lo schema
 * verra' utilizzato per potare a termine particolari operazioni (si veda la definizione
 * dei metodi per osservare come lo schema viene utilizzato).
 * 
 * @author Veronico, Mazzone, Sgaramella
 * 
 */
public class TableData {

	/**
	 * Fornisce alla classe l'accesso alla base di dati.
	 * 
	 */
	DbAccess db;

	/**
	 * Il costruttore della classe si occupa di inizializzare il membro {@link #db}
	 * in modo da rendere disponibile l'accesso alla base di dati all'intera classe.
	 * 
	 * @param db Riferimento ad un oggetto istanza della classe {@link database.DBAccess}
	 */
	public TableData(DbAccess db) {
		this.db=db;
	}
	
	/**
	 * Ricava lo schema della tabella con nome table. Esegue una interrogazione per estrarre le 
	 * tuple distinte da tale tabella. Per ogni tupla del resultset, si crea un oggetto, istanza 
	 * della classe Example, il cui riferimento va incluso nella lista da restituire.
	 * In particolare, per la tupla corrente nel resultset, si estraggono i valori dei singoli 
	 * campi, e li si aggiungono all'oggetto istanza della classe 
	 * Example che si sta costruendo.
	 * 
	 * @param table La tabella a partire della quale si effettuer&agrave l'interrogazione
	 * 
	 * @return Lista completa contente tutte le tuple distinte.
	 * 
	 * @throws SQLException		   L'eccezione &egrave sollevata e propagata in presenza di errori 
	 * 							   nella esecuzione della query.
	 * @throws EmptySetException   Se il resultset, ottenuto dalla query, e' vuoto.
	 * 
	 */
	@SuppressWarnings("static-access")
	public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException
	{
		TableSchema schema = new TableSchema(this.db, table);
		String query = "SELECT DISTINCT * FROM " + table;
		Statement s = db.getConnection().createStatement();
		ResultSet result = s.executeQuery(query);
		List<Example> list = new ArrayList<Example>();
		while(result.next()) {
			Example ex = new Example();
			for(int i = 0; i < schema.getNumberOfAttributes(); i++) {
				if(schema.getColumn(i).isNumber()) {
					ex.add(result.getDouble(i+1));
				} else {
					ex.add(result.getString(i+1));
				}
			}
			list.add(ex);
		}
		return list;

	}

	/**
	 * Formula ed esegue una interrogazione SQL per estrarre i valori distinti ordinati di 
	 * column e popolare un insieme da restituire. L'insieme restituito sar&agrave un oggetto
	 * istanza della classe TreeSet.
	 * Il metodo puo' propagare un eccezione di tipo SQLException (in presenza di errori 
	 * nella esecuzione della query)
	 * 
	 * @param table 	La tabella a partire della quale si effettuera' l'interrogazione 
	 * @param column	La colonna da considerare per la tabella specificata
	 * 
	 * @return				Valori distinti e ordinati appartenenti alla colonna specificata
	 * 
	 * @throws SQLException		   L'eccezione e' sollevata e propagata in presenza di errori 
	 * 							   nella esecuzione della query.
	 * 
	 */
	@SuppressWarnings("static-access")
	public Set<Object>getDistinctColumnValues(String table,Column column) throws SQLException
	{
		String query = "SELECT DISTINCT " + column.getColumnName() + " FROM " + table + " ORDER BY " + column.getColumnName() + " ASC";
		Set<Object> set = new TreeSet<Object>();
		Statement s = db.getConnection().createStatement();
		ResultSet result = s.executeQuery(query);
		while(result.next()) {
			set.add(result.getObject(1));
		}
		return set;
	}
	
	/**
	 * Formula ed esegue una interrogazione SQL per estrarre il valore aggregato (valore minimo 
	 * o valore massimo) cercato nella colonna di nome column della tabella di nome table.
	 * Il metodo solleva e propaga una NoValueException se il resultset e' vuoto o il valore 
	 * calcolato e' pari a null.
	 * 
	 * @param table		   La tabella a partire della quale si effettuera' l'interrogazione
	 * @param column	   La colonna da considerare per la tabella specificata
	 * @param aggregate	   Valore enumerativo (MAX o MIN) per determinare se effettuare una query
	 * 					   per estrarre il valore minimo o il valore massimo
	 * 
	 * @return Il valore di massimo o minimo calcolato per la colonna specificata come parametro
	 * 
	 * @throws SQLException		   L'eccezione e' sollevata e propagata in presenza di errori 
	 * 							   nella esecuzione della query.
	 * @throws NoValueException	   Eccezione sollevata se il resultset e' vuoto o il valore
	 * 							   calcolato e' pari a null
	 */
	@SuppressWarnings("static-access")
	public  Object getAggregateColumnValue(String table,Column column,QUERY_TYPE aggregate) throws SQLException,NoValueException
	{
		String query = "SELECT " + aggregate.toString() +"(" + column.getColumnName() +")" + " FROM " + table;
		Statement s = db.getConnection().createStatement();
		ResultSet result = s.executeQuery(query);
		if(result.next())
			return result.getObject(1);
		else
			return null;
	}
}
