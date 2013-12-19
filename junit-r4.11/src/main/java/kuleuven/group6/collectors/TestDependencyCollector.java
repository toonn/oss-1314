package kuleuven.group6.collectors;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kuleuven.group6.testcharacteristics.testdatas.ITestData;
import kuleuven.group6.testcharacteristics.testdatas.MethodCalls;
import kuleuven.group6.testrun.RunNotificationSubscriber;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import be.kuleuven.cs.ossrewriter.Monitor;
import be.kuleuven.cs.ossrewriter.MonitorEntrypoint;
import be.kuleuven.cs.ossrewriter.OSSRewriter;
import be.kuleuven.cs.ossrewriter.Predicate;

/**
 * A data collector that collects all methods called by each test.
 * 
 * This collector assumes that tests are run sequentially. If they are run in
 * parallel, the data collected by this collector may be incorrect.
 * 
 */
public class TestDependencyCollector extends DataCollector<MethodCalls> {

	protected RunNotificationSubscriber runNotificationSubscriber;
	protected RunListener testListener;
	protected List<String> currentMethodCalls = new ArrayList<>();
	protected MethodCallMonitor methodCallMonitor = null;

	/**
	 * Construct a new data collector that will collect all methods called by
	 * each test.
	 * 
	 * @param sourceDirectory
	 *            The root directory in which all class files reside from which
	 *            calls to their methods must be collected. This directory must
	 *            correspond to the empty package.
	 * @param runNotificationSubscriber
	 *            The notifier on which this collector can subscribe itself.
	 */
	public TestDependencyCollector(File sourceDirectory,
			RunNotificationSubscriber runNotificationSubscriber) {
		setupOssRewriter(findAllClassNames(sourceDirectory, ""));
		this.runNotificationSubscriber = runNotificationSubscriber;
		this.testListener = new TestListener();
	}

	private void setupOssRewriter(final Collection<String> sourceClassNames) {
		OSSRewriter.setUserExclusionFilter(new Predicate<String>() {
			@Override
			public boolean apply(String className) {
				// A nested class (denoted after a $ sign) is defined in its
				// parent class file.
				int indexOfDollar = className.indexOf('$');
				if (indexOfDollar >= 0) {
					className = className.substring(0, indexOfDollar);
				}

				String javaClassName = className.replace('/', '.');
				boolean isSourceClass = sourceClassNames
						.contains(javaClassName);
				return !isSourceClass;
			}
		});
		OSSRewriter.enable();
	}

	@Override
	public void startCollecting() {
		super.startCollecting();
		methodCallMonitor = new MethodCallMonitor();
		MonitorEntrypoint.register(methodCallMonitor);
		runNotificationSubscriber.addListener(testListener);
	}

	@Override
	public void stopCollecting() {
		super.stopCollecting();
		OSSRewriter.disable();
		OSSRewriter.resetUserExclusionFilter();
		MonitorEntrypoint.unregister(methodCallMonitor);
		runNotificationSubscriber.removeListener(testListener);

		methodCallMonitor = null;
		testListener = null;
	}
	
	@Override
	public <T extends ITestData> boolean canProduce(Class<T> testDataClass) {
		return testDataClass.isAssignableFrom(MethodCalls.class);
	}

	protected Collection<String> findAllClassNames(File directory, String parentPackage) {
		Collection<String> files = new ArrayList<String>();
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				files.addAll(getClassNamesFromDirectory(file, parentPackage));
			} else if (file.isFile() && file.getName().endsWith(".class")) {
				files.add(getClassNameFromFile(file, parentPackage));
			}
		}
		return files;
	}

	protected Collection<String> getClassNamesFromDirectory(File directory,
			String currentPackage) {
		String subPackage;
		if (currentPackage.isEmpty()) {
			subPackage = directory.getName();
		} else {
			subPackage = currentPackage + "." + directory.getName();
		}

		return findAllClassNames(directory, subPackage);
	}

	protected String getClassNameFromFile(File file, String currentPackage) {
		String fileName = file.getName();
		String fileNameWithoutExtension = fileName.substring(0,
				fileName.lastIndexOf("."));
		String className = currentPackage + "." + fileNameWithoutExtension;
		return className;
	}

	protected class TestListener extends RunListener {

		@Override
		public void testStarted(Description description) {
			currentMethodCalls = new ArrayList<String>();
		}

		@Override
		public void testFinished(Description description) {
			onDataCollected(new MethodCalls(description, currentMethodCalls));
		}

	}

	protected class MethodCallMonitor extends Monitor {

		@Override
		public void enterMethod(String methodName) {
			currentMethodCalls.add(methodName);
		}

	}

}
