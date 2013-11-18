package kuleuven.group6.collectors;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.runner.Description;
import static java.nio.file.StandardWatchEventKinds.*;
import jdepend.framework.*;
import kuleuven.group6.testcharacteristics.CodeChange;

/**
 * 
 * @author Team 6
 *
 */
public class CodeChangeCollector extends DataCollector<CodeChange> {
	protected HashSet<JavaClass> importedClasses;
	protected WatchService watchService;
	protected Description suiteDescription;
	private File codeDir;
	private File testDir;
	private CodeChangeWatchThread ccwt;

	public CodeChangeCollector(Description suiteDescription, File testDir, File codeDir){
		this.suiteDescription = suiteDescription;
		this.testDir = testDir;
		this.codeDir = codeDir;
	}

	/**
	 * Calling this method will start the collection of code changes.
	 * Every time it is called all test classes' dependencies are checked again.
	 */
	@Override
	public void startCollecting() {
		try {
			//Initiate the WatchService and WatchKeys by registering the test and code paths
			registerPath(testDir);
			registerPath(codeDir);
			//Start watching directories
			ccwt = new CodeChangeWatchThread(this,importedClasses,watchService);
			ccwt.run();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private class CodeChangeWatchThread extends Thread {
		private HashSet<JavaClass> importedClasses;
		private WatchService watchService;
		private CodeChangeCollector parent;

		public CodeChangeWatchThread(CodeChangeCollector parent, HashSet<JavaClass> importedClasses, WatchService watchService) {
			this.importedClasses = importedClasses;
			this.watchService = watchService;
			this.parent = parent;
		}

		public void run() {
			while(true){
				WatchKey key;
				try {
					key = watchService.take();
					List<Path> paths = new ArrayList<Path>();
					//We know the WatchEvent<T>s will be WatchEvent<Path>s because of the 
					//kinds of events that we registered
					for(WatchEvent event : key.pollEvents()){
						if(event.kind().type().equals(Path.class)){
							paths.add((Path)event.context());
						}
					}
					parent.reportEventPaths(paths);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void reportEventPaths(List<Path> paths) {
		for(Path path : paths){
			//TODO TEST THIS!
			String className = FileSystems.getDefault().getPath(codeDir.getAbsolutePath()).relativize(path).toString().replace('/', '.');
			CodeChange data = new CodeChange(suiteDescription,className,new Date());
			onDataCollected(data);
		}
	}

	protected WatchKey registerPath(File file)
			throws IOException {
		watchService = FileSystems.getDefault().newWatchService();
		Path path = FileSystems.getDefault().getPath(file.getPath());
		return path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
	}

	@Override
	public void stopCollecting() {
		ccwt.interrupt();
	}

}
