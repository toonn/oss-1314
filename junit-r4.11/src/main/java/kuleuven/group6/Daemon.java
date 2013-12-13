package kuleuven.group6;

import java.util.concurrent.Semaphore;
import kuleuven.group6.collectors.IDataCollectedListener;
import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.policies.*;
import kuleuven.group6.testcharacteristics.testdatas.CodeChange;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class Daemon {
	protected RunNotifier runNotifier;
	protected TestRunCreator testRunCreator;
	protected IDataEnroller dataEnroller;

	protected IPolicy activePolicy;
	protected Thread runThread = null;
	protected Semaphore mayRunSemaphore = null;
	protected IDataCollectedListener<CodeChange> fileChangedListener;

	public Daemon(IPolicy activePolicy, IDataEnroller dataEnroller) {
		this.activePolicy = activePolicy;
		this.fileChangedListener = new IDataCollectedListener<CodeChange>() {
			@Override
			public void dataCollected(CodeChange data) {
				onFileChanged();
			}
		};
	}

	public IPolicy getActivePolicy() {
		return activePolicy;
	}

	public void setActivePolicy(IPolicy policy) {
		this.activePolicy = policy;
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
	 * This method will be called once a policy has been chosen through the
	 * consoleView.
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

	protected void startCore() {
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
