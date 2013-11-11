package kuleuven.group6.tests.testsubject.tests;

/**
 * Easily locate the binary folder of the tests folder of the test subject.
 */
public class TestsLocator {

	public static String getLocation() {
		return TestsLocator.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	}
	
}
