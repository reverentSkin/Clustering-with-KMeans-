package mining;

import data.Data;
import data.OutOfRangeSampleSize;
import java.io.*;

/**
 * La classe KmeansMiner e' la classe che si occupa di effettuare la scoperta
 * dei cluster (attraverso l'algoritmo kmeans che contiene). Inoltre la classe si occupa
 * sia di salvare su file le attivita' di scoperta dei cluster e sia di leggere
 * da file attivita' precedentemente effettuate.
 * La classe percio' implementa l'interfaccia {@link java.io.Serializable} in modo
 * da permettere il salvataggio su file degli oggetti che essa manipola (ovviamente tutte le
 * classi utilizzate implementano l'interfaccia).
 * La logica mantenuta nel salvataggio (e quindi nella lettura) dei file utilizzati e'
 * la seguente:
 * nome_tabella_database + numero di cluster da scoprire
 *  
 * @author Veronico, Mazzone, Sgaramella
 *
 */

@SuppressWarnings("serial")
public class KMeansMiner implements Serializable
{
	
	/**
	 * L'attributo rappresenta l'insieme dei cluster che saranno risultato
	 * dell'attivita' di scoperta oppure saranno il risultato di una lettura
	 * di tale attivita' da file.
	 * 
	 */
	private ClusterSet C; 
	
	/**
	 * Il costruttore si occupa di inizializzare il ClusterSet in base
	 * al numero di Cluster che si vuole scoprire.
	 * 
	 * @param k Numero di Cluster da scoprire.
	 */
	public KMeansMiner(int k) 
	{
		C=new ClusterSet(k);
	}

	/**
	 * Il costruttore si occupa di inizializzare il ClusterSet. In questo caso l'inizializzazione
	 * avviene leggendo il file (identificato dal parametro fileName). Il nome del file segue
	 * la logica descritta precedentemente (vedi descrizione della classe).
	 * In questo caso il ClusterSet conterra' l'insieme di Cluster dovuto a precedenti
	 * attivita' di scoperta.
	 * 
	 * @param fileName	Nome del file da leggere.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public KMeansMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
		this.C = (ClusterSet)in.readObject();
		in.close();
	}
	
	/**
	 * Il metodo si occupa di effettuare il salvataggio su file (detto anche serializzazione)
	 * delle attivita' di scoperta. Il salvataggio risultera' utile nel caso in cui
	 * si vorranno ottenere precedenti attivita' di scoperta senza interragare la base
	 * di dati.
	 * @param fileName Nome del file da leggere.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void salva(String fileName) throws FileNotFoundException, IOException
	{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
		out.writeObject(C);
		out.close();
	}

	/**
	 * Ritorna il ClusterSet {@link #C}
	 */
	public ClusterSet getC()
	{
		return C;
	}

	/**
	 * Il metodo esegue l'algoritmo k-means eseguendo i seguenti passi:
	 * 1. Scelta casuale di centroidi per k clusters assegnandoli al ClusterSet.
	 * 2. Assegnazione di ciascuna riga della matrice in data al cluster avente 
	 * 	  centroide piu' vicino all'esempio (utilizzando il metodo 
	 * 	  {@link ClusterSet#nearestCluster(Tuple)}).
	 * 3. Calcolo dei nuovi centroidi per ciascun cluster (utilizzando il metodo
	 * 	  {@link ClusterSet#updateCentroids(Data)}
	 * 4. Ripete i passi 2 e 3 finche' due iterazioni consecuitive non restituiscono 
	 * 	  centroidi uguali.
	 * Il metodo propaga l'eccezione OutOfRangeSampleSize nel caso in cui il numero
	 * k di cluster da scoprire risulta essere maggiore delle tuple presenti nella
	 * tabella {@link data.Data} oppure se tale numero e' 0.
	 * 
	 * @param data	La tabella {@link data.Data} che costituisce l'insieme di dati
	 * 				su cui eseguire l'algoritmo.
	 * 
	 * @return Numero di iterazioni eseguite
	 * @throws OutOfRangeSampleSize
	 * 
	 */
	public int kmeans(Data data) throws OutOfRangeSampleSize
	{
		int numberOfIterations=0;
		//STEP 1
		C.initializeCentroids(data);
		boolean changedCluster=false;
		do{
			numberOfIterations++;
			//STEP 2
			changedCluster=false;
			for(int i=0;i<data.getNumberOfExamples();i++){
				Cluster nearestCluster = C.nearestCluster(data.getItemSet(i));
				Cluster oldCluster=C.currentCluster(i);
				boolean currentChange=nearestCluster.addData(i);
				if(currentChange)
					changedCluster=true;
				//rimuovo la tupla dal vecchio cluster
				if(currentChange && oldCluster!=null)
					//il nodo va rimosso dal suo vecchio cluster
					oldCluster.removeTuple(i);

			}			
			//STEP 3
			C.updateCentroids(data);
		}
		while(changedCluster);
		return numberOfIterations;
	}
}
