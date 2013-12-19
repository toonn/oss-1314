package kuleuven.group6.statistics;

import java.util.HashSet;
import java.util.Set;

import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.statistics.teststatistics.ITestStatistic;
import kuleuven.group6.testrun.RunNotificationSubscriber;

/**
 * (non-Javadoc)
 * @see IStatisticProvider
 */
public class StatisticProvider implements IStatisticProvider {
	
	protected Set<Statistic<? extends ITestStatistic>> statistics = new HashSet<>();
	
	protected StatisticProvider() {

	}
	
	/**
	 * This configures a StatisticProvider with everything it needs. The
	 * responsibility for the creation of Statistic's does not fit well here. A
	 * possible better solution is to have a chain-of-responsibility of
	 * factories.
	 * 
	 * @param runNotificationSubscriber
	 *            Some DataCollector's need to see the results of testruns
	 * @param dataEnroller
	 *            An IDataEnroller that Statistic's can use to subscribe for a
	 *            certain type of ITestData
	 */
	private void configure(IDataEnroller dataEnroller, RunNotificationSubscriber runNotificationSubscriber) {
		statistics.add(new LastFailureStatistic(dataEnroller));
		statistics.add(new MaxFailureCountStatistic(dataEnroller));
		statistics.add(new FailureTraceStatistic(dataEnroller, runNotificationSubscriber));
		statistics.add(new LastDependencyChangeStatistic(dataEnroller));

	}

	/**
	 * @return A configured StatisticProvider because it is not the responsibitlity
	 *         of e.g. Daemon to configure a StatisticProvider.
	 */
	public static StatisticProvider createConfiguredStatisticProvider(
			IDataEnroller dataEnroller,
			RunNotificationSubscriber runNotificationSubscriber) {
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
