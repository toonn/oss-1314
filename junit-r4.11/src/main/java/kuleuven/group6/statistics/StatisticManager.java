package kuleuven.group6.statistics;

import kuleuven.group6.testcharacteristics.ITestStatistic;

public class StatisticManager implements IStatisticManager {
	//TODO: field: managedStatistics: Statistic<ITestStatistic>

	@Override
	public <T extends ITestStatistic> Statistic<T> getStatistic(Class<T> statisticType) {
		return null;
	}
	
}
