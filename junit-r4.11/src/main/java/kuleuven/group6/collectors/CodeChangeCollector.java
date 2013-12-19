package kuleuven.group6.collectors;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Date;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;
import kuleuven.group6.collectors.testdatas.CodeChange;
import kuleuven.group6.collectors.testdatas.ITestData;

import org.junit.runner.Description;

/**
 * CodeChangeCollector collects data from files that have been modified in the directory
 * of tests and in the directory of code being tested.
 * This data can be used to decide which tests to run first under the
 * "Changed code first" policy or to decide to run a testrun if you only
 * want to start a testrun when you expect the results may have changed.
 * 
 * 
 */
public class CodeChangeCollector extends DataCollector<CodeChange> {
	protected WatchService watchService;
	protected Description rootDescription;
	private File absoluteCodeDir;
	private File absoluteTestDir;
	private CodeChangeWatchThread ccwt;

	public CodeChangeCollector(String rootSuiteClassName, File testDir, File codeDir){
		this.rootDescription = Description.createSuiteDescription(rootSuiteClassName);
		this.absoluteTestDir = testDir.getAbsoluteFile();
		this.absoluteCodeDir = codeDir.getAbsoluteFile();
	}

	@Override
	public void startCollecting() {
		super.startCollecting();
		//Initiate the WatchService and WatchKeys by registering the test and code paths
		try {
			watchService = FileSystems.getDefault().newWatchService();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO may not continue after this!!!
		}
		registerPathRecursive(absoluteTestDir);
		registerPathRecursive(absoluteCodeDir);
		//Start watching directories
		ccwt = new CodeChangeWatchThread();
		ccwt.setDaemon(true);
		ccwt.start();
	}
	
	@Override
	public <T extends ITestData> boolean canProduce(Class<T> testDataClass) {
		return testDataClass.isAssignableFrom(CodeChange.class);
	}

	/**
	 * The CodeChangeWatchThread makes use of a watchservice to monitor
	 * filechanges in the testDir and codeDir directories.
	 *
	 */
	private class CodeChangeWatchThread extends Thread {

		public CodeChangeWatchThread() {
		}

		@Override
		public void run() {
			while(true) {
				WatchKey key;
				try {
					List<WatchEvent<?>> events;
					do{
						key = watchService.take();
						events = key.pollEvents();
					} while(events.isEmpty());
					//We know the WatchEvent<T>s will be WatchEvent<Path>s because of the 
					//kinds of events that we registered
					for(WatchEvent<?> event : events) {
						if (event.kind() == StandardWatchEventKinds.OVERFLOW)
							continue;
						
						@SuppressWarnings("unchecked")
						WatchEvent<Path> typedEvent = (WatchEvent<Path>)event; 
						processWatchEvent(typedEvent, key);
					}
					key.reset();
				} catch (InterruptedException e) {
					try {
						watchService.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					break;
				}
			}
		}
	}

	protected void processWatchEvent(WatchEvent<Path> event, WatchKey key) {
		Path relativePath = event.context();
		Path watchedDirectory = (Path)key.watchable();
		Path absolutePath = watchedDirectory.resolve(relativePath);
		File absoluteFile = absolutePath.toFile();
		
		// Removed files need to be handled as a real file since events for them
		// are important! There's no way to distinguish between a removed file or 
		// directory, so handle them all as a file.
		if (absoluteFile.isFile() || (event.kind() == ENTRY_DELETE)) {
			processFileEvent(absolutePath);
		} else if (absoluteFile.isDirectory()) {
			registerPathRecursive(absoluteFile);
		}
	}
		
	private void processFileEvent(Path absolutePath) {
		if (!absolutePath.toString().endsWith(".class"))
			return;
		
		String changedClassName = absolutePathToClassName(absolutePath);	
		CodeChange data = new CodeChange(rootDescription, changedClassName, new Date());
		onDataCollected(data);
	}
	
	protected String absolutePathToClassName(Path path) {
		Path codeDir = absoluteCodeDir.toPath();
		Path testDir = absoluteTestDir.toPath();
		Path baseDir;
		if (path.startsWith(codeDir))
			baseDir = codeDir;
		else if (path.startsWith(testDir))
			baseDir = testDir;
		else
			throw new AssertionError();
		
		String relativePath = baseDir.relativize(path).toString();
		
		// Don't just replace ".class" with "" since ".class" could be 
		// part of the path, for example in bin/.classfiles/File.class
		int indexOfDotClass = relativePath.lastIndexOf(".class");
		if (indexOfDotClass == -1)
			throw new AssertionError();
		
		String className = relativePath.substring(0, indexOfDotClass);
		return className;
	}
	
	private void registerPathRecursive(File file) {
		if (!file.isDirectory())
			return;
		
		try {
			file.toPath().register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
			for (File f : file.listFiles()) {
				registerPathRecursive(f);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stopCollecting() {
		super.stopCollecting();
		ccwt.interrupt();
	}

}
