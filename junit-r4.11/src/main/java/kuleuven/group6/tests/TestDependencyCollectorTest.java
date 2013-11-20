package kuleuven.group6.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import kuleuven.group6.RunNotificationSubscriber;
import kuleuven.group6.TestCollectionInfo;
import kuleuven.group6.collectors.DataCollectedListener;
import kuleuven.group6.collectors.TestDependencyCollector;
import kuleuven.group6.testcharacteristics.MethodCalls;
import kuleuven.group6.tests.testsubject.source.Dummy;
import kuleuven.group6.tests.testsubject.source.SourceLocator;
import kuleuven.group6.tests.testsubject.tests.TestsLocator;
import org.junit.*;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

public class TestDependencyCollectorTest {

	protected TestCollectionInfo testCollectionInfo;
	protected RunNotifier runNotifier;
	protected TestDependencyCollector failureTraceCollector;
	
	@Before
	public void initializeTestEnvironment() {
		runNotifier = new RunNotifier();
		
		File subjectSourceDirectory = new File(SourceLocator.getLocation());
		File subjectTestsDirectory = new File(TestsLocator.getLocation());
		// TODO remove the need of TestCollectionInfo
		testCollectionInfo = new TestCollectionInfo(subjectTestsDirectory, subjectSourceDirectory, runNotifier);
		
		failureTraceCollector = new TestDependencyCollector(testCollectionInfo.getSourceClassNames(), new RunNotificationSubscriber(runNotifier));
		failureTraceCollector.startCollecting();
	}
	
	@After
	public void tearDownTestEnvironment() {
		failureTraceCollector.stopCollecting();
	}
	
	@Test
	public void testMethodCall() {
		CallTraceListener listener = new CallTraceListener();
		failureTraceCollector.addListener(listener);

		Description description = Description.createTestDescription(getClass(), "testMethodCall");
		runNotifier.fireTestStarted(description);
		Dummy testSubject = new Dummy();
		testSubject.dummyMethodA();
		testSubject.dummyMethodB();
		testSubject.nestingMethodAB();
		runNotifier.fireTestFinished(description);
		
		MethodCalls methodCalls = listener.getLastCollectedMethodCalls();
		String[] actualMethods = methodCalls.getMethodNames().toArray(new String[] { });
		String dummyClass = Dummy.class.getName().replace('.', '/');
		String[] expectedMethods = {
			dummyClass + ".<init>()V",
			dummyClass + ".dummyMethodA()V",
			dummyClass + ".dummyMethodB()V",
			dummyClass + ".nestingMethodAB()V",
			dummyClass + ".dummyMethodA()V",
			dummyClass + ".dummyMethodB()V"
		};
		Collection<String> temp = new ArrayList<String>();
		for (String method : actualMethods) {
			if (!method.contains("jacoco"))
				temp.add(method);
		}
		actualMethods = temp.toArray(new String[] {});
		assertArrayEquals(expectedMethods, actualMethods);
		
		assertEquals(description, methodCalls.getTestDescription());
	}

	
	protected class CallTraceListener implements DataCollectedListener<MethodCalls> {

		protected MethodCalls lastCollectedMethodCalls = null;
		
		public MethodCalls getLastCollectedMethodCalls() {
			return lastCollectedMethodCalls;
		}
		
		@Override
		public void dataCollected(MethodCalls methodCalls) {
			lastCollectedMethodCalls = methodCalls;
		}
		
	}
}
