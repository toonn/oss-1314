package kuleuven.group6.tests.statistics;

import kuleuven.group6.collectors.DataCollector;
import kuleuven.group6.testcharacteristics.testdatas.ITestData;
import kuleuven.group6.testcharacteristics.testdatas.TestFailure;

/**
 * 
 * @author Team 6
 *
 */
public class MockTestFailureCollector extends DataCollector<TestFailure> {

	@Override
	public <T extends ITestData> boolean canProduce(Class<T> testDataClass) {
		return testDataClass.isAssignableFrom(TestFailure.class);
	}
	
	public void doCollect(TestFailure data) {
		onDataCollected(data);
	}
	
}
