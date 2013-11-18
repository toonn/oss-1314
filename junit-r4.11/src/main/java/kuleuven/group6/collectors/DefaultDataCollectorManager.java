package kuleuven.group6.collectors;

import java.util.HashMap;
import java.util.Map;

import kuleuven.group6.testcharacteristics.ITestData;

/**
 * 
 * @author Team 6
 *
 */
public class DefaultDataCollectorManager implements IDataCollectorManager {

	protected Map<Class<? extends ITestData>, DataCollector<? extends ITestData>> collectors = new HashMap<>();
	
	public DefaultDataCollectorManager() {
		// TODO: default collectors toevoegen
	}

	@Override
	public <T extends ITestData> void subscribe(Class<T> testDataClass, DataCollectedListener<? super T> listener) {
		DataCollector<T> collector = findCollector(testDataClass);
		collector.addListener(listener);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ITestData> DataCollector<T> findCollector(Class<T> testDataClass) {
		for (Class<? extends ITestData> entryTestDataClass : collectors.keySet()) {
			if (entryTestDataClass.equals(testDataClass)) {
				return (DataCollector<T>)collectors.get(entryTestDataClass);
			}
		}
		
		throw new NoSuitableCollectorException(testDataClass);
	}
	
}
