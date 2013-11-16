package kuleuven.group6.collectors;

import be.kuleuven.cs.ossrewriter.Monitor;
import be.kuleuven.cs.ossrewriter.MonitorEntrypoint;
import be.kuleuven.cs.ossrewriter.OSSRewriter;
import be.kuleuven.cs.ossrewriter.Predicate;
import kuleuven.group6.TestCollectionInfo;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

/**
 * 
 * @author Team 6
 *
 */
public class FailureTraceCollector extends DataCollector<MethodCallTrace> {

	protected MethodCallTrace currentMethodCallTrace = null;
	protected RunListener testListener = null;
	protected MethodCallMonitor methodCallMonitor = null;
	
	
	@Override
	public void startCollecting(final TestCollectionInfo testCollectionInfo) {
		OSSRewriter.setUserExclusionFilter(new Predicate<String>() {
			public boolean apply(String className) {
				// A nested class (denoted after a $ sign) is defined in its parent class file. 
				int indexOfDollar = className.indexOf('$');
				if (indexOfDollar >= 0) {
					className = className.substring(0, indexOfDollar);
				}
				
				String javaClassName = className.replace('/', '.');
				boolean isSourceClass = testCollectionInfo.containsSourceClass(javaClassName);
				return ! isSourceClass;
			}
		});
		OSSRewriter.enable();
		
		methodCallMonitor = new MethodCallMonitor();
		MonitorEntrypoint.register(methodCallMonitor);
		
		testListener = new TestListener();
		testCollectionInfo.addTestRunListener(testListener);
	}
	
	@Override
	public void stopCollecting(final TestCollectionInfo testCollectionInfo) {
		OSSRewriter.disable();
		OSSRewriter.resetUserExclusionFilter();
		MonitorEntrypoint.unregister(methodCallMonitor);
		testCollectionInfo.removeTestRunListener(testListener);
	}
	
	

	protected class TestListener extends RunListener {

		@Override
		public void testStarted(Description description) throws Exception {
			currentMethodCallTrace = new MethodCallTrace();
		}

		@Override
		public void testFinished(Description description) throws Exception {
			onDataCollected(description, currentMethodCallTrace);
		}
		
	}
	
	protected class MethodCallMonitor extends Monitor {

		@Override
		public void enterMethod(String methodName) {
			currentMethodCallTrace.addMethodCall(methodName);
		}
		
	}
	
}
