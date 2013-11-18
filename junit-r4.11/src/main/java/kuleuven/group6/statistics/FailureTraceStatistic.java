package kuleuven.group6.statistics;

import kuleuven.group6.collectors.DataCollectedListener;
import kuleuven.group6.collectors.IDataCollectorManager;
import kuleuven.group6.testcharacteristics.FailureTrace;
import kuleuven.group6.testcharacteristics.TestFailure;

import org.junit.runner.Description;

public class FailureTraceStatistic extends
		Statistic<FailureTrace> {
	
	public FailureTraceStatistic(IDataCollectorManager dataCollectorManager) {
		dataCollectorManager.subscribe(TestFailure.class, new FailureTraceListener());
	}

	protected class FailureTraceListener implements DataCollectedListener<TestFailure> {

		@Override
		public void dataCollected(TestFailure data) {
			putTestStatistic(calculateStatistic(data));
		}
		
	}


	@Override
	protected FailureTrace composeTestStatistic(Description description) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected FailureTrace getDefaultTestStatistic(Description description) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected FailureTrace calculateStatistic(TestFailure data) {
		return null;
	}

}
