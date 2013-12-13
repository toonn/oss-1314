package kuleuven.group6.policies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Request;

public class CompositeSortingPolicy extends SortingPolicy {
	protected List<SortingPolicy> childPolicies = new ArrayList<SortingPolicy>();
	protected List<Description> orderedDescriptions;

	@Override
	public void addChildPolicy(SortingPolicy childPolicy)
			throws UnsupportedOperationException {
		childPolicies.add(childPolicy);
	}

	@Override
	public void removeChildPolicy(SortingPolicy childPolicy)
			throws UnsupportedOperationException {
		childPolicies.remove(childPolicy);
	}

	@Override
	public int getNbChildPolicies() throws UnsupportedOperationException {
		return childPolicies.size();
	}

	protected void mergeChildOrders(Request request) {
		LinkedList<List<Description>> childLists = new LinkedList<List<Description>>();
		for (SortingPolicy childPolicy : childPolicies) {
			List<Description> childList = childPolicy.getOrderedSubset(request);
			if (!childList.isEmpty())
				childLists.add(childList);
		}

		while (!childLists.isEmpty()) {
			List<Description> childList = childLists.pop();

			Description description = childList.remove(0);
			orderedDescriptions.add(description);

			for (List<Description> otherList : childLists) {
				otherList.remove(description);
			}

			if (!childList.isEmpty())
				childLists.offer(childList);
		}
	}

	@Override
	public Request apply(Request request) {
		orderedDescriptions = new ArrayList<Description>();
		mergeChildOrders(request);
		return super.apply(request);
	}

	@Override
	protected Comparator<Description> getComparator() {
		return new Comparator<Description>() {
			@Override
			public int compare(Description o1, Description o2) {
				int i1 = orderedDescriptions.indexOf(o1);
				int i2 = orderedDescriptions.indexOf(o2);

				if (i1 == -1)
					return (i2 == -1) ? 0 : 1;
				if (i2 == -1)
					return -1;
				return Integer.compare(i1, i2);
			}
		};
	}

	@Override
	protected boolean hasOrderFor(Description description) {
		return orderedDescriptions.contains(description);
	}

}
