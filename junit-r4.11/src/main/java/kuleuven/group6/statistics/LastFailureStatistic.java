package kuleuven.group6.statistics;

import java.util.Date;

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
		boolean hasLastFailureDate = false;
		Date lastFailureDate = null;
		
		for (Description childDescription : description.getChildren()) {
			LastFailureDate childData = getTestStatistic(childDescription);
			if (childData == null)
				continue;
			
			Date childLastFailureDate = childData.getLastFailureDate();
			if (!hasLastFailureDate || childLastFailureDate.after(lastFailureDate)) {
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
	
	protected LastFailureDate calculateStatistic(TestFailure data) {
		return new LastFailureDate(data.getTestDescription(), new Date());
	}

}
