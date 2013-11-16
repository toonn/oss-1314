package kuleuven.group6.testcharacteristics;

import java.util.Date;

import org.junit.runner.Description;

public class LastDependencyChangeDate extends AbstractTestCharacteristic<Date> 
	implements ITestStatistic<Date> {

	public LastDependencyChangeDate(Description testDescription, Date lastDependencyChangeDate) {
		super(testDescription, lastDependencyChangeDate);
	}

}
