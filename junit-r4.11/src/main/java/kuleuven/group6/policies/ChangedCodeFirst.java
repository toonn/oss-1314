package kuleuven.group6.policies;

import java.util.Comparator;

import kuleuven.group6.statistics.Statistic;
import kuleuven.group6.statistics.StatisticManager;
import kuleuven.group6.testcharacteristics.LastDependencyChange;

import org.junit.runner.Description;

public class ChangedCodeFirst extends SortingPolicy {
	
	protected final Statistic<LastDependencyChange> statistic;
	
	public ChangedCodeFirst(StatisticManager statMan) {
		this.statistic = statMan.getStatistic(LastDependencyChange.class);
	}

	@Override
	protected Comparator<Description> getComparator() {
		// TODO Auto-generated method stub
		return null;
	}

}
