package kuleuven.group6.statistics;

import kuleuven.group6.testcharacteristics.ITestStatistic;

public interface IStatisticProvider {

	public <T extends ITestStatistic> Statistic<T> getStatistic(Class<T> testStatisticClass);

}