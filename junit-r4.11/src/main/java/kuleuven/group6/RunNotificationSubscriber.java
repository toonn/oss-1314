package kuleuven.group6;

import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class RunNotificationSubscriber {

	protected RunNotifier runNotifier;
	
	public RunNotificationSubscriber(RunNotifier runNotifier) {
		this.runNotifier = runNotifier;
	}
	
	public void addListener(RunListener runListener) {
		runNotifier.addListener(runListener);
	}
	
	public void removeListener(RunListener runListener) {
		runNotifier.removeListener(runListener);
	}
	
}
