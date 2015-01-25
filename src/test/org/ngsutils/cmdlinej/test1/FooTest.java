package org.ngsutils.cmdlinej.test1;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ngsutils.cmdlinej.MainBuilder;

public class FooTest {

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
			.findAndRun(new String[] { "foo", "--str", "foo", "-vi", "1", "--int", "2" });
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
