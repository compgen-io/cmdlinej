package org.ngsutils.cmdlinej.test1;

import static org.junit.Assert.fail;
import io.compgen.cmdline.MainBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Baz2Test {

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
			.addCommand(Baz2.class)
			.findAndRun(new String[] { "baz2", "foo", "bar", "baz" });
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
