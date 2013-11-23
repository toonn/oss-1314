package kuleuven.group6;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import kuleuven.group6.collectors.DataEnroller;
import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.policies.*;
import kuleuven.group6.statistics.IStatisticProvider;
import kuleuven.group6.statistics.StatisticProvider;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class Daemon {

	protected String rootSuiteClassName;
	protected File codeDirectory, testDirectory;
	protected RunNotifier runNotifier;

	protected IDataEnroller dataEnroller;
	protected IStatisticProvider statisticProvider;
	protected Map<String, IPolicy> registeredPolicies = new HashMap<>();

	protected String activePolicyName = null;
	protected Thread runThread = null;

	protected Daemon(String rootSuiteClassName, File codeDirectory, File testDirectory) {
		if (! codeDirectory.exists() || ! codeDirectory.isDirectory() ||
				! testDirectory.exists() || ! testDirectory.isDirectory()) {
			throw new IllegalArgumentException("The given directories are not valid.");
		}
		
		this.rootSuiteClassName = rootSuiteClassName;
		this.codeDirectory = codeDirectory;
		this.testDirectory = testDirectory;

		this.runNotifier = new RunNotifier();
		RunNotificationSubscriber runNotificationSubscriber = 
				new RunNotificationSubscriber(runNotifier);
		this.dataEnroller = DataEnroller.createConfiguredDataEnroller(
				runNotificationSubscriber, rootSuiteClassName, testDirectory, codeDirectory);
		this.statisticProvider = StatisticProvider.createConfiguredStatisticProvider(
				dataEnroller, runNotificationSubscriber);
	}

	public static Daemon createConfiguredDaemon(
			String rootSuiteClassName, File codeDirectory, File testDirectory) {
		Daemon daemon = new Daemon(rootSuiteClassName, codeDirectory, testDirectory);
		daemon.configurePolicies();
		return daemon;
	}

	private void configurePolicies() {
		registeredPolicies.put("LastFailureFirst", new LastFailureFirst(statisticProvider));
		registeredPolicies.put("FrequentFailureFirst", new FrequentFailureFirst(statisticProvider));
		registeredPolicies.put("DistinctFailureFirst", new DistinctFailureFirst(statisticProvider));
		registeredPolicies.put("ChangedCodeFirst", new ChangedCodeFirst(statisticProvider));
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
			throw new IllegalArgumentException("A policy with the given name does not exist.");

		activePolicyName = policyName;
	}
	
	
	public void addListener(RunListener listener) {
		runNotifier.addListener(listener);
	}
	
	public void removeListener(RunListener listener) {
		runNotifier.removeListener(listener);
	}
	

	public boolean isRunning() {
		return runThread != null;
	}
	
	public void start() {
		if (isRunning())
			return;
		
		runThread = new Thread(new Runnable() {
			@Override
			public void run() {
				startCore();
			}
		});
		runThread.start();
	}

	public void stop() {
		if (!isRunning())
			return;

		runThread.interrupt();
		runThread = null;
		
		// TODO this line will cause failing of a start() after a stop()
		dataEnroller.close();
	}

	protected void startCore() {
		boolean keepRunning = true;
		while (keepRunning) {
			try {
				doTestRun();
			} catch (Exception e) {
				e.printStackTrace();
				keepRunning = false;
			}
			
			if (Thread.currentThread().isInterrupted())
				keepRunning = false;
		}
	}

	protected void doTestRun() throws ClassNotFoundException, IOException {
		URL[] paths = { 
			codeDirectory.toURI().toURL(), 
			testDirectory.toURI().toURL()
		};
		URLClassLoader classLoader = URLClassLoader.newInstance(paths);
		try {
			Class<?> rootSuiteClass = Class.forName(rootSuiteClassName, true, classLoader);
			Request request = createNewRequest(rootSuiteClass);
			Runner runner = request.getRunner();
			
			runNotifier.fireTestRunStarted(runner.getDescription());
			Result result = new Result();
			RunListener resultListener = result.createListener();
			runNotifier.addFirstListener(resultListener);
			runner.run(runNotifier);
			runNotifier.removeListener(resultListener);
			runNotifier.fireTestRunFinished(result);
		} finally {
			classLoader.close();
		}
	}
	
	public Request createNewRequest(Class<?> rootSuiteClass) {
		Request request = Request.aClass(rootSuiteClass);
		Request flattenedRequest = FlattenedRequest.flatten(request);
		return getActivePolicy().apply(flattenedRequest);
	}
	
	
	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("Invalid number of arguments.");
			System.err.println("Example usage: " + 
					"java kuleuven.group6.Daemon <suite class> <code directory> <test directory>");
			return;
		}
		
		File codeDirectory = new File(args[1]);
		if (!codeDirectory.exists() || !codeDirectory.isDirectory()) {
			System.err.println("Not a valid code directory.");
			return;
		}
		
		File testDirectory = new File(args[2]);
		if (!testDirectory.exists() || !testDirectory.isDirectory()) {
			System.err.println("Not a valid test directory.");
			return;
		}
		
		Daemon daemon = Daemon.createConfiguredDaemon(args[0], codeDirectory, testDirectory);
		new ConsoleView(daemon).start();
	}
	
}
