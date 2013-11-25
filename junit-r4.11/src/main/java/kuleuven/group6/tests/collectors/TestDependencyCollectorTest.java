package kuleuven.group6.tests.collectors;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kuleuven.group6.RunNotificationSubscriber;
import kuleuven.group6.collectors.IDataCollectedListener;
import kuleuven.group6.collectors.TestDependencyCollector;
import kuleuven.group6.testcharacteristics.testdatas.MethodCalls;
import kuleuven.group6.tests.testsubject.source.Dummy;
import kuleuven.group6.tests.testsubject.source.SourceLocator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;

/**
 * A test for the TestDependencyCollector class.
 * 
 * IMPORTANT: The TestDependencyCollector class depends on the OSSRewriter library, which
 * needs to be run with the following VM arguments:
 * 		-javaagent:lib/ossrewriter-1.0.jar  
 * 
 * @author team 6
 *
 */
public class TestDependencyCollectorTest {

	protected RunNotifier runNotifier;
	protected Description testDescription;
	protected TestDependencyCollector failureTraceCollector;
	protected MethodCalls expectedMethodCalls = null;
	protected MethodCalls lastCollectedMethodCalls = null;
	
	@Before
	public void initializeTestEnvironment() {
		runNotifier = new RunNotifier();
		testDescription = Description.createTestDescription(getClass(), "testMethodCall");
		RunNotificationSubscriber runNotificationSubscriber = new RunNotificationSubscriber(runNotifier);
		File subjectSourceDirectory = new File(SourceLocator.getLocation());
		failureTraceCollector = new TestDependencyCollector(subjectSourceDirectory, runNotificationSubscriber);
		failureTraceCollector.addListener(new CallTraceListener());
		failureTraceCollector.startCollecting();
	}
	
	@After
	public void tearDownTestEnvironment() {
		failureTraceCollector.stopCollecting();
	}
	
	@Test
	public void testConstructorCall() {
		runNotifier.fireTestStarted(testDescription);
		new Dummy();
		runNotifier.fireTestFinished(testDescription);
		
		testExpectedDummyCalls(
			"<init>()V"
		);
	}
	
	@Test
	public void testSingleCall() {
		Dummy testSubject = new Dummy();
		runNotifier.fireTestStarted(testDescription);
		testSubject.dummyMethodA();
		runNotifier.fireTestFinished(testDescription);
		
		testExpectedDummyCalls(
			"dummyMethodA()V"
		);
	}
	
	@Test
	public void testNestedCall() {
		Dummy testSubject = new Dummy();
		runNotifier.fireTestStarted(testDescription);
		testSubject.nestingMethodAB();
		runNotifier.fireTestFinished(testDescription);
		
		testExpectedDummyCalls(
			"nestingMethodAB()V",
			"dummyMethodA()V",
			"dummyMethodB()V"
		);
	}
	
	@Test
	public void testCombinedCalls() {
		runNotifier.fireTestStarted(testDescription);
		Dummy testSubject = new Dummy();
		testSubject.dummyMethodA();
		testSubject.dummyMethodB();
		testSubject.nestingMethodAB();
		runNotifier.fireTestFinished(testDescription);
		
		testExpectedDummyCalls(
			"<init>()V",
			"dummyMethodA()V",
			"dummyMethodB()V",
			"nestingMethodAB()V",
			"dummyMethodA()V",
			"dummyMethodB()V"
		);
	}
	
	private void testExpectedDummyCalls(String... calls) {
		String dummyClass = Dummy.class.getName().replace('.', '/');
		List<String> callsWithClass = new ArrayList<>();
		for (String call : calls) {
			callsWithClass.add(dummyClass + '.' + call);
		}
		expectedMethodCalls = new MethodCalls(testDescription, callsWithClass);
		MethodCalls filteredMethodCalls = getFilteredMethodCalls();
		assertEquals(expectedMethodCalls, filteredMethodCalls);
	}

	/**
	 * Get a filtered MethodCalls object. Methods that contain the string 
	 * "jacoco" are filtered because they are injected by some code coverage 
	 * tools.
	 * 
	 * @return
	 */
	private MethodCalls getFilteredMethodCalls() {
		List<String> filteredList = new ArrayList<>(lastCollectedMethodCalls.getMethodNames());
		for (int i = 0; i < filteredList.size(); i++) {
			String method = filteredList.get(i);
			if (method.contains("jacoco"))
				filteredList.remove(i);
		}
		
		return new MethodCalls(
			lastCollectedMethodCalls.getTestDescription(), 
			filteredList
		);
	}

	
	protected class CallTraceListener implements IDataCollectedListener<MethodCalls> {
		
		@Override
		public void dataCollected(MethodCalls methodCalls) {
			lastCollectedMethodCalls = methodCalls;
		}
		
	}
}
