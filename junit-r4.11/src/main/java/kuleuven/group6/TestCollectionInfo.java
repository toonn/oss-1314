package kuleuven.group6;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 
 * @author team 6
 *
 */
public class TestCollectionInfo {
	
	protected File testDirectory;
	protected File sourceDirectory;
	protected RunNotificationSubscriber runNotificationSubscriber;
	
	protected Collection<String> testClassNames;
	protected Collection<String> sourceClassNames;
	
	public TestCollectionInfo(File testDirectory, File sourceDirectory, RunNotificationSubscriber runNotificationSubscriber) {
		this.testDirectory = testDirectory;
		this.sourceDirectory = sourceDirectory;
		this.runNotificationSubscriber = runNotificationSubscriber;
		
		this.testClassNames = findAllClassNames(testDirectory, "");
		this.sourceClassNames = findAllClassNames(sourceDirectory, "");
	}
	
	protected Collection<String> findAllClassNames(File directory, String parentPackage) {
		Collection<String> files = new ArrayList<String>();
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				String subPackage;
				// TODO herschrijf
				if (parentPackage.isEmpty()) {
					subPackage = file.getName();
				} else {
					subPackage = parentPackage + "." + file.getName();
				}
				
				Collection<String> filesInDirectory = findAllClassNames(file, subPackage);
				files.addAll(filesInDirectory);
			} else if (file.isFile() && file.getName().endsWith(".class")) {
				String fileName = file.getName();
				String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
				String className = parentPackage + "." + fileNameWithoutExtension;
				files.add(className);
			}
		}
		return files;
	}
	
	public File getTestDirectory() {
		return testDirectory;
	}
	
	public File getSourceDirectory() {
		return sourceDirectory;
	}
	
	
	public RunNotificationSubscriber getRunNotificationSubscriber() {
		return runNotificationSubscriber;
	}


	public Collection<String> getTestClassNames() {
		return Collections.unmodifiableCollection(testClassNames);
	}
	
	public boolean containsTestClass(String testClassName) {
		return testClassNames.contains(testClassName);
	}

	public Collection<String> getSourceClassNames() {
		return Collections.unmodifiableCollection(sourceClassNames);
	}
	
	public boolean containsSourceClass(String sourceClassName) {
		return sourceClassNames.contains(sourceClassName);
	}

}
