package io.compgen.cmdline.exceptions;


public class UnknownArgumentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8061990904124867691L;
	public final Class<?> clazz;
	
	public UnknownArgumentException(Class<?> clazz, String str) {
		super(str);
		this.clazz = clazz;
	}
}
