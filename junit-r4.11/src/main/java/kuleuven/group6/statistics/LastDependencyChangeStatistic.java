package kuleuven.group6.statistics;

import kuleuven.group6.collectors.DataCollectedListener;
import kuleuven.group6.collectors.IDataEnroller;
import kuleuven.group6.testcharacteristics.CodeChange;
import kuleuven.group6.testcharacteristics.LastDependencyChange;
import kuleuven.group6.testcharacteristics.MethodCalls;

import org.junit.runner.Description;

public class LastDependencyChangeStatistic<TestStatisticT> extends
		Statistic<LastDependencyChange> {
	
	public LastDependencyChangeStatistic(IDataEnroller dataCollectorManager) {
		dataCollectorManager.subscribe(CodeChange.class, new CodeChangeListener());
	}

	protected class CodeChangeListener implements DataCollectedListener<CodeChange> {

		@Override
		public void dataCollected(CodeChange data) {
			putTestStatistic(calculateStatistic(data));
		}
		
	}
	
	protected class TestDependencyListener implements DataCollectedListener<MethodCalls> {

		@Override
		public void dataCollected(MethodCalls data) {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	protected LastDependencyChange composeTestStatistic(Description description) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected LastDependencyChange getDefaultTestStatistic(
			Description description) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected LastDependencyChange calculateStatistic(CodeChange data) {
		return null;
	}

}
