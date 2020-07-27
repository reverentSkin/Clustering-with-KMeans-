package database;

import java.sql.*;

/**
 * La classe DbAccess che realizza l'accesso alla base di dati.
 * La classe si occupa di effettuare una connessione alla base di dati sfruttando i driver di
 * connessione mysql.
 * La connessione e' effettuata in locale attraverso una url del tipo:
 * "jdbc:mysql://127.0.0.1:PORT/MapDBBig"
 * Dove PORT sara' la porta in cui e' disponibile il servizio mysql
 * Per la connessione saranno necessaria anche uno userid e una password.
 * Sia la porta, sia lo userid che la password saranno specificati all'interno del 
 * {@link server.DatabaseDialog}. I dati specificati all'interno della finestra modale,
 * verranno utilizzati all'interno del costruttore.
 * 
 * @author Veronico, Mazzone, Sgaramella
 *
 */
public class DbAccess {
	
	/**
	 * Nome del driver utilizzato per la connessione al database.
	 * Sara' inizializzato con: org.gjt.mm.mysql.Driver
	 */
	String DRIVER_CLASS_NAME = "org.gjt.mm.mysql.Driver";
	
	/**
	 * Tipo di DBMS.
	 * Sara' inizializzato con jdbc:mysql
	 */
	private static final String DBMS = "jdbc:mysql";
	
	/**
	 * Indirizzo necessario per la connessione al database
	 * Sara' inizializzato con localhost
	 */
	private static final String SERVER="localhost";
	
	/**
	 * Nome del database
	 * Sara' inizializzato con MapDB
	 */
	private static final String DATABASE = "MapDB";
	
	/**
	 * Porta sulla quale sar&agrave disponibile il servizio mysql
	 * Sara' inizializzato a 3306
	 */
	private static final String PORT="3306";
	
	/**
	 * User id necessario per la connessione
	 * Sara' inizializzato a MapUser
	 */
	private static final String USER_ID = "MapUser";
	
	/**
	 * Password necessaria per la connessione
	 * Sara' inizilizzato a map
	 */
	private static final String PASSWORD = "map";
	
	/**
	 * Oggetto istanza della classe Connection.
	 * 
	 */
	private static Connection conn;// gestisce una connessione
	
	/**
	 * Impartisce al class loader il compito di caricare il driver mysql e inizializza la connessione riferita da conn.
	 * @throws DatabaseConnectionException solleva e propaga una eccezione di tipo DatabaseConnectionException in caso di fallimento nella connessione al database.
	 * @throws ClassNotFoundException  
	 * @throws InstantiationException 
	 * @throws SQLException 
	 */
	public static void initConnection() throws DatabaseConnectionException, ClassNotFoundException, SQLException, InstantiationException
	{
		try
		{
			Class.forName("org.gjt.mm.mysql.Driver");
		}
		catch (ClassNotFoundException e)
		{
			System.err.println("Problema con il Driver del DB");
		}
		conn = DriverManager.getConnection(DBMS+"://"+SERVER+":"+PORT+"/"+DATABASE, USER_ID, PASSWORD);
	}
	
	public static Connection getConnection()
	{
		return conn;
	}
	
	public static void closeConnection() throws SQLException
	{
		conn.close();
	}

}
