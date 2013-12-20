package kuleuven.group6.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Request;

import kuleuven.group6.policies.CompositeSortingPolicy;
import kuleuven.group6.policies.SortingPolicy;
import kuleuven.group6.tests.testsubject.tests.NumberedTests;

public class CompositeSortingPolicyTest {
	Request request;
	Request smartSorted;
	Request cluelessSorted;
	CompositeSortingPolicy csp;
	CompositeSortingPolicy ccsp;
	ForwardSortingPolicy smartForward;
	ReverseSortingPolicy reverse;
	ForwardSortingPolicy cluelessForward;
	

	@Before
	public void before(){
		// Get a request (NumberedTests contains tests t1,t2,...,t5)
		request = Request.aClass(NumberedTests.class);
		// Initialize policies
		csp = new CompositeSortingPolicy();
		ccsp = new CompositeSortingPolicy();
		smartForward = new ForwardSortingPolicy(false);
		reverse = new ReverseSortingPolicy(false);
		cluelessForward = new ForwardSortingPolicy(true);
		// Compose
		csp.addChildPolicy(reverse);
		csp.addChildPolicy(smartForward);
		ccsp.addChildPolicy(reverse);
		//ccsp.addChildPolicy(cluelessForward);
		// Apply
		smartSorted = csp.apply(request);
		cluelessSorted = ccsp.apply(request);
		
	}
	
	@Test
	public void forwardReverseTest() {
		List<Description> childDescriptions = smartSorted.getRunner().getDescription().getChildren();
		String firstName = childDescriptions.get(0).getMethodName();
		String secondName = childDescriptions.get(1).getMethodName();
		if(firstName.equals("t1")){
			assertTrue(secondName.equals("t5"));
		} else {
			assertTrue(firstName.equals("t5"));
			assertTrue(secondName.equals("t1"));
		}
	}
	
	@Test
	public void cluelessForwardReverseTest() {
		List<Description> childDescriptions = cluelessSorted.getRunner().getDescription().getChildren();
		String firstName = childDescriptions.get(0).getMethodName();
		String secondName = childDescriptions.get(1).getMethodName();
		for(int i=0;i<childDescriptions.size();i++){
			System.out.println(ccsp.apply(request).getRunner().getDescription().getChildren().get(i).getMethodName());
			System.out.println(ccsp.getOrderedSubset(request).get(i).getMethodName());
			//System.out.println(ccsp.getChildAt(0).getOrderedSubset(request).get(i).getMethodName());
			//System.out.println(childDescriptions.get(i).getMethodName());
		}
		assertTrue(firstName.equals("t5"));
		assertTrue(secondName.equals("t4"));
	}
	
	private class ForwardSortingPolicy extends SortingPolicy {
		private boolean clueless;
		ForwardSortingPolicy(boolean clueless) {
			this.clueless = clueless;
		}
		@Override
		protected Comparator<Description> getComparator() {
			return new Comparator<Description>() {
				@Override
				public int compare(Description o1, Description o2) {
					return o1.getMethodName().compareTo(o2.getMethodName());
				}
			};
		}
		@Override
		public List<Description> getOrderedSubset(Request request) {
			return clueless?(new ArrayList<Description>()):super.getOrderedSubset(request);
		}
		@Override
		protected boolean hasInfoFor(Description description) {
			return !clueless;
		}
	}
	
	private class ReverseSortingPolicy extends SortingPolicy {
		private boolean clueless;
		ReverseSortingPolicy(boolean clueless) {
			this.clueless = clueless;
		}
		@Override
		protected Comparator<Description> getComparator() {
			return new Comparator<Description>(){
				@Override
				public int compare(Description o1, Description o2) {
					return o2.getMethodName().compareTo(o1.getMethodName());
				}
			};
		}
		@Override
		public List<Description> getOrderedSubset(Request request) {
			return clueless?(new ArrayList<Description>()):super.getOrderedSubset(request);
		}
		@Override
		protected boolean hasInfoFor(Description description) {
			return !clueless;
		}
	}

}
