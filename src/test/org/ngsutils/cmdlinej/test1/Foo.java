package org.ngsutils.cmdlinej.test1;

import io.compgen.cmdline.annotation.Command;
import io.compgen.cmdline.annotation.Option;

@Command(name="foo")
public class Foo {

	private String s=null;
	private int i=-1;
	private Integer intObj = null;
	private boolean verbose=false;
	private boolean shouldBeFalse=false;
	
	@Option(name="str", charName="s", desc="String option", defaultValue="defstr")
	public void setString(String s) {
		this.s = s;
	}

	@Option(charName="i", desc="int option")
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
		assert s.equals("foo");
		assert intObj!=null;
		assert i == 1;
		assert intObj == 2;
		assert verbose;
		assert !shouldBeFalse;
		System.err.println("Good!");
	}
}
