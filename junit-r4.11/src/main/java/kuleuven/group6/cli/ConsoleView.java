package kuleuven.group6.cli;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import kuleuven.group6.Launcher;
import kuleuven.group6.policies.CompositeSortingPolicy;
import kuleuven.group6.policies.IPolicy;
import kuleuven.group6.policies.SortingPolicy;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * A console interface of the system.
 */
public class ConsoleView {

	protected Launcher launcher;
	protected boolean isOutputSuspended = false;
	protected boolean isStopped = false;
	protected Scanner console = null;
	
	protected ConsoleMenu optionsMenu;
	
	public ConsoleView(Launcher launcher) {
		this.launcher = launcher;
		this.launcher.addListener(new TestRunListener());
		initializeOptionsMenu();
	}
	
	protected void initializeOptionsMenu() {
		optionsMenu = new ConsoleMenu("Available commands:", "Enter a choice: ");
		optionsMenu.addMenuAction(new ConsoleMenuAction("P", "Change the active policy") {
			@Override
			protected void execute() {
				askForPolicy();
			}
		});
		optionsMenu.addMenuAction(new ConsoleMenuAction("C", "Continue running") {
			@Override
			protected void execute() { }
		});
		optionsMenu.addMenuAction(new ConsoleMenuAction("N", "Queue a new testrun") {
			@Override
			protected void execute() {
				launcher.queueNewTestRun();
			}
		});
		optionsMenu.addMenuAction(new ConsoleMenuAction("Q", "Quit") {
			@Override
			protected void execute() {
				stop();
			}
		});
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
	 * Method that will be called once the Deamon is created. It will also stop the Deamon when the program is done.
	 */
	public void start() {
		console = new Scanner(System.in);
		showInfo();
		askForPolicy();
		
		launcher.start();
		while (!isStopped()) {
			console.nextLine();
			askForCommand();
		}
		launcher.stop();
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
		System.out.println("Press <ENTER> during testing to access the menu.");
	}
	
	private void askForCommand() {
		suspendOutput();
		optionsMenu.show();
		if (!isStopped())
			resumeOutput();
	}
	
	/**
	 * Ask the user to choose a new active policy.
	 */
	private void askForPolicy() {
		Map<String, IPolicy> policies = launcher.getRegisteredPolicies();
		ConsoleMenu policyMenu = new ConsoleMenu("Available policies:", "Enter a choice: ");
		int lastItem = 0;
		for (String policyName : policies.keySet()) {
			String commandString = Integer.toString(++lastItem);
			policyMenu.addMenuAction(new ChangePolicyMenuAction(commandString, policyName, launcher));
		}
		String commandString = Integer.toString(++lastItem);
		policyMenu.addMenuAction(new ConsoleMenuAction(commandString, "Compose a new sorting policy...") {
			@Override
			protected void execute() {
				String policyName;
				do {
					System.out.print("Enter a name for the new composite sorting policy: ");
					policyName = console.nextLine();
				} while (policyName == null || policyName.isEmpty());
				
				IPolicy policy = askForCompositePolicy();
				launcher.setActivePolicy(policyName, policy);
			}
		});
	
		policyMenu.show();
	}
	
	private CompositeSortingPolicy askForCompositePolicy() {
		final CompositeSortingPolicyBuilder policyBuilder = new CompositeSortingPolicyBuilder();
		Map<String, IPolicy> policies = launcher.getRegisteredPolicies();
		int lastActionNb = 0;
		ConsoleMenu menu = new ConsoleMenu("Choose an action for the new composite policy:", "Enter a choice: ");
		for (String policyName : policies.keySet()) {
			final IPolicy policy = policies.get(policyName);
			if (! (policy instanceof SortingPolicy))
				continue;
			
			menu.addMenuAction(createAddPolicyMenuAction(++lastActionNb, policyName, (SortingPolicy)policy, policyBuilder));
		}
		menu.addMenuAction(new ConsoleMenuAction(Integer.toString(++lastActionNb), "Add a new composite policy...") {
			@Override
			protected void execute() {
				policyBuilder.addChildPolicy(askForCompositePolicy());
			}
		});
		
		final boolean[] keepAsking = { true };
		menu.addMenuAction(new ConsoleMenuAction(Integer.toString(++lastActionNb), "Stop adding and select the new composite policy") {
			@Override
			protected void execute() {
				keepAsking[0] = false;
			}
		});
		
		while (keepAsking[0]) {
			menu.show();
		}
		
		return policyBuilder.getCompositeSortingPolicy();
	}
	
	private ConsoleMenuAction createAddPolicyMenuAction(int actionNb, 
			final String policyName, final SortingPolicy policy, final CompositeSortingPolicyBuilder policyBuilder) {
		String title = "Add the \"" + policyName + "\" policy";
		return new ConsoleMenuAction(Integer.toString(actionNb), title) {
			@Override
			protected void execute() {
				policyBuilder.addChildPolicy(policy);
			}
		};
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
