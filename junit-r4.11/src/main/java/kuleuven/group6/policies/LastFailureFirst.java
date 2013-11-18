package kuleuven.group6.policies;

import java.util.Comparator;

import kuleuven.group6.statistics.IStatisticManager;
import kuleuven.group6.statistics.Statistic;
import kuleuven.group6.testcharacteristics.LastFailureDate;

import org.junit.runner.Description;

public class LastFailureFirst extends SortingPolicy {
	
	protected final Statistic<LastFailureDate> statistic;

	public LastFailureFirst(IStatisticManager statMan) {
		this.statistic = statMan.getStatistic(LastFailureDate.class);
	}
	
	@Override
	protected Comparator<Description> getComparator() {
		// TODO Auto-generated method stub
		return null;
	}

}
