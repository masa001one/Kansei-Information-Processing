import java.util.*;
import java.io.*;

/**
 * RuleBaseSystem
 * 
 */
public class RuleBaseSystem {
    public static void main(String[] args){
        FileManager fm = new FileManager();
        ArrayList<Rule> rules = fm.loadRules("CarShopRule.data");
        ArrayList<String> wm = fm.loadWm("CarShopWm.data");
        RuleBase rb = new RuleBase(rules,wm);
        rb.forwardChain();      
    }
}
