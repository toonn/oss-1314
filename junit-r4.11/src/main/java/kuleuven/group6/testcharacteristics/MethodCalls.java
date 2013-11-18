package kuleuven.group6.testcharacteristics;

import java.util.Collection;
import java.util.Collections;

import org.junit.runner.Description;

public class MethodCalls implements ITestData {

	protected final Description testDescription;
	protected final Collection<String> methodCalls;
	
	public MethodCalls(Description testDescription, Collection<String> methodCalls) {
		this.testDescription = testDescription;
		this.methodCalls = Collections.unmodifiableCollection(methodCalls);
	}

	public Description getTestDescription() {
		return testDescription;
	}

	public Collection<String> getMethodCalls() {
		return methodCalls;
	}
	
}