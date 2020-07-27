package database;

@SuppressWarnings("serial")
public class EmptyTypeException extends Exception{
	
	EmptyTypeException(){
		super("Eccezione sollevata!");
	}
	
	public String toString() {
		return "Tipo vuoto!";
	}
}
