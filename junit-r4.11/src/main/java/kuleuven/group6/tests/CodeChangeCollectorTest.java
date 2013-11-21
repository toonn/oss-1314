package kuleuven.group6.tests;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import kuleuven.group6.collectors.CodeChangeCollector;
import kuleuven.group6.collectors.DataCollectedListener;
import kuleuven.group6.testcharacteristics.testdatas.CodeChange;
import kuleuven.group6.tests.testsubject.source.SourceLocator;
import kuleuven.group6.tests.testsubject.tests.TestsLocator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;

import kuleuven.group6.tests.testsubject.tests.AllTests;

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
		
		assertTrue(subjectSourceDirectory.isDirectory());
		try {
			Charset cs = Charset.forName("UTF-8");
			Path newFilePath = Paths.get(subjectSourceDirectory.getPath().concat("/newfile"));
			System.out.println(newFilePath);
			Files.createFile(newFilePath);
			BufferedWriter writer = Files.newBufferedWriter(newFilePath,cs);

			//assertTrue(subjectSourceDirectory.createNewFile());
			assertTrue(ccl.getLastCollectedCodeChange()==null);
		} catch (Exception e) {
			e.printStackTrace();
		}

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
