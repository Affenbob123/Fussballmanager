package fussballmanager.exceptions;

public class DatumFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1058480637276127117L;
	final String message;
	
	public DatumFormatException(String s) {
		super();
		message = s;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
