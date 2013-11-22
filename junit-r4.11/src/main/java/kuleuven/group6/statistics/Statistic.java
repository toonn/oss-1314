package kuleuven.group6.statistics;

import java.util.HashMap;
import java.util.Map;

import kuleuven.group6.testcharacteristics.teststatistics.ITestStatistic;
import org.junit.runner.Description;

/**
 * 
 * @author Team 6
 *
 * @param <TestStatisticT>
 */
public abstract class Statistic<TestStatisticT extends ITestStatistic> {
	
	protected Map<Description, TestStatisticT> statistics = new HashMap<>();

	public TestStatisticT getTestStatistic(Description description) {
		TestStatisticT stat;
		if (statistics.containsKey(description))
			stat = statistics.get(description);
		else if (!description.getChildren().isEmpty())
			stat = composeTestStatistic(description);
		else
			stat = getDefaultTestStatistic(description);
		
		return stat;
	}
	
	protected void putTestStatistic(TestStatisticT testStatistic) {
		statistics.put(testStatistic.getTestDescription(), testStatistic);
	}
	
	protected abstract TestStatisticT composeTestStatistic(Description description);
	
	protected abstract TestStatisticT getDefaultTestStatistic(Description description);
	
	public abstract <T extends ITestStatistic> boolean canSummarize(Class<T> testStatisticClass);
}
