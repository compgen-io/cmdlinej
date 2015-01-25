package org.ngsutils.cmdlinej.test1;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ngsutils.cmdlinej.MainBuilder;

public class BazTest {

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
			.addCommand(Baz.class)
			.findAndRun(new String[] { "baz", "foo", "bar", "baz" });
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
