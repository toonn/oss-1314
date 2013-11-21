package kuleuven.group6.policies;

import java.util.Comparator;

import kuleuven.group6.statistics.IStatisticProvider;
import kuleuven.group6.statistics.Statistic;
import kuleuven.group6.testcharacteristics.teststatistics.FailureCount;
import org.junit.runner.Description;

/**
 * 
 * @author Team 6
 *
 */
public class FrequentFailureFirst extends SortingPolicy {

	protected final Statistic<FailureCount> statistic;

	public FrequentFailureFirst(IStatisticProvider statMan) {
		this.statistic = statMan.getStatistic(FailureCount.class);
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
				int failureCount1 = statistic.getTestStatistic(o1)
						.getFailureCount();
				int failureCount2 = statistic.getTestStatistic(o2)
						.getFailureCount();
				int orderingByLeastFailures = Integer.compare(failureCount1, failureCount2);
				return -1 * orderingByLeastFailures;
			}
		};
	}
}
