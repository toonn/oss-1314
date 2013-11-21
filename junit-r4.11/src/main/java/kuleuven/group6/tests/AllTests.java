package kuleuven.group6.tests;

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
	TestDependencyCollectorTest.class,
	FlattenedRequestTest.class,
//	CodeChangeCollectorTest.class
})
public class AllTests {

}
