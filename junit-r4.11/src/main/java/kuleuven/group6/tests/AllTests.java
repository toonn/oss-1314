package kuleuven.group6.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TestDependencyCollectorTest.class,
	FlattenedRequestTest.class
})
public class AllTests {

}