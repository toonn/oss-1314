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
	protected Collection<String> pointsOfFailure;
	
	public FailureTrace(Description testDescription, String pointOfFailure) {
		this.testDescription = testDescription;
		Collection<String> pointsOfFailure = new ArrayList<>();
		pointsOfFailure.add(pointOfFailure);
		this.pointsOfFailure = pointsOfFailure;
	}
	
	public FailureTrace(Description testDescription, Collection<String> pointsOfFailure) {
		this.testDescription = testDescription;
		this.pointsOfFailure = Collections.unmodifiableCollection(pointsOfFailure);
	}
	
	@Override
	public Description getTestDescription() {
		return testDescription;
	}

	/*
	 * The pointOfFailure string is (fully qualified className).methodName
	 * So this does not comply to the JVM spec 4.3.3
	 */
	public Collection<String> getPointsOfFailure() {
		return pointsOfFailure;
	}
	
}
