import java.util.*;
import java.io.*;

/**
 * ルールを表すクラス．
 */
class Rule implements Serializable{
    String name;
    ArrayList<String> antecedents;
    String consequent;

    Rule(String theName,ArrayList<String> theAntecedents,String theConsequent){
	this.name = theName;
	this.antecedents = theAntecedents;
	this.consequent = theConsequent;
    }

    public Rule getRenamedRule(int uniqueNum){
	ArrayList<String> vars = new ArrayList<String>();
	for(int i = 0 ; i < antecedents.size() ; i++){
	    String antecedent = (String)this.antecedents.get(i);
	    vars = getVars(antecedent,vars);
	}
	vars = getVars(this.consequent,vars);
	HashMap<String,String> renamedVarsTable = makeRenamedVarsTable(vars,uniqueNum);

	ArrayList<String> newAntecedents = new ArrayList<String>();
	for(int i = 0 ; i < antecedents.size() ; i++){
	    String newAntecedent =
		renameVars((String)antecedents.get(i),
			   renamedVarsTable);
	    newAntecedents.add(newAntecedent);
	}
	String newConsequent = renameVars(consequent,
					  renamedVarsTable);

	Rule newRule = new Rule(name,newAntecedents,newConsequent);
	return newRule;
    }

    private ArrayList<String> getVars(String thePattern,ArrayList<String> vars){
	StringTokenizer st = new StringTokenizer(thePattern);
	for(int i = 0 ; i < st.countTokens();){
	    String tmp = st.nextToken();
	    if(var(tmp)){
		vars.add(tmp);
	    }
	}
	return vars;
    }

    private HashMap<String,String> makeRenamedVarsTable(ArrayList<String> vars,int uniqueNum){
	HashMap<String,String> result = new HashMap<String,String>();
	for(int i = 0 ; i < vars.size() ; i++){
	    String newVar =
		(String)vars.get(i) + uniqueNum;
	    result.put((String)vars.get(i),newVar);
	}
	return result;
    }
    
    private String renameVars(String thePattern,
			      HashMap<String,String> renamedVarsTable){
	String result = new String();
	StringTokenizer st = new StringTokenizer(thePattern);
	for(int i = 0 ; i < st.countTokens();){
	    String tmp = st.nextToken();
	    if(var(tmp)){
		result = result + " " + renamedVarsTable.get(tmp);
	    } else {
		result = result + " " + tmp;
	    }
	}
	return result.trim();
    }

    private boolean var(String str){
	// 先頭が ? なら変数
	return str.startsWith("?");
    }

    public String getName(){
	return name;
    }

    public String toString(){
	return name+" "+antecedents.toString()+"->"+consequent;
    }

    public ArrayList<String> getAntecedents(){
	return antecedents;
    }

    public String getConsequent(){
	return consequent;
    }
}
