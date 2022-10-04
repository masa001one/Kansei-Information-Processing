import java.util.*;
import java.io.*;

class FileManager {
    FileReader f;
    StreamTokenizer st;

    public ArrayList<Rule> loadRules(String theFileName){
	ArrayList<Rule> rules = new ArrayList<Rule>();
	String line;
	try{
	    int token;
	    f = new FileReader(theFileName);
	    st = new StreamTokenizer(f);
	    while((token = st.nextToken())!= StreamTokenizer.TT_EOF){
		switch(token){
		    case StreamTokenizer.TT_WORD:
			String name = null;
			ArrayList<String> antecedents = null;
			String consequent = null;
			if(st.sval.equals("rule")){
			    st.nextToken();
			    name = st.sval;
			    st.nextToken();
			    if(st.sval.equals("if")){
				antecedents = new ArrayList<String>();
				st.nextToken();
				while(!st.sval.equals("then")){
				    antecedents.add(st.sval);
				    st.nextToken();
				}
				st.nextToken();
				consequent = st.sval;
			    }
			}
			rules.add(new Rule(name,antecedents,consequent));
			break;
		    default:
			System.out.println(token);
			break;
		}
	    }
	} catch(Exception e){
	    System.out.println(e);
	}
	return rules;
    }

    public ArrayList<String> loadWm(String theFileName){
	ArrayList<String> wm = new ArrayList<String>();
	String line;
	try{
	    int token;
	    f = new FileReader(theFileName);
	    st = new StreamTokenizer(f);
	    st.eolIsSignificant(true);
	    st.wordChars('\'','\'');
	    while((token = st.nextToken())!= StreamTokenizer.TT_EOF){
		line = new String();
		while( token != StreamTokenizer.TT_EOL){
		    line = line + st.sval + " ";
		    token = st.nextToken();
		}
		wm.add(line.trim());
	    }
	} catch(Exception e){
	    System.out.println(e);
	}
	return wm;
    }
}
