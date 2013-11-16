package kuleuven.group6.testcharacteristics;

import org.junit.runner.Description;

public class AbstractTestCharacteristic<ValueT> implements ITestCharacteristic<ValueT> {

	protected Description testDescription;
	protected ValueT value;
	
	protected AbstractTestCharacteristic(Description testDescription, ValueT value) {
		this.testDescription = testDescription;
		this.value = value;
	}
	
	public Description getTestDescription() {
		return this.testDescription;
	}

	public ValueT getValue() {
		return value;
	}

}
