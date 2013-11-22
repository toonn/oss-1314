package kuleuven.group6.testcharacteristics.teststatistics;

import java.util.Date;

import org.junit.runner.Description;

/**
 * Contains the date of the most recent failure of a test.
 */
public class LastFailureDate implements ITestStatistic {

	protected final Description testDescription;
	protected final Date lastFailureDate;
	
	public LastFailureDate(Description testDescription, Date lastFailureDate) {
		this.testDescription = testDescription;
		this.lastFailureDate = lastFailureDate;
	}

	@Override
	public Description getTestDescription() {
		return testDescription;
	}

	public Date getLastFailureDate() {
		return lastFailureDate;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (! (obj instanceof LastFailureDate))
			 return false;
		LastFailureDate other = (LastFailureDate)obj;
		if (! getTestDescription().equals(other.getTestDescription())) 
			return false;
		if (! getLastFailureDate().equals(other.getLastFailureDate()))
			return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + testDescription.hashCode();
		hash = hash * 31 + lastFailureDate.hashCode();
		return hash;
	}
	
}
