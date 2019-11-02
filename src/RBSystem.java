import java.util.*;

/**
 * 
 * Conflict Resolution
 * Before firing a rule, the conflict set is computed. A rule is then chosen according t the strategy. After
 * wards the rule is fired and the procedure is repeated from the beginning. A hierarchical strategy is thus
 * used. The conflict resolution strategy combines refactoring with recency and rule ordering. With recency
 * having the higher priority.
 *
 */
public class RBSystem {
	ArrayList<Rule> rules;
	LinkedList<String> kb;
	ArrayList<String> history;
	
	public RBSystem(ArrayList<Rule> rules, LinkedList<String> kb, ArrayList<String> history) {
		this.kb = kb;
		this.rules = rules;
		this.history = history;
	}

	public void apply() {
		ArrayList<Rule> matchingRules;
		System.out.println();
		while (true) {
			System.out.println("==========================");
			System.out.println("--> Matching Rules..");
			matchingRules = match();
			
			System.out.println("--> Refactoring..");
			matchingRules = refactor(matchingRules);
			System.out.println("# Of Matched Rules after refactoring = " + matchingRules.size());

			if (matchingRules.size() == 0) {
				System.out.println("==========================\n");
				return;
			}
			
			System.out.println("--> Recency Ordering..");
			matchingRules = recencyOrder(matchingRules);
			

			System.out.println("--> Firing..");
			fire((Rule) (matchingRules.get(0)));
			System.out.println("==========================\n");

		}

	}

	private void fire(Rule matchedRule) {
		System.out.println("Firing matchedRule " + matchedRule.name);
		history.add(matchedRule.name);
		
		for (int i = 0; i < matchedRule.actions.size(); i++) {
			if(!kb.contains(matchedRule.actions.get(i))) {
				kb.addLast(matchedRule.actions.get(i));
			}
		}
		
		for (int i = 0; i < matchedRule.removes.size(); i++) {
			String itemToRemove = matchedRule.removes.get(i);
			System.out.println("Removing item (" + itemToRemove + ") from KB");
			kb.remove(itemToRemove);
		}
	}

	private ArrayList<Rule> match() {
		ArrayList<Rule> matchingRules = new ArrayList<Rule>();
		System.out.print("[ ");
		for (int i = 0; i < rules.size(); i++) {
			if (((Rule) (rules.get(i))).match(kb) == true) {
				System.out.print(rules.get(i).name + " ");
				matchingRules.add((Rule) (rules.get(i)));
			}
		}
		System.out.println("] matched.");
		return matchingRules;
	}

	private ArrayList<Rule> refactor(ArrayList<Rule> matchingRules) {
		System.out.print("Removed rules [");
		for (int i = 0; i < history.size(); i++)
			matchingRules = removeByName(matchingRules, history.get(i));
		System.out.println("].");
		return matchingRules;
	}
	
	private ArrayList<Rule> recencyOrder(ArrayList<Rule> matchingRules) {
		PriorityQueue<Rule> sortedRules =
				new PriorityQueue<Rule>(new Comparator<Rule> () {
			@Override
			public int compare(Rule r1, Rule r2) {
				return RBSystem.caculateRecency(kb, r1) -
						RBSystem.caculateRecency(kb, r2);
			}
			
		});
	
		sortedRules.addAll(matchingRules);
		
		matchingRules.clear();
//		ArrayList<Rule> sortedRulesList = new ArrayList<Rule>();
		matchingRules.addAll(sortedRules);
		System.out.print("[ ");
		for(Rule r : matchingRules) {
			System.out.print(r.name + " ");
		}
		System.out.println("] ordered.");
		return matchingRules;
	}

	private ArrayList<Rule> removeByName(ArrayList<Rule> matchingRules, String name) {
		for (int i = 0; i < matchingRules.size(); i++)
			if (((Rule) matchingRules.get(i)).name == name) {
				Rule removedRule = matchingRules.remove(i);
				System.out.print(removedRule.name + " ");
			}
		return matchingRules;
	}
	
	private static int caculateRecency(LinkedList<String> kb, Rule rule) {
		int recencyValue = 0;
		for(int i = 0; i < rule.prec.size(); i++) {
			recencyValue+= kb.indexOf(rule.prec.get(i));
		}
		return recencyValue;
	}
	
}