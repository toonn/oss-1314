package kuleuven.group6.collectors;

import kuleuven.group6.testcharacteristics.ITestData;

/**
 * 
 * @author Team 6
 *
 * @param <DataT>
 */
public interface DataCollectedListener<TestDataT extends ITestData> {

	public void dataCollected(TestDataT data);
	
}
