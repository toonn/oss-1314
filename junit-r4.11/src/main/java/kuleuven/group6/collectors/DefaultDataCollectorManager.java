package kuleuven.group6.collectors;

import java.util.HashMap;
import java.util.Map;

import kuleuven.group6.RunNotificationSubscriber;
import kuleuven.group6.testcharacteristics.ITestData;
import kuleuven.group6.testcharacteristics.TestFailure;

/**
 * 
 * @author Team 6
 *
 */
public class DefaultDataCollectorManager implements IDataCollectorManager {

	protected Map<Class<? extends ITestData>, DataCollector<? extends ITestData>> collectors = new HashMap<>();
	
	public DefaultDataCollectorManager(RunNotificationSubscriber runNotificationSubscriber) {
		collectors.put(TestFailure.class, new TestFailureCollector(runNotificationSubscriber));
		// TODO: andere default collectors toevoegen
	}

	@Override
	public <T extends ITestData> void subscribe(Class<T> testDataClass, DataCollectedListener<? super T> listener) {
		DataCollector<T> collector = findCollector(testDataClass);
		collector.addListener(listener);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ITestData> DataCollector<T> findCollector(Class<T> testDataClass) {
		if (! collectors.containsKey(testDataClass))
			throw new NoSuitableCollectorException(testDataClass);
		
		return (DataCollector<T>) collectors.get(testDataClass);
	}
	
}
