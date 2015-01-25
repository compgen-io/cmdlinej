package org.ngsutils.cmdlinej.test1;

import org.ngsutils.cmdlinej.Exec;
import org.ngsutils.cmdlinej.annotation.Command;
import org.ngsutils.cmdlinej.annotation.UnnamedArg;

@Command(name="baz2", desc="Baz2 description", category="baz")
public class Baz2 implements Exec {
	private String[] s=null;
	
	@UnnamedArg()
	public void setString(String[] s) {
		this.s = s;
	}

	@Override
	public void exec() throws Exception {
		assert s!=null;
		assert s.length==3;
		assert s[0].equals("foo");
		assert s[1].equals("bar");
		assert s[2].equals("baz");
		System.err.println("Good!");
	}
}
