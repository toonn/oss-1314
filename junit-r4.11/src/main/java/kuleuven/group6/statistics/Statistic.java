package kuleuven.group6.statistics;

import java.util.Map;

import kuleuven.group6.testcharacteristics.ITestStatistic;

import org.junit.runner.Description;

public abstract class Statistic<TestStatisticT extends ITestStatistic> {
	
	protected Map<Description, TestStatisticT> statistics;

	public TestStatisticT getTestStatistic(Description description) {
		return statistics.get(description);
	}
	
	protected void putTestStatistic(TestStatisticT testStatistic) {
		statistics.put(testStatistic.getTestDescription(), testStatistic);
	}
	
	protected abstract TestStatisticT composeTestStatistic(Description description);
	
	protected abstract TestStatisticT getDefaultTestStatistic(Description description);
}
