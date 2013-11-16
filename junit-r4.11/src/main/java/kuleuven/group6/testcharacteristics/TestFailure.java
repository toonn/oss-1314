package kuleuven.group6.testcharacteristics;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

public class TestFailure extends AbstractTestCharacteristic<Failure>
	implements ITestData<Failure> {

	protected TestFailure(Description testDescription, Failure failure) {
		super(testDescription, failure);
	}

}
