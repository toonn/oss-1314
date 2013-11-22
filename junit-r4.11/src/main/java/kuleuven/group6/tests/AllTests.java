package kuleuven.group6.tests;

import kuleuven.group6.tests.collectors.AllCollectorTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * @author Team 6
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
	FlattenedRequestTest.class,
	AllCollectorTests.class
})
public class AllTests {

}
