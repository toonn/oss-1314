package kuleuven.group6.testcharacteristics;

import org.junit.runner.Description;

public interface ITestCharacteristic<ValueT> {
	
	public Description getTestDescription();
	
	public ValueT getValue();
	
}
