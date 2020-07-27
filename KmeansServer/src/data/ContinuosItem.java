package data;

import java.io.Serializable;

/**
 * La classe ContinuousItem estende la classe astratta Item e rappresenta una coppia 
 * <Attributo continuo - valore numerico>
 * 
 * @author Veronico, Mazzone, Sgaramella
 */
@SuppressWarnings("serial")
public class ContinuosItem extends Item implements Serializable{
	
	/**
	 * Invoca il costruttore della classe madre inizializzando l'attributo coninvolto
	 * nell'item di tipo {@link ContinuousAttribute} assegnandoli il valore specificato.
	 * 
	 * @param attribute ContinuousAttribute coinvolto nell'item corrente.
	 * @param value	Valore da assegnare all'item.
	 */
	public ContinuosItem(Attribute attribute, double value) {
		super(attribute, value);
	}
	
	/**
	 * Determina la distanza (in valore assoluto) tra il valore
	 * scalato memorizzato nello item corrente {@link Item#getValue()} e quello scalato
	 * associato al parametro a.
	 * Per ottenere valori scalati si utilizza {@link ContinuousAttribute#getScaledValue(double)}
	 */
	@Override
	public double distance(Object a) {
		double distance=0.0;
		distance=Math.abs(((ContinuosAttribute)this.getAttribute()).getScaledValue(Double.parseDouble(this.getValue().toString()))-
				((ContinuosAttribute)this.getAttribute()).getScaledValue(Double.parseDouble(a.toString())));
		return distance;
	}


}
