package kuleuven.group6.tests.collectors;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import kuleuven.group6.collectors.CodeChangeCollector;
import kuleuven.group6.collectors.DataCollectedListener;
import kuleuven.group6.testcharacteristics.testdatas.CodeChange;
import kuleuven.group6.tests.testsubject.source.SourceLocator;
import kuleuven.group6.tests.testsubject.tests.AllTests;
import kuleuven.group6.tests.testsubject.tests.TestsLocator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;

public class CodeChangeCollectorTest {

	private CodeChangeCollector changeCollector;
	private File subjectSourceDirectory;
	private File subjectTestsDirectory;

	@Before
	public void initializeTestEnvironment() {
		subjectSourceDirectory = new File(SourceLocator.getLocation());
		subjectTestsDirectory = new File(TestsLocator.getLocation());
		Description allTestsDescription = Description.createSuiteDescription(AllTests.class);
		changeCollector = new CodeChangeCollector(allTestsDescription.getClassName(),subjectTestsDirectory,subjectSourceDirectory);
		changeCollector.startCollecting();
	}
	
	@After
	public void tearDownTestEnvironment() {
		changeCollector.stopCollecting();
	}
	
	@Test
	public void testReportEventPaths() {
		CodeChangeListener ccl = new CodeChangeListener();
		changeCollector.addListener(ccl);		
		File[] ls = subjectSourceDirectory.listFiles();
		List<File> lsList = new ArrayList<File>(Arrays.asList(ls));
		List<File> tempList = new ArrayList<File>(lsList);
		boolean containsClassFiles = false;
		//Look for the first directory that can be found that contains class files
		while(!containsClassFiles){
			for(File file : lsList){
				if(file.isDirectory()){
					tempList.addAll(Arrays.asList(file.listFiles()));
				}
			}
			for(File file : tempList){
				if(file.getName().contains(".class")){
					containsClassFiles = true;
					lsList = new ArrayList<File>();
					lsList.add(file.getParentFile());
					break;
				} else if(file.isDirectory()){
					lsList.add(file);
				}
			}
		}
		File classFileDir = lsList.get(0);
		
		//Test reporting the creation of files
		Path newFilePath = Paths.get(classFileDir.getPath().concat("/newfile.class"));
		try {
			if(!Files.exists(newFilePath)){
				Files.createFile(newFilePath);
			} 
			else {
				Files.delete(newFilePath);
				Files.createFile(newFilePath);
			}
			CodeChange createFileChange = ccl.getLastCollectedCodeChange();
			while(createFileChange==null){
				createFileChange = ccl.getLastCollectedCodeChange();
			}
			assertFalse(createFileChange==null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Test reporting editing of files
		newFilePath.toFile().setLastModified((new Date()).getTime());
		CodeChange editFileChange = ccl.getLastCollectedCodeChange();
		while(editFileChange==null){
			editFileChange = ccl.getLastCollectedCodeChange();
		}
		assertFalse(editFileChange==null);
		
		//Test reporting deletion of files
		try {
			Files.delete(newFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		CodeChange deleteFileChange = ccl.getLastCollectedCodeChange();
		while(deleteFileChange==null){
			deleteFileChange = ccl.getLastCollectedCodeChange();
		}
		assertFalse(deleteFileChange==null);
		
	}

//	@Test
//	public void testRegisterPath() {
//		assertTrue(true);
//	}

	protected class CodeChangeListener implements DataCollectedListener<CodeChange> {

		CodeChange lastCollectedCodeChange = null;
		
		public CodeChange getLastCollectedCodeChange() {
			return lastCollectedCodeChange;
		}
		
		@Override
		public void dataCollected(CodeChange codeChange) {
			lastCollectedCodeChange = codeChange;
		}
		
	}
	
}
