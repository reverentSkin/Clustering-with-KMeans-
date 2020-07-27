package server;

import java.io.*;
import java.net.*;

/**
 * La classe MultiServer modella un server in grado di ascoltare differenti richieste per
 * differenti client andando quindi a realizzare un server che ascolta piu' richieste.
 * 
 * @author Veronico, Mazzone, Sgaramella
 */
public class MultiServer 
{
	/**
	 * Porta su cui il server sara' in ascolto.
	 * Sara' inizializzato a 8080
	 */
	private int PORT = 8080;

	/**
	 * Costruttore di classe. Inizializza la porta ed invoca run()
	 * @param port Porta di connessione
	 */
	public MultiServer(int port) throws IOException
	{
		this.PORT=port;
		run();
	}

	public static void main(String[] args) throws IOException
	{
		new MultiServer(8080);
	}

	/**
	 * Istanzia un oggetto istanza della classe ServerSocket che pone in attesa 
	 * di richiesta di connessioni da parte del client. Ad ogni nuova richiesta 
	 * connessione si istanzia ServerOneClient.
	 */
	private void run() throws IOException
	{
		ServerSocket s = new ServerSocket(PORT);
		System.out.println("Server Started");
		try 
		{
			while(true) 
			{
				// Si blocca finchè non si verifica una connessione:
				Socket socket = s.accept();
				try 
				{
					new ServerOneClient(socket);
				} 
				catch(IOException e) 
				{
					// Se fallisce chiude il socket,
					// altrimenti il thread la chiuderà:
					socket.close();
				}
			}
		} 
		finally 
		{
			s.close();	
		}
	}
}
