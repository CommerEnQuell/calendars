package nl.commerquell.calendar.error;

public class BogusDateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2344009664534100120L;
	
	public BogusDateException(String message) {
		super(message);
	}

}
