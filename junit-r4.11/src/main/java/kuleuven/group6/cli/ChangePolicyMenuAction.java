package kuleuven.group6.cli;

import kuleuven.group6.DaemonSystem;

public class ChangePolicyMenuAction extends ConsoleMenuAction {

	protected String policyName;
	protected DaemonSystem daemonSystem;
	
	protected ChangePolicyMenuAction(String commandString, String policyName, DaemonSystem daemonSystem) {
		super(commandString, policyName);
		
		this.policyName = policyName;
		this.daemonSystem = daemonSystem;
	}

	@Override
	protected void execute() {
		daemonSystem.setActivePolicy(policyName);
	}
	
}
