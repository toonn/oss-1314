package kuleuven.group6;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

/**
 * A FlattenedRequest flattens the test hierarchy of a given Request by putting all the leaves at 
 * the same, single level. This can be useful to allow an ordering of test methods across the levels
 * and nodes of the original hierarchy.
 * 
 * @author Team 6
 *
 */
public class FlattenedRequest extends Request {

	protected final Runner runner;
	
	protected FlattenedRequest(Runner runner) {
		this.runner = runner;
	}
	
	
	public static FlattenedRequest flatten(Request originalRequest) {
		Description rootDescription = originalRequest.getRunner().getDescription();
		List<Description> leafDescriptions = findLeaves(rootDescription);
		Runner runner = createRunnerForLeafDescriptions(leafDescriptions);
		return new FlattenedRequest(runner);
	}
	
	private static Runner createRunnerForLeafDescriptions(List<Description> leafDescriptions) {
		List<Runner> leafRunners = new ArrayList<>();
		for (Description leaf : leafDescriptions) {
			Runner runner = new MethodRunner(leaf.getTestClass(), leaf.getMethodName());
			leafRunners.add(runner);
		}
		
		try {
			return new Suite(null, leafRunners) { };
		} catch (InitializationError e) {
			return new ErrorReportingRunner(null, e);
		}
	}
	
	private static List<Description> findLeaves(Description rootDescription) {
		List<Description> leafDescriptions = new ArrayList<>();
		findLeaves(rootDescription, leafDescriptions);
		return leafDescriptions;
	}
	
	private static void findLeaves(Description description, List<Description> leafDescriptions) {
		if (description.isTest()) {
			leafDescriptions.add(description);
		} else {
			for (Description child : description.getChildren()) {
				findLeaves(child, leafDescriptions);
			}
		}
	}
	
	
	@Override
	public Runner getRunner() {
		return runner;
	}

}
