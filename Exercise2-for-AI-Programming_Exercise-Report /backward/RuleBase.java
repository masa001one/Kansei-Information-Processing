import java.util.*;
import java.io.*;

class RuleBase {
    String fileName;
    ArrayList<String> wm;
    ArrayList<Rule> rules;
    
    RuleBase(ArrayList<Rule> theRules, ArrayList<String> theWm) {
	wm = theWm;
	rules = theRules;
    }

    public void setWm(ArrayList<String> theWm) {
	wm = theWm;
    }

    public void setRules(ArrayList<Rule> theRules) {
	rules = theRules;
    }

    public void backwardChain(ArrayList<String> hypothesis) {
	System.out.println("Hypothesis: " + hypothesis);
	ArrayList<String> orgQueries = new ArrayList<>(hypothesis);
	HashMap<String,String> binding = new HashMap<>();
	if ( matchingPatterns(hypothesis,binding) ) {
	    System.out.println("Yes");
	    System.out.println("binding: " + binding);
	    // 最終的な結果を基のクェリーに代入して表示する
	    for (int i = 0; i < orgQueries.size(); i++) {
		String aQuery = orgQueries.get(i);
		String anAnswer = instantiate(aQuery,binding);
		System.out.println("Query: " + aQuery);
		System.out.println("Answer: " + anAnswer);
	    }
	} else {
	    System.out.println("No");
	}
    }

    /**
     * マッチするワーキングメモリのアサーションとルールの後件
     * に対するバインディング情報を返す
     */
    private boolean matchingPatterns(ArrayList<String> thePatterns,
				     HashMap<String,String> theBinding) {

        String firstPattern = thePatterns.get(0);
	if ( thePatterns.size() == 1 ) {
	    if ( matchingPatternOne(firstPattern,theBinding,0) != -1 ) {
		return true;
	    } else {
		return false;
	    }
	} else {
	    thePatterns.remove(0);

	    int cPoint = 0;
	    while ( cPoint < wm.size() + rules.size() ) {
		// 元のバインディングを取っておく
		HashMap<String,String> orgBinding = new HashMap<>(theBinding);
		int tmpPoint = matchingPatternOne(firstPattern,theBinding,cPoint);
		//System.out.println("tmpPoint: " + tmpPoint);
		if ( tmpPoint != -1 ) {
		    //System.out.println("Success: " + firstPattern);
		    if ( matchingPatterns(thePatterns,theBinding) ) { // 成功
			return true;
		    } else { // 失敗
			//choiceポイントを進める
			cPoint = tmpPoint;
			// 失敗したのでバインディングを戻す
			theBinding.clear();
			theBinding.putAll(orgBinding);
		    }
		} else {
		    // 失敗したのでバインディングを戻す
		    theBinding.clear();
		    theBinding.putAll(orgBinding);
		    return false;
		}
	    }
	    return false;
	}
    }

    private int matchingPatternOne(String thePattern,
				   HashMap<String,String> theBinding,
				   int cPoint) {

	// WME(Working Memory Elements) と Unify してみる．
	int i;
	for (i = cPoint; i < wm.size(); i++) {
	    if ( (new Unifier()).unify(thePattern, wm.get(i), theBinding) ) {
		System.out.println("Found \"" + thePattern + "\" in WM: "
				   + wm.get(i));
		return i+1;
	    }
	}

	// Ruleと Unify してみる．ここでは i >= wm.size
	for (i = i - wm.size(); i < rules.size(); i++) {
	    Rule aRule = rename(rules.get(i));
	    // 元のバインディングを取っておく．
	    HashMap<String,String> orgBinding = new HashMap<>(theBinding);

	    if ( (new Unifier()).unify(thePattern, aRule.getConsequent(),
				       theBinding) ) {
		System.out.println("Found \"" + thePattern + "\" in " + aRule);

		// さらにbackwardChaining
		ArrayList<String> newPatterns = aRule.getAntecedents();
		if ( matchingPatterns(newPatterns,theBinding) ) {
		    return wm.size()+i+1;
		} else {
		    // 失敗したら元に戻す．
		    theBinding.clear();
		    theBinding.putAll(orgBinding);
		}
	    }
	}
	return -1;
    }

    /**
     * 与えられたルールの変数をリネームしたルールのコピーを返す．
     * @param   変数をリネームしたいルール
     * @return  変数がリネームされたルールのコピーを返す．
     */
    int uniqueNum = 0;
    private Rule rename(Rule theRule) {
	Rule newRule = theRule.getRenamedRule(uniqueNum);
	uniqueNum = uniqueNum + 1;
	return newRule;
    }
    
    private String instantiate(String thePattern,
			       HashMap<String,String> theBindings) {

	String result = new String();
	StringTokenizer st = new StringTokenizer(thePattern);

	for ( int i = 0; i < st.countTokens(); ) {
	    String token = st.nextToken();
	    if( var(token) ){
		result = result + " " + theBindings.get(token);
	    } else {
		result = result + " " + token;
	    }
	}
	return result.trim();
    }

    private boolean var(String str1) {
	// 先頭が ? なら変数
	return str1.startsWith("?");
    }
}
