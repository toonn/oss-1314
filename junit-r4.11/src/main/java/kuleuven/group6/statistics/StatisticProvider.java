package kuleuven.group6.statistics;

import java.util.HashSet;
import java.util.Set;

import kuleuven.group6.RunNotificationSubscriber;
import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.testcharacteristics.teststatistics.ITestStatistic;

/**
 * 
 * @author Team 6
 *
 */
public class StatisticProvider implements IStatisticProvider {
	
	protected Set<Statistic<? extends ITestStatistic>> statistics = new HashSet<>();
	
	protected StatisticProvider() {
		
	}
	
	private void configure(IDataEnroller dataEnroller, RunNotificationSubscriber runNotificationSubscriber) {
		statistics.add(new LastFailureStatistic(dataEnroller));
		statistics.add(new MaxFailureCountStatistic(dataEnroller));
		statistics.add(new FailureTraceStatistic(dataEnroller, runNotificationSubscriber));
		statistics.add(new LastDependencyChangeStatistic(dataEnroller));
	}
	
	public static StatisticProvider createConfiguredStatisticProvider(
			IDataEnroller dataEnroller, RunNotificationSubscriber runNotificationSubscriber) {
		StatisticProvider statisticProvider = new StatisticProvider();
		statisticProvider.configure(dataEnroller, runNotificationSubscriber);
		return statisticProvider;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends ITestStatistic> Statistic<? extends T> getStatistic(Class<T> testStatisticClass) {
		for (Statistic<?> statistic : statistics) {
			if (statistic.canSummarize(testStatisticClass)) {
				return (Statistic<? extends T>)statistic;
			}
		}

		throw new NoSuitableStatisticException(testStatisticClass);
	}
	
}
