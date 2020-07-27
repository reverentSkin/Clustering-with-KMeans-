package data;

import java.io.Serializable;

/**
 * La classe ContinuousAttribute estende la classe Attribute e modella un attributo
 * continuo (numerico). Tale classe include i metodi per la ìnormalizzazioneî del 
 * dominio dell'attributo nell'intervallo [0,1] al fine da rendere confrontabili 
 * attributi aventi domini diversi.
 * 
 * @author Veronico, Mazzone, Sgaramella
 *
 */
@SuppressWarnings("serial")
public class ContinuosAttribute extends Attribute implements Serializable{

	/**
	 * Estremo massimo dell'intervallo di valori (dominio) che l'attributo puo' reamente assumere.
	 */
	private double max;
	
	/**
	 * Estremo minimo dell'intervallo di valori (dominio) che l'attributo puo' reamente assumere.
	 */
	private double min ;// rappresentano gli estremi dell'intervallo di valori (dominio) che l'attributo pu√≤ reamente assumere.

	/**
	 * Il costruttore invoca il costruttore della classe madre e inizializza i membri aggiunti 
	 * per estensione
	 * 
	 * @param name Nome dell'attributo 
	 * @param index Identificativo dell'atributo
	 * @param min Estremo minimo dell'intervallo
	 * @param max Estremo massimo dell'intervallo
	 */
	public ContinuosAttribute(String name, int index,double min,double max) {
		super(name, index);
		this.min=min;
		this.max=max;
	}
	
	/**
	 * Calcola e restituisce il valore normalizzato del parametro passato in input. 
	 * La normalizzazione ha come codominio lo intervallo [0,1]. 
	 * La normalizzazione di v e' quindi calcolata come segue:
	 * v'=(v-min)/(max-min)
	 * 
	 * @param v Valore da normalizzare
	 * 
	 * @return double che rappresenta il valore normalizzato
	 */
	public double getScaledValue(double v)
	{
		return (v-min)/(max-min);	
	}

	


}
