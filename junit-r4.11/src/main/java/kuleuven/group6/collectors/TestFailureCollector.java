package kuleuven.group6.collectors;

import kuleuven.group6.RunNotificationSubscriber;
import kuleuven.group6.testcharacteristics.TestFailure;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * 
 * @author Team 6
 *
 */
public class TestFailureCollector extends DataCollector<TestFailure> {

	protected RunNotificationSubscriber runNotificationSubscriber;
	protected RunListener runListener = new FailureListener();
	
	public TestFailureCollector(RunNotificationSubscriber runNotificationSubscriber) {
		this.runNotificationSubscriber = runNotificationSubscriber;
	}

	@Override
	public void startCollecting() {
		runNotificationSubscriber.addListener(runListener);
	}
	
	@Override
	public void stopCollecting() {
		runNotificationSubscriber.removeListener(runListener);
	}
	
	
	protected class FailureListener extends RunListener {
		@Override
		public void testFailure(Failure failure) throws Exception {
			TestFailure testFailure = new TestFailure(failure.getDescription(), failure);
			onDataCollected(testFailure);
		}
	}
	
}
