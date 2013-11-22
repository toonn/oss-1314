package kuleuven.group6.statistics;

import kuleuven.group6.testcharacteristics.teststatistics.ITestStatistic;

/**
 * 
 * @author Team 6
 *
 */
public interface IStatisticProvider {

	public <T extends ITestStatistic> Statistic<? extends T> getStatistic(Class<T> testStatisticClass);

}