package kuleuven.group6.testcharacteristics;

import kuleuven.group6.collectors.MethodCallTrace;
import org.junit.runner.Description;

public class MethodCalls extends AbstractTestCharacteristic<MethodCallTrace>
	implements ITestData<MethodCallTrace> {

	public MethodCalls(Description testDescription, MethodCallTrace methodCallTrace) {
		super(testDescription, methodCallTrace);
	}
	
}
