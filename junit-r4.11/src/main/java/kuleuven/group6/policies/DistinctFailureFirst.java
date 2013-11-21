package kuleuven.group6.policies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import kuleuven.group6.statistics.IStatisticProvider;
import kuleuven.group6.statistics.Statistic;
import kuleuven.group6.testcharacteristics.teststatistics.FailureTrace;
import org.junit.runner.Description;
import org.junit.runner.Request;

/**
 * The DistinctFailureFirstPolicy will order descriptions by comparing their
 * FailureTrace's. This makes sure every distinct failure is tested early on
 * during a testrun.
 * 
 * @author Team 6
 * 
 */
public class DistinctFailureFirst extends SortingPolicy {
	protected final Statistic<FailureTrace> statistic;

	private Map<String, Bucket> fBuckets;
	private Map<Description, Set<Bucket>> dBuckets;

	private List<Description> fullyOrderedDescriptions;

	public DistinctFailureFirst(IStatisticProvider statisticProvider) {
		this.statistic = statisticProvider.getStatistic(FailureTrace.class);
	}

	@Override
	public Request apply(Request request) {
		Description rootDescription = request.getRunner().getDescription();
		fBuckets = new HashMap<>();
		dBuckets = new HashMap<>();
		createBuckets(rootDescription);
		final Queue<Bucket> buckets = new PriorityQueue<>(fBuckets.values());
		fullyOrderedDescriptions = new ArrayList<>();
		while (!buckets.isEmpty()) {
			Description nextDescription = buckets.peek().getFirst();
			for (Bucket buck : dBuckets.get(nextDescription)) {
				buck.remove(nextDescription);
				if (buck.getSize() == 0)
					buckets.remove(buck);
			}
			fullyOrderedDescriptions.add(nextDescription);
		}

		return super.apply(request);
	}

	@Override
	protected Comparator<Description> getComparator() {
		return new Comparator<Description>() {

			@Override
			public int compare(Description o1, Description o2) {
				boolean o1HasOrder = fullyOrderedDescriptions.contains(o1);
				boolean o2HasOrder = fullyOrderedDescriptions.contains(o2);

				if (!o1HasOrder && !o2HasOrder)
					return 0;
				else if (!o1HasOrder)
					return 1;
				else if (!o2HasOrder)
					return -1;
				else
					return Integer.compare(
							fullyOrderedDescriptions.indexOf(o1),
							fullyOrderedDescriptions.indexOf(o2));
			}
		};
	}

	private void createBuckets(Description description) {
		// TODO: More efficient to first create buckets for leafs (istest()) and
		// then add parents to the buckets of their children.
		FailureTrace ft = statistic.getTestStatistic(description);
		for (String POF : ft.getPointsOfFailure()) {
			if (fBuckets.containsKey(POF))
				fBuckets.get(POF).add(description);
			else
				fBuckets.put(POF, new Bucket(description));
			if (dBuckets.containsKey(description))
				dBuckets.get(description).add(fBuckets.get(POF));
			else {
				Set<Bucket> bucket = new HashSet<Bucket>();
				bucket.add(fBuckets.get(POF));
				dBuckets.put(description, bucket);
			}
		}
		for (Description child : description.getChildren())
			createBuckets(child);
	}

	private class Bucket implements Comparable<Bucket> {
		/*
		 * A Bucket contains descriptions of tests/suites that have the same
		 * point of failure.
		 */
		private LinkedList<Description> bucketList = new LinkedList<>();
		private int executionCount = 0;

		public Bucket(Description description) {
			add(description);
		}

		public int getCount() {
			return executionCount;
		}

		public int getSize() {
			return bucketList.size();
		}

		public Description getFirst() {
			return bucketList.pollFirst();
		}

		public void add(Description description) {
			if (description.isTest())
				bucketList.offerFirst(description);
			else
				bucketList.offerLast(description);
		}

		public void remove(Description description) {
			bucketList.remove(description);
		}

		/*
		 * Note: this comparator imposes orderings that are inconsistent with
		 * equals.
		 * 
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Bucket o) {
			if (getCount() < o.getCount())
				return -1;
			else if (getCount() > o.getCount())
				return 1;
			else
				return Integer.compare(getSize(), o.getSize());
		}
	}

}
