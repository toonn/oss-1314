package kuleuven.group6;

import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

public class MethodRunner extends Runner {

	protected Runner wrappedRunner;
	protected Description methodDescription;
	
	public MethodRunner(Class<?> clazz, String methodName) {
		wrappedRunner = Request.method(clazz, methodName).getRunner();
	}
	
	@Override
	public Description getDescription() {
		Description testCaseDescription = 
				wrappedRunner.getDescription();
		Description actualMethodDescription = 
				testCaseDescription.getChildren().get(0);
		return actualMethodDescription;
	}

	@Override
	public void run(RunNotifier notifier) {
		wrappedRunner.run(notifier);
	}

}
