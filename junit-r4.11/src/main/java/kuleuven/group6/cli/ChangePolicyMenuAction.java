package kuleuven.group6.cli;

import kuleuven.group6.Launcher;

public class ChangePolicyMenuAction extends ConsoleMenuAction {

	protected String policyName;
	protected Launcher launcher;
	
	protected ChangePolicyMenuAction(String commandString, String policyName, Launcher launcher) {
		super(commandString, policyName);
		
		this.policyName = policyName;
		this.launcher = launcher;
	}

	@Override
	protected void execute() {
		launcher.setActivePolicy(policyName);
	}
	
}
