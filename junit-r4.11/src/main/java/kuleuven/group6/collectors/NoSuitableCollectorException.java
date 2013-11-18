package kuleuven.group6.collectors;

import kuleuven.group6.testcharacteristics.ITestData;

public class NoSuitableCollectorException extends RuntimeException {

	private static final long serialVersionUID= 1L;
	
	public NoSuitableCollectorException(ITestData testData) {
		super("No collector was found that can produce data of the type \"" + testData.getClass().getSimpleName() + "\"");
	}
	
}
