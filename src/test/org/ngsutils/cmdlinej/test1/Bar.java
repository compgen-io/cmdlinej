package org.ngsutils.cmdlinej.test1;

import io.compgen.annotation.Command;
import io.compgen.annotation.Option;
import io.compgen.annotation.UnnamedArg;

@Command(name="bar", desc="Bar description")
public class Bar {
	private String s=null;
	private String arg = null;
	private int i=-1;
	private Integer intObj = null;
	private boolean verbose=false;
	private boolean shouldBeFalse=false;
	
	@UnnamedArg()
	public void setArg(String s) {
		this.arg = s;
	}

	@Option(name="str", desc="String option")
	public void setString(String s) {
		this.s = s;
	}

	@Option(charName="i", desc="int option w/ default", defaultValue="42")
	public void setInt(int i) {
		this.i = i;
	}

	@Option(charName="v", desc="boolean option")
	public void setVerbose(boolean v) {
		this.verbose = v;
	}

	@Option(charName="f", desc="default boolean option")
	public void setShouldBeFalse(boolean v) {
		this.shouldBeFalse = v;
	}

	@Option(name="int", desc="Integer option")
	public void setIntObj(Integer i) {
		this.intObj = i;
	}

	public void exec() throws Exception {
		assert s!=null;
		assert s.equals("bar");
		assert intObj!=null;
		assert i == 42;
		assert intObj == 2;
		assert !verbose;
		assert !shouldBeFalse;
		assert arg != null;
		assert arg.equals("bar");
		System.err.println("Good!");
	}
}
