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

}
