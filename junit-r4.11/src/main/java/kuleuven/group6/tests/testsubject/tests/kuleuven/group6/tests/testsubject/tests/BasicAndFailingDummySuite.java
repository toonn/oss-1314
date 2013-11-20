package kuleuven.group6.tests.testsubject.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * A test suite that contains the tests BasicTest and FailingDummyTest.
 * 
 * @author team 6
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
	BasicTest.class,
	FailingDummyTest.class
})
public class BasicAndFailingDummySuite {

	public static int getNbTests() {
		return BasicTest.getNbTests() + FailingDummyTest.getNbTests();
	}
	
}
