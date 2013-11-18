package kuleuven.group6.testcharacteristics;

import java.util.Date;

import org.junit.runner.Description;

/**
 * This class will tell when the last failure has occured
 * @author Team 6
 *
 */
public class LastFailureDate implements ITestStatistic {

	protected final Description testDescription;
	protected final Date lastFailureDate;
	
	public LastFailureDate(Description testDescription, Date lastFailureDate) {
		this.testDescription = testDescription;
		this.lastFailureDate = lastFailureDate;
	}

	public Description getTestDescription() {
		return testDescription;
	}

	public Date getLastFailureDate() {
		return lastFailureDate;
	}
	
}
