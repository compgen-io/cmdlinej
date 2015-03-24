package org.ngsutils.cmdlinej.test1;

import io.compgen.cmdline.annotation.Command;
import io.compgen.cmdline.annotation.UnnamedArg;
import io.compgen.cmdline.impl.AbstractCommand;

import java.util.List;

@Command(name="baz", desc="Baz description", category="baz")
public class Baz extends AbstractCommand {
	private List<String> s=null;
	
	@UnnamedArg()
	public void setString(List<String> s) {
		this.s = s;
	}

	public void exec() throws Exception {
		assert s!=null;
		assert s.size()==3;
		assert s.get(0).equals("foo");
		assert s.get(1).equals("bar");
		assert s.get(2).equals("baz");
		System.err.println("Good!");
	}
}
