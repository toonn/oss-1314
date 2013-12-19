package kuleuven.group6.collectors;

import java.util.Collection;
import java.util.HashSet;

import kuleuven.group6.collectors.testdatas.ITestData;

/**
 * All DataCollector's extend this "interface."
 * These are subjects observed by DataCollectedListener's.
 * 
 * @param <TestDataT>
 * 		This "interface" is generic in the type of data it collects,
 * 		concrete DataCollector subclasses are parameterized with a specific
 * 		type of ITestData and are no longer generic.
 */
public abstract class DataCollector<TestDataT extends ITestData> {

	protected Collection<IDataCollectedListener<? super TestDataT>> listeners = new HashSet<>();
	protected boolean isCollecting = false;

	public void addListener(IDataCollectedListener<? super TestDataT> listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener(IDataCollectedListener<? super TestDataT> listener) {
		this.listeners.remove(listener);
	}
	
	
	protected void onDataCollected(TestDataT data) {
		for (IDataCollectedListener<? super TestDataT> listener : this.listeners) {
			listener.dataCollected(data);
		}
	}
	
	public boolean isCollecting() {
		return isCollecting;
	}
	
	public void startCollecting() {
		isCollecting = true;
	}
	
	public void stopCollecting() {
		isCollecting = false;
	}
	
	/**
	 * Check whether this data collector can produce data of the given class. A
	 * DataCollector<T> can produce data for type T and it's super types. 
	 * 
	 * @param testDataClass
	 * @return
	 */
	public abstract <T extends ITestData> boolean canProduce(Class<T> testDataClass);
	
}
