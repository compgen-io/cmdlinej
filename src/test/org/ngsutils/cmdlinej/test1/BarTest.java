package org.ngsutils.cmdlinej.test1;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ngsutils.cmdlinej.MainBuilder;

public class BarTest {

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
			.addCommand(Bar.class)
			.findAndRun(new String[] { "bar", "--str", "bar", "--int", "2", "bar" });
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
