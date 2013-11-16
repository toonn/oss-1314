package kuleuven.group6.collectors;

import org.junit.runner.Description;

/**
 * 
 * @author Team 6
 *
 * @param <DataT>
 */
public interface DataCollectedListener<DataT> {

	public void dataCollected(Description identifier, DataT data);
	
}
