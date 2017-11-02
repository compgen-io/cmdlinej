package io.compgen.cmdline.exceptions;

import java.lang.reflect.Method;

public class CommandArgumentException extends Exception {

	public final Method method;
	public final String value;
	public final String name;
	public final Class<?> clazz;
	
	public CommandArgumentException(Method m, String name, Class<?> clazz, String val) {
		super(m.getName()+":"+name+" ("+clazz.getCanonicalName()+") => "+val);
		this.method = m;
		this.value = val;
		this.name = name;
		this.clazz = clazz;
	}

	public CommandArgumentException(Method m, String name) {
		super(m.getName()+":"+name);
		this.method = m;
		this.value = null;
		this.clazz = null;
		this.name = name;
	}

	public CommandArgumentException(String str) {
		super(str);
		this.method = null;
		this.value = null;
		this.clazz = null;
		this.name = null;
	}

	public CommandArgumentException(Exception e) {
		super(e);
		this.method = null;
		this.value = null;
		this.clazz = null;
		this.name = null;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4443321464121336991L;
}
