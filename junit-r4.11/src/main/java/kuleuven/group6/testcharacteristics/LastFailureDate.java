package kuleuven.group6.testcharacteristics;

import java.util.Date;

import org.junit.runner.Description;

public class LastFailureDate extends AbstractTestCharacteristic<Date> 
	implements ITestStatistic<Date> {

	protected LastFailureDate(Description testDescription, Date lastFailureDate) {
		super(testDescription, lastFailureDate);
	}

}
