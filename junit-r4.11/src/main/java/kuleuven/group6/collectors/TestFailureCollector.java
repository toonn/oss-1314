package kuleuven.group6.collectors;

import kuleuven.group6.TestCollectionInfo;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class TestFailureCollector extends DataCollector<Failure> {

	protected RunListener runListener = new FailureListener();

	@Override
	public void startCollecting(TestCollectionInfo testCollectionInfo) {
		testCollectionInfo.addTestRunListener(runListener);
	}
	
	@Override
	public void stopCollecting(TestCollectionInfo testCollectionInfo) {
		testCollectionInfo.removeTestRunListener(runListener);
	}
	
	
	protected class FailureListener extends RunListener {

		@Override
		public void testFailure(Failure failure) throws Exception {
			onDataCollected(failure.getDescription(), failure);
		}
		
	}
	
}
