package kuleuven.group6.tests.statistics;

import static org.junit.Assert.*;

import java.util.Date;

import kuleuven.group6.statistics.LastFailureStatistic;
import kuleuven.group6.testcharacteristics.testdatas.TestFailure;
import kuleuven.group6.testcharacteristics.teststatistics.LastFailureDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

public class LastFailureStatisticTest {

	protected final long SLEEP_MS = 1;
	
	protected MockTestFailureCollector collector;
	protected LastFailureStatistic statistic;
	
	@Before
	public void initializeStatistic() {
		collector = new MockTestFailureCollector();
		MockDataEnroller dataEnroller = new MockDataEnroller(collector);
		statistic = new LastFailureStatistic(dataEnroller);
	}
	
	@Test
	public void testSingleTest() {
		Description description = 
				Description.createTestDescription(getClass(), "testSingleTest");
		
		Date beforeCollect = new Date();
		sleep();
		collectError(description);
		sleep();
		Date afterCollect = new Date();
		
		LastFailureDate lastFailure = statistic.getTestStatistic(description);
		assertNotNull(lastFailure);
		assertEquals(description, lastFailure.getTestDescription());
		assertBetween(lastFailure.getLastFailureDate(), beforeCollect, afterCollect);
	}
	
	@Test
	public void testNotFailedTest() {
		Description description = 
				Description.createTestDescription(getClass(), "testSingleTest");
		
		LastFailureDate lastFailure = statistic.getTestStatistic(description);
		assertNull(lastFailure);
	}
	
	@Test
	public void testLastFailureDate() {
		Description description = 
				Description.createTestDescription(getClass(), "testSingleTest");
		
		collectError(description);
		Date betweenCollects = new Date();
		sleep();
		collectError(description);
		sleep();
		Date afterCollects = new Date();
		
		LastFailureDate lastFailure = statistic.getTestStatistic(description);
		assertNotNull(lastFailure);
		assertBetween(lastFailure.getLastFailureDate(), betweenCollects, afterCollects);
	}
	
	@Test
	public void testTwoTests() {
		Description description1 = 
				Description.createTestDescription(getClass(), "test1");
		Description description2 = 
				Description.createTestDescription(getClass(), "test2");
		
		Date beforeCollects = new Date();
		sleep();
		collectError(description1);
		sleep();
		Date betweenCollects = new Date();
		sleep();
		collectError(description2);
		sleep();
		Date afterCollects = new Date();
		
		LastFailureDate lastFailure1 = statistic.getTestStatistic(description1);
		assertNotNull(lastFailure1);
		assertEquals(description1, lastFailure1.getTestDescription());
		assertBetween(lastFailure1.getLastFailureDate(), beforeCollects, betweenCollects);
		
		LastFailureDate lastFailure2 = statistic.getTestStatistic(description2);
		assertNotNull(lastFailure2);
		assertEquals(description2, lastFailure2.getTestDescription());
		assertBetween(lastFailure2.getLastFailureDate(), betweenCollects, afterCollects);
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
		sleep();
		collectError(childDescription2);
		sleep();
		collectError(childDescription3);
		
		LastFailureDate suiteDate = statistic.getTestStatistic(suiteDescription);
		assertNotNull(suiteDate);
		assertEquals(suiteDescription, suiteDate.getTestDescription());
		
		LastFailureDate childDate3 = statistic.getTestStatistic(childDescription3);
		assertEquals(childDate3.getLastFailureDate(), suiteDate.getLastFailureDate());
	}
	
	@Test
	public void testEmptySuite() {
		Description suiteDescription = 
				Description.createSuiteDescription(getClass());
		Description testDescription = 
				Description.createTestDescription(getClass(), "test");
		
		collectError(testDescription);
		
		LastFailureDate suiteDate = statistic.getTestStatistic(suiteDescription);
		assertNull(suiteDate);
	}
	
	private void sleep() {
		try { 
			Thread.sleep(SLEEP_MS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			// Should never happen
			throw new AssertionError();
		}
	}
	
	private void collectError(Description description) {
		collector.doCollect(new TestFailure(
				description, new Failure(description, new Exception())
		));
	}
	
	private static void assertBetween(Date checkedDate, Date begin, Date end) {
		assertFalse(checkedDate.before(begin));
		assertFalse(checkedDate.after(end));
	}
	
}
