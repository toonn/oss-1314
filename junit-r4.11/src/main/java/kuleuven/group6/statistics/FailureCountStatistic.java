package kuleuven.group6.statistics;

import org.junit.runner.Description;
import kuleuven.group6.collectors.DataCollectedListener;
import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.testcharacteristics.testdatas.TestFailure;
import kuleuven.group6.testcharacteristics.teststatistics.FailureCount;

public abstract class FailureCountStatistic extends Statistic<FailureCount> {

	public FailureCountStatistic(IDataEnroller dataEnroller) {
		dataEnroller.subscribe(TestFailure.class, new FailureCountListener());
	}

	protected class FailureCountListener implements DataCollectedListener<TestFailure> {

		@Override
		public void dataCollected(TestFailure data) {
			putTestStatistic(calculateStatistic(data));
		}
		
	}

	@Override
	protected FailureCount getDefaultTestStatistic(Description description) {
		putTestStatistic(new FailureCount(description, 0));
		return getTestStatistic(description);
	}
	
	protected FailureCount calculateStatistic(TestFailure data) {
		return getTestStatistic(data.getTestDescription()).increment();
	}
}
