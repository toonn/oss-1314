package kuleuven.group6;

import java.util.concurrent.Semaphore;

import kuleuven.group6.collectors.IDataCollectedListener;
import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.collectors.testdatas.CodeChange;
import kuleuven.group6.policies.*;
import kuleuven.group6.testrun.TestRun;

/**
 * Daemon will execute the testruns. It can make use of different policies to 
 * manipulate the way tests are run. For example, a SortingPolicy will make
 * sure the tests are run in a specific order.
 * 
 * It is required that users use the automatic building feature of their IDE, 
 * so the daemon can pick up the changes in the compiled class files. 
 * Testruns are always executed completely and all tests are executed.
 */
public class Daemon {
	protected TestRunCreator testRunCreator;
	protected IDataEnroller dataEnroller;

	protected IPolicy activePolicy = null;
	protected Thread runThread = null;
	protected Semaphore mayRunSemaphore = null;
	protected IDataCollectedListener<CodeChange> fileChangedListener;

	public Daemon(TestRunCreator testRunCreator, IDataEnroller dataEnroller) {
		this.testRunCreator = testRunCreator;
		this.dataEnroller = dataEnroller;
		this.fileChangedListener = new IDataCollectedListener<CodeChange>() {
			@Override
			public void dataCollected(CodeChange data) {
				onFileChanged();
			}
		};
	}

	public boolean hasActivePolicy() {
		return activePolicy != null;
	}
	
	public IPolicy getActivePolicy() {
		return activePolicy;
	}

	public void setActivePolicy(IPolicy policy) {
		this.activePolicy = policy;
	}
	

	public boolean isRunning() {
		return runThread != null;
	}

	
	public void start() {
		if (isRunning())
			return;
		if (!hasActivePolicy())
			throw new IllegalStateException(
					"An active policy must be set before the daemon can be started.");

		dataEnroller.subscribe(CodeChange.class, this.fileChangedListener);
		mayRunSemaphore = new Semaphore(1);

		runThread = new Thread(new Runnable() {
			@Override
			public void run() {
				startRunning();
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
	 * Ensure that a testrun is queued, by queuing one if no testrun is
	 * currently running.
	 */
	public void ensureQueuedTestRun() {
		if (!isRunning())
			return;
		if (mayRunSemaphore.availablePermits() != 0)
			return;

		mayRunSemaphore.release();
	}

	public void queueNewTestRun() {
		if (!isRunning())
			return;

		mayRunSemaphore.release();
	}

	protected void startRunning() {
		boolean keepRunning = true;
		while (keepRunning) {
			try {
				mayRunSemaphore.acquire();
				// In case of grouped file changes, let the system
				// stabilize a bit before really starting
				Thread.sleep(500);
				TestRun testRun = testRunCreator.create(getActivePolicy());
				testRun.run();
			} catch (InterruptedException e) {
				keepRunning = false;
			} catch (Throwable e) {
				// Catch all throwables, since half written class files can
				// cause all different kinds of faults.
				System.err
						.println("[ERROR] A problem occured while starting a testrun.");
				System.err
						.println("        Make sure all class files are available.");
				keepRunning = true;
			}
		}
	}

}
