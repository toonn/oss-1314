package kuleuven.group6.policies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import kuleuven.group6.FlattenedRequest;
import org.junit.runner.Description;
import org.junit.runner.Request;

public class CompositeSortingPolicy extends SortingPolicy {
	protected List<SortingPolicy> childPolicies = new ArrayList<SortingPolicy>(); 
	protected Map<Description, Double> testPriorities;
	
	public void addChildPolicy(SortingPolicy childPolicy) {
		childPolicies.add(childPolicy);
	}
	
	public void removeChildPolicy(SortingPolicy childPolicy) {
		childPolicies.remove(childPolicy);
	}
	
	public int getNbChildPolicies() {
		return childPolicies.size();
	}
	

	@Override
	public Request apply(Request request) {
		testPriorities = new HashMap<Description, Double>();
		for (SortingPolicy policy : childPolicies) {
			updateScoresForChild(policy, request);
		}
		return super.apply(request);
	}
	
	protected void updateScoresForChild(SortingPolicy policy, Request request) {
		Request copiedRequest = FlattenedRequest.flatten(request);
		copiedRequest = policy.apply(copiedRequest);
		Description sortedDescription = copiedRequest.getRunner().getDescription();
		List<Description> sortedDescriptions = getAllDescriptions(sortedDescription);
		for (int i = 0; i < sortedDescriptions.size(); i++) {
			Description description = sortedDescriptions.get(i);
			double score = getScore(i);
			updateScore(description, score);
		}
	}
	
	protected double getScore(int sequenceNumber) {
		return Math.exp(-sequenceNumber);
	}
	
	protected void updateScore(Description description, double additionalValue) {
		if (! testPriorities.containsKey(description))
			testPriorities.put(description, 0.0);
		
		double currentValue = testPriorities.get(description);
		testPriorities.put(description, currentValue + additionalValue);
	}
	
	protected List<Description> getAllDescriptions(Description rootDescription) {
		List<Description> allDescriptions = new ArrayList<Description>();
		Stack<Description> descriptionsToVisit = new Stack<Description>();
		
		descriptionsToVisit.push(rootDescription);
		while (!descriptionsToVisit.isEmpty()) {
			Description currentDescription = descriptionsToVisit.pop();
			allDescriptions.add(currentDescription);
			descriptionsToVisit.addAll(currentDescription.getChildren());
		}
		
		return allDescriptions;
	}

	@Override
	protected Comparator<Description> getComparator() {
		return new Comparator<Description>() {
			@Override
			public int compare(Description o1, Description o2) {
				double v1 = testPriorities.get(o1);
				double v2 = testPriorities.get(o2);
				return Double.compare(v1, v2);
			}
		};
	}

}
