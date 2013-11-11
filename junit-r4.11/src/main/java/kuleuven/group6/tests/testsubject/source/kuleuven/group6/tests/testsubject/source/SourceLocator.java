package kuleuven.group6.tests.testsubject.source;

/**
 * Easily locate the binary folder of the source folder of the test subject.
 */
public class SourceLocator {

	public static String getLocation() {
		return SourceLocator.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	}
	
}
