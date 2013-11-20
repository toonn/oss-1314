package kuleuven.group6.tests.testsubject.tests;

import static org.junit.Assert.*;
import org.junit.Test;

public class BasicTest {

	public static int getNbTests() {
		return 6;
	}
	
	
	@Test
	public void passingTestA() {
	}
	
	@Test
	public void passingTestB() {
	}
	
	@Test
	public void passingTestC() {
	}
	
	@Test
	public void failingTestA() {
		fail();
	}
	
	@Test
	public void failingTestB() {
		fail();
	}
	
	@Test
	public void failingTestC() {
		fail();
	}
	
}
