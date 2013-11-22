package kuleuven.group6.testcharacteristics.teststatistics;

import java.util.Date;

import org.junit.runner.Description;

/**
 * 
 * @author Team 6
 *
 */
public class LastDependencyChange implements ITestStatistic {

	protected final Description testDescription;
	protected final Date date;
	
	public LastDependencyChange(Description testDescription, Date lastDependencyChangeDate) {
		this.testDescription = testDescription;
		this.date = lastDependencyChangeDate;
	}

	@Override
	public Description getTestDescription() {
		return testDescription;
	}

	public Date getDate() {
		return date;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (! (obj instanceof LastDependencyChange))
			 return false;
		LastDependencyChange other = (LastDependencyChange)obj;
		if (! getTestDescription().equals(other.getTestDescription())) 
			return false;
		if (! getDate().equals(other.getDate()))
			return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + testDescription.hashCode();
		hash = hash * 31 + date.hashCode();
		return hash;
	}

}
