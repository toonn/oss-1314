package kuleuven.group6.cli;

import kuleuven.group6.policies.CompositeSortingPolicy;
import kuleuven.group6.policies.SortingPolicy;

public class CompositeSortingPolicyBuilder {

	protected CompositeSortingPolicy compositeSortingPolicy;
	
	public CompositeSortingPolicyBuilder() {
		compositeSortingPolicy = new CompositeSortingPolicy();
	}
	
	public void addChildPolicy(SortingPolicy childPolicy) {
		compositeSortingPolicy.addChildPolicy(childPolicy);
	}
	
	public CompositeSortingPolicy getCompositeSortingPolicy() {
		return compositeSortingPolicy;
	}
	
}
