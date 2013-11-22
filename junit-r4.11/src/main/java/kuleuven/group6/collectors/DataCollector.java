package kuleuven.group6.collectors;

import java.util.Collection;
import java.util.HashSet;

import kuleuven.group6.testcharacteristics.testdatas.ITestData;

/**
 * 
 * @author Team 6
 *
 * @param <TestDataT>
 */
public abstract class DataCollector<TestDataT extends ITestData> {

	protected Collection<DataCollectedListener<? super TestDataT>> listeners = new HashSet<>();
	protected boolean isCollecting = false;

	public void addListener(DataCollectedListener<? super TestDataT> listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener(DataCollectedListener<? super TestDataT> listener) {
		this.listeners.remove(listener);
	}
	
	
	protected void onDataCollected(TestDataT data) {
		for (DataCollectedListener<? super TestDataT> listener : this.listeners) {
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
