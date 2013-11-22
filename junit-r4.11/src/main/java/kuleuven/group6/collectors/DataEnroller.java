package kuleuven.group6.collectors;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import kuleuven.group6.RunNotificationSubscriber;
import kuleuven.group6.testcharacteristics.testdatas.ITestData;

/**
 * 
 * @author Team 6
 *
 */
public class DataEnroller implements IDataEnroller {

	protected Set<DataCollector<? extends ITestData>> collectors = new HashSet<>();
	
	protected DataEnroller() {
		
	}
	
	private void configure(
			RunNotificationSubscriber runNotificationSubscriber, 
			String rootSuiteClassName, File testDirectory, File codeDirectory) {
		collectors.add(new TestFailureCollector(runNotificationSubscriber));
		collectors.add(new TestDependencyCollector(codeDirectory, runNotificationSubscriber));
		collectors.add(new CodeChangeCollector(rootSuiteClassName, testDirectory, codeDirectory));
	}
	
	public static DataEnroller createConfiguredDataEnroller(
			RunNotificationSubscriber runNotificationSubscriber, 
			String rootSuiteClassName, File testDirectory, File codeDirectory) {
		// TODO put all these arguments inside a value object (i.e. TestCollectionInfo)
		DataEnroller dataEnroller = new DataEnroller();
		dataEnroller.configure(runNotificationSubscriber, rootSuiteClassName, testDirectory, codeDirectory);
		return dataEnroller;
	}

	@Override
	public <T extends ITestData> void subscribe(Class<T> testDataClass, DataCollectedListener<? super T> listener) {
		DataCollector<? extends T> collector = findCollector(testDataClass);
		if (!collector.isCollecting())
			collector.startCollecting();
		collector.addListener(listener);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends ITestData> DataCollector<? extends T> findCollector(Class<T> testDataClass) {
		for (DataCollector<?> collector : collectors) {
			if (collector.canProduce(testDataClass))
				return (DataCollector<? extends T>)collector;
		}
		throw new NoSuitableCollectorException(testDataClass);
	}
	
	
	@Override
	public void close() {
		for (DataCollector<?> collector : collectors) {
			if (collector.isCollecting())
				collector.stopCollecting();
		}
	}
	
}
