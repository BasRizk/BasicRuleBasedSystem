import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
/**
 * Interface
 * The system should be augmented with an interface. Through the interface we should be able to write the
 * rules in a corresponding text area. Another option is also to upload the rules from a file on the system.
 * Through the interface, the user can enter the pieces of information (knowledge) to the store.
 * 
 * Parser
 * Once the rules are written/uploaded the parser module makes sure they are in the correct format. They
 * could then be parsed and saved into any data structure that is used by the system.
 * 
 *
 */
public class Interface {

	ArrayList<Rule> systemRules;
	LinkedList<String> knowledgeBase;
	String rulesFilepath = "rules.input";
	String kbFilepath = "kb.input";
		
	public Interface() {
		systemRules = new ArrayList<Rule>();
		knowledgeBase = new LinkedList<String>();
	}
	
	public void getInput() {
		System.out.println("**********Files Formats*********");
		System.out.println("________________________________");
		System.out.println("KB file format (Ex.):\nA\nB\nC");
		System.out.println("________________________________");
		System.out.println("Rules file format (Ex.):\n"
				+ "IF\nCond1,\nCondn\n" 
				+ "THEN\nACT1,\n...,\nACT3\n"
				+ "REMOVE\nPre1,\n...,\nPren");
		System.out.println("********************************");

		Scanner input = new Scanner(System.in); 
	    System.out.println("> Enter KB filepath (Default:: rules.input): ");
	    String ruleFilepathInput = input.nextLine();
	    System.out.println("> Enter Rules filepath (Default:: kb.input): ");
	    String kbFilepathInput = input.nextLine();
	    input.close();
	    
	    if(ruleFilepathInput != null && ruleFilepathInput.length() > 0) {
			this.rulesFilepath = ruleFilepathInput;
	    }
	    if(kbFilepathInput != null && kbFilepathInput.length() > 0 ) {
			this.kbFilepath = kbFilepathInput;
	    }
	}
	
	public void loadKB() throws IOException {
		File file = new File(this.kbFilepath);

		BufferedReader br;
		br = new BufferedReader(new FileReader(file));
			
		String line;
		while ((line = br.readLine()) != null) {
			knowledgeBase.addLast(line);
		}
		
		br.close();
	}
	
	/**
	 * Input Format:
	 * 
	 * IF
	 * Cond1, ..., Condn
	 * THEN
	 * ACT1, ..., ACT3
	 * REMOVE
	 * Pre1, ..., Pren
	 * @throws IOException 
	 */
	public void loadRules() throws IOException {
		File file = new File(rulesFilepath);

		BufferedReader br;
		br = new BufferedReader(new FileReader(file));
			
		String line, lineCut;
		String ruleName = ""; int ruleNum = 1;
		ArrayList<String> rulePrec;
		ArrayList<String> ruleActions;
		ArrayList<String> ruleRemoves;
		
		rulePrec = new ArrayList<String>();
		ruleActions = new ArrayList<String>();
		ruleRemoves = new ArrayList<String>();
		
		LinkedList<String> linesQueue = new LinkedList<String>();
		
		while ((line = br.readLine()) != null) {
			linesQueue.addLast(line);
		}
		
		while(!linesQueue.isEmpty() && (line = linesQueue.peekFirst()) != null) {
			lineCut = line.split(" ")[0];
//			System.out.println("lineCut = " + lineCut);
		    if(lineCut.equals("IF")) {
		    	linesQueue.removeFirst();

		    	rulePrec = new ArrayList<String>();
				ruleActions = new ArrayList<String>();
				ruleRemoves = new ArrayList<String>();
		    	ruleName = "R" + ruleNum++;
		    	
		    	while((line = linesQueue.peekFirst()) != null) {
		    		lineCut = line.split(",")[0];
		    		if(lineCut.equals("THEN")) {
				    	break;
		    		} 
		    		linesQueue.removeFirst();
			    	rulePrec.add(lineCut);
		    	}
		    }
		    
		    if(lineCut.equals("THEN")) {
		    	linesQueue.removeFirst();
		    	while((line = linesQueue.peekFirst()) != null) {
		    		lineCut = line.split(",")[0];
		    		if(lineCut.equals("IF") ||
		    			lineCut.equals("REMOVE")) {
				    	break;
		    		} 
		    		linesQueue.removeFirst();
			    	ruleActions.add(line.split(",")[0]);
		    	}
		    }
		    
		    if(lineCut.equals("REMOVE")) {
		    	linesQueue.removeFirst();
		    	while((line = linesQueue.peekFirst()) != null) {
		    		lineCut = line.split(",")[0];
		    		if(lineCut.equals("IF")) {
		    			break;
		    		}
		    		linesQueue.removeFirst();
			    	ruleRemoves.add(lineCut);
		    	}
		    }
		    Rule ruleToBeAdded = new Rule(ruleName, rulePrec, ruleActions, ruleRemoves); 
		    systemRules.add(ruleToBeAdded);
		}
		
		br.close();
	}
}
