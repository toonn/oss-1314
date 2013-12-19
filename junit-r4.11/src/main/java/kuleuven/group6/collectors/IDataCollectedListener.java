package kuleuven.group6.collectors;

import kuleuven.group6.collectors.testdatas.ITestData;

/**
 * Observers of DataCollector's need to implement this interface.
 *
 * @param <TestDataT>
 * 		This interface is generic because the data collected by a DataCollector
 * 		is of a specific type of ITestData.
 */
public interface IDataCollectedListener<TestDataT extends ITestData> {

	public void dataCollected(TestDataT data);
	
}
