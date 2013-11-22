package kuleuven.group6.testcharacteristics.teststatistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.runner.Description;

/**
 * Contains the top of the stacktrace of the failure of a test.
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
		return Collections.unmodifiableCollection(pointsOfFailure);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (! (obj instanceof FailureTrace))
			 return false;
		FailureTrace other = (FailureTrace)obj;
		if (! getTestDescription().equals(other.getTestDescription())) 
			return false;
		if (! pointsOfFailure.equals(other.pointsOfFailure))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + testDescription.hashCode();
		hash = hash * 31 + pointsOfFailure.hashCode();
		return hash;
	}
	
}
