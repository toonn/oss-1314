package kuleuven.group6.tests.testsubject.source;

public class FailingDummy {

	public boolean trueButActuallyFalse() {
		return false;
	}
	
	public void methodThrowingRuntimeException() {
		throw new RuntimeException();
	}
	
}
