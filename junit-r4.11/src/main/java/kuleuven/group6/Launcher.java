package kuleuven.group6;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.junit.runner.notification.RunNotifier;

import kuleuven.group6.Daemon;
import kuleuven.group6.RunNotificationSubscriber;
import kuleuven.group6.TestRunCreator;
import kuleuven.group6.collectors.DataEnroller;
import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.policies.ChangedCodeFirst;
import kuleuven.group6.policies.DistinctFailureFirst;
import kuleuven.group6.policies.FrequentFailureFirst;
import kuleuven.group6.policies.IPolicy;
import kuleuven.group6.policies.LastFailureFirst;
import kuleuven.group6.statistics.IStatisticProvider;
import kuleuven.group6.statistics.StatisticProvider;

public class Launcher {
	protected String rootSuiteClassName;
	protected File codeDirectory, testDirectory;
	protected TestRunCreator testRunCreator;
	protected RunNotifier runNotifier;

	protected IDataEnroller dataEnroller;
	protected IStatisticProvider statisticProvider;
	protected Map<String, IPolicy> registeredPolicies;
	protected String activePolicyName;
	
	protected Daemon daemon;

	public Launcher(String rootSuiteClassName, File codeDirectory,
			File testDirectory) {
		if (!codeDirectory.exists() || !codeDirectory.isDirectory()
				|| !testDirectory.exists() || !testDirectory.isDirectory()) {
			throw new IllegalArgumentException(
					"The given directories are not valid.");
		}
		this.rootSuiteClassName = rootSuiteClassName;
		this.codeDirectory = codeDirectory;
		this.testDirectory = testDirectory;

		this.runNotifier = new RunNotifier();
		RunNotificationSubscriber runNotificationSubscriber = new RunNotificationSubscriber(
				runNotifier);

		URL[] paths;
		try {
			paths = new URL[] { codeDirectory.toURI().toURL(),
					testDirectory.toURI().toURL() };
		} catch (MalformedURLException e) {
			throw new AssertionError();
		}
		this.testRunCreator = new TestRunCreator(rootSuiteClassName, paths,
				runNotifier);

		this.dataEnroller = DataEnroller.createConfiguredDataEnroller(
				runNotificationSubscriber, rootSuiteClassName, testDirectory,
				codeDirectory);
		this.statisticProvider = StatisticProvider
				.createConfiguredStatisticProvider(dataEnroller,
						runNotificationSubscriber);
		
		this.daemon = new Daemon(getActivePolicy(), dataEnroller);
	}

	public static Launcher createConfiguredLauncher(String rootSuiteClassName,
			File codeDirectory, File testDirectory) {
		Launcher launcher = new Launcher(rootSuiteClassName, codeDirectory,
				testDirectory);
		launcher.configurePolicies();
		return launcher;
	}

	private void configurePolicies() {
		registeredPolicies.put("LastFailureFirst", new LastFailureFirst(
				statisticProvider));
		registeredPolicies.put("FrequentFailureFirst",
				new FrequentFailureFirst(statisticProvider));
		registeredPolicies.put("DistinctFailureFirst",
				new DistinctFailureFirst(statisticProvider));
		registeredPolicies.put("ChangedCodeFirst", new ChangedCodeFirst(
				statisticProvider));
	}

	public Set<String> getRegisteredPolicies() {
		return registeredPolicies.keySet();
	}

	public IPolicy getActivePolicy() {
		if (activePolicyName == null)
			throw new RuntimeException("An active policy must be selected.");

		return registeredPolicies.get(activePolicyName);
	}

	public void setActivePolicy(String policyName) {
		if (!registeredPolicies.containsKey(policyName))
			throw new IllegalArgumentException(
					"A policy with the given name does not exist.");

		this.activePolicyName = policyName;

		daemon.queueNewTestRun();
	}

	/**
	 * This is the main method. The first argument is the suite of tests, the
	 * second is the directory were the code is that will be tested and the
	 * third argument is the directory where the tests that will be executed are
	 * situated.
	 */
	public static void main(String[] args) {
		// This will check if there are three arguments given. It will give an
		// error and a description of how to use it
		// if there are not enough or to many arguments.
		if (args.length != 3) {
			System.err.println("Invalid number of arguments.");
			System.err
					.println("Example usage: "
							+ "java kuleuven.group6.Daemon <suite class> <code directory> <test directory>");
			return;
		}

		// This will check if the second argument is a valid directory for the
		// code.
		File codeDirectory = new File(args[1]);
		if (!codeDirectory.exists() || !codeDirectory.isDirectory()) {
			System.err.println("Not a valid code directory.");
			return;
		}

		// This will check if the third argument is a valid directory for the
		// tests.
		File testDirectory = new File(args[2]);
		if (!testDirectory.exists() || !testDirectory.isDirectory()) {
			System.err.println("Not a valid test directory.");
			return;
		}

		// If the arguments are correct, the deamon class will be created and
		// given the three parameters and a console
		// of his deamon will be started.
		Launcher launcher = Launcher.createConfiguredLauncher(args[0], codeDirectory,
				testDirectory);
		new ConsoleView(launcher).start();
	}
}
