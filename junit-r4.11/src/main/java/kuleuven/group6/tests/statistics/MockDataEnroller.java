package kuleuven.group6.tests.statistics;

import kuleuven.group6.collectors.DataCollectedListener;
import kuleuven.group6.collectors.DataCollector;
import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.collectors.NoSuitableCollectorException;
import kuleuven.group6.testcharacteristics.testdatas.ITestData;

public class MockDataEnroller implements IDataEnroller {

	protected DataCollector<?>[] collectors;
	
	public MockDataEnroller(DataCollector<?>... collectors) {
		this.collectors = collectors;
	}
	
	@Override
	public <T extends ITestData> void subscribe(Class<T> testDataClass, DataCollectedListener<? super T> listener) {
		DataCollector<? extends T> collector = findCollector(testDataClass);
		collector.addListener(listener);
		if (! collector.isCollecting())
			collector.startCollecting();
	}
	
	@Override
	public <T extends ITestData> void unsubscribe(Class<T> testDataClass, DataCollectedListener<? super T> listener) {
		DataCollector<? extends T> collector = findCollector(testDataClass);
		collector.removeListener(listener);
	}
	
	protected <T extends ITestData> DataCollector<? extends T> findCollector(Class<T> testDataClass) {
		for (DataCollector<?> collector : collectors) {
			if (collector.canProduce(testDataClass)) {
				@SuppressWarnings("unchecked")
				DataCollector<? extends T> typedCollector = 
						(DataCollector<? extends T>) collector;
				return typedCollector;
			}
		}
		
		throw new NoSuitableCollectorException(testDataClass);
	}

	@Override
	public void close() {
		for (DataCollector<?> collector : collectors) {
			collector.stopCollecting();
		}
	}
	
}
