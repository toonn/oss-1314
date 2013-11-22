package kuleuven.group6.collectors;

import kuleuven.group6.testcharacteristics.testdatas.ITestData;

/**
 * This interface provides a way to subscribe to a DataCollector that collects a
 * specific kind of data.
 */
public interface IDataEnroller {

	public <T extends ITestData> void subscribe(Class<T> testDataClass,
			DataCollectedListener<? super T> listener);

	public void close();

}
