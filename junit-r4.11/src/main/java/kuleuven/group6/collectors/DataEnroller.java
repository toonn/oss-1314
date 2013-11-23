package kuleuven.group6.collectors;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import kuleuven.group6.RunNotificationSubscriber;
import kuleuven.group6.testcharacteristics.testdatas.ITestData;

/*
 * (non-Javadoc)
 * @see IDataEnroller
 *
 */
public class DataEnroller implements IDataEnroller {

	protected Set<DataCollector<? extends ITestData>> collectors = new HashSet<>();
	
	protected DataEnroller() {

	}

	/**
	 * This configures a DataEnroller with everything it needs. The
	 * responsibility for the creation of DataCollector's does not fit well
	 * here. A possible better solution is to have a chain-of-responsibility of
	 * factories.
	 * 
	 * @param runNotificationSubscriber
	 *            Some DataCollector's need to see the results of testruns
	 * @param rootSuiteClassName
	 *            Some DataCollector's need to know the root Suite containing
	 *            all tests.
	 * @param testDirectory
	 *            Some DataCollector's need to know which tests are running.
	 * @param codeDirectory
	 *            Some DataCollector's need to know which codebase is being
	 *            tested.
	 */
	private void configure(RunNotificationSubscriber runNotificationSubscriber,
			String rootSuiteClassName, File testDirectory, File codeDirectory) {
		collectors.add(new TestFailureCollector(runNotificationSubscriber));
		collectors.add(new TestDependencyCollector(codeDirectory, runNotificationSubscriber));
		collectors.add(new CodeChangeCollector(rootSuiteClassName, testDirectory, codeDirectory));
	}

	/**
	 * @return A configured DataEnroller because it is not the responsibitlity
	 *         of e.g. Daemon to configure a DataEnroller.
	 */
	public static DataEnroller createConfiguredDataEnroller(
			RunNotificationSubscriber runNotificationSubscriber,
			String rootSuiteClassName, File testDirectory, File codeDirectory) {
		// TODO put all these arguments inside a value object (i.e.
		// TestCollectionInfo)
		DataEnroller dataEnroller = new DataEnroller();
		dataEnroller.configure(runNotificationSubscriber, rootSuiteClassName,
				testDirectory, codeDirectory);
		return dataEnroller;
	}

	@Override
	public <T extends ITestData> void subscribe(Class<T> testDataClass, DataCollectedListener<? super T> listener) {
		DataCollector<? extends T> collector = findCollector(testDataClass);
		if (!collector.isCollecting())
			collector.startCollecting();
		collector.addListener(listener);
	}
	
	@Override
	public <T extends ITestData> void unsubscribe(Class<T> testDataClass, DataCollectedListener<? super T> listener) {
		DataCollector<? extends T> collector = findCollector(testDataClass);
		collector.removeListener(listener);
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
