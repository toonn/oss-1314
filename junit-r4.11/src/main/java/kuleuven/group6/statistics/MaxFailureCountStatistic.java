package kuleuven.group6.statistics;

import kuleuven.group6.collectors.IDataCollectorManager;
import kuleuven.group6.testcharacteristics.FailureCount;

import org.junit.runner.Description;

public class MaxFailureCountStatistic extends FailureCountStatistic {
	
	public MaxFailureCountStatistic(IDataCollectorManager dataCollectorManager) {
		super(dataCollectorManager);
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
