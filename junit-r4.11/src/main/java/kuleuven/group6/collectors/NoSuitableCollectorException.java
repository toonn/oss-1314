package kuleuven.group6.collectors;

import kuleuven.group6.testcharacteristics.testdatas.ITestData;

/**
 * This exception should be thrown when an IDataEnroller can't find a collector
 * for the specified ITestData.
 */
public class NoSuitableCollectorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoSuitableCollectorException(Class<? extends ITestData> testDataClass) {
		super("No collector was found that can produce data of the type \""
				+ testDataClass.getSimpleName() + "\"");
	}

}
