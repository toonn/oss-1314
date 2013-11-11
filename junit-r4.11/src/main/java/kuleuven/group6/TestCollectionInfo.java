package kuleuven.group6;

import java.io.File;

import org.junit.runner.notification.RunNotifier;

public class TestCollectionInfo {
	
	protected File testDirectory;
	protected File codeDirectory;
	protected RunNotifier runNotifier;
	
	public TestCollectionInfo(File testDirectory, File codeDirectory, RunNotifier runNotifier) {
		this.testDirectory = testDirectory;
		this.codeDirectory = codeDirectory;
		this.runNotifier = runNotifier;
	}
	
	public File getTestDirectory() {
		return testDirectory;
	}
	
	public File getCodeDirectory() {
		return codeDirectory;
	}

	public RunNotifier getRunNotifier() {
		return runNotifier;
	}
	
}
