package kuleuven.group6.tests.testsubject.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	BasicTest.class, 
	FailingDummyTest.class,
	MostlyHighPctFailingTest.class,
	MostlyLowPctFailingTest.class,
	DistinctFailingTest.class
})
public class AllTests {

}
