package kuleuven.group6.testcharacteristics;

import org.junit.runner.Description;

public class FailureCount extends AbstractTestCharacteristic<Integer> implements ITestStatistic<Integer> {

	protected FailureCount(Description testDescription, int failureCount) {
		super(testDescription, failureCount);
	}
	
}
