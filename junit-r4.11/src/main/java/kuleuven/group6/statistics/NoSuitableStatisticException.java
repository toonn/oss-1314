package kuleuven.group6.statistics;

import kuleuven.group6.statistics.teststatistics.ITestStatistic;

/**
 * IStatisticProvider's should throw this exception when they can't find a
 * Statistic for a requested ITestStatistic type.
 */
public class NoSuitableStatisticException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NoSuitableStatisticException(Class<? extends ITestStatistic> testStatisticClass) {
		super("No statistic was found for the type \"" + testStatisticClass.getSimpleName() + "\"");
	}
	
}
