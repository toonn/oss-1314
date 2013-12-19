package kuleuven.group6.testrun;

import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class TestRun {

	protected Request request;
	protected RunNotifier runNotifier;

	public TestRun(Request request, RunNotifier notifier) {
		this.request = request;
		this.runNotifier = notifier;
	}

	public Result run() {
		Runner runner = request.getRunner();

		runNotifier.fireTestRunStarted(runner.getDescription());

		Result result = new Result();
		RunListener resultListener = result.createListener();
		runNotifier.addFirstListener(resultListener);
		runner.run(runNotifier);
		runNotifier.removeListener(resultListener);
		runNotifier.fireTestRunFinished(result);
		
		return result;
	}

}
