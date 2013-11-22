package kuleuven.group6.statistics;

import org.junit.runner.Description;
import kuleuven.group6.collectors.DataCollectedListener;
import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.testcharacteristics.testdatas.TestFailure;
import kuleuven.group6.testcharacteristics.teststatistics.FailureCount;
import kuleuven.group6.testcharacteristics.teststatistics.ITestStatistic;

/**
 * This statistic collects the number of times a certain test has failed.
 */
public abstract class FailureCountStatistic extends Statistic<FailureCount> {

	public FailureCountStatistic(IDataEnroller dataEnroller) {
		dataEnroller.subscribe(TestFailure.class, new FailureCountListener());
	}

	protected class FailureCountListener implements
			DataCollectedListener<TestFailure> {

		@Override
		public void dataCollected(TestFailure data) {
			calculateStatistic(data);
		}

	}

	/*
	 * A Description without a failureCount has a failureCount equal to 0, but
	 * this description would not be known to this Statistic because it only
	 * receives messages when a test fails.
	 */
	@Override
	protected FailureCount getDefaultTestStatistic(Description description) {
		putTestStatistic(new FailureCount(description, 0));
		return getTestStatistic(description);
	}

	protected void calculateStatistic(TestFailure data) {
		putTestStatistic(getTestStatistic(data.getTestDescription())
				.increment());
	}
	
	@Override
	public <T extends ITestStatistic> boolean canSummarize(Class<T> testStatisticClass) {
		return testStatisticClass.isAssignableFrom(FailureCount.class);
	}
}
