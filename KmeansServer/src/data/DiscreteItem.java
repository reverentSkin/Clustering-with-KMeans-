package data;

import java.io.Serializable;

/**
 * La classe DiscreteItem estende la classe astratta Item e rappresenta una coppia 
 * <Attributo discreto- valore discreto>
 * Il valore che fara' parte di un DiscreteItem sara' uno dei valori
 * appartenenti al dominio dell'attributo discreto specificato.
 * 
 * @author Veronico, Mazzone, Sgaramella
 */
@SuppressWarnings("serial")
public class DiscreteItem extends Item implements Serializable{

	/**
	 * Invoca il costruttore della classe madre inizializzando l'attributo coninvolto
	 * nell'item di tipo {@link DiscreteAttribute} assegnandoli il valore specificato.
	 * 
	 * @param attribute DiscreteAttribute coinvolto nell'item corrente.
	 * @param value	Valore da assegnare all'item. (Fara' parte del dominio dei valori di un DiscreteAttribute)
	 */
	public DiscreteItem(Attribute attribute, Object value) 
	{
		super(attribute, value);
	}
	
	/**
	 * Implementa il metodo definito come astratto nella classe madre.
	 * Esso effettua un controllo tra stringhe.
	 * In particolare verra' restituito 0 se (getValue().equals(a)), cioe' se
	 * il valore dell'attributo corrente (di tipo String) e' uguale al valore 
	 * specificato come parametro. Se le due stringhe sono diverse viene restituito 1.
	 */
	public double distance(Object a)
	{
		if(getValue().equals(a)) 
			return 0;
		else
			return 1;
	}
}
