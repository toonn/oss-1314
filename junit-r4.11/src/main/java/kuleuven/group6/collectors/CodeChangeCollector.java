package kuleuven.group6.collectors;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collection;
import java.util.HashSet;

import static java.nio.file.StandardWatchEventKinds.*;
import jdepend.framework.*;
import kuleuven.group6.TestCollectionInfo;
import kuleuven.group6.testcharacteristics.SourceCodeChangeDate;

/**
 * 
 * @author Team 6
 *
 */
//public class CodeChangeCollector extends DataCollector<String> {
//	protected HashSet<JavaClass> importedClasses;
//	protected WatchService watchService;
//	protected WatchKey codeKey;
//	protected WatchKey testKey;
//
//	/**
//	 * Calling this method will start the collection of code changes.
//	 * Every time it is called all test classes' dependencies are checked again.
//	 */
//	@Override
//	public void startCollecting(TestCollectionInfo testCollectionInfo) {
//		try {
//			//Initiate the WatchService and WatchKeys by registering the test and code paths
//			registerPaths(testCollectionInfo);
//			//Extract dependencies using JDepend
//			extractDependencies(testCollectionInfo);
//			//Start watching directories
//			//TODO
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}
//
//	private class CodeChangeWatchThread extends Thread {
//		private HashSet<JavaClass> importedClasses;
//		private WatchService watchService;
//		private WatchKey codeKey;
//		private WatchKey testKey;
//
//		public CodeChangeWatchThread(HashSet<JavaClass> importedClasses, WatchService watchService, WatchKey codeKey, WatchKey testKey) {
//			this.importedClasses = importedClasses;
//			this.watchService = watchService;
//			this.codeKey = codeKey;
//			this.testKey = testKey;
//		}
//		
//		public void run() {
//			for(;;){
//				//TODO
//			}
//		}
//		
//	}
//	
//	protected void extractDependencies(TestCollectionInfo testCollectionInfo)
//			throws IOException {
//		JDepend jDepend = new JDepend();
//		jDepend.addDirectory(testCollectionInfo.getTestDirectory().getPath());
//		HashSet<JavaClass> classSet = new HashSet<JavaClass>(jDepend.analyze());
//		importedClasses = new HashSet<JavaClass>();
//		for(JavaClass jc : classSet){
//			HashSet<JavaPackage> ic = new HashSet<JavaPackage>((Collection<JavaPackage>)jc.getImportedPackages());
//			for(JavaPackage jp : ic){
//				importedClasses.addAll((Collection<JavaClass>)jp.getClasses());
//			}
//		}
//	}
//
//	protected void registerPaths(TestCollectionInfo testCollectionInfo)
//			throws IOException {
//		watchService = FileSystems.getDefault().newWatchService();
//		Path codePath = FileSystems.getDefault().getPath(testCollectionInfo.getSourceDirectory().getPath(),(String[]) null);
//		codeKey = codePath.register(watchService,ENTRY_MODIFY);
//		Path testPath = FileSystems.getDefault().getPath(testCollectionInfo.getSourceDirectory().getPath(), (String[]) null);
//		testKey = testPath.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
//	}
//
//	@Override
//	public void stopCollecting(TestCollectionInfo testCollectionInfo) {
//		// TODO Auto-generated method stub
//	}
//
//}
