package kuleuven.group6.collectors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MethodCallTrace {

	protected Collection<String> methodCalls = new ArrayList<String>();
	
	
	public Collection<String> getMethodCalls() {
		return Collections.unmodifiableCollection(methodCalls);
	}
	
	/**
	 * Add a method call to this trace.
	 * 
	 * @param method A method descriptor, following the Java VM specification (§4.3.3).
	 */
	public void addMethodCall(String method) {
		this.methodCalls.add(method);
	}
	
}
