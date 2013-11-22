package kuleuven.group6.testcharacteristics.testdatas;

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

	@Override
	public Description getTestDescription() {
		return testDescription;
	}

	public String getClassName() {
		return className;
	}
	
	public Date getDate() {
		return date;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (! (obj instanceof CodeChange))
			 return false;
		CodeChange other = (CodeChange)obj;
		if (! getTestDescription().equals(other.getTestDescription())) 
			return false;
		if (! getClassName().equals(other.getClassName()))
			return false;
		if (! getDate().equals(other.getDate()))
			return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + testDescription.hashCode();
		hash = hash * 23 + className.hashCode();
		hash = hash * 31 + date.hashCode();
		return hash;
	}
	
}
