package kuleuven.group6.policies;

import java.util.Comparator;

import kuleuven.group6.statistics.IStatisticProvider;
import kuleuven.group6.statistics.Statistic;
import kuleuven.group6.testcharacteristics.LastDependencyChange;

import org.junit.runner.Description;

public class ChangedCodeFirst extends SortingPolicy {
	
	protected final Statistic<LastDependencyChange> statistic;
	
	public ChangedCodeFirst(IStatisticProvider statMan) {
		this.statistic = statMan.getStatistic(LastDependencyChange.class);
	}

	@Override
	protected Comparator<Description> getComparator() {
		return new Comparator<Description>() {
			/*
			 * Note: this comparator imposes orderings that are inconsistent
			 * with equals.
			 */
			@Override
			public int compare(Description o1, Description o2) {
				LastDependencyChange change1 = statistic.getTestStatistic(o1);
				LastDependencyChange change2 = statistic.getTestStatistic(o2);
				
				return change1.getDate().compareTo(change2.getDate());
			}
			
		};
	}

}
