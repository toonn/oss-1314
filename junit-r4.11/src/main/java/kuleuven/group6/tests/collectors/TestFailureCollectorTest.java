package kuleuven.group6.tests.collectors;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.*;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import kuleuven.group6.collectors.IDataCollectedListener;
import kuleuven.group6.collectors.TestFailureCollector;
import kuleuven.group6.testcharacteristics.testdatas.TestFailure;
import kuleuven.group6.testrun.RunNotificationSubscriber;
import kuleuven.group6.tests.testsubject.tests.AllTests;

public class TestFailureCollectorTest {

	private RunNotifier runNotifier;
	private Description suiteDescription;
	private Collection<Failure> thrownFailures;
	private Collection<TestFailure> collectedFailures;
	private TestFailureCollector collector;
	
	@Before
	public void initializeCollector() {
		runNotifier = new RunNotifier();
		suiteDescription = Description.createSuiteDescription(AllTests.class);
		thrownFailures = new ArrayList<>();
		collectedFailures = new ArrayList<>();
		
		RunNotificationSubscriber subscriber = new RunNotificationSubscriber(runNotifier);
		collector = new TestFailureCollector(subscriber);
		collector.addListener(new Listener());
		collector.startCollecting();
	}
	
	
	@Test
	public void testOneFailingTest() {
		throwNewFailure();
		verifyCorrectResults();
	}
	
	@Test
	public void testFiveFailingTests() {
		throwNewFailure();
		throwNewFailure();
		throwNewFailure();
		throwNewFailure();
		throwNewFailure();
		
		verifyCorrectResults();
	}
	
	@Test
	public void testNoFailingTest() {
		verifyCorrectResults();
	}
	
	
	private void verifyCorrectResults() {
		assertEquals(thrownFailures.size(), collectedFailures.size());
		for (TestFailure testFailure : collectedFailures) {
			boolean contains = false;
			for (Failure thrownFailure : thrownFailures) {
				if (testFailure.getFailure() == thrownFailure) {
					assertEquals(
							thrownFailure.getDescription(), 
							testFailure.getTestDescription());
					contains = true;
					break;
				}
			}
			assertTrue(contains);
		}
	}
	
	private void throwNewFailure() {
		Failure failure = new Failure(suiteDescription, new Exception());
		thrownFailures.add(failure);
		runNotifier.fireTestFailure(failure);
	}
	
	
	protected class Listener implements IDataCollectedListener<TestFailure> {
		
		@Override
		public void dataCollected(TestFailure data) {
			collectedFailures.add(data);
		}
		
	}

}
