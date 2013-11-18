package kuleuven.group6.collectors;

import java.util.Collection;
import java.util.HashSet;

import kuleuven.group6.testcharacteristics.ITestData;

/**
 * 
 * @author Team 6
 *
 * @param <TestDataT>
 */
public abstract class DataCollector<TestDataT extends ITestData> {

	protected Collection<DataCollectedListener<? super TestDataT>> listeners = new HashSet<>();
	

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
	
	
	public abstract void startCollecting();
	
	public abstract void stopCollecting();
	
}
