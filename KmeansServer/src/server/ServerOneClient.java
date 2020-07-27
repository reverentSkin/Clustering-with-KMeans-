package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import data.Data;
import data.OutOfRangeSampleSize;
import database.DatabaseConnectionException;
import database.EmptySetException;
import database.NoValueException;
import mining.KMeansMiner;

/**
 * Classe concreta che modella il comportamento del server a connessione avvenuta.
 * @author Veronico, Mazzone, Sgaramella
 */
public class ServerOneClient extends Thread{

	private Socket socket;
	private ObjectInputStream in; 
	private ObjectOutputStream out;
	private KMeansMiner kmeans;

	/**
//	 * Costuttore di classe che inizializza la socket, gli stream di input e output e avvia il thread.
	 * @param s Socket.
	 * @throws IOException
	 */
	public ServerOneClient(Socket s) throws IOException {
		this.socket = s;
		out = new ObjectOutputStream(socket.getOutputStream());	
		in = new ObjectInputStream(socket.getInputStream()) ;		
		this.start();		
	}

	/**
	 * Metodo che esegue la richiesta del client.
	 */
	@SuppressWarnings("unused")
	@Override
	public void run() {
		try {
			int answerMenu = in.readInt();
			switch(answerMenu) {
			case 1:
				String fileName = in.readObject().toString();
				try
				{
					this.kmeans = new KMeansMiner(fileName + ".dmp");
					out.writeObject("OK");
				}
				catch(FileNotFoundException e)
				{
					out.writeObject("Errore!");
					break;
				}
				out.writeObject(kmeans.getC().toString());
				break;							
			case 2:
				int k = Integer.parseInt(in.readObject().toString());
				String tableName = in.readObject().toString();
				String file = in.readObject().toString();
				try
				{
					Data data = new Data(tableName);
					out.writeObject("OK");
				}
				catch(SQLException e)
				{
					out.writeObject("Errore!");
					break;
				}
				Data data = new Data(tableName);
				if(k<=0||k>data.getNumberOfExamples())
				{
					out.writeObject("NO");
					break;
				} 
				else
				{
					out.writeObject("OK");
				}
				this.kmeans = new KMeansMiner(k);
				kmeans.kmeans(data);
				kmeans.salva(file + ".dmp");
				out.writeObject(kmeans.getC().toString(data));
				out.writeObject("OK");
				break;
			default:
				return;
			}		
		} catch(IOException e) {
			System.err.println("Connessione al Client persa!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (DatabaseConnectionException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (EmptySetException e) {
			e.printStackTrace();
		} catch (NoValueException e) {
			e.printStackTrace();
		} catch (OutOfRangeSampleSize e) {
			e.printStackTrace();
		}
		finally 
		{
			try {
				socket.close();
			} catch(IOException e) {
				System.err.println("Socket non chiusa");
			}
		}

	}
}

