package kuleuven.group6.collectors;

import kuleuven.group6.testcharacteristics.ITestData;

/**
 * 
 * @author Team 6
 *
 */
public interface IDataCollectorManager {

	public <T extends ITestData> void subscribe(Class<T> testDataClass, DataCollectedListener<? super T> listener);
	
}