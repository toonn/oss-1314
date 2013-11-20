package kuleuven.group6.policies;

import java.util.Comparator;

import kuleuven.group6.statistics.IStatisticProvider;
import kuleuven.group6.statistics.Statistic;
import kuleuven.group6.testcharacteristics.teststatistics.FailureTrace;
import org.junit.runner.Description;

public class DistinctFailureFirst extends SortingPolicy {
	protected final Statistic<FailureTrace> statistic;
	
	public DistinctFailureFirst(IStatisticProvider statMan) {
		this.statistic = statMan.getStatistic(FailureTrace.class);
	}

	@Override
	protected Comparator<Description> getComparator() {
		// TODO Auto-generated method stub
		return null;
	}

}
