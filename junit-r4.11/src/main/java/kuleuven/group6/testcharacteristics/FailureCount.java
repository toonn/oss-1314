package kuleuven.group6.testcharacteristics;

import org.junit.runner.Description;

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
