package kuleuven.group6.policies;

import java.util.Comparator;

import kuleuven.group6.statistics.IStatisticProvider;
import kuleuven.group6.statistics.Statistic;
import kuleuven.group6.testcharacteristics.teststatistics.LastFailureDate;
import org.junit.runner.Description;

/**
 * 
 * @author Team 6
 *
 */
public class LastFailureFirst extends SortingPolicy {
	
	protected final Statistic<LastFailureDate> statistic;

	public LastFailureFirst(IStatisticProvider statMan) {
		this.statistic = statMan.getStatistic(LastFailureDate.class);
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
				LastFailureDate date1 = statistic.getTestStatistic(o1);
				LastFailureDate date2 = statistic.getTestStatistic(o2);
				
				if (date1 == null)
					return 1;
				if (date2 == null)
					return -1;
				
				return date1.getLastFailureDate().compareTo(date2.getLastFailureDate());
			}
			
		};
	}

}
