package kuleuven.group6.collectors;

import java.util.Collection;
import java.util.HashSet;

import kuleuven.group6.TestCollectionInfo;
import org.junit.runner.Description;

public abstract class DataCollector<DataT> {

	protected Collection<DataCollectedListener<DataT>> listeners = new HashSet<DataCollectedListener<DataT>>();
	

	public void addListener(DataCollectedListener<DataT> listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener(DataCollectedListener<DataT> listener) {
		this.listeners.remove(listener);
	}
	
	
	protected void onDataCollected(Description identifier, DataT data) {
		for (DataCollectedListener<DataT> listener : this.listeners) {
			listener.dataCollected(identifier, data);
		}
	}
	
	
	public abstract void startCollecting(TestCollectionInfo testCollectionInfo);
	
}
