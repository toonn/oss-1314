package kuleuven.group6.statistics;

import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.testcharacteristics.teststatistics.FailureCount;
import org.junit.runner.Description;

/**
 * 
 * @author Team 6
 *
 */
public class MaxFailureCountStatistic extends FailureCountStatistic {
	
	public MaxFailureCountStatistic(IDataEnroller dataEnroller) {
		super(dataEnroller);
	}

	@Override
	protected FailureCount composeTestStatistic(Description description) {
		int failureCount = 0;
		for (Description child : description.getChildren()) {
			int count = getTestStatistic(child).getFailureCount();
			if (failureCount < count)
				failureCount = count;
		}
		
		return new FailureCount(description, failureCount);
	}

}
