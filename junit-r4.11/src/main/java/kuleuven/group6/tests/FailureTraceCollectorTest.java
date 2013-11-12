package kuleuven.group6.tests;

import static org.junit.Assert.*;

import java.io.File;

import kuleuven.group6.TestCollectionInfo;
import kuleuven.group6.collectors.DataCollectedListener;
import kuleuven.group6.collectors.FailureTraceCollector;
import kuleuven.group6.collectors.MethodCallTrace;
import kuleuven.group6.tests.testsubject.source.Dummy;
import kuleuven.group6.tests.testsubject.source.SourceLocator;
import kuleuven.group6.tests.testsubject.tests.TestsLocator;
import org.junit.*;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

public class FailureTraceCollectorTest {

	protected TestCollectionInfo testCollectionInfo;
	protected RunNotifier runNotifier;
	protected FailureTraceCollector failureTraceCollector;
	
	@Before
	public void initializeTestEnvironment() {
		runNotifier = new RunNotifier();
		
		File subjectSourceDirectory = new File(SourceLocator.getLocation());
		File subjectTestsDirectory = new File(TestsLocator.getLocation());
		testCollectionInfo = new TestCollectionInfo(subjectTestsDirectory, subjectSourceDirectory, runNotifier);
		
		failureTraceCollector = new FailureTraceCollector();
		failureTraceCollector.startCollecting(testCollectionInfo);
	}
	
	@After
	public void tearDownTestEnvironment() {
		failureTraceCollector.stopCollecting(testCollectionInfo);
	}
	
	@Test
	public void testMethodCall() {
		CallTraceListener listener = new CallTraceListener();
		failureTraceCollector.addListener(listener);

		Description description = Description.createTestDescription(getClass(), "testMethodCall");
		runNotifier.fireTestStarted(description);
		Dummy testSubject = new Dummy();
		testSubject.dummyMethod();
		runNotifier.fireTestFinished(description);
		
		MethodCallTrace collectedData = listener.getLastCollectedData();
		String[] actualMethods = collectedData.getMethodCalls().toArray(new String[] { });
		String dummyClass = Dummy.class.getName().replace('.', '/');
		String[] expectedMethods = {
			dummyClass + ".<init>()V",
			dummyClass + ".dummyMethod()V",
		};
		assertArrayEquals(expectedMethods, actualMethods);
		
		assertEquals(description, listener.getLastCollectedDescription());
	}

	
	protected class CallTraceListener implements DataCollectedListener<MethodCallTrace> {

		protected MethodCallTrace lastCollectedData = null;
		protected Description lastCollectedDescription = null;
		
		public MethodCallTrace getLastCollectedData() {
			return lastCollectedData;
		}
		
		public Description getLastCollectedDescription() {
			return lastCollectedDescription;
		}
		
		public void dataCollected(Description identifier, MethodCallTrace data) {
			lastCollectedData = data;
			lastCollectedDescription = identifier;
		}
		
	}
}
