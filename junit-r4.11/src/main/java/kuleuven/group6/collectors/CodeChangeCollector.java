package kuleuven.group6.collectors;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;
import kuleuven.group6.testcharacteristics.testdatas.CodeChange;
import kuleuven.group6.testcharacteristics.testdatas.ITestData;
import org.junit.runner.Description;

/**
 * 
 * CodeChangeCollector is a data collector that will collect the tests that execute modified code
 * 
 * @author Team 6
 *
 */
public class CodeChangeCollector extends DataCollector<CodeChange> {
	protected WatchService watchService;
	protected Description rootDescription;
	private File codeDir;
	private File testDir;
	private CodeChangeWatchThread ccwt;

	public CodeChangeCollector(String rootSuiteClassName, File testDir, File codeDir){
		this.rootDescription = Description.createSuiteDescription(rootSuiteClassName);
		this.testDir = testDir;
		this.codeDir = codeDir;
	}

	/**
	 * Calling this method will start the collection of code changes.
	 * Every time it is called all test classes' dependencies are checked again.
	 */
	@Override
	public void startCollecting() {
		super.startCollecting();
		//Initiate the WatchService and WatchKeys by registering the test and code paths
		registerPathRecursive(testDir);
		registerPathRecursive(codeDir);
		//Start watching directories
		ccwt = new CodeChangeWatchThread(this,watchService);
		ccwt.start();
	}
	
	@Override
	public <T extends ITestData> boolean canProduce(Class<T> testDataClass) {
		return testDataClass.isAssignableFrom(CodeChange.class);
	}

	private class CodeChangeWatchThread extends Thread {
		private WatchService watchService;
		private CodeChangeCollector parent;

		public CodeChangeWatchThread(CodeChangeCollector parent, WatchService watchService) {
			this.watchService = watchService;
			this.parent = parent;
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
					System.out.println("A key has been taken! "+events.get(0).kind().name());
					List<Path> paths = new ArrayList<Path>();
					//We know the WatchEvent<T>s will be WatchEvent<Path>s because of the 
					//kinds of events that we registered
					for(WatchEvent<?> event : events){
						Path context = (Path)event.context();
						System.out.println(context);
						paths.add(((Path)key.watchable()).resolve(context));
					}
					parent.reportEventPaths(paths);
					System.out.println("Paths have been reported");
				} catch (Exception e) {
					System.out.println("Exception! "+e.getMessage());
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

	public void reportEventPaths(List<Path> paths) {
		System.out.println("reportEventPaths: Paths="+paths.toString());
		for(Path path : paths){
			System.out.println("Path: "+path.toString());
			System.out.println("Code: "+codeDir.toString());
			String className = FileSystems.getDefault().getPath(codeDir.getPath()).relativize(path).toString().replace(".class", "");
			CodeChange data = new CodeChange(rootDescription,className,new Date());
			System.out.println("New CodeChange! "+data.toString());
			onDataCollected(data);
		}
	}

	protected WatchKey registerPath(File file)
			throws IOException {
		watchService = FileSystems.getDefault().newWatchService();
		Path path = FileSystems.getDefault().getPath(file.getPath());
		return path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
	}
	
	private void registerPathRecursive(File file){
		List<File> tempList = new ArrayList<File>(Arrays.asList(file.listFiles()));
		for(File f : tempList){
			if(f.isDirectory()){
				try {
					registerPath(f);
					registerPathRecursive(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void stopCollecting() {
		super.stopCollecting();
		ccwt.interrupt();
	}

}
