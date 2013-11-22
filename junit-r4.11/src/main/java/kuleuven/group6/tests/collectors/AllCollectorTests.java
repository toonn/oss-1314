package kuleuven.group6.tests.collectors;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestFailureCollectorTest.class,
	TestDependencyCollectorTest.class
})
public class AllCollectorTests {
	
}
