package kuleuven.group6.collectors;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import kuleuven.group6.RunNotificationSubscriber;
import kuleuven.group6.testcharacteristics.CodeChange;
import kuleuven.group6.testcharacteristics.ITestData;
import kuleuven.group6.testcharacteristics.MethodCalls;
import kuleuven.group6.testcharacteristics.TestFailure;
import org.junit.runner.Description;

/**
 * 
 * @author Team 6
 *
 */
public class DataEnroller implements IDataEnroller {

	protected Map<Class<? extends ITestData>, DataCollector<? extends ITestData>> collectors = new HashMap<>();
	
	protected DataEnroller() {
		
	}
	
	private void configure(
			RunNotificationSubscriber runNotificationSubscriber, Collection<String> sourceClassNames, 
			Description topLevelDescription, File testDirectory, File codeDirectory) {
		collectors.put(TestFailure.class, new TestFailureCollector(runNotificationSubscriber));
		collectors.put(MethodCalls.class, new TestDependencyCollector(sourceClassNames, runNotificationSubscriber));
		collectors.put(CodeChange.class, new CodeChangeCollector(topLevelDescription, testDirectory, codeDirectory));
	}
	
	public static DataEnroller createConfiguredDataEnroller(
			RunNotificationSubscriber runNotificationSubscriber, Collection<String> sourceClassNames, 
			Description topLevelDescription, File testDirectory, File codeDirectory) {
		// TODO put all these arguments inside a value object (i.e. TestCollectionInfo)
		DataEnroller dataEnroller = new DataEnroller();
		dataEnroller.configure(runNotificationSubscriber, sourceClassNames, topLevelDescription, testDirectory, codeDirectory);
		return dataEnroller;
	}

	@Override
	public <T extends ITestData> void subscribe(Class<T> testDataClass, DataCollectedListener<? super T> listener) {
		DataCollector<T> collector = findCollector(testDataClass);
		collector.addListener(listener);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends ITestData> DataCollector<T> findCollector(Class<T> testDataClass) {
		if (! collectors.containsKey(testDataClass))
			throw new NoSuitableCollectorException(testDataClass);
		
		return (DataCollector<T>) collectors.get(testDataClass);
	}
	
}
