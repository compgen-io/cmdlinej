package org.ngsutils.cmdlinej.test1;

import io.compgen.Exec;
import io.compgen.annotation.Command;
import io.compgen.annotation.UnnamedArg;

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
