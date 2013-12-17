package kuleuven.group6.cli;

import java.util.Scanner;

public class ConsoleMenu {
	 
	protected String menuTitle, askChoiceString;
	protected ConsoleMenuAction firstMenuAction = null;
	
	public ConsoleMenu(String menuTitle, String askChoiceString) {
		this.menuTitle = menuTitle;
		this.askChoiceString = askChoiceString;
	}
	
	
	public String getMenuTitle() {
		return menuTitle;
	}
	
	public void setMenuTitle(String menuTitle) {
		this.menuTitle = menuTitle; 
	}
	
	
	public String getAskChoiceString() {
		return askChoiceString;
	}
	
	public void setAskChoiceString(String askChoiceString) {
		this.askChoiceString = askChoiceString;
	}
	
	
	public boolean isEmpty() {
		return (firstMenuAction == null);
	}
	
	public void addMenuAction(ConsoleMenuAction menuAction) {
		if (isEmpty()) {
			this.firstMenuAction = menuAction;
		} else {
			ConsoleMenuAction currentLastAction = firstMenuAction.getLastAction();
			currentLastAction.setNextAction(menuAction);
		}
	}
	
	public void show() {
		if (isEmpty())
			return;
		
		System.out.println();
		System.out.println(menuTitle);
		firstMenuAction.showMenuChain();
		askChoice();
		System.out.println();
	}
	
	protected void askChoice() {
		assert !isEmpty();
		
		// Don't close console, since System.in should not be closed.
		@SuppressWarnings("resource")
		Scanner console = new Scanner(System.in);
		boolean isHandled;
		do {
			System.out.print(askChoiceString);
			String enteredCommandString = console.nextLine();
			isHandled = firstMenuAction.handle(enteredCommandString);
		} while (!isHandled);
	}
	
}
