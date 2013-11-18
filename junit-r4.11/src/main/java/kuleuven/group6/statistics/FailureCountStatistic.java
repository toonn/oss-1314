package kuleuven.group6.statistics;

import org.junit.runner.Description;

import kuleuven.group6.collectors.DataCollectedListener;
import kuleuven.group6.collectors.IDataCollectorManager;
import kuleuven.group6.testcharacteristics.FailureCount;
import kuleuven.group6.testcharacteristics.TestFailure;

public class FailureCountStatistic extends Statistic<FailureCount> {

	public FailureCountStatistic(IDataCollectorManager dataCollectorManager) {
		dataCollectorManager.subscribe(TestFailure.class, new FailureCountListener());
	}

	protected class FailureCountListener implements DataCollectedListener<TestFailure> {

		@Override
		public void dataCollected(TestFailure data) {
			putTestStatistic(calculateStatistic(data));
		}
		
	}

	@Override
	protected FailureCount composeTestStatistic(Description description) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected FailureCount getDefaultTestStatistic(Description description) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected FailureCount calculateStatistic(TestFailure data) {
		return null;
	}
}
