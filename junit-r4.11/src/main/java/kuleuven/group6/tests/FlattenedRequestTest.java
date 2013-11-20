package kuleuven.group6.tests;

import static org.junit.Assert.*;
import kuleuven.group6.FlattenedRequest;
import kuleuven.group6.tests.testsubject.tests.BasicAndFailingDummySuite;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Request;

public class FlattenedRequestTest {
	
	@Test
	public void testFlatten() {
		Request request = Request.aClass(BasicAndFailingDummySuite.class);
		FlattenedRequest flattenedRequest = FlattenedRequest.flatten(request);
		Description runnerDescription = flattenedRequest.getRunner().getDescription();
		
		assertTrue(runnerDescription.isSuite());
		for (Description child : runnerDescription.getChildren()) {
			assertTrue(child.isSuite());
			assertEquals(1, child.getChildren().size());
			assertTrue(child.getChildren().get(0).isTest());
		}
		
		assertEquals(BasicAndFailingDummySuite.getNbTests(), runnerDescription.getChildren().size());
	}

}
