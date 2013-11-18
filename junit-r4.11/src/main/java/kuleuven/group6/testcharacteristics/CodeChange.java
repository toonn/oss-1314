package kuleuven.group6.testcharacteristics;

import java.util.Date;

import org.junit.runner.Description;

/**
 * 
 * @author Team 6
 *
 */
public class CodeChange implements ITestData {

	protected final Description testDescription;
	protected final String className;
	protected final Date date;
	
	public CodeChange(Description testDescription, String className, Date sourceCodeChangeDate) {
		this.testDescription = testDescription;
		this.className = className;
		this.date = sourceCodeChangeDate;
	}

	public Description getTestDescription() {
		return testDescription;
	}

	public String getClassName() {
		return className;
	}
	
	public Date getDate() {
		return date;
	}
	
}
