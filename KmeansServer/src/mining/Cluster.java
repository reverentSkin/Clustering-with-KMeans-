package mining;

import java.io.Serializable;
import java.util.*;

import data.*;

/**
 * La classe si occupa di modellare un singolo Cluster.
 * Ciascun cluster e' rappresentato da una tupla (Centroide) e da un insieme
 * di interi rappresentanti le righe della tabella {@link data.Data} appartenenti al
 * cluster.
 * Il centroide e' modellato usando la classe {@link data.Tuple}, mentre l'insieme
 * delle tuple appartenenti al cluster e' modellato usando un insieme {@link java.util.Set}
 * 
 * @author Veronico, Mazzone, Sgaramella
 */
@SuppressWarnings("serial")
public class Cluster implements Serializable {
	
	/**
	 * Attributo rappresentante la tupla centroide del Cluster.
	 */
	private Tuple centroid;
	
	/**
	 * Insieme delle righe della tabella Data appartenenti al Cluster.
	 */
	Set<Integer> clusteredData;

	/**
	 * Il costruttore si occupa di inizializzare i dati membro. In particolare
	 * verra' inizializzata la tupla centroide (attraverso il parametro)
	 * e verra' inizializzato l'insieme {@link #clusteredData}
	 * 
	 * @param centroid Tupla rappresentante il centroide del Cluster
	 */
	Cluster(Tuple centroid){
		this.centroid=centroid;
		clusteredData=new HashSet<Integer>();
	}

	/**
	 * Ritorna il centroide per questo cluster.
	 */
	Tuple getCentroid(){
		return centroid;
	}

	/**
	 * Calcola il centroide per questo cluster utilizzando il metodo
	 * {@link data.Item#update(Data, Set)}
	 * 
	 * @param data La tabella {@link data.Data} su cui effettuare il calcolo.
	 */
	void computeCentroid(Data data){
		for(int i=0;i<centroid.getLength();i++){
			centroid.get(i).update(data,clusteredData);
		}
	}
	
	/**
	 * Aggiunge un elemento all'insieme {@link #clusteredData}.
	 * L'aggiunta avviene solo nel caso in cui tale elemento identificato da id,
	 * non e' presente nell'insieme.
	 * Cio' e' vero solo nel caso in cui la tupla identificata da id ha cambiato cluster.
	 * Quindi l'aggiunta all'insieme {@link #clusteredData} si verifica solo in questo caso.
	 * 
	 * @param id Identificativo rappresentante la riga della tabella {@link data.Data}
	 * 			 appertenente al cluster
	 * 
	 */
	boolean addData(int id){
		return clusteredData.add(id);

	}

	/**
	 * Il metodo verifica se una transazione e' clusterizzata 
	 * (ovvero se e' presente nell'insieme {@link #clusteredData}).
	 * 
	 * Ritorna vero se la transazione e' clusterizzata, falso altrimenti.
	 */
	boolean contain(int id){
		return clusteredData.contains(id);
	}

	/**
	 * Il metodo elimina la tupla identificata da id dall'insieme {@link #clusteredData} nel
	 * momento in cui questa cambia cluster.
	 */
	void removeTuple(int id){
		clusteredData.remove(id);
	}

	/**
	 * Il metodo si occupa di creare (e ritornare) una stringa contenente il centroide 
	 * di questo cluster.
	 */
	public String toString(){
		String str="Centroid=(";
		for(int i=0;i<centroid.getLength();i++)
			str+=centroid.get(i);
		str+=")";
		return str;
	}

	/**
	 * Il metodo si occupa di creare una stringa contenente il centroide di questo cluster.
	 * E per questo cluster si occupa inoltre di mostrare tutte le tuple in esso clusterizzate e
	 * la distanza massima.
	 * Cio' avviene utilizzando il riferimento passato come parametro.
	 * 
	 * @param data La tabella {@link data.Data} su cui vengono ottenute le tuple clusterizzate
	 * 			   in questo cluster
	 */
	public String toString(Data data)
	{
		String str="Centroid=(";
		for(int i=0;i<centroid.getLength();i++)
			str+=centroid.get(i)+ " ";
		str+=")\nExamples:\n";
		int array[] = new int[clusteredData.size()];
		int k = 0;
		for(Object o : clusteredData.toArray()) {
			array[k++] = Integer.parseInt(o.toString());
		}		
		for(int i=0;i<array.length;i++){
			str+="[";
			for(int j=0;j<data.getNumberOfAttributes();j++)
				str+=data.getAttributeValue(array[i], j)+" ";
			str+="] dist = "+getCentroid().getDistance(data.getItemSet(array[i]))+"\n";

		}
		str+="AvgDistance="+getCentroid().avgDistance(data, array) + "\n";
		return str;


	}

}
