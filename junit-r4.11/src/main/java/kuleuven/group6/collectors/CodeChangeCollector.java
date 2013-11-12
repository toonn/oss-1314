package kuleuven.group6.collectors;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.util.HashSet;

import static java.nio.file.StandardWatchEventKinds.*;
import jdepend.framework.*;
import kuleuven.group6.TestCollectionInfo;

public class CodeChangeCollector extends DataCollector<String> {

	@Override
	public void startCollecting(TestCollectionInfo testCollectionInfo) {
		try {
			WatchService watchService = FileSystems.getDefault().newWatchService();
			Path codePath = FileSystems.getDefault().getPath(testCollectionInfo.getSourceDirectory().getPath(),(String[]) null);
			codePath.register(watchService,ENTRY_MODIFY);
			JDepend jDepend = new JDepend();
			jDepend.addDirectory(testCollectionInfo.getTestDirectory().getPath());
			HashSet<JavaClass> classSet = new HashSet<JavaClass>(jDepend.analyze());
			//TODO something like this:
			for(JavaClass jc : classSet){
				jc.getImportedPackages();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void stopCollecting(TestCollectionInfo testCollectionInfo) {
		// TODO Auto-generated method stub
	}

}
