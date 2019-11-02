import java.util.Comparator;

public class KBItem implements Comparator<KBItem> {
	
	String item;
	int id;
	
	public KBItem(String item, int id) {
		this.item = item;
		this.id = id;
	}

	@Override
	public int compare(KBItem i1, KBItem i2) {
		return i1.id - i2.id;
	}


}
