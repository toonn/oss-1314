package kuleuven.group6.tests.testsubject.tests;

import kuleuven.group6.tests.testsubject.source.ExceptionThrower;
import org.junit.Test;

public class DistinctFailingTest {

	@Test
	public void test1FailingOnA() {
		new ExceptionThrower().failA();
	}
	
	@Test
	public void test2FailingOnA() {
		new ExceptionThrower().failA();
	}
	
	@Test
	public void test3FailingOnA() {
		new ExceptionThrower().failA();
	}
	
	
	@Test
	public void test1FailingOnB() {
		new ExceptionThrower().failB();
	}
	
	@Test
	public void test2FailingOnB() {
		new ExceptionThrower().failB();
	}
	
	@Test
	public void test3FailingOnB() {
		new ExceptionThrower().failB();
	}
	
	
	@Test
	public void test1FailingOnC() {
		new ExceptionThrower().failC();
	}
	
	@Test
	public void test2FailingOnC() {
		new ExceptionThrower().failC();
	}
	
	@Test
	public void test3FailingOnC() {
		new ExceptionThrower().failC();
	}
	
}
