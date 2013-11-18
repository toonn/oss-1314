package kuleuven.group6.policies;

import java.util.Comparator;

import kuleuven.group6.statistics.Statistic;
import kuleuven.group6.statistics.StatisticManager;
import kuleuven.group6.testcharacteristics.FailureCount;


import org.junit.runner.Description;

public class FrequentFailureFirst extends SortingPolicy {
	
	protected final Statistic<FailureCount> statistic;

	public FrequentFailureFirst(StatisticManager statMan) {
		this.statistic = statMan.getStatistic(FailureCount.class);
	}
	
	@Override
	protected Comparator<Description> getComparator() {
		// TODO Auto-generated method stub
		return null;
	}

}
