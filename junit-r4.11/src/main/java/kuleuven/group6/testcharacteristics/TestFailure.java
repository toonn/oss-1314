package kuleuven.group6.testcharacteristics;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

public class TestFailure implements ITestData {

	protected final Description testDescription;
	protected final Failure failure;
	
	public TestFailure(Description testDescription, Failure failure) {
		this.testDescription = testDescription;
		this.failure = failure;
	}

	public Description getTestDescription() {
		return testDescription;
	}

	public Failure getFailure() {
		return failure;
	}
	
}
