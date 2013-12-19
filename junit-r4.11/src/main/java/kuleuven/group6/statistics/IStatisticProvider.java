package kuleuven.group6.statistics;

import kuleuven.group6.statistics.teststatistics.ITestStatistic;

/**
 * This interface provides a way to get a Statistic that collects a specific
 * kind of ITestStatistic.
 */
public interface IStatisticProvider {

	public <T extends ITestStatistic> Statistic<? extends T> getStatistic(Class<T> testStatisticClass);

}