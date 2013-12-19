package kuleuven.group6.tests;

import static org.junit.Assert.*;
import kuleuven.group6.testrun.FlattenedRequest;
import kuleuven.group6.tests.testsubject.tests.BasicAndFailingDummySuite;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Request;

/**
 * 
 * @author Team 6
 *
 */
public class FlattenedRequestTest {
	
	@Test
	public void testFlatten() {
		Request request = Request.aClass(BasicAndFailingDummySuite.class);
		FlattenedRequest flattenedRequest = FlattenedRequest.flatten(request);
		Description runnerDescription = flattenedRequest.getRunner().getDescription();
		
		assertTrue(runnerDescription.isSuite());
		for (Description child : runnerDescription.getChildren()) {
			assertTrue(child.isTest());
		}
		
		assertEquals(BasicAndFailingDummySuite.getNbTests(), runnerDescription.getChildren().size());
	}

}
