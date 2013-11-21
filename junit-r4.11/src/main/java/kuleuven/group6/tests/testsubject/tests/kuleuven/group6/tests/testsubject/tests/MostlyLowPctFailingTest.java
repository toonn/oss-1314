package kuleuven.group6.tests.testsubject.tests;

import static org.junit.Assert.fail;

import java.util.Random;

import org.junit.Test;

public class MostlyLowPctFailingTest {
	
	private void failFraction(double fraction) {
		Random rnd = new Random();
		if (rnd.nextDouble() <= fraction)
			fail();
	}
	
	@Test
	public void fail10pct() {
		failFraction(0.1);
	}
	
	@Test
	public void fail20pct() {
		failFraction(0.2);
	}
	
	@Test
	public void fail30pct() {
		failFraction(0.3);
	}
	
	@Test
	public void fail85pct() {
		failFraction(0.85);
	}
	
}
