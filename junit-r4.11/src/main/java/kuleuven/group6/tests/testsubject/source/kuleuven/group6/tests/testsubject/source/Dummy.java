package kuleuven.group6.tests.testsubject.source;

/**
 * A dummy class, implementing a few dummy methods. 
 * 
 * @author team 6
 *
 */
public class Dummy {

	/**
	 * Does nothing
	 */
	public void dummyMethodA() {
	
	}
	
	/**
	 * Does nothing
	 */
	public void dummyMethodB() {

	}
	
	/**
	 * Calls dummyMethodA() and dummyMethodB() in this order.
	 */
	public void nestingMethodAB() {
		dummyMethodA();
		dummyMethodB();
	}
}
