package kuleuven.group6.tests.testsubject.tests;

import kuleuven.group6.tests.testsubject.source.FailingDummy;
import org.junit.Test;
import static org.junit.Assert.*;

public class FailingDummyTest {
	
	public static int getNbTests() {
		return 2;
	}
	
	
	@Test
	public void testTrueButActuallyFalse() {
		FailingDummy instance = new FailingDummy();
		boolean value = instance.trueButActuallyFalse();
		assertTrue(value);
	}
	
	@Test
	public void testMethodThrowingRuntimeException() {
		FailingDummy instance = new FailingDummy();
		instance.methodThrowingRuntimeException();
	}
	
}
