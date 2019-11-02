import java.io.IOException;
import java.util.ArrayList;

public class Main {

	public static void main(String args []) {
		
		Interface systemInterface = new Interface();
		systemInterface.getInput();
		try {
			systemInterface.loadKB();
			System.out.println("Knowledgebase (" + systemInterface.kbFilepath + ") has been loaded successfully.");
		} catch (IOException e) {
			System.out.println("KnowledgeBase (" + systemInterface.kbFilepath  + ") file was not properly created.");
			e.printStackTrace();
		}
		try {
			systemInterface.loadRules();
			System.out.println("Rules (" + systemInterface.rulesFilepath + ") has been loaded successfully.");
		} catch (IOException e) {
			System.out.println("Rules (" + systemInterface.rulesFilepath  + ") file was not properly created.");
			e.printStackTrace();
		}
		
		System.out.println("Loaded KB contains\n"
				+ systemInterface.knowledgeBase.toString());
		System.out.println("Loaded Rules are");
		for(Rule r : systemInterface.systemRules) {
			System.out.println(r.toString());
		}
		
		
		RBSystem rBSystem = new RBSystem(
				systemInterface.systemRules,
				systemInterface.knowledgeBase,
				new ArrayList<String>());
		System.out.println("Rule Based System has been initialized.");
		
		
		rBSystem.apply();
		
		System.out.println("\n"
				+ "After running the system, \n"
				+ "applying recency of added data in the KB\n"
				+ "with simple closed loop hierarichal strategy.\n"
				+ "System's KnowledgeBase now contains the following:\n"
				+ rBSystem.kb.toString()
				+ "\nHistory Of Fired Rules:\n"
				+ rBSystem.history.toString());
		
	}
}
