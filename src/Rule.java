import java.util.*;

public class Rule implements Comparator<Rule> {

	String name;
	ArrayList<String> prec;
	ArrayList<String> actions;
	ArrayList<String> removes;

	private Rule(String name, ArrayList<String> pre, ArrayList<String> act) {
		this.name = name;
		this.prec = pre;
		this.actions = act;
	}
	
	public Rule(String name, ArrayList<String> pre, ArrayList<String> act, ArrayList<String> removes) {
		this(name, pre, act);
		this.removes = removes;
	}

	public boolean match(ArrayList<String> kb) {
		
		for (int i = 0; i < prec.size(); i++)
			if (kb.contains(prec.get(i)) == false)
				return false;

		return true;
	}
	
	public boolean match(LinkedList<String> kb) {
		for(int i=0;i<prec.size();i++)
			if(kb.contains(prec.get(i))==false)
				return false;

		return true;
	}

	@Override
	public int compare(Rule r1, Rule r2) {
		return r1.prec.size() - r2.prec.size();
	}
	
	public String toString() {
		String string =  name + ":: IF " + prec.toString() + " THEN " + actions.toString();
		if(removes != null && removes.size() > 0) {
			string += " REMOVE " + removes.toString();
		}
		return string;
		
	}
	
	
}