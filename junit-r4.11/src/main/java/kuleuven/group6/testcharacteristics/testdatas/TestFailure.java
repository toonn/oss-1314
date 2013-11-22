package kuleuven.group6.testcharacteristics.testdatas;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * Contains Failure's for tests.
 */
public class TestFailure implements ITestData {

	protected final Description testDescription;
	protected final Failure failure;
	
	public TestFailure(Description testDescription, Failure failure) {
		this.testDescription = testDescription;
		this.failure = failure;
	}

	@Override
	public Description getTestDescription() {
		return testDescription;
	}

	public Failure getFailure() {
		return failure;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (! (obj instanceof TestFailure))
			 return false;
		TestFailure other = (TestFailure)obj;
		if (! getTestDescription().equals(other.getTestDescription())) 
			return false;
		if (! getFailure().equals(other.getFailure()))
			return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + testDescription.hashCode();
		hash = hash * 31 + failure.hashCode();
		return hash;
	}
	
}
