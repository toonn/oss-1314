package kuleuven.group6.collectors;

import org.junit.runner.Description;

public interface DataCollectedListener<DataT> {

	public void dataCollected(Description identifier, DataT data);
	
}
