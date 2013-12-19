package kuleuven.group6.collectors;

import kuleuven.group6.collectors.testdatas.ITestData;

/**
 * This interface provides a way to subscribe to a DataCollector that collects a
 * specific kind of data. 
 * TODO
 */
public interface IDataEnroller {

	public <T extends ITestData> void subscribe(Class<T> testDataClass,
			IDataCollectedListener<? super T> listener);
	public <T extends ITestData> void unsubscribe(Class<T> testDataClass,
			IDataCollectedListener<? super T> listener);

	public void close();

}
