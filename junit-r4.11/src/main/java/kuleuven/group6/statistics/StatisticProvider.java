package kuleuven.group6.statistics;

import java.util.HashMap;
import java.util.Map;

import kuleuven.group6.RunNotificationSubscriber;
import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.testcharacteristics.teststatistics.FailureCount;
import kuleuven.group6.testcharacteristics.teststatistics.FailureTrace;
import kuleuven.group6.testcharacteristics.teststatistics.ITestStatistic;
import kuleuven.group6.testcharacteristics.teststatistics.LastDependencyChange;
import kuleuven.group6.testcharacteristics.teststatistics.LastFailureDate;

public class StatisticProvider implements IStatisticProvider {
	
	protected Map<Class<? extends ITestStatistic>, Statistic<? extends ITestStatistic>> statistics = new HashMap<>();
	
	protected StatisticProvider() {
		
	}
	
	private void configure(IDataEnroller dataEnroller, RunNotificationSubscriber runNotificationSubscriber) {
		statistics.put(LastFailureDate.class, new LastFailureStatistic(dataEnroller));
		statistics.put(FailureCount.class, new MaxFailureCountStatistic(dataEnroller));
		statistics.put(FailureTrace.class, new FailureTraceStatistic(dataEnroller, runNotificationSubscriber));
		statistics.put(LastDependencyChange.class, new LastDependencyChangeStatistic(dataEnroller));
	}
	
	public static StatisticProvider createConfiguredStatisticProvider(
			IDataEnroller dataEnroller, RunNotificationSubscriber runNotificationSubscriber) {
		StatisticProvider statisticProvider = new StatisticProvider();
		statisticProvider.configure(dataEnroller, runNotificationSubscriber);
		return statisticProvider;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends ITestStatistic> Statistic<T> getStatistic(Class<T> testStatisticClass) {
		if (! statistics.containsKey(testStatisticClass))
			throw new NoSuitableStatisticException(testStatisticClass);
		
		return (Statistic<T>) statistics.get(testStatisticClass);
	}
	
}
