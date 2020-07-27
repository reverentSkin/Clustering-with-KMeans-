package mining;

import java.io.Serializable;

import data.Data;
import data.OutOfRangeSampleSize;
import data.Tuple;

/**
 * La classe ClusterSet rappresenta un insieme di cluster. Tale insieme e' rapprentato
 * dal vettore (attributo della classe) C. 
 * Ciascuna cella del vettore conterra' un cluster che sara' rapprentato da una tupla
 * (centroide) e da un insieme di indici di riga della tabella {@link Data} appartenenti al
 * a ciascun centroide.
 * 
 * @author Veronico, Mazzone, Sgaramella
 *
 */
@SuppressWarnings("serial")
public class ClusterSet implements Serializable{

	/**
	 * Attributo che rappresenta un vettore di Cluster e rappresenta il ClusterSet
	 */
	Cluster C[]; 
	
	/**
	 * Attributo che rappresenta l'indice del vettore {@link #C}.
	 * L'attributo viene incrementato ogni qualvolta viene aggiunto un nuovo cluster.
	 * Esso e' inizializzato a 0
	 */
	int i=0;

	/**
	 * Crea il vettore che rappresenta il ClusterSet assegnadogli dimensione k
	 * 
	 * @param k Dimensione del vettore rappresentante il ClusterSet {@link #C}
	 */
	ClusterSet(int k) 
	{
		C= new Cluster[k]; 
	}

	/**
	 * Il metodo si occupa di aggiungere un Cluster al vettore, e di incrementare
	 * l'indice {@link #i} per successive aggiunte.
	 * 
	 * @param c Cluster da aggiungere al vettore.
	 */
	void add(Cluster c)
	{
		C[i]=c;
		i++;
	}

	/**
	 * Il metodo ritorna un Cluster presente all'interno del vettore {link #C}
	 * 
	 * @param i Indice corrispondente al Cluster che si vuole ottenere
	 * 
	 * @return Cluster in posizione i del vettore {@link #C}
	 */
	Cluster get(int i)
	{
		return C[i];
	}

	/**
	 * Sceglie i centroidi, crea un cluster per ogni centroide e lo memorizza in C
	 * 
	 * @param data La tabella rappresentata dalla classe {@link data.Data}
	 * 
	 * @throws OutOfRangeSampleSize
	 */
	void initializeCentroids(Data data) throws OutOfRangeSampleSize
	{
		try 
		{
			int centroidIndexes[]=data.sampling(C.length);
			for(int i=0;i<centroidIndexes.length;i++)
			{
				Tuple centroidI=data.getItemSet(centroidIndexes[i]);
				add(new Cluster(centroidI));
			}
		}
		catch(OutOfRangeSampleSize ex)
		{
			ex.toString();
		}
	}

	/**
	 * Calcola la distanza tra la tupla riferita da tuple ed il centroide di ciascun 
	 * cluster in C e restituisce il cluster piu' vicino.
	 * 
	 * @param tuple Tupla da considerare.
	 * 
	 * @return Cluster piu' vicino alla tupla passata come paramentro.
	 */
	Cluster nearestCluster(Tuple tuple)
	{
		double currentDistance = tuple.getDistance(C[0].getCentroid());
		int indexCluster = 0;
		for(int j = 1; j < i; j++) {
			if(tuple.getDistance(C[j].getCentroid()) < currentDistance) {
				currentDistance = tuple.getDistance(C[j].getCentroid());
				indexCluster = j;
			}
		}
		return get(indexCluster);
	}
	
	/**
	 * Identifica e restituisce il cluster a cui la tupla (rappresentate l'esempio identificato da id) 
	 * appartiene. Se la tupla non e' inclusa in nessun cluster restituisce null
	 * 
	 * @param id Indice di una riga della tabella {@link data.Data}
	 * 
	 * @return Cluster a cui appartiene la tupla identificata da id 
	 */
	Cluster currentCluster(int id)
	{
		int index = 0;
		boolean found = false;
		for(int j = 0; j < i; j++) {
			if(get(j).contain(id)) {
				found = true;
				index = j;
				break;
			}
		}
		if(found == true) {
			return get(index);
		}else {
			return null;
		}
	}
	
	/**
	 * Calcola il nuovo centroide per ciascun cluster in C
	 * 
	 * @param data La tabella rappresentata dalla classe {@link data.Data}
	 */
	void updateCentroids(Data data)
	{
		for(int j=0;j<i;j++)
			get(j).computeCentroid(data);
	}
	
	/**
	 * Il metodo si occupa di restituire una stringa formata dalla
	 * concatenzione delle diverse stringhe, ottenute richiamando il metodo
	 * {@link Cluster#toString()} su ciascun Cluster appartenente al vettore
	 * {@link #C}.
	 */
	public String toString()
	{
		String str = "";
		for(int i = 0; i < C.length; i++) {
			str += get(i).getCentroid().toString() + " ";
		}

		return str;
	}
	
	/**
	 * Il metodo si occupa di restituire una stringa formata dalla
	 * concatenzione delle diverse stringhe, ottenute richiamando il metodo
	 * {@link Cluster#toString(Data)} su ciascun Cluster appartenente al vettore
	 * {@link #C}.
	 * 
	 * @param data La tabella rappresentata dalla classe {@link data.Data}
	 * 
	 */
	public String toString(Data data) {
		String str="";
		for(int i = 0; i < C.length; i++){
			if (C[i] != null){
				str += i+1 + ":" + C[i].toString(data) + "\n";		
			}
		}
		return str;		
	}
}
