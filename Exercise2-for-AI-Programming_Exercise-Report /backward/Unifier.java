import java.util.*;
import java.io.*;

class Unifier {
    String buffer1[];    
    String buffer2[];
    HashMap<String,String> vars;
    
    public boolean unify(String string1, String string2,
			 HashMap<String,String> theBindings) {
	// 元のものを取っておく
	HashMap<String,String> orgBindings = new HashMap<>(theBindings);
	this.vars = theBindings;
	if ( unify(string1,string2) ) {
	    return true;
	} else {
	    // 失敗したら元に戻す
	    theBindings.clear();
	    theBindings.putAll(orgBindings);
	    return false;
	}
    }

    public boolean unify(String string1, String string2) {
	// 同じなら成功
	if ( string1.equals(string2) ) return true;
	
	// 各々トークンに分ける
	StringTokenizer st1 = new StringTokenizer(string1);
	StringTokenizer st2 = new StringTokenizer(string2);
	
	// 数が異なったら失敗
	if ( st1.countTokens() != st2.countTokens() ) return false;
	
	// 定数同士
	int length = st1.countTokens();
	buffer1 = new String[length];
	buffer2 = new String[length];
	for (int i = 0 ; i < length; i++) {
	    buffer1[i] = st1.nextToken();
	    buffer2[i] = st2.nextToken();
	}

	// 初期値としてバインディングが与えられていたら
	if ( this.vars.size() != 0 ) {
	    for (Iterator<String> i = vars.keySet().iterator(); i.hasNext(); ) {
		String key = i.next();
		String value = vars.get(key);
		replaceBuffer(key,value);
	    }
	}

	for(int i = 0; i < length; i++) {
	    if ( !tokenMatching(buffer1[i],buffer2[i]) ) {
		return false;
	    }
	}

	return true;
    }

    boolean tokenMatching(String token1, String token2) {
	if ( token1.equals(token2) ) return true;
	if ( var(token1) ) return varMatching(token1,token2);
	if ( var(token2) ) return varMatching(token2,token1);
	return false;
    }

    boolean varMatching(String vartoken, String token) {
	if ( vars.containsKey(vartoken) ) {
	    if ( token.equals(vars.get(vartoken)) ) {
		return true;
	    } else {
		return false;
	    }
	} else {
	    replaceBuffer(vartoken,token);
	    if ( vars.containsValue(vartoken) ) {
		replaceBindings(vartoken,token);
	    }
	    vars.put(vartoken,token);
	}
	return true;
    }

    void replaceBuffer(String preString, String postString) {
	for (int i = 0; i < buffer1.length; i++) {
	    if ( preString.equals(buffer1[i]) ) {
		buffer1[i] = postString;
	    }
	    if ( preString.equals(buffer2[i]) ) {
		buffer2[i] = postString;
	    }
	}
    }
    
    void replaceBindings(String preString, String postString) {
	for (Iterator<String> i = vars.keySet().iterator(); i.hasNext(); ) {
	    String key = i.next();
	    if ( preString.equals(vars.get(key)) ) {
		vars.put(key,postString);
	    }
	}
    }
    
    boolean var(String str1) {
	// 先頭が ? なら変数
	return str1.startsWith("?");
    }
}
