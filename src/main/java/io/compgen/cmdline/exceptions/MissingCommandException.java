package io.compgen.cmdline.exceptions;

public class MissingCommandException extends Exception {

	public MissingCommandException(String s) {
		super(s);
	}

	public MissingCommandException() {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3275862577567323839L;

}
