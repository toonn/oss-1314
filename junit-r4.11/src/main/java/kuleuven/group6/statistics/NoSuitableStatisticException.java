package kuleuven.group6.statistics;

import kuleuven.group6.testcharacteristics.ITestStatistic;

/**
 * 
 * @author Team 6
 *
 */
public class NoSuitableStatisticException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NoSuitableStatisticException(Class<? extends ITestStatistic> testStatisticClass) {
		super("No statistic was found for the type \"" + testStatisticClass.getSimpleName() + "\"");
	}
	
}
