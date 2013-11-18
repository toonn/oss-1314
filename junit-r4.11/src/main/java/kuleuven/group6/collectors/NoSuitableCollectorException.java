package kuleuven.group6.collectors;

import kuleuven.group6.testcharacteristics.ITestData;

/**
 * 
 * @author Team 6
 *
 */
public class NoSuitableCollectorException extends RuntimeException {

	private static final long serialVersionUID= 1L;
	
	public NoSuitableCollectorException(Class<? extends ITestData> testDataClass) {
		super("No collector was found that can produce data of the type \"" + testDataClass.getSimpleName() + "\"");
	}
	
}
