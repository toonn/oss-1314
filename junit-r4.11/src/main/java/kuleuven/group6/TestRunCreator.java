package kuleuven.group6;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.runner.Request;
import org.junit.runner.notification.RunNotifier;

import kuleuven.group6.policies.IPolicy;

public class TestRunCreator {
	protected String rootSuiteClassName;
	protected URL[] paths;
	protected boolean flattened;
	protected RunNotifier runNotifier;
	
	public TestRunCreator(String rootSuiteClassName, URL[] paths, RunNotifier runNotifier) {
		this.rootSuiteClassName = rootSuiteClassName;
		this.paths = paths;
		this.runNotifier = runNotifier;
		this.flattened = true;
	}
	
	public TestRunCreator(String rootSuiteClassName, URL[] paths, RunNotifier runNotifier, boolean flattened) {
		this.rootSuiteClassName = rootSuiteClassName;
		this.paths = paths;
		this.runNotifier = runNotifier;
		this.flattened = flattened;
	}
	
	public TestRun create(IPolicy policy) throws ClassNotFoundException, IOException {
		Request request;
		URLClassLoader classLoader = URLClassLoader.newInstance(paths);
		try {
			Class<?> rootSuiteClass = Class.forName(rootSuiteClassName, true, classLoader);
			request = createNewRequest(rootSuiteClass, policy);

		} finally {
			classLoader.close();
		}
		
		return new TestRun(request, runNotifier);
	}
	
	
	protected Request createNewRequest(Class<?> rootSuiteClass, IPolicy policy) {
		Request request = Request.aClass(rootSuiteClass);
		Request flattenedRequest = FlattenedRequest.flatten(request);
		// the active Policy is applied on the request
		return policy.apply(flattenedRequest);
	}
}
