package kuleuven.group6.testcharacteristics;

import java.util.Date;

import org.junit.runner.Description;

public class SourceCodeChangeDate extends AbstractTestCharacteristic<Date>
		implements ITestData<Date> {

	public SourceCodeChangeDate(Description testDescription, Date sourceCodeChangeDate) {
		super(testDescription, sourceCodeChangeDate);
	}

}
