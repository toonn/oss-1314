package kuleuven.group6.policies;

import java.util.Comparator;

import kuleuven.group6.statistics.IStatisticProvider;
import kuleuven.group6.statistics.Statistic;
import kuleuven.group6.statistics.teststatistics.LastDependencyChange;

import org.junit.runner.Description;

/**
 * This policy will order tests on the date of the last change to the code they
 * test.
 */

public class ChangedCodeFirst extends SortingPolicy {
	protected final Statistic<? extends LastDependencyChange> statistic;
	
	public ChangedCodeFirst(IStatisticProvider statMan) {
		this.statistic = statMan.getStatistic(LastDependencyChange.class);
	}
	
	@Override
	protected Comparator<Description> getComparator() {
		return new Comparator<Description>() {
			/*
			 * Note: this comparator imposes orderings that are inconsistent
			 * with equals. (Different descriptions can have the same
			 * LastDepencyChange, this will cause the comparator to return 0.)
			 */
			@Override
			public int compare(Description o1, Description o2) {
				LastDependencyChange change1 = statistic.getTestStatistic(o1);
				LastDependencyChange change2 = statistic.getTestStatistic(o2);

				if (change1 == null)
					return 1;
				if (change2 == null)
					return -1;

				return change1.getDate().compareTo(change2.getDate());
			}

		};
	}

	@Override
	protected boolean hasInfoFor(Description description) {
		return (statistic.getTestStatistic(description) != null);
	}
	
}
