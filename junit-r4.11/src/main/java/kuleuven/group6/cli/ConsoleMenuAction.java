package kuleuven.group6.cli;

public abstract class ConsoleMenuAction {
	protected String commandString;
	protected String title;
	protected ConsoleMenuAction nextAction;
	
	protected ConsoleMenuAction(String commandString, String title, ConsoleMenuAction nextAction) {
		this(commandString, title);
		this.nextAction = nextAction;
	}
	
	protected ConsoleMenuAction(String commandString, String title) {
		this.commandString = commandString;
		this.title = title;
	}
	
	public boolean handle(String enteredCommandString) {
		if (!canHandle(enteredCommandString))
			return letNextActionHandle(enteredCommandString);
		
		execute();
		return true; 
	}
	
	protected boolean letNextActionHandle(String enteredCommandString) {
		if (!hasNextAction())
			return false;
		
		return getNextAction().handle(enteredCommandString);
	}
	
	public boolean hasNextAction() {
		return nextAction != null;
	}
	
	public ConsoleMenuAction getNextAction() {
		return nextAction;
	}
	
	public void setNextAction(ConsoleMenuAction nextAction) {
		this.nextAction = nextAction;
	}
	
	public ConsoleMenuAction getLastAction() {
		if (!hasNextAction())
			return this;
		
		return getNextAction().getLastAction();
	}
	
	
	protected boolean canHandle(String enteredCommandString) {
		return commandString.equalsIgnoreCase(enteredCommandString);
	}
	
	protected abstract void execute();
	
	
	public void showMenuChain() {
		show();
		if (hasNextAction())
			getNextAction().showMenuChain();
	}
	
	protected void show() {
		System.out.println("\t[" + commandString + "]\t" + title);
	}
}
