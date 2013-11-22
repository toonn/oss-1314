package kuleuven.group6.testcharacteristics.teststatistics;

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

	@Override
	public Description getTestDescription() {
		return testDescription;
	}

	public int getFailureCount() {
		return failureCount;
	}
	
	public FailureCount increment() {
		int incrementedFailureCount = getFailureCount() + 1;
		return new FailureCount(getTestDescription(), incrementedFailureCount); 
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (! (obj instanceof FailureCount))
			 return false;
		FailureCount other = (FailureCount)obj;
		if (! getTestDescription().equals(other.getTestDescription())) 
			return false;
		if (getFailureCount() != other.getFailureCount())
			return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + testDescription.hashCode();
		hash = hash * 31 + failureCount;
		return hash;
	}

}
