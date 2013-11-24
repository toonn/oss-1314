package kuleuven.group6;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * This class is the console view of the deamon. It will give output to the programmer and report ... TODO
 * @author Team 6
 *
 */
public class ConsoleView {

	protected Daemon daemon;
	protected boolean isOutputSuspended = false;
	protected boolean isStopped = false;
	protected Scanner console = null;
	
	
	public ConsoleView(Daemon daemon) {
		this.daemon = daemon;
		this.daemon.addListener(new TestRunListener());
	}
	
	
	protected boolean isOutputSuspended() {
		return this.isOutputSuspended;
	}
	
	protected void suspendOutput() {
		this.isOutputSuspended = true;
		outputln();
	}
	
	protected void resumeOutput() {
		this.isOutputSuspended = false;
	}
	
	
	protected boolean isStopped() {
		return this.isStopped;
	}
	
	
	/**
	 * Method that will be called once the deamon is made. 
	 */
	public void start() {
		console = new Scanner(System.in);
		showInfo();
		askForPolicy();
		
		daemon.start();
		while (!isStopped()) {
			console.nextLine();
			askForCommand();
		}
		daemon.stop();
		
		console.close();
	}
	
	protected void stop() {
		isStopped = true;
	}
	
	
	/**
	 * This will give information on how to use the console view.
	 */
	private void showInfo() {
		System.out.println();
		System.out.println("IMPORTANT:");
		System.out.println("Press enter during testing to access the menu.");
	}
	
	private void askForCommand() {
		suspendOutput();
		
		System.out.println();
		System.out.println("Available commands:");
		System.out.println("\tP : change policy");
		System.out.println("\tC : continue");
		System.out.println("\tN : queue new testrun");
		System.out.println("\tQ : quit");
		System.out.println();
			
		boolean isCommandProcessed = false;
		while (! isCommandProcessed) {
			System.out.print("Enter command: ");
			String command = console.nextLine().toLowerCase();
			if (command.length() == 0)
				continue;
			
			switch (command.charAt(0)) {
				case 'p':
					askForPolicy();
					break;
				case 'c':
					break;
				case 'n':
					daemon.queueNewTestRun();
					break;
				case 'q':
					stop();
					break;
				default:
					continue;
			}
			
			isCommandProcessed = true;
		}
		
		System.out.println();
		
		if (!isStopped())
			resumeOutput();
	}
	
	/**
	 * This will return the policies that are registered. Then the user had to choose a valid policy and 
	 * this will be the active policy.
	 * 
	 */
	private void askForPolicy() {
		// printing out the registered policies
		System.out.println();
		System.out.println("Available policies:");
		List<String> policies = new ArrayList<>(daemon.getRegisteredPolicies());
		for (int i = 1; i <= policies.size(); i++) {
			System.out.println("\t" + i + " : " + policies.get(i - 1));
		}
		
		// Here you can choose which policy to run.
		boolean isCommandProcessed = false;
		while (! isCommandProcessed) {
			System.out.print("Enter the new policy number: ");
			String policyNumberString = console.nextLine();
			try {
				int policyNumber = Integer.parseInt(policyNumberString);
				if (policyNumber < 1 || policyNumber > policies.size()) {
					System.out.println("The number has to be greater then zero or smaller then " + policies.size());
					continue;
				}
				
				// Active policy is chosen correctly and the process will continue
				daemon.setActivePolicy(policies.get(policyNumber - 1));
				isCommandProcessed = true;
			} catch (NumberFormatException e) {
				System.out.println("The number you were providing was not a number. Please give a valid number");
			}
		}
		
		System.out.println();
	}
	
	protected void outputln() {
		outputln("");
	}
	protected void outputln(String message) {
		if (isOutputSuspended())
			return;
		
		System.out.println(message);
	}
	
	protected void output(String message) {
		if (isOutputSuspended())
			return;
		
		System.out.print(message);
	}
	
	protected class TestRunListener extends RunListener {

		protected boolean currentFailed = false;
		
		@Override
		public void testRunStarted(Description description) throws Exception {
			String now = DateFormat.getDateTimeInstance().format(new Date());
			String line = "*                                       *";
			String nowLine = replaceStringAt(line, "at " + now, 6);
			
			outputln();
			outputln("*****************************************");
			outputln("* New testrun started                   *");
			outputln(nowLine);
			outputln("*****************************************");
			outputln();
		}

		@Override
		public void testRunFinished(Result result) throws Exception {
			String line = "*                                       *";
			
			String results = result.getFailureCount() + "/" + result.getRunCount() + " tests failed";
			String resultsLine = replaceStringAt(line, results, 6);
			
			String now = DateFormat.getDateTimeInstance().format(new Date());
			String nowLine = replaceStringAt(line, "at " + now, 6);
			
			outputln();
			outputln("*****************************************");
			outputln("* Testrun finished                      *");
			outputln(nowLine);
			outputln(resultsLine);
			outputln("*****************************************");
			outputln();
		}
		
		private String replaceStringAt(String original, String replace, int index) {
			if (index + replace.length() > original.length())
				throw new IllegalArgumentException();
			
			return 
					original.substring(0, index) + 
					replace + 
					original.substring(index + replace.length());
		}

		@Override
		public void testStarted(Description description) throws Exception {
			currentFailed = false;
		}

		@Override
		public void testFinished(Description description) throws Exception {
			if (currentFailed) {
				output("[FAIL] ");
			} else {
				output("[ OK ] ");
			}
			
			if (description.isTest()) {
				printTestDescription(description);
			} else {
				printSuiteDescription(description);
			}
			outputln();
		}
		
		protected void printTestDescription(Description testDescription) {
			String fullClassName = testDescription.getClassName();
			int indexOfClassSeparator = fullClassName.lastIndexOf('.');
			String className = fullClassName.substring(indexOfClassSeparator + 1);
			String packageName = fullClassName.substring(0, indexOfClassSeparator);
			outputln(testDescription.getMethodName() + " - in " + className);
			outputln("           (in package " + packageName + ")");
		}
		
		protected void printSuiteDescription(Description suiteDescription) {
			String fullClassName = suiteDescription.getClassName();
			int indexOfClassSeparator = fullClassName.lastIndexOf('.');
			String className = fullClassName.substring(indexOfClassSeparator + 1);
			String packageName = fullClassName.substring(0, indexOfClassSeparator);
			outputln(className);
			outputln("           (in package " + packageName + ")");
		}
		

		@Override
		public void testFailure(Failure failure) throws Exception {
			currentFailed = true;
		}
		
	}
	
}
