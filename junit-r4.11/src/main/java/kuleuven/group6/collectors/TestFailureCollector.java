package kuleuven.group6.collectors;

import kuleuven.group6.RunNotificationSubscriber;
import kuleuven.group6.testcharacteristics.testdatas.ITestData;
import kuleuven.group6.testcharacteristics.testdatas.TestFailure;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * 
 * TestFailureCollector collects every failure for every test that is run.
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
		super.startCollecting();
		runNotificationSubscriber.addListener(runListener);
	}
	
	@Override
	public void stopCollecting() {
		super.startCollecting();
		runNotificationSubscriber.removeListener(runListener);
	}
	
	@Override
	public <T extends ITestData> boolean canProduce(Class<T> testDataClass) {
		return testDataClass.isAssignableFrom(TestFailure.class);
	}
	
	protected class FailureListener extends RunListener {
		@Override
		public void testFailure(Failure failure) throws Exception {
			TestFailure testFailure = new TestFailure(failure.getDescription(), failure);
			onDataCollected(testFailure);
		}
	}
	
}
