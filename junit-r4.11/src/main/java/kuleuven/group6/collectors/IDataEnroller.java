package kuleuven.group6.collectors;

import kuleuven.group6.testcharacteristics.testdatas.ITestData;

/**
 * 
 * @author Team 6
 *
 */
public interface IDataEnroller {

	public <T extends ITestData> void subscribe(Class<T> testDataClass, DataCollectedListener<? super T> listener);

	public void close();
	
}
