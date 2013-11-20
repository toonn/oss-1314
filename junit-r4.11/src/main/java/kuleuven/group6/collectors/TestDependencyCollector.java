package kuleuven.group6.collectors;

import java.util.ArrayList;
import java.util.Collection;

import be.kuleuven.cs.ossrewriter.Monitor;
import be.kuleuven.cs.ossrewriter.MonitorEntrypoint;
import be.kuleuven.cs.ossrewriter.OSSRewriter;
import be.kuleuven.cs.ossrewriter.Predicate;
import kuleuven.group6.RunNotificationSubscriber;
import kuleuven.group6.testcharacteristics.MethodCalls;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

/**
 * A data collector that collects all methods called by each test.
 * 
 * This collector assumes that tests are run sequentially. If they are run in parallel,
 * the data collected by this collector is incorrect. 
 * 
 * @author Team 6
 *
 */
public class TestDependencyCollector extends DataCollector<MethodCalls> {

	protected RunNotificationSubscriber runNotificationSubscriber;
	protected RunListener testListener;
	protected Collection<String> currentMethodCalls = null;
	protected MethodCallMonitor methodCallMonitor = null;
	
	/**
	 * Construct a new data collector that will collect all methods called by each test.
	 * 
	 * @param sourceClassNames The fully qualified names of all the classes of which method calls are to be included.
	 * @param runNotificationSubscriber The notifier on which this collector can subscribe itself. 
	 */
	public TestDependencyCollector(Collection<String> sourceClassNames, RunNotificationSubscriber runNotificationSubscriber) {
		setupOssRewriter(sourceClassNames);
		this.runNotificationSubscriber = runNotificationSubscriber;
		this.testListener = new TestListener();
	}
	
	private void setupOssRewriter(final Collection<String> sourceClassNames) {
		OSSRewriter.setUserExclusionFilter(new Predicate<String>() {
			@Override
			public boolean apply(String className) {
				// A nested class (denoted after a $ sign) is defined in its parent class file. 
				int indexOfDollar = className.indexOf('$');
				if (indexOfDollar >= 0) {
					className = className.substring(0, indexOfDollar);
				}
				
				String javaClassName = className.replace('/', '.');
				boolean isSourceClass = sourceClassNames.contains(javaClassName);
				return ! isSourceClass;
			}
		});
		OSSRewriter.enable();
	}
	
	
	@Override
	public void startCollecting() {
		methodCallMonitor = new MethodCallMonitor();
		MonitorEntrypoint.register(methodCallMonitor);
		runNotificationSubscriber.addListener(testListener);
	}
	
	@Override
	public void stopCollecting() {
		OSSRewriter.disable();
		OSSRewriter.resetUserExclusionFilter();
		MonitorEntrypoint.unregister(methodCallMonitor);
		runNotificationSubscriber.removeListener(testListener);
		
		methodCallMonitor = null;
		testListener = null;
	}
	

	protected class TestListener extends RunListener {

		@Override
		public void testStarted(Description description) {
			currentMethodCalls = new ArrayList<String>();
		}

		@Override
		public void testFinished(Description description) {
			onDataCollected(new MethodCalls(description, currentMethodCalls));
		}
		
	}
	
	protected class MethodCallMonitor extends Monitor {

		@Override
		public void enterMethod(String methodName) {
			currentMethodCalls.add(methodName);
		}
		
	}
	
}
