import java.util.*;
import java.io.*;

public class RuleBaseSystem {
    public static void main(String[] args) {
	ArrayList<String> queries =
	    new ArrayList<>(Arrays.asList("?x is an Accord Wagon",
					  "?y is a Ferrari F50"));

	FileManager fm = new FileManager();
	ArrayList<Rule> rules = fm.loadRules("CarShopRule.data");
	ArrayList<String> wm = fm.loadWm("CarShopWm.data");
	RuleBase rb = new RuleBase(rules,wm);
	rb.backwardChain(queries);
    }
}
