package kuleuven.group6.policies;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import kuleuven.group6.testrun.FlattenedRequest;

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

	
	public void addChildPolicy(SortingPolicy childPolicy)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("The child could not be added.");
	}

	public void removeChildPolicy(SortingPolicy childPolicy)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException("The child could not be removed");
	}
	
	public SortingPolicy getChildAt(int index) {
		return null;
	}
	

	/**
	 * Get the ordered subset of the descriptions in the given request that have
	 * an explicit order.
	 * 
	 * @param request
	 * @return
	 */
	public List<Description> getOrderedSubset(Request request) {
		Request flattenedRequest = FlattenedRequest.flatten(request);
		flattenedRequest = apply(flattenedRequest);
		Description sortedDescription = flattenedRequest.getRunner()
				.getDescription();
		List<Description> sortedDescriptions = getAllDescriptions(sortedDescription);
		return sortedDescriptions;
	}

	abstract protected boolean hasInfoFor(Description description);

	protected List<Description> getAllDescriptions(Description rootDescription) {
		List<Description> allDescriptions = new LinkedList<Description>();
		LinkedList<Description> descriptionsToVisit = new LinkedList<Description>();

		descriptionsToVisit.push(rootDescription);
		while (!descriptionsToVisit.isEmpty()) {
			Description currentDescription = descriptionsToVisit.pop();
			allDescriptions.add(currentDescription);
			descriptionsToVisit.addAll(currentDescription.getChildren());
		}

		return allDescriptions;
	}

}
