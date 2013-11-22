package kuleuven.group6.policies;

import java.util.Comparator;

import org.junit.runner.Description;
import org.junit.runner.Request;

/**
 * A SortingPolicy determines an order for tests and suites. Examples of
 * possible criteria for ordering: most frequent failures, shortest execution
 * time.
 */
public abstract class SortingPolicy implements IPolicy {

	@Override
	public Request apply(Request request) {
		return request.sortWith(getComparator());
	}

	abstract protected Comparator<Description> getComparator();

}
