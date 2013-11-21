package kuleuven.group6;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.notification.RunListener;

public class ConsoleView {

	protected Daemon daemon;
	protected boolean isOutputSuspended = false;
	protected boolean isStopped = false;
	
	public ConsoleView(Daemon daemon) {
		this.daemon = daemon;
		this.daemon.addListener(new TestRunListener());
	}
	
	
	protected boolean isOutputSuspended() {
		return this.isOutputSuspended;
	}
	
	protected void suspendOutput() {
		this.isOutputSuspended = true;
	}
	
	protected void resumeOutput() {
		this.isOutputSuspended = false;
	}
	
	
	protected boolean isStopped() {
		return this.isStopped;
	}
	
	
	public void start() {
		askForPolicy();
		
		daemon.start();
		while (!isStopped()) {
			System.console().readLine();
			askForCommand();
		}
		daemon.stop();
	}
	
	protected void stop() {
		isStopped = true;
	}
	
	
	private void askForCommand() {
		suspendOutput();
		
		System.out.println();
		System.out.println("Available commands:");
		System.out.println("\tP : change policy");
		System.out.println("\tC : continue");
		System.out.println("\tQ : quit");
		System.out.println();
			
		boolean isCommandProcessed = false;
		while (! isCommandProcessed) {
			String command = System.console().readLine("Enter command: ").toLowerCase();
			if (command.length() == 0)
				continue;
			
			switch (command.charAt(0)) {
				case 'p':
					askForPolicy();
					break;
				case 'c':
					break;
				case 'q':
					stop();
					break;
				default:
					continue;
			}
			
			isCommandProcessed = true;
		}
		
		resumeOutput();
	}
	
	private void askForPolicy() {
		System.out.println();
		System.out.println("Available policies:");
		List<String> policies = new ArrayList<>(daemon.getRegisteredPolicies());
		for (int i = 1; i <= policies.size(); i++) {
			System.out.println("\t" + i + " : " + policies.get(i - 1));
		}
		
		boolean isCommandProcessed = false;
		while (! isCommandProcessed) {
			String policyNumberString = System.console().readLine("Enter the new policy number: ");
			try {
				int policyNumber = Integer.parseInt(policyNumberString);
				if (policyNumber < 1 || policyNumber > policies.size())
					continue;
				
				daemon.setActivePolicy(policies.get(policyNumber));
				isCommandProcessed = true;
			} catch (NumberFormatException e) {
				// Ask again
			}
		}
	}
	
	
	protected class TestRunListener extends RunListener {
		
	}
	
}
