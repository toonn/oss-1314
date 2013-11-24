package kuleuven.group6;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import kuleuven.group6.collectors.DataCollectedListener;
import kuleuven.group6.collectors.DataEnroller;
import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.policies.*;
import kuleuven.group6.statistics.IStatisticProvider;
import kuleuven.group6.statistics.StatisticProvider;
import kuleuven.group6.testcharacteristics.testdatas.CodeChange;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

/**
 * 
 * @author Team 6
 *
 */
public class Daemon {

	protected String rootSuiteClassName;
	protected File codeDirectory, testDirectory;
	protected RunNotifier runNotifier;

	protected IDataEnroller dataEnroller;
	protected IStatisticProvider statisticProvider;
	protected Map<String, IPolicy> registeredPolicies = new HashMap<>();

	protected String activePolicyName = null;
	protected Thread runThread = null;
	protected Semaphore mayRunSemaphore = null;
	protected DataCollectedListener<CodeChange> fileChangedListener;

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
		
		this.fileChangedListener = new DataCollectedListener<CodeChange>() {
			@Override
			public void dataCollected(CodeChange data) {
				onFileChanged();
			}
		};
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
		
		queueNewTestRun();
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
	
	/**
	 * This method will be called once a policy has been chosen through the consoleView.
	 * 
	 * 
	 */
	public void start() {
		if (isRunning())
			return;
		
		dataEnroller.subscribe(CodeChange.class, this.fileChangedListener);
		mayRunSemaphore = new Semaphore(1);
		
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
		mayRunSemaphore = null;
		dataEnroller.unsubscribe(CodeChange.class, this.fileChangedListener);
		
		// TODO this line will cause failing of a start() after a stop()
		dataEnroller.close();
	}
	
	private void onFileChanged() {
		ensureQueuedTestRun();
	}
	
	/**
	 * Ensure that a testrun is queued, by queuing one if no testrun is currently running.
	 */
	public void ensureQueuedTestRun() {
		if (!isRunning())
			return;
		if (mayRunSemaphore.availablePermits() != 0)
			return;
		
		mayRunSemaphore.release();
	}
	
	/**
	 * Queue a new testrun.
	 */
	public void queueNewTestRun() {
		if (!isRunning())
			return;
		
		mayRunSemaphore.release();
	}
	
	/**
	 * TODO hier ook een description toevoegen
	 */
	protected void startCore() {
		boolean keepRunning = true;
		while (keepRunning) {			
			try {
				mayRunSemaphore.acquire();
				// In case of grouped file changes, let the system 
				// stabilize a bit before really starting
				Thread.sleep(500);
				doTestRun();
			} catch (InterruptedException e) {
				keepRunning = false;
			} catch (Throwable e) {
				// Catch all throwables, since half written class files can 
				// cause all different kinds of faults. 
				System.err.println("[ERROR] A problem occured while starting a testrun.");
				System.err.println("        Make sure all class files are available.");
				keepRunning = true;
			}
		}
	}


	/**
	 *  //TODO Uitleggen. heb misschien niet goed begrepen 
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */

	protected void doTestRun() throws Exception {
		URL[] paths = { 
			codeDirectory.toURI().toURL(), 
			testDirectory.toURI().toURL()
			// TODO waarom hebben we hier een verschil nu ook alweer tussen code en test?
		};
		URLClassLoader classLoader = URLClassLoader.newInstance(paths);
		try {
			Class<?> rootSuiteClass = Class.forName(rootSuiteClassName, true, classLoader);
			Request request = createNewRequest(rootSuiteClass);
			Runner runner = request.getRunner();
			
			runNotifier.fireTestRunStarted(runner.getDescription());
			//TODO fireTestRunStarted ? do not invoke? 
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
	
	/**
	 * HOW to run it?? 
	 * 
	 * This is the main method. The first argument is the suite of tests, the second is the directory were the code is that will
	 * be tested and the third argument is the directory where the tests that will be executed are situated. 
	 *  TODO
	 * @param args TODO Waarom niet het eerste argument testen?
	 */
	public static void main(String[] args) {
		// This will check if there are three arguments given. It will give an error and a description of how to use it
		// if there are not enough or to many arguments.
		if (args.length != 3) {
			System.err.println("Invalid number of arguments.");
			System.err.println("Example usage: " + 
					"java kuleuven.group6.Daemon <suite class> <code directory> <test directory>");
			return;
		}
		
		// This will check if the second argument is a valid directory for the code.
		File codeDirectory = new File(args[1]);
		if (!codeDirectory.exists() || !codeDirectory.isDirectory()) {
			System.err.println("Not a valid code directory.");
			return;
		}
		
		// This will check if the third argument is a valid directory for the tests.
		File testDirectory = new File(args[2]);
		if (!testDirectory.exists() || !testDirectory.isDirectory()) {
			System.err.println("Not a valid test directory.");
			return;
		}
		
		// If the arguments are correct, the deamon class will be created and given the three parameters and a console 
		// of his deamon will be started.
		Daemon daemon = Daemon.createConfiguredDaemon(args[0], codeDirectory, testDirectory);
		new ConsoleView(daemon).start();
	}
	
}
