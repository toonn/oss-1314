package kuleuven.group6.testcharacteristics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Description;

/**
 * 
 * @author Team 6
 *
 */
public class FailureTrace implements ITestStatistic {

	protected Description testDescription;
	protected List<StackTraceElement> stackTrace;
	
	public FailureTrace(Description testDescription, StackTraceElement[] stackTrace) {
		this.testDescription = testDescription;
		this.stackTrace = Arrays.asList(stackTrace);
	}
	
	@Override
	public Description getTestDescription() {
		return testDescription;
	}

	public List<StackTraceElement> getStackTrace() {
		return Collections.unmodifiableList(stackTrace);
	}
	
}
