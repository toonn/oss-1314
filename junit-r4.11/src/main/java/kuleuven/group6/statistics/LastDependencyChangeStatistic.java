package kuleuven.group6.statistics;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kuleuven.group6.collectors.DataCollectedListener;
import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.testcharacteristics.testdatas.CodeChange;
import kuleuven.group6.testcharacteristics.testdatas.MethodCalls;
import kuleuven.group6.testcharacteristics.teststatistics.ITestStatistic;
import kuleuven.group6.testcharacteristics.teststatistics.LastDependencyChange;
import org.junit.runner.Description;

/**
 * This Statistic makes use of two DataCollector's to create
 * LastDependencyChange's. (A collector that collects dependencies for tests and
 * one that collects which files have changed.) This Statistic will then combine
 * that information to create LastDependencyChange's.
 */
public class LastDependencyChangeStatistic extends
		Statistic<LastDependencyChange> {
	protected Map<String, Set<Description>> dependencies = new HashMap<>();

	public LastDependencyChangeStatistic(IDataEnroller dataEnroller) {
		dataEnroller.subscribe(CodeChange.class, new CodeChangeListener());
	}
	
	
	@Override
	public <T extends ITestStatistic> boolean canSummarize(Class<T> testStatisticClass) {
		return testStatisticClass.isAssignableFrom(LastDependencyChange.class);
	}
	

	protected class CodeChangeListener implements
			DataCollectedListener<CodeChange> {

		@Override
		public void dataCollected(CodeChange data) {
			calculateStatistic(data);
		}

	}

	protected class TestDependencyListener implements
			DataCollectedListener<MethodCalls> {

		@Override
		public void dataCollected(MethodCalls data) {
			addDependencies(data);
		}

	}

	@Override
	protected LastDependencyChange composeTestStatistic(Description description) {
		boolean hasLastFailureDate = false;
		Date lastDependencyChangeDate = null;

		for (Description childDescription : description.getChildren()) {
			LastDependencyChange childData = getTestStatistic(childDescription);
			if (childData == null)
				continue;

			Date childLastDependencyChangeDate = childData.getDate();
			if (!hasLastFailureDate
					|| childLastDependencyChangeDate
							.after(lastDependencyChangeDate)) {
				lastDependencyChangeDate = childLastDependencyChangeDate;
				hasLastFailureDate = true;
			}
		}

		if (!hasLastFailureDate)
			return null;

		return new LastDependencyChange(description, lastDependencyChangeDate);
	}

	@Override
	protected LastDependencyChange getDefaultTestStatistic(
			Description description) {
		return null;
	}

	protected void addDependencies(MethodCalls methodCalls) {
		for (String methodName : methodCalls.getMethodNames()) {
			String containingClass;
			int dollarIndex = methodName.indexOf('$');
			int dotIndex = methodName.indexOf('.');
			if (dollarIndex != -1 && dollarIndex < dotIndex)
				containingClass = methodName.substring(0, dollarIndex);
			else
				// A dot always occurs in a fully qualified (JVM spec 4.3.3)
				// method name (descriptor)
				containingClass = methodName.substring(0, dotIndex);

			Set<Description> dependencySet = dependencies.get(containingClass);
			if (dependencySet == null) {
				dependencySet = new HashSet<>();
				dependencySet.add(methodCalls.getTestDescription());
			} else
				dependencySet.add(methodCalls.getTestDescription());
		}
	}

	protected void calculateStatistic(CodeChange data) {
		Set<Description> changedClassDependencies = dependencies.get(data.getClassName());
		if (changedClassDependencies == null)
			return;
		
		for (Description description : changedClassDependencies) {
			putTestStatistic(new LastDependencyChange(description,
					data.getDate()));
		}
	}
}
