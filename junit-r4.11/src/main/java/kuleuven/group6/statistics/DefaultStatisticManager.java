package kuleuven.group6.statistics;

import java.util.HashMap;
import java.util.Map;

import kuleuven.group6.testcharacteristics.ITestStatistic;

public class DefaultStatisticManager implements IStatisticManager {
	
	protected Map<Class<? extends ITestStatistic>, Statistic<? extends ITestStatistic>> statistics = new HashMap<>();
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends ITestStatistic> Statistic<T> getStatistic(Class<T> testStatisticClass) {
		if (! statistics.containsKey(testStatisticClass))
			throw new NoSuitableStatisticException(testStatisticClass);
		
		return (Statistic<T>) statistics.get(testStatisticClass);
	}
	
}
