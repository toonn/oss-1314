package kuleuven.group6.policies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
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
 * Failures that originate in the org.junit.Assert class are actually distinct,
 * since these failures are not caused by the method in the Assert class, but 
 * due by the calling code. As such, these failures are handled as distinct by 
 * this policy.
 * 
 * @author Team 6
 * 
 */
public class DistinctFailureFirst extends SortingPolicy {
	protected final Statistic<? extends FailureTrace> statistic;

	private Map<String, Bucket> fBuckets;
	private Map<Description, Set<Bucket>> dBuckets;
	private int currentAssertFailureCount = 0;
	
	private List<Description> fullyOrderedDescriptions;

	public DistinctFailureFirst(IStatisticProvider statisticProvider) {
		this.statistic = statisticProvider.getStatistic(FailureTrace.class);
	}

	@Override
	public Request apply(Request request) {
		Description rootDescription = request.getRunner().getDescription();
		createNewBuckets(rootDescription);
		final List<Bucket> buckets = new ArrayList<>(fBuckets.values());
		fullyOrderedDescriptions = new ArrayList<>();
		while (!buckets.isEmpty()) {
			Bucket nextBucket = buckets.get(0);
			for (Bucket b : buckets)
				if (b.compareTo(nextBucket) < 0)
					nextBucket = b;
			Description nextDescription = nextBucket.getFirst();
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
				int index1 = fullyOrderedDescriptions.indexOf(o1);
				int index2 = fullyOrderedDescriptions.indexOf(o2);
				boolean o1HasOrder = index1 != -1;
				boolean o2HasOrder = index2 != -1;

				if (!o1HasOrder && !o2HasOrder)
					return 0;
				else if (!o1HasOrder)
					return 1;
				else if (!o2HasOrder)
					return -1;
				else
					return Integer.compare(index1, index2);
			}
		};
	}

	private void createNewBuckets(Description rootDescription) {
		fBuckets = new HashMap<>();
		dBuckets = new HashMap<>();
		currentAssertFailureCount = 0;
		createBuckets(rootDescription);
	}
	
	private void createBuckets(Description description) {
		// TODO: More efficient to first create buckets for leafs (istest()) and
		// then add parents to the buckets of their children.
		FailureTrace ft = statistic.getTestStatistic(description);
		if (ft == null)
			return;
		for (String POF : ft.getPointsOfFailure()) {
			if (POF.startsWith(Assert.class.getName() + '.')) {
				POF += "#" + currentAssertFailureCount;
				currentAssertFailureCount++;
			}
			
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
			executionCount++;
			return bucketList.pollFirst();
		}

		public void add(Description description) {
			if (description.isTest())
				bucketList.offerFirst(description);
			else
				bucketList.offerLast(description);
		}

		public void remove(Description description) {
			executionCount++;
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
				return Integer.compare(o.getSize(), getSize());
		}
	}

}
