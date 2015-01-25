package org.ngsutils.cmdlinej.exceptions;

import java.lang.reflect.Method;

public class CommandArgumentException extends Exception {

	public final Method method;
	public final String value;
	public final Class<?> clazz;
	
	public CommandArgumentException(Method m, String val, Class<?> clazz) {
		super(m.getName()+":"+val+" => "+clazz.getCanonicalName());
		this.method = m;
		this.value = val;
		this.clazz = clazz;
	}

	public CommandArgumentException(Method m, String val) {
		super(m.getName()+":"+val);
		this.method = m;
		this.value = val;
		this.clazz = null;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4443321464121336991L;
}
