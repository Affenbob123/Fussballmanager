package fussballmanager.mvc.auktionshaus;

public class AuktionshausException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5022369378981656461L;
	final String message;
	
	public AuktionshausException(String s) {
		super();
		message = s;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
