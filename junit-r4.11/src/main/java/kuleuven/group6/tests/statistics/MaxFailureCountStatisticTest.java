package kuleuven.group6.tests.statistics;

import static org.junit.Assert.*;

import kuleuven.group6.collectors.testdatas.TestFailure;
import kuleuven.group6.statistics.MaxFailureCountStatistic;
import kuleuven.group6.statistics.teststatistics.FailureCount;
import kuleuven.group6.statistics.teststatistics.ITestStatistic;
import kuleuven.group6.statistics.teststatistics.LastDependencyChange;
import kuleuven.group6.statistics.teststatistics.LastFailureDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

/**
 * 
 * @author Team 6 TODO beschrijvind klasse maxfailurecountstatistictest
 *
 *
 */
public class MaxFailureCountStatisticTest {

	protected final long SLEEP_MS = 1;
	
	protected MockTestFailureCollector collector;
	protected MaxFailureCountStatistic statistic;
	
	@Before
	public void initializeStatistic() {
		collector = new MockTestFailureCollector();
		MockDataEnroller dataEnroller = new MockDataEnroller(collector);
		statistic = new MaxFailureCountStatistic(dataEnroller);
	}
	
	@Test
	public void testSingleTest() {
		Description description = 
				Description.createTestDescription(getClass(), "testSingleTest");
		
		collectError(description);
		
		FailureCount expected = new FailureCount(description, 1); 
		FailureCount actual = statistic.getTestStatistic(description);
		assertEquals(expected, actual);
		
		collectError(description);
		
		expected = new FailureCount(description, 2); 
		actual = statistic.getTestStatistic(description);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testNotFailedTest() {
		Description testDescription =
				Description.createTestDescription(getClass(), "testNoFailure");
		FailureCount expected = new FailureCount(testDescription, 0);
		FailureCount actual = statistic.getTestStatistic(testDescription);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testTwoTests() {
		Description description1 = 
				Description.createTestDescription(getClass(), "test1");
		Description description2 = 
				Description.createTestDescription(getClass(), "test2");
		
		collectError(description1);
		collectError(description2);
		collectError(description1);
		collectError(description1);
		collectError(description2);
		
		FailureCount expected1 = new FailureCount(description1, 3);
		FailureCount actual1 = statistic.getTestStatistic(description1);
		assertEquals(expected1, actual1);
		
		FailureCount expected2 = new FailureCount(description2, 2);
		FailureCount actual2 = statistic.getTestStatistic(description2);
		assertEquals(expected2, actual2);
	}
	
	@Test
	public void testSuite() {
		Description suiteDescription = 
				Description.createSuiteDescription(getClass());
		Description childDescription1 = 
				Description.createTestDescription(getClass(), "test1");
		Description childDescription2 = 
				Description.createTestDescription(getClass(), "test2");
		Description childDescription3 = 
				Description.createTestDescription(getClass(), "test3");
		suiteDescription.addChild(childDescription1);
		suiteDescription.addChild(childDescription2);
		suiteDescription.addChild(childDescription3);
		
		collectError(childDescription1);
		collectError(childDescription1);
		collectError(childDescription2);
		collectError(childDescription2);
		collectError(childDescription2);
		collectError(childDescription3);
		
		FailureCount suiteExpected = new FailureCount(suiteDescription, 3);
		FailureCount suiteActual= statistic.getTestStatistic(suiteDescription);
		assertEquals(suiteExpected, suiteActual);
	}
	
	@Test
	public void testEmptySuite() {
		Description suiteDescription = 
				Description.createSuiteDescription(getClass());
		Description testDescription = 
				Description.createTestDescription(getClass(), "test");
		
		collectError(testDescription);
		
		FailureCount suiteExpected = new FailureCount(suiteDescription, 0);
		FailureCount suiteActual = statistic.getTestStatistic(suiteDescription);
		assertEquals(suiteExpected, suiteActual);
	}
	
	@Test
	public void testCanSummarize() {
		assertTrue(statistic.canSummarize(FailureCount.class));
		assertTrue(statistic.canSummarize(ITestStatistic.class));
		assertFalse(statistic.canSummarize(LastFailureDate.class));
		assertFalse(statistic.canSummarize(LastDependencyChange.class));
	}
	
	private void collectError(Description description) {
		collector.doCollect(new TestFailure(
				description, new Failure(description, new Exception())
		));
	}

}
