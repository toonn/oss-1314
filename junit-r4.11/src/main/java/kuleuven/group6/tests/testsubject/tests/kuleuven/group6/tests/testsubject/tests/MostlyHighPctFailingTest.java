package kuleuven.group6.tests.testsubject.tests;

import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

public class MostlyHighPctFailingTest {
	
	private void failFraction(double fraction) {
		Random rnd = new Random();
		if (rnd.nextDouble() <= fraction)
			fail();
	}

	@Test
	public void fail80pct() {
		failFraction(0.8);
	}
	
	@Test
	public void fail70pct() {
		failFraction(0.7);
	}
	
	@Test
	public void fail60pct() {
		failFraction(0.6);
	}
	
	@Test
	public void fail15pct() {
		failFraction(0.15);
	}
	
}
