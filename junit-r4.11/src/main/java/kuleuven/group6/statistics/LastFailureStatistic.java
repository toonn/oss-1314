package kuleuven.group6.statistics;

import kuleuven.group6.collectors.DataCollectedListener;
import kuleuven.group6.collectors.IDataCollectorManager;
import kuleuven.group6.testcharacteristics.LastFailureDate;
import kuleuven.group6.testcharacteristics.TestFailure;

import org.junit.runner.Description;

public class LastFailureStatistic<TestStatisticT> extends Statistic<LastFailureDate> {
	
	public LastFailureStatistic(IDataCollectorManager dataCollectorManager) {
		dataCollectorManager.subscribe(TestFailure.class, new LastFailureListener());
	}

	protected class LastFailureListener implements DataCollectedListener<TestFailure> {

		@Override
		public void dataCollected(TestFailure data) {
			putTestStatistic(calculateStatistic(data));
		}
		
	}

	@Override
	protected LastFailureDate composeTestStatistic(Description description) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected LastFailureDate getDefaultTestStatistic(Description description) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected LastFailureDate calculateStatistic(TestFailure data) {
		return null;
	}

}
