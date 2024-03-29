package kuleuven.group6.statistics;

import java.util.ArrayList;
import java.util.Collection;

import kuleuven.group6.collectors.IDataCollectedListener;
import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.collectors.testdatas.TestFailure;
import kuleuven.group6.statistics.teststatistics.FailureTrace;
import kuleuven.group6.statistics.teststatistics.ITestStatistic;
import kuleuven.group6.testrun.RunNotificationSubscriber;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

/**
 * A FailureTraceStatistic stores FailureTrace instances for each test it has
 * data for. When a new testrun starts, all stored FailureTrace instances are
 * removed, since they don't have a use anymore. This is in contrast to other
 * statistics which summarize data over time.
 */
public class FailureTraceStatistic extends Statistic<FailureTrace> {

	public FailureTraceStatistic(IDataEnroller dataEnroller,
			RunNotificationSubscriber runNotificationSubscriber) {
		dataEnroller.subscribe(TestFailure.class, new FailureTraceListener());
		runNotificationSubscriber.addListener(new TestRunStartedListener());
	}

	protected class FailureTraceListener implements
			IDataCollectedListener<TestFailure> {

		@Override
		public void dataCollected(TestFailure data) {
			calculateStatistic(data);
		}

	}

	@Override
	protected FailureTrace composeTestStatistic(Description description) {
		// TODO calculate directly from all leafs instead of own children
		Collection<String> pointsOfFailure = new ArrayList<>();
		for (Description childDescription : description.getChildren()) {
			FailureTrace childTrace = getTestStatistic(childDescription);
			if (childTrace != null) {
				pointsOfFailure.addAll(childTrace.getPointsOfFailure());
			}
		}
		if (pointsOfFailure.size() > 0)
			return new FailureTrace(description, pointsOfFailure);
		else
			return null;
	}

	@Override
	protected FailureTrace getDefaultTestStatistic(Description description) {
		return null;
	}

	protected void calculateStatistic(TestFailure data) {
		StackTraceElement trace = data.getFailure().getException()
				.getStackTrace()[0];
		String pointOfFailure = trace.getClassName() + '.'
				+ trace.getMethodName();
		FailureTrace failureTrace = new FailureTrace(data.getTestDescription(),
				pointOfFailure);
		putTestStatistic(failureTrace);
	}
	
	@Override
	public <T extends ITestStatistic> boolean canSummarize(Class<T> testStatisticClass) {
		return testStatisticClass.isAssignableFrom(FailureTrace.class);
	}

	private void clearStatistics() {
		statistics.clear();
	}

	protected class TestRunStartedListener extends RunListener {

		@Override
		public void testRunStarted(Description description) throws Exception {
			clearStatistics();
		}

	}

}
