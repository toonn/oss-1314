package kuleuven.group6.statistics;

import java.util.Date;

import kuleuven.group6.collectors.DataCollectedListener;
import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.testcharacteristics.testdatas.TestFailure;
import kuleuven.group6.testcharacteristics.teststatistics.ITestStatistic;
import kuleuven.group6.testcharacteristics.teststatistics.LastFailureDate;
import org.junit.runner.Description;

/**
 * 
 * @author Team 6
 *
 */
public class LastFailureStatistic extends Statistic<LastFailureDate> {

	public LastFailureStatistic(IDataEnroller dataEnroller) {
		dataEnroller.subscribe(TestFailure.class, new LastFailureListener());
	}
	
	@Override
	public <T extends ITestStatistic> boolean canSummarize(Class<T> testStatisticClass) {
		return testStatisticClass.isAssignableFrom(LastFailureDate.class);
	}

	protected class LastFailureListener implements
			DataCollectedListener<TestFailure> {

		@Override
		public void dataCollected(TestFailure data) {
			calculateStatistic(data);
		}

	}

	@Override
	protected LastFailureDate composeTestStatistic(Description description) {
		boolean hasLastFailureDate = false;
		Date lastFailureDate = null;

		for (Description childDescription : description.getChildren()) {
			LastFailureDate childData = getTestStatistic(childDescription);
			if (childData == null)
				continue;

			Date childLastFailureDate = childData.getLastFailureDate();
			if (!hasLastFailureDate
					|| childLastFailureDate.after(lastFailureDate)) {
				lastFailureDate = childLastFailureDate;
				hasLastFailureDate = true;
			}
		}

		if (!hasLastFailureDate)
			return null;

		return new LastFailureDate(description, lastFailureDate);
	}

	@Override
	protected LastFailureDate getDefaultTestStatistic(Description description) {
		return null;
	}

	protected void calculateStatistic(TestFailure data) {
		// TODO : new Date in ITestData (TestFailure)
		putTestStatistic(new LastFailureDate(data.getTestDescription(),
				new Date()));
	}

}
