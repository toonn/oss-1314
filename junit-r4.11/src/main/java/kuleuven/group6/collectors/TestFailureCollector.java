package kuleuven.group6.collectors;

import kuleuven.group6.testcharacteristics.TestFailure;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

/**
 * 
 * @author Team 6
 *
 */
public class TestFailureCollector extends DataCollector<TestFailure> {

	protected RunNotifier runNotifier;
	protected RunListener runListener = new FailureListener();
	
	public TestFailureCollector(RunNotifier runNotifier) {
		this.runNotifier = runNotifier;
	}

	@Override
	public void startCollecting() {
		runNotifier.addListener(runListener);
	}
	
	@Override
	public void stopCollecting() {
		runNotifier.removeListener(runListener);
	}
	
	
	protected class FailureListener extends RunListener {
		@Override
		public void testFailure(Failure failure) throws Exception {
			TestFailure testFailure = new TestFailure(failure.getDescription(), failure);
			onDataCollected(testFailure);
		}
	}
	
}
