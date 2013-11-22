package kuleuven.group6.testcharacteristics.testdatas;

import java.util.Collections;
import java.util.List;

import org.junit.runner.Description;

/**
 * 
 * @author Team 6
 *
 */
public class MethodCalls implements ITestData {

	protected final Description testDescription;
	protected final List<String> methodNames;
	
	public MethodCalls(Description testDescription, List<String> methodCalls) {
		this.testDescription = testDescription;
		this.methodNames = Collections.unmodifiableList(methodCalls);
	}

	@Override
	public Description getTestDescription() {
		return testDescription;
	}

	public List<String> getMethodNames() {
		return methodNames;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (! (obj instanceof MethodCalls))
			 return false;
		MethodCalls other = (MethodCalls)obj;
		if (! getTestDescription().equals(other.getTestDescription())) 
			return false;
		if (! methodNames.equals(other.methodNames))
			return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + testDescription.hashCode();
		hash = hash * 31 + methodNames.hashCode();
		return hash;
	}
	
}