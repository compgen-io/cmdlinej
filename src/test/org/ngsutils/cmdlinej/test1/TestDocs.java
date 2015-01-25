package org.ngsutils.cmdlinej.test1;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ngsutils.cmdlinej.MainBuilder;
import org.ngsutils.cmdlinej.exceptions.MissingCommandException;

public class TestDocs {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		try {
			new MainBuilder()
			.addCommand(Foo.class)
			.addCommand(Bar.class)
			.addCommand(Baz.class)
			.addCommand(Baz2.class)
			.showCommands();
		} catch (MissingCommandException e) {
			fail();
		}
	}

	@Test
	public void testFoo() {
		try {
			new MainBuilder()
			.addCommand(Foo.class)
			.addCommand(Bar.class)
			.addCommand(Baz.class)
			.addCommand(Baz2.class)
			.showCommandHelp("foo");
		} catch (MissingCommandException e) {
			fail();
		}
	}

}
