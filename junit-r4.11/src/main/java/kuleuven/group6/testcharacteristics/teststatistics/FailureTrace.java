package kuleuven.group6.testcharacteristics.teststatistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.runner.Description;

/**
 * 
 * @author Team 6
 *
 */
public class FailureTrace implements ITestStatistic {

	protected Description testDescription;
	protected Collection<StackTraceElement> pointsOfFailure;
	
	public FailureTrace(Description testDescription, StackTraceElement pointOfFailure) {
		this.testDescription = testDescription;
		Collection<StackTraceElement> pointsOfFailure = new ArrayList<>();
		pointsOfFailure.add(pointOfFailure);
		this.pointsOfFailure = pointsOfFailure;
	}
	
	public FailureTrace(Description testDescription, Collection<StackTraceElement> pointsOfFailure) {
		this.testDescription = testDescription;
		this.pointsOfFailure = Collections.unmodifiableCollection(pointsOfFailure);
	}
	
	@Override
	public Description getTestDescription() {
		return testDescription;
	}

	public Collection<StackTraceElement> getPointsOfFailure() {
		return pointsOfFailure;
	}
	
}
