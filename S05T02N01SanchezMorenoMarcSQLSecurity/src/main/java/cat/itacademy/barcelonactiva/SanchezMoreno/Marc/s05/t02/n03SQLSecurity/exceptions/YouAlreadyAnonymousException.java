package cat.itacademy.barcelonactiva.SanchezMoreno.Marc.s05.t02.n03SQLSecurity.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class YouAlreadyAnonymousException extends EntityNotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public YouAlreadyAnonymousException() {
		super("You already are Anonymous");
	}
}
