package kuleuven.group6.testcharacteristics;

import org.junit.runner.Description;

/**
 * This class will count how many times a test has failed
 * @author Team 6
 *
 */
public class FailureCount implements ITestStatistic {

	protected final Description testDescription;
	protected final int failureCount;
	
	public FailureCount(Description testDescription, int failureCount) {
		this.testDescription = testDescription;
		this.failureCount = failureCount;
	}

	public Description getTestDescription() {
		return testDescription;
	}

	public int getFailureCount() {
		return failureCount;
	}

}
