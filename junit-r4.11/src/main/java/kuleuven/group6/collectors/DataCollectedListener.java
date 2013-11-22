package kuleuven.group6.collectors;

import kuleuven.group6.testcharacteristics.testdatas.ITestData;

/**
 * Observers of DataCollector's need to implement this interface.
 *
 * @param <TestDataT>
 * 		This interface is generic because the data collected by a DataCollector
 * 		is of a specific type of ITestData.
 */
public interface DataCollectedListener<TestDataT extends ITestData> {

	public void dataCollected(TestDataT data);
	
}
